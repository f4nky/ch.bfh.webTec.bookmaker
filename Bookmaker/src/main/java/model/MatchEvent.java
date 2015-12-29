package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Fanky on 22.12.15.
 */
@Entity
public class MatchEvent {

    public static final String TABLE_NAME = "MatchEvent";
    public static final String COLUMN_NAME_MATCH_EVENT_NR = "matchEventNr";
    public static final String COLUMN_NAME_MATCH_EVENT_DATE_TIME = "matchEventDateTime";
    public static final String COLUMN_NAME_STAGE_ID = "stageId";
    public static final String COLUMN_NAME_MATCH_EVENT_GROUP = "matchEventGroup";
    public static final String COLUMN_NAME_TEAM_HOME_ID = "teamHomeId";
    public static final String COLUMN_NAME_TEAM_AWAY_ID = "teamAwayId";
    public static final String COLUMN_NAME_SCORE_TEAM_HOME = "scoreTeamHome";
    public static final String COLUMN_NAME_SCORE_TEAM_AWAY = "scoreTeamAway";
    public static final String COLUMN_NAME_IS_FINISHED = "isFinished";

    @GeneratedValue
    @Id
    private Long id;
    @Basic
    @Column(name = COLUMN_NAME_MATCH_EVENT_NR)
    private Integer matchEventNr;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_STAGE_ID)
    private Stage stage;
    @Basic
    @Column(name = COLUMN_NAME_MATCH_EVENT_GROUP)
    private String matchEventGroup;
    @Basic
    @Column(name = COLUMN_NAME_MATCH_EVENT_DATE_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private Date matchEventDateTime;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_TEAM_HOME_ID)
    private Team teamHome;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_TEAM_AWAY_ID)
    private Team teamAway;
    @Basic
    @Column(name = COLUMN_NAME_SCORE_TEAM_HOME)
    private Integer scoreTeamHome;
    @Basic
    @Column(name = COLUMN_NAME_SCORE_TEAM_AWAY)
    private Integer scoreTeamAway;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatchEventNr() {
        return matchEventNr;
    }

    public void setMatchEventNr(Integer matchEventNr) {
        this.matchEventNr = matchEventNr;
    }

    public Date getMatchEventDateTime() {
        return matchEventDateTime;
    }

    public void setMatchEventDateTime(Date matchEventDateTime) {
        this.matchEventDateTime = matchEventDateTime;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getMatchEventGroup() {
        return matchEventGroup;
    }

    public void setMatchEventGroup(String group) {
        this.matchEventGroup = group;
    }

    public Team getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    public Team getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(Team teamAway) {
        this.teamAway = teamAway;
    }

    public Integer getScoreTeamHome() {
        return scoreTeamHome;
    }

    public void setScoreTeamHome(Integer scoreTeamHome) {
        this.scoreTeamHome = scoreTeamHome;
    }

    public Integer getScoreTeamAway() {
        return scoreTeamAway;
    }

    public void setScoreTeamAway(Integer scoreTeamAway) {
        this.scoreTeamAway = scoreTeamAway;
    }
}
