package beans;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import dao.MatchBetDao;
import dao.MatchEventDao;
import dao.UserBetDao;
import helpers.LanguageHelper;
import model.MatchBet;
import model.MatchEvent;
import model.UserBet;
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
    private List<UserBet> allUserBetsForThisMatchEvent;

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
        if (matchBets == null) {
            matchBets = MatchBetDao.getInstance().getMatchBets(matchEventId);
        }
        return matchBets;
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
            //Save bet in database
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

    public Double getTotalSetUserAmount(MatchEvent matchEvent) {
        List<UserBet> userBets = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    public List<MatchBet> getActiveMatchBetsWithUserAmount(MatchEvent matchEvent) {
        List<MatchBet> activeMatchBetsWithUserAmount = MatchBetDao.getInstance().getMatchBets(matchEvent, true);

        for (MatchBet activeMatchBet : activeMatchBetsWithUserAmount) {
            activeMatchBet.setSetUserAmount(0);
            List<UserBet> userBets = getAllUserBetsForMatchEvent(matchEvent);
            if (userBets != null) {
                for (UserBet userBet : userBets) {
                    if (userBet.getMatchBetId().getId().equals(activeMatchBet.getId())) {
                        double userAmount = activeMatchBet.getSetUserAmount();
                        userAmount += userBet.getAmount();
                        activeMatchBet.setSetUserAmount(userAmount);
                    }
                }
            }
        }
        return activeMatchBetsWithUserAmount;
    }

    public List<MatchBet> getNotActiveMatchBetsWithUserAmount(MatchEvent matchEvent) {
        List<MatchBet> notActiveMatchBetsWithUserAmount = MatchBetDao.getInstance().getMatchBets(matchEvent, false);

        for (MatchBet notActiveMatchBet : notActiveMatchBetsWithUserAmount) {
            notActiveMatchBet.setSetUserAmount(0);
            List<UserBet> userBets = getAllUserBetsForMatchEvent(matchEvent);
            if (userBets != null) {
                for (UserBet userBet : userBets) {
                    if (userBet.getMatchBetId().getId().equals(notActiveMatchBet.getId())) {
                        double userAmount = notActiveMatchBet.getSetUserAmount();
                        userAmount += userBet.getAmount();
                        notActiveMatchBet.setSetUserAmount(userAmount);
                    }
                }
            }
        }
        return notActiveMatchBetsWithUserAmount;
    }

    public List<UserBet> getAllUserBetsForMatchEvent(MatchEvent matchEvent) {
        if (allUserBetsForThisMatchEvent == null || (allUserBetsForThisMatchEvent.size() > 0 &&
                !allUserBetsForThisMatchEvent.get(0).getMatchBetId().getMatchEventId().getId().equals(matchEvent.getId()))) {
            allUserBetsForThisMatchEvent = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        }
        return allUserBetsForThisMatchEvent;
    }

    public double calculateMatchBetWinAmount(double matchBetOdd, double setUserAmount) {
        return matchBetOdd * setUserAmount;
    }

    public double calculateMatchEventWinLostAmount(MatchEvent matchEvent) {
        double lostAmount = 0.0;
        double winAmount = 0.0;
        for (MatchBet notActiveMatchBet : getNotActiveMatchBetsWithUserAmount(matchEvent)) {
            lostAmount += notActiveMatchBet.getSetUserAmount();
        }
        for (MatchBet activeMatchBet : getActiveMatchBetsWithUserAmount(matchEvent)) {
            winAmount += activeMatchBet.getOdds() * activeMatchBet.getSetUserAmount();
        }
        return winAmount - lostAmount;
    }
}
