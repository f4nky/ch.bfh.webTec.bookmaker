package beans;

import dao.MatchBetDao;
import dao.MatchEventDao;
import helpers.LanguageHelper;
import model.MatchBet;
import model.MatchEvent;
import validators.MatchBetValidator;
import validators.ValidationFault;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser  Class created.
 * 1.1  28.12.2015  Joel Holzer         Updated method {@link #createMatchBet()} with validation routine. Added method
 *                                      {@link #getMatchEvent()}.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.1
 * @since 28.12.2015
 */
@ManagedBean(name = "matchBetBean")
@ViewScoped
public class MatchBetBean implements Serializable {
    private Long matchEventId;
    private MatchEvent matchEvent;
    private String descriptionEn;
    private String descriptionDe;
    private String descriptionFr;
    private String descriptionIt;
    private String odds;
    private List<MatchBet> matchBets;
    private String createBetErrorMessage = null;

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    private static final String CREATE_BET_FORM_NAME = "newBet";

    public Long getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(Long matchEventId) {
        this.matchEventId = matchEventId;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
    }

    public String getDescriptionIt() {
        return descriptionIt;
    }

    public void setDescriptionIt(String descriptionIt) {
        this.descriptionIt = descriptionIt;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public List<MatchBet> getMatchBets() {
        return MatchBetDao.getInstance().getMatchBets(matchEventId);
    }

    public String getCreateBetErrorMessage() {
        return createBetErrorMessage;
    }

    public void setCreateBetErrorMessage(String createBetErrorMessage) {
        this.createBetErrorMessage = createBetErrorMessage;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
     * Validates the input data for creating a new match bet and add the match bet to the database if no validation
     * fault occurred. After adding the bet to the database, the modal dialog to add the match bet is closed and the
     * site is refreshed.
     * If at least one validation fault occurred, the validation fault are displayed and the match bet is not added to
     * the database. See {@link MatchBetValidator} for the validation routine.
     * @since 28.12.2015
     */
    public void createMatchBet() throws IOException {
        MatchEvent matchEvent = getMatchEvent();

        MatchBet matchBet = new MatchBet();
        matchBet.setMatchEventId(matchEvent);
        matchBet.setDescriptionEn(descriptionEn);
        matchBet.setDescriptionDe(descriptionDe);
        matchBet.setDescriptionFr(descriptionFr);
        matchBet.setDescriptionIt(descriptionIt);

        //Validate input data
        MatchBetValidator matchBetValidator = new MatchBetValidator();
        List<ValidationFault> validationFaults = matchBetValidator.validateAddMatchBet(matchBet, odds);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Save bet in database and display success message
            matchBet.setOdds(Double.parseDouble(odds));
            MatchBetDao.getInstance().createMatchBet(matchBet);
            createBetErrorMessage = null;
            navigationBean.redirectToManagerMatchDetail(matchEventId);
        } else {
            //Validation faults -> display validation faults
            createBetErrorMessage = LanguageHelper.createValidationFaultOutput(CREATE_BET_FORM_NAME, validationFaults);
        }
    }

    /**
     * Returns the match event which this match bet belongs to.
     *
     * @return Match event which this match bet belongs to.
     * @since 28.12.2015
     */
    public MatchEvent getMatchEvent() {
        if (matchEvent == null) {
            matchEvent = MatchEventDao.getInstance().findMatchEventById(matchEventId);
        }
        return matchEvent;
    }
}
