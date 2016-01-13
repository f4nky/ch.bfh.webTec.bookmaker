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
 * This bean is used for actions concerning the match bets
 * For more information about match bets, see {@link MatchBet}.
 *
 * Action concerning match bets are actions to read the match bets of a specific match or a specific user, to
 * read the result of match bets, to calculate win/lost amounts, to create match bets, and many more.

 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser  Class created.
 * 1.1  28.12.2015  Joel Holzer         Updated method {@link #createMatchBet()} with validation routine. Added method
 *                                      {@link #getMatchEvent()}.
 * 1.2  29.12.2015  Joel Holzer         Added the following methods: {@link #getMatchBets}, {@link #getTotalSetUserAmountFromLoggedInUser},
 *                                      {@link #getAllLoggedInUserBetsForMatchEvent},
 *                                      {@link #calculateMatchBetWinAmount}, {@link #calculateMatchEventWinLostAmount}
 * 1.3  01.01.2016  Joel Holzer         Added the following methods: {@link #getActiveMatchBets()}, {@link #getNotActiveMatchBets()}
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.3
 * @since 01.01.2016
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
    private List<MatchBet> activeMatchBetsWithUserAmount;
    private List<MatchBet> notActiveMatchBetsWithUserAmount;
    private List<UserBet> allLoggedInUserBetsForThisMatchEvent;

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
     *
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
     * Gets and returns the match event which this match bet belongs to.
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
     * Gets and returns all the match bets of the set match (set match is variable {@link #matchEvent}). This are all match bets
     * which the manager has created for a match.
     *
     * @return All the match bets of the set match in a list. Empty list if no match bets is available for the match.
     * @since 29.12.2015
     */
    public List<MatchBet> getMatchBets() {
        if (matchBets == null) {
            matchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent());
        }
        return matchBets;
    }

    /**
     * Gets and returns all active match bets of the set match (set match is variable {@link #matchEvent}).
     * A match bet is active as soon as the manager marked the match bet as active (if the criterion of the match bet
     * occurred).
     *
     * @return List of all active match bets of the set match. Empty list if no match bet is active for the match.
     * @since 01.01.2016
     */
    public List<MatchBet> getActiveMatchBets() {
        if (activeMatchBets == null) {
            activeMatchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent(), true);
        }
        return activeMatchBets;
    }

    /**
     * Gets and returns all not active match bets of the set match (set match is variable {@link #matchEvent}).
     * A match bet is not active when the match is not finished or the criterion of the match bet does not occurred.
     *
     * @return List of all not active match bets of the set match. Empty list if no match bet is not active for the match.
     * @since 01.01.2016
     */
    public List<MatchBet> getNotActiveMatchBets() {
        if (notActiveMatchBets == null) {
            notActiveMatchBets = MatchBetDao.getInstance().getMatchBets(getMatchEvent(), false);
        }
        return notActiveMatchBets;
    }

    /**
     * Calculates and returns the total amount which the logged in user has set for the given match.
     * The total set amount of the user is the sum of all the set amounts for a match.
     *
     * @param matchEvent Match to get/calculate the total set amount of the logged in user.
     * @return Total amount which the logged in user has set for the given match. 0.00 if no amount is set.
     * @since 29.12.2015
     */
    public Double getTotalSetUserAmountFromLoggedInUser(MatchEvent matchEvent) {
        List<UserBet> userBets = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    /**
     * Calculates and returns the sum of all set amounts from all gamblers for a specific match bet, e.g.
     * sum of all set amounts (all users together) for the match bet "France wins after 90min".
     *
     * @param matchBet Match bet to calculate and return the sum of all set amounts (all gamblers together).
     * @return Sum of all set amounts from all gamblers together. 0.00 if no amount is set.
     * @since 01.01.2016
     */
    public Double getTotalSetAmountAllUsersByMatchBet(MatchBet matchBet) {
        List<UserBet> userBets = UserBetDao.getInstance().getUserBetsByMatchBet(matchBet);
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    /**
     * Gets all the active match bets from the given match event (only from the logged in user).
     * Every active match bet this method returns contains also the sum of all user set amount.
     *
     * @param matchEvent Event which active match bets (with the total of set amount) should get/return.
     * @return List which contains all the active match bets and for every match bet the sum of all set user amounts.
     *         The sum of all set user amounts of a match bet can read with the method {@link MatchBet#getSetUserAmount()}
     * @since 29.12.2015
     */
    public List<MatchBet> getActiveMatchBetsWithUserAmount(MatchEvent matchEvent) {
        //Condition to minimize database requests.
        if (activeMatchBetsWithUserAmount == null || activeMatchBetsWithUserAmount.size() == 0 ||
                (activeMatchBetsWithUserAmount.size() > 0 && activeMatchBetsWithUserAmount.get(0).getMatchEventId().getId() != matchEvent.getId())) {
            activeMatchBetsWithUserAmount = MatchBetDao.getInstance().getMatchBets(matchEvent, true);

            for (MatchBet matchBet : activeMatchBetsWithUserAmount) {
                matchBet.setSetUserAmount(0);
                List<UserBet> userBets = getAllLoggedInUserBetsForMatchEvent(matchEvent);
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
        }
        return activeMatchBetsWithUserAmount;
    }


    /**
     * Gets all the not active match bets from the given match event (only from the logged in user).
     * Every not active match bet this method returns contains also the sum of all user set amount.
     *
     * @param matchEvent Event which not active match bets (with the total of set amount) should get/return.
     * @return List which contains all the not active match bets and for every match bet the sum of all set user amounts.
     *         The sum of all set user amounts of a match bet can read with the method {@link MatchBet#getSetUserAmount()}
     * @since 29.12.2015
     */
    public List<MatchBet> getNotActiveMatchBetsWithUserAmount(MatchEvent matchEvent) {
        //Condition to minimize database requests
        if (notActiveMatchBetsWithUserAmount == null || notActiveMatchBetsWithUserAmount.size() == 0 ||
                (notActiveMatchBetsWithUserAmount.size() > 0 && notActiveMatchBetsWithUserAmount.get(0).getMatchEventId().getId() != matchEvent.getId())) {
            notActiveMatchBetsWithUserAmount = MatchBetDao.getInstance().getMatchBets(matchEvent, false);

            for (MatchBet matchBet : notActiveMatchBetsWithUserAmount) {
                matchBet.setSetUserAmount(0);
                List<UserBet> userBets = getAllLoggedInUserBetsForMatchEvent(matchEvent);
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
        }
        return notActiveMatchBetsWithUserAmount;
    }

    /**
     * Gets all the user bets, which the logged in user bets on the given match events. For more infos about the user bets,
     * see {@link UserBet}. Every user can bet multiple times on the same match event and also for the same match bet.
     *
     * @param matchEvent Match to get all the user bets which the logged in user placed.
     * @return List which contains all the user bets which the logged in user placed for the given match.
     * @since 29.12.2015
     */
    public List<UserBet> getAllLoggedInUserBetsForMatchEvent(MatchEvent matchEvent) {
        if (allLoggedInUserBetsForThisMatchEvent == null || allLoggedInUserBetsForThisMatchEvent.size() == 0 || (allLoggedInUserBetsForThisMatchEvent.size() > 0 &&
                !allLoggedInUserBetsForThisMatchEvent.get(0).getMatchBetId().getMatchEventId().getId().equals(matchEvent.getId()))) {
            allLoggedInUserBetsForThisMatchEvent = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), matchEvent);
        }
        return allLoggedInUserBetsForThisMatchEvent;
    }

    /**
     * Calculates an returns the win amount of a bet. This win amount is calculated with the following formula:
     * Odd * Set amount
     *
     *
     * @param matchBetOdd Odd.
     * @param setUserAmount Set amount.
     * @return Win amount of a bet with the given odd and amount.
     * @since 29.12.2015
     */
    public double calculateMatchBetWinAmount(double matchBetOdd, double setUserAmount) {
        return matchBetOdd * setUserAmount;
    }

    /**
     * Calculates the amount which the logged in user win or lost for the given match event.
     * The calculated amount is positive if he win money or negative if he lost mone.
     *
     * @param matchEvent Event to calculate the win/lost amount of the logged in user.
     * @return Win/Lost amount which the logged in user win or lost for the given match. Positive (win) or negative (lost).
     * @since 29.12.2015
     */
    public double calculateMatchEventWinLostAmount(MatchEvent matchEvent) {
        double lostAmount = 0.0;
        double winAmount = 0.0;
        for (MatchBet notActiveMatchBet : getNotActiveMatchBetsWithUserAmount(matchEvent)) {
            lostAmount += notActiveMatchBet.getSetUserAmount();
        }

        List<MatchBet> activeMatchBets = getActiveMatchBetsWithUserAmount(matchEvent);
        for (MatchBet activeMatchBet : activeMatchBets) {
            winAmount += activeMatchBet.getOdds() * activeMatchBet.getSetUserAmount();
        }
        return winAmount - lostAmount;
    }
}
