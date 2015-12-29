package beans;

import dao.MatchBetDao;
import dao.MatchEventDao;
import dao.UserBetDao;
import helpers.LanguageHelper;
import model.MatchBet;
import model.MatchEvent;
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
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 27.12.2015
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
    private List<UserBet> allUserBets;
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

    public List<MatchBet> getMatchBets() {
        if (matchBets == null) {
            matchBets = MatchBetDao.getInstance().getMatchBets(matchEventId);
        }
        return matchBets;
    }

    public List<UserBet> getAllUserBets() {
        if (allUserBets == null) {
            allUserBets = UserBetDao.getInstance().getAllUserBets(SessionBean.getUser());
        }
        return allUserBets;
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
            userBet.setUserId(SessionBean.getUser());
            UserBetDao.getInstance().addUserBet(userBet);
            //setAmount(null);
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

    public Double getTotalSetAmount() {
        List<UserBet> userBets = getUserBetsByMatch();
        Double totalAmount = 0.0;
        for (UserBet userBet : userBets) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    public List<UserBet> getUserBetsByMatch() {
        if (userBetsByMatch == null) {
            userBetsByMatch = UserBetDao.getInstance().getUserBetsByMatchEvent(SessionBean.getUser(), getMatchEvent());
        }
        return userBetsByMatch;
    }

    public double calculateMatchBetWinAmount(double matchBetOdd, double setUserAmount) {
        return matchBetOdd * setUserAmount;
    }
}