package beans;

import dao.*;
import helpers.LanguageHelper;
import model.*;
import validators.MatchEventValidator;
import validators.ValidationFault;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This bean is used for actions concerning a championship. A championship is a match event, respectively the europe
 * championship which contains multiple matches. For more infos about matches, see {@link MatchEvent}.
 *
 * This class contains actions to
 *
 * <b>History:</b>
 * <pre>
 * 1.0	23.12.2015	Michael Fankhauser  Class created.
 * 1.1  25.12.2015  Michael Fankhauser  Methods for coming + finished matches implemented.
 * 1.2  31.12.2015  Joel Holzer         Method for matches in progress implemented.
 * 1.3  01.01.2016  Joel Holzer         Added methods {@link #getTotalUserBetAmountByMatch} and
 *                                      {@link #calculateManagerWinLostAmountByMatch}
 * 1.4  02.01.2016  Joel Holzer         Added method {@link #finishMatch(MatchEvent, List)}
 * 1.5  12.01.2016  Michael Fankhauser  Added methods {@link #getStage()}, {@link #getTeamHome()} and {@link #getTeamAway()}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.5
 * @since 12.01.2016
 */
@ManagedBean(name = "championship")
@RequestScoped
public class ChampionshipBean implements Serializable {

    private String matchEventDateTime;
    private String matchEventGroup;
    private String matchEventNr;
    private Long stageId;
    private Stage stage;
    private Long teamHomeId;
    private Team teamHome;
    private Long teamAwayId;
    private Team teamAway;
    private String scoreTeamHome;
    private String scoreTeamAway;
    private List<MatchEvent> matchEventsComing;
    private List<MatchEvent> matchEventsPast;
    private String finishMatchErrorMessage = null;
    private String createEventErrorMessage = null;
    private String updateEventErrorMessage = null;

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;


    private static final String FINISH_MATCH_FORM_NAME = "finishMatch";
    private static final String CREATE_EVENT_FORM_NAME = "newMatch";

    public String getMatchEventDateTime() {
        return matchEventDateTime;
    }

    public void setMatchEventDateTime(String matchEventDateTime) {
        this.matchEventDateTime = matchEventDateTime;
    }

    public String getMatchEventGroup() {
        return matchEventGroup;
    }

    public void setMatchEventGroup(String matchEventGroup) {
        this.matchEventGroup = matchEventGroup;
    }

    public String getMatchEventNr() {
        return matchEventNr;
    }

    public void setMatchEventNr(String matchEventNr) {
        this.matchEventNr = matchEventNr;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Long getTeamHomeId() {
        return teamHomeId;
    }

    public void setTeamHomeId(Long teamHomeId) {
        this.teamHomeId = teamHomeId;
    }

    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    public Long getTeamAwayId() {
        return teamAwayId;
    }

    public void setTeamAwayId(Long teamAwayId) {
        this.teamAwayId = teamAwayId;
    }

    public void setTeamAway(Team teamAway) {
        this.teamAway = teamAway;
    }

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

    public String getCreateEventErrorMessage() {
        return createEventErrorMessage;
    }

    public void setCreateEventErrorMessage(String createEventErrorMessage) {
        this.createEventErrorMessage = createEventErrorMessage;
    }

    public String getUpdateEventErrorMessage() {
        return updateEventErrorMessage;
    }

    public void setUpdateEventErrorMessage(String updateEventErrorMessage) {
        this.updateEventErrorMessage = updateEventErrorMessage;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    /**
     * Gets and returns all the coming matches.
     *
     * @return All the coming matches.
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsComing() {
        return MatchEventDao.getInstance().getMatchesComing();
    }

    /**
     * Gets and returns all the matches in progress. A match is in progress when the start date of the match is in the
     * past but the finished flag was not set by the manager.
     *
     * @return All the matches in progress
     * @since 31.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsInProgress() {
        return MatchEventDao.getInstance().getMatchesInProgress();
    }

    /**
     * Gets and returns all the past matches. A match is a past match when the finished flag is set.
     *
     * @return All the past matches.
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsPast() {
        return MatchEventDao.getInstance().getMatchesPast();
    }

    /**
     * Validates the input data for creating a new match event and add the match event to the database if no validation
     * fault occurred. After adding the event to the database, the modal dialog to add the match event is closed and the
     * site is refreshed.
     * If at least one validation fault occurred, the validation fault are displayed and the match event is not added to
     * the database. See {@link MatchEventValidator} for the validation routine.
     *
     * @since 11.01.2016
     */
    public void createMatchEvent() throws IOException, ParseException {
        MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventNr(matchEventNr);
        matchEvent.setMatchEventGroup(matchEventGroup);
        matchEvent.setStage(getStage());
        matchEvent.setTeamHome(getTeamHome());
        matchEvent.setTeamAway(getTeamAway());

        //Validate input data
        MatchEventValidator matchEventValidator = new MatchEventValidator();
        List<ValidationFault> validationFaults = matchEventValidator.validateAddMatchEvent(matchEvent, matchEventDateTime);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Save event in database
            matchEvent.setMatchEventDateTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(matchEventDateTime));
            MatchEventDao.getInstance().createMatchEvent(matchEvent);
            createEventErrorMessage = null;
            navigationBean.redirectToManagerHome();
        } else {
            //Validation faults -> display validation faults
            createEventErrorMessage = LanguageHelper.createValidationFaultOutput(CREATE_EVENT_FORM_NAME, validationFaults);
        }
    }

    /**
     * Validates the input data for editing a match event and update the match event on the database if no validation
     * fault occurred. After updating the event on the database, the modal dialog to update the match event is closed and the
     * site is refreshed.
     * If at least one validation fault occurred, the validation fault are displayed and the match event is not updated on
     * the database. See {@link MatchEventValidator} for the validation routine.
     * @since 13.01.2016
     */
    public void updateMatchEvent() throws IOException, ParseException {
        MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventNr(matchEventNr);
        matchEvent.setMatchEventGroup(matchEventGroup);
        matchEvent.setStage(getStage());
        matchEvent.setTeamHome(getTeamHome());
        matchEvent.setTeamAway(getTeamAway());

        //Validate input data
        MatchEventValidator matchEventValidator = new MatchEventValidator();
        List<ValidationFault> validationFaults = matchEventValidator.validateAddMatchEvent(matchEvent, matchEventDateTime);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Save event in database
            matchEvent.setMatchEventDateTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(matchEventDateTime));
            MatchEventDao.getInstance().updateMatchEvent(matchEvent);
            updateEventErrorMessage = null;
            navigationBean.redirectToManagerHome();
        } else {
            //Validation faults -> display validation faults
            updateEventErrorMessage = LanguageHelper.createValidationFaultOutput(CREATE_EVENT_FORM_NAME, validationFaults);
        }
    }

    /**
     * Calculates the sum of all amounts which the logged in user has set to the given match.
     *
     * @param matchEvent Match to calculate the sum of all set amounts of the logged in user.
     * @return Sum of all amounts which the logged in user has set to the given match. 0 if no amount is set.
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
     * Calculates the amount which the manager win or lost for the given match event.
     * The manager wins if the returned amount is positive and lost if the returned amount is negative.
     *
     * @param matchEvent Match event to calculate the win/lost amount of the manager.
     * @return Win/Lost amount of the manager. Positiv value = win amount, Negative vaule = lost amount.
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
     * Marks the given match as finished. This method sets the finished flag of the match, sets the scores and marks
     * also the match bets of the match as active or not active (depends on the flags the manager sets in the frontend).
     * Then this method also calculates the new saldos of the manager and every users which set something to the match
     * event and updates this saldos in the database.
     *
     * @param matchEvent Match to mark as finished.
     * @param matchBets List of the match bets to update their active status and to update the user amounts of all the user
     *                  which sets money to a match bet which is set as active.
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

    /**
     * Returns the stage which this match event belongs to.
     *
     * @return stage which this match event belongs to.
     * @since 12.01.2016
     */
    public Stage getStage() {
        if (stage == null) {
            stage = StageDao.getInstance().findStageById(stageId);
        }
        return stage;
    }

    /**
     * Returns the team home which belongs to a match event.
     *
     * @return team home which belongs to a match event.
     * @since 12.01.2016
     */
    public Team getTeamHome() {
        if (teamHome == null) {
            teamHome = TeamDao.getInstance().findTeamById(teamHomeId);
        }
        return teamHome;
    }

    /**
     * Returns the team away which belongs to a match event.
     *
     * @return team away which belongs to a match event.
     * @since 12.01.2016
     */
    public Team getTeamAway() {
        if (teamAway == null) {
            teamAway = TeamDao.getInstance().findTeamById(teamAwayId);
        }
        return teamAway;
    }
}