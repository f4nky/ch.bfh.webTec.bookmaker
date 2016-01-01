package beans;

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
 * 1.2  29.12.2015  Joel Holzer         Added the following methods: {@link #getMatchBets}, {@link #getTotalSetUserAmount},
 *                                      {@link #getMatchBetsWithUserAmount}, {@link #getAllUserBetsForMatchEvent},
 *                                      {@link #calculateMatchBetWinAmount}, {@link #calculateMatchEventWinLostAmount}
 * 1.3  01.01.2016  Joel Holzer         Added the following methods: {@link #getActiveMatchBets()}, {@link #getNotActiveMatchBets()}
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.2
 * @since 29.12.2015
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
    private List<MatchBet> activeMatchBets;
    private List<MatchBet> notActiveMatchBets;
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

    /**
     *
     * @return
     * @since 29.12.2015
     */
    public List<MatchBet> getMatchBets() {
        if (matchBets == null) {
            matchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent());
        }
        return matchBets;
    }

    /**
     *
     * @return
     * @since 01.01.2016
     */
    public List<MatchBet> getActiveMatchBets() {
        if (activeMatchBets == null) {
            activeMatchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent(), true);
        }
        return activeMatchBets;
    }

    /**
     *
     * @return
     * @since 01.01.2016
     */
    public List<MatchBet> getNotActiveMatchBets() {
        if (notActiveMatchBets == null) {
            notActiveMatchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent(), false);
        }
        return notActiveMatchBets;
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 29.12.2015
     */
    public Double getTotalSetUserAmount(MatchEvent matchEvent) {
        List<UserBet> userBets = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 29.12.2015
     */
    public List<MatchBet> getMatchBetsWithUserAmount(MatchEvent matchEvent, boolean isActiveMatchBet) {
        List<MatchBet> matchBetsWithUserAmount = MatchBetDao.getInstance().getMatchBets(matchEvent, isActiveMatchBet);

        for (MatchBet matchBet : matchBetsWithUserAmount) {
            matchBet.setSetUserAmount(0);
            List<UserBet> userBets = getAllUserBetsForMatchEvent(matchEvent);
            if (userBets != null) {
                for (UserBet userBet : userBets) {
                    if (userBet.getMatchBetId().getId().equals(matchBet.getId())) {
                        double userAmount = matchBet.getSetUserAmount();
                        userAmount += userBet.getAmount();
                        matchBet.setSetUserAmount(userAmount);
                    }
                }
            }
        }
        return matchBetsWithUserAmount;
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 29.12.2015
     */
    public List<UserBet> getAllUserBetsForMatchEvent(MatchEvent matchEvent) {
        if (allUserBetsForThisMatchEvent == null || (allUserBetsForThisMatchEvent.size() > 0 &&
                !allUserBetsForThisMatchEvent.get(0).getMatchBetId().getMatchEventId().getId().equals(matchEvent.getId()))) {
            allUserBetsForThisMatchEvent = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        }
        return allUserBetsForThisMatchEvent;
    }

    /**
     *
     * @param matchBetOdd
     * @param setUserAmount
     * @return
     * @since 29.12.2015
     */
    public double calculateMatchBetWinAmount(double matchBetOdd, double setUserAmount) {
        return matchBetOdd * setUserAmount;
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 29.12.2015
     */
    public double calculateMatchEventWinLostAmount(MatchEvent matchEvent) {
        double lostAmount = 0.0;
        double winAmount = 0.0;
        for (MatchBet notActiveMatchBet : getMatchBetsWithUserAmount(matchEvent, false)) {
            lostAmount += notActiveMatchBet.getSetUserAmount();
        }
        for (MatchBet activeMatchBet : getMatchBetsWithUserAmount(matchEvent, true)) {
            winAmount += activeMatchBet.getOdds() * activeMatchBet.getSetUserAmount();
        }
        return winAmount - lostAmount;
    }
}
