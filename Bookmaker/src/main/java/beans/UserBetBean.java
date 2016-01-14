package beans;

import dao.MatchBetDao;
import dao.MatchEventDao;
import dao.UserBetDao;
import dao.UserDao;
import helpers.LanguageHelper;
import model.MatchBet;
import model.MatchEvent;
import model.User;
import model.UserBet;
import validators.UserBetValidator;
import validators.ValidationFault;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This bean is used for actions concerning the user bets
 * For more information about user bets, see {@link UserBet}.
 *
 * Action concerning user bets are actions to read the user bets of a specific match, a specific user or from a specific
 * match bet, to calculate win/lost amounts, to create user bets, and many more.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser  Class created.
 * 1.1  29.12.2015  Joel Holzer         Added the following methods: {@link #calculateMatchBetWinAmount},
 *                                      {@link #getUserBetsByMatch}, {@link #getTotalSetAmount}, {@link #getMatchEvent},
 *                                      {@link #getMatchBets}

 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.1
 * @since 29.12.2015
 */
@ManagedBean(name = "userBetBean")
@ViewScoped
public class UserBetBean implements Serializable {
    private Long matchEventId;
    private MatchEvent matchEvent;
    private Long matchBetId;
    private String amount;
    private Double totalSetAmount;
    private List<MatchBet> matchBets;
    private List<UserBet> outstandingUserBets;
    private List<UserBet> finishedUserBets;
    private List<UserBet> userBetsByMatch;
    private String addUserBetErrorMessage = null;
    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    private static final String ADD_USER_BET_FORM_NAME = "addUserBet";

    public Long getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(Long matchEventId) {
        this.matchEventId = matchEventId;
    }

    public Long getMatchBetId() {
        return matchBetId;
    }

    public void setMatchBetId(Long matchBetId) {
        this.matchBetId = matchBetId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddUserBetErrorMessage() {
        return addUserBetErrorMessage;
    }

    public void setAddUserBetErrorMessage(String addUserBetErrorMessage) {
        this.addUserBetErrorMessage = addUserBetErrorMessage;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
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
     * Gets and returns all the outstanding user bets of the logged in user. An outstanding user bet is a bet which a
     * user set to a match in the future. As soon as the match has finished and the manager marked the match as finished,
     * the user bet is no longer outstanding.
     *
     * For more information about user bets, see {@link UserBet}.
     *
     * @return All the outstanding user bets of the logged in user.
     * @since 31.12.2015
     */
    public List<UserBet> getOutstandingUserBets() {
        if (outstandingUserBets == null) {
            outstandingUserBets = UserBetDao.getInstance().getOutstandingUserBets(SessionBean.getUser());
        }
        return outstandingUserBets;
    }

    /**
     * Gets and returns all the finished user bets of the logged in user. A user bet is finished as soon as the gambler
     * marked the match, which the user bets belongs to, as finished.
     *
     * For more information about user bets, see {@link UserBet}.
     *
     * @return All the finished user bets of the logged in user.
     * @since 31.12.2015
     */
    public List<UserBet> getFinishedUserBets() {
        if (finishedUserBets == null) {
            finishedUserBets = UserBetDao.getInstance().getFinishedUserBets(SessionBean.getUser());
        }
        return finishedUserBets;
    }

    /**
     * Adds a user bet (to the database). The user bet is only added if all form inputs are correct (first validated).
     * If a validation error occurred, the user bet is not added and a validation error message with the wrong inputs
     * is displayed.
     *
     * When a gambler adds a user bet, the set amount is withdraw from his account and the account is updated.
     *
     * @throws IOException
     * @since 27.12.2015
     */
    public void addUserBet() throws IOException {
        MatchBet matchBet = MatchBetDao.getInstance().findMatchBetById(matchBetId);

        //Validate input data
        UserBetValidator userBetValidator = new UserBetValidator();
        List<ValidationFault> validationFaults = userBetValidator.validateAddUserBet(amount);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Save user bet in database
            UserBet userBet = new UserBet();
            userBet.setAmount(Double.parseDouble(amount));
            userBet.setEntryDateTime(new Date());
            userBet.setMatchBetId(matchBet);
            User loggedInUser = SessionBean.getUser();
            userBet.setUserId(loggedInUser);
            UserBetDao.getInstance().addUserBet(userBet);

            //Update saldo of user
            loggedInUser.setSaldo(loggedInUser.getSaldo() - Double.parseDouble(amount));
            UserDao.getInstance().updateSaldo(loggedInUser);

            addUserBetErrorMessage = null;
            navigationBean.redirectToUserMatchDetail(matchEventId);
        } else {
            //Validation faults -> display validation faults
            addUserBetErrorMessage = LanguageHelper.createValidationFaultOutput(ADD_USER_BET_FORM_NAME, validationFaults);
        }
    }

    /**
     * Returns the match event which this user bet belongs to.
     *
     * @return Match event which this user bet belongs to.
     * @since 29.12.2015
     */
    public MatchEvent getMatchEvent() {
        if (matchEvent == null) {
            matchEvent = MatchEventDao.getInstance().findMatchEventById(matchEventId);
        }
        return matchEvent;
    }

    /**
     * Calculates and returns the total amount which the logged in user has set to the match which is set for this bean.
     * The match can set with the member variable {@link #matchEvent}.
     *
     * @return The total amount which the logged in user has set to the match which is set for this bean. 0.00 if no amount is set.
     * @since 29.12.2015
     */
    public Double getTotalSetAmount() {
        List<UserBet> userBets = getUserBetsByMatch();
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    /**
     * Gets the user bets for the logged in user and for a specific match (the set match (see {@link #matchEvent}).
     *
     * @return All user bets from the logged in user for a specific match.
     * @since 29.12.2015
     */
    public List<UserBet> getUserBetsByMatch() {
        if (userBetsByMatch == null) {
            userBetsByMatch = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), getMatchEvent());
        }
        return userBetsByMatch;
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
     * Is the match event in past.
     *
     * @return Match event in past.
     * @since 29.12.2015
     */
    public boolean isMatchEventInPast() {
        Date dateNow = new Date();
        Date dateEvent = getMatchEvent().getMatchEventDateTime();
        if (dateNow.after(dateEvent)) {
            return true;
        }
        return false;
    }
}