package beans;

import dao.MatchBetDao;
import dao.MatchEventDao;
import dao.UserBetDao;
import dao.UserDao;
import helpers.LanguageHelper;
import model.MatchBet;
import model.MatchEvent;
import model.UserBet;
import validators.MatchEventValidator;
import validators.ValidationFault;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	23.12.2015	Michael Fankhauser  Class created.
 * 1.1  25.12.2015  Michael Fankhauser  Methods for coming + finished matches implemented.
 * 1.2  31.12.2015  Joel Holzer         Method for matches in progress implemented.
 * 1.3  01.01.2016  Joel Holzer         Added methods {@link #getTotalUserBetAmountByMatch} and
 *                                      {@link #calculateManagerWinLostAmountByMatch}
 * 1.4  02.01.2016  Joel Holzer         Added method {@link #finishMatch(MatchEvent, List)}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.2
 * @since 02.01.2016
 */
@ManagedBean(name = "championship")
@RequestScoped
public class ChampionshipBean implements Serializable {

    private String scoreTeamHome;
    private String scoreTeamAway;
    private List<MatchEvent> matchEventsComing;
    private List<MatchEvent> matchEventsPast;
    private String finishMatchErrorMessage = null;

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;


    private static final String FINISH_MATCH_FORM_NAME = "finishMatch";

    public String getScoreTeamHome() {
        return scoreTeamHome;
    }

    public void setScoreTeamHome(String scoreTeamHome) {
        this.scoreTeamHome = scoreTeamHome;
    }

    public String getScoreTeamAway() {
        return scoreTeamAway;
    }

    public void setScoreTeamAway(String scoreTeamAway) {
        this.scoreTeamAway = scoreTeamAway;
    }

    public String getFinishMatchErrorMessage() {
        return finishMatchErrorMessage;
    }

    public void setFinishMatchErrorMessage(String finishMatchErrorMessage) {
        this.finishMatchErrorMessage = finishMatchErrorMessage;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
     *
     * @return
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsComing() {
        return MatchEventDao.getInstance().getMatchesComing();
    }

    /**
     *
     * @return
     * @since 31.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsInProgress() {
        return MatchEventDao.getInstance().getMatchesInProgress();
    }

    /**
     *
     * @return
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsPast() {
        return MatchEventDao.getInstance().getMatchesPast();
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 01.01.2016
     */
    public double getTotalUserBetAmountByMatch(MatchEvent matchEvent) {
        List<UserBet> userBetsFromMatch = UserBetDao.getInstance().getUserBetsByMatchEvent(matchEvent);
        double totalAmount = 0;
        for (UserBet userBet : userBetsFromMatch) {
            totalAmount += userBet.getAmount();
        }
        return totalAmount;
    }

    /**
     *
     * @param matchEvent
     * @return
     * @since 01.01.2016
     */
    public double calculateManagerWinLostAmountByMatch(MatchEvent matchEvent) {
        double lostAmount = 0.0;
        double winAmount = 0.0;
        List<UserBet> userBetsFromMatch = UserBetDao.getInstance().getUserBetsByMatchEvent(matchEvent);
        for (UserBet userBet : userBetsFromMatch) {
            if (userBet.getMatchBetId().getIsActive()) {
                lostAmount += (userBet.getMatchBetId().getOdds() * userBet.getAmount());
            } else {
                winAmount += userBet.getAmount();
            }
        }
        return winAmount - lostAmount;
    }

    /**
     *
     * @param matchEvent
     * @param matchBets
     * @throws IOException
     * @since 02.01.2016
     */
    public void finishMatch(MatchEvent matchEvent, List<MatchBet> matchBets) throws IOException {
        MatchEventValidator matchEventValidator = new MatchEventValidator();
        List<ValidationFault> validationFaults = matchEventValidator.validateFinishMatchEvent(scoreTeamHome, scoreTeamAway);

        if (validationFaults.size() == 0) {
            //No validation faults -> update score
            matchEvent.setScoreTeamAway(Integer.parseInt(scoreTeamAway));
            matchEvent.setScoreTeamHome(Integer.parseInt(scoreTeamHome));
            MatchEventDao.getInstance().updateScores(matchEvent);

            //update active status of match bets
            for (MatchBet matchBet : matchBets) {
                if (matchBet.getIsActive()) {
                    MatchBetDao.getInstance().updateIsActive(matchBet);

                    //Calculate new saldos of users
                    List<UserBet> userBetsOfMatchBet = UserBetDao.getInstance().getUserBetsByMatchBet(matchBet);
                    Map<Long, Double> userWinAmounts = new HashMap<Long, Double>();
                    for (UserBet userBet : userBetsOfMatchBet) {
                        Long userId = userBet.getUserId().getId();
                        if (userWinAmounts.containsKey(userId)) {
                            double newWinAmount = userWinAmounts.get(userId) + (userBet.getAmount() * matchBet.getOdds());
                            userWinAmounts.put(userId, newWinAmount);
                        } else {
                            userWinAmounts.put(userId, userBet.getAmount() * matchBet.getOdds());
                        }
                    }
                    //Update saldos of users in the database
                    Iterator iterator = userWinAmounts.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Long, Double> entry = (Map.Entry)iterator.next();
                        UserDao.getInstance().addAmountToSaldo(entry.getKey(), entry.getValue());
                        iterator.remove();
                    }
                }
            }
            finishMatchErrorMessage = null;
            navigationBean.redirectToManagerMatchDetailsFinished(matchEvent.getId());
        } else {
            //Validation faults -> display validation faults
            finishMatchErrorMessage = LanguageHelper.createValidationFaultOutput(FINISH_MATCH_FORM_NAME, validationFaults);
        }
    }
}