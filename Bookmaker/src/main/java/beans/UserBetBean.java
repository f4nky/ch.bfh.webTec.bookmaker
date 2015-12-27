package beans;

import dao.MatchBetDao;
import dao.UserBetDao;
import model.MatchBet;
import model.UserBet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private Long matchBetId;
    private Double amount;
    private List<MatchBet> matchBets;
    private List<UserBet> allUserBets;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<MatchBet> getMatchBets() {
        return MatchBetDao.getInstance().getMatchBets(matchEventId);
    }

    public List<UserBet> getAllUserBets() {
        return UserBetDao.getInstance().getAllUserBets();
    }

    public void addUserBet() {
        MatchBet matchBet = MatchBetDao.getInstance().findMatchBetById(matchBetId);

        UserBet userBet = new UserBet();
        userBet.setAmount(amount);
        userBet.setEntryDateTime(new Date());
        userBet.setMatchBetId(matchBet);
        userBet.setUserId(SessionBean.getUser());

        UserBetDao.getInstance().addUserBet(userBet);
    }
}