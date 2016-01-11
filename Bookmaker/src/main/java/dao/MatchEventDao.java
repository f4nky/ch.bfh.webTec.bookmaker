package dao;

import model.MatchEvent;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
 * 1.0	22.12.2015	Michael Fankhauser          Class created.
 * 1.1  27.12.2015  Michael Fankhauser          Method for finding a single matchEvent by id implemented.
 * 1.2  31.12.2015  Joel Holzer                 Method {@link #getMatchesInProgress} added.
 * 1.3  02.01.2016  Joel Holzer                 Added method {@link #updateScores(MatchEvent)}
 * 1.4  11.01.2016  Michael Fankhauser          Added method {@link #createMatchEvent(MatchEvent)}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.4
 * @since 11.01.2016
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
     * Gets only the coming matches from the database and returns them.
     *
     * @return A List of the coming matches
     * @since 25.12.2015
     */
    public List<MatchEvent> getMatchesComing() {
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m WHERE m.matchEventDateTime > :matchEventDateTime");
        query.setParameter("matchEventDateTime", new Date());
        return query.getResultList();
    }

    /**
     * Gets only the matches which start date is in past and their result is set from the database and returns them.
     *
     * @return A List of the past matches
     * @since 25.12.2015
     */
    public List<MatchEvent> getMatchesPast() {
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m WHERE m.matchEventDateTime <= :dateNow AND m.scoreTeamAway IS NOT NULL AND m.scoreTeamHome IS NOT NULL");
        query.setParameter("dateNow", new Date());
        return query.getResultList();
    }

    /**
     * Gets only the matches which start date is in past and their result is not set from the database and returns them.
     *
     * @return A List of the matches in progress
     * @since 31.12.2015
     */
    public List<MatchEvent> getMatchesInProgress() {
        Query query = entityManager.createQuery("SELECT m FROM " + model.MatchEvent.TABLE_NAME + " m WHERE m.matchEventDateTime <= :dateNow AND m.scoreTeamAway IS NULL AND m.scoreTeamHome IS NULL");
        query.setParameter("dateNow", new Date());
        return query.getResultList();
    }

    /**
     * Gets a single matchEvent by id.
     *
     * @return A single matchEvent
     * @since 27.12.2015
     */
    public MatchEvent findMatchEventById(Long matchEventId) {
        return entityManager.find(MatchEvent.class, matchEventId);
    }

    /**
     * Adds the given match event to the database (creates a new matchEvent-record).
     *
     * @param newMatchEvent Match event to add to the database.
     * @since 11.01.2016
     */
    public void createMatchEvent(MatchEvent newMatchEvent) {
        entityManager.getTransaction().begin();
        entityManager.persist(newMatchEvent);
        entityManager.getTransaction().commit();
    }

    /**
     * Updates the scores of the teams of a match (score from the home team and away team).
     * The new score (score to update the score in the database) is part of the given match event object.
     *
     * @param matchEventToUpdateScores Match event with the new scores of the teams. Id must be the same as by the
     *                                 match event to update the scores.
     * @since 02.01.2016
     */
    public void updateScores(MatchEvent matchEventToUpdateScores) {
        MatchEvent matchEvent = entityManager.find(MatchEvent.class, matchEventToUpdateScores.getId());
        entityManager.getTransaction().begin();
        matchEvent.setScoreTeamHome(matchEventToUpdateScores.getScoreTeamHome());
        matchEvent.setScoreTeamAway(matchEventToUpdateScores.getScoreTeamAway());
        entityManager.getTransaction().commit();
    }
}
