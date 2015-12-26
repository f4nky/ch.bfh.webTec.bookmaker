package dao;

import model.MatchEvent;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for team-objects.
 * Contains methods to read and write team-objects to the database.
 * Singleton-class.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	22.12.2015	Michael Fankhauser         Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 22.12.2015
 */
public class MatchEventDao {

    private static MatchEventDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 22.12.2015
     */
    public static MatchEventDao getInstance() {
        if (instance == null) {
            instance = new MatchEventDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets all matches from the database and returns them.
     * @return A List of Matches
     * @since 22.12.2015
     */
    public List<MatchEvent> getMatches() {
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m");
        List<MatchEvent> matchEvents = query.getResultList();
        return matchEvents;
    }

    /**
     * Gets only the coming matches from the database and returns them.
     * @return A List of Matches
     * @since 25.12.2015
     */
    public List<MatchEvent> getMatchesComing() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m WHERE m.matchEventDateTime > '" + sdf.format(new Date()) + "'");
        List<MatchEvent> matchEventsComing = query.getResultList();
        return matchEventsComing;
    }

    /**
     * Gets only the finished matches from the database and returns them.
     * @return A List of Matches
     * @since 25.12.2015
     */
    public List<MatchEvent> getMatchesFinished() {
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m " +
                                                "WHERE (m.scoreTeamHome IS NOT NULL) AND (m.scoreTeamAway IS NOT NULL)");
        List<MatchEvent> matchEventsFinished = query.getResultList();
        return matchEventsFinished;
    }
}
