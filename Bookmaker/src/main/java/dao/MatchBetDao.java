package dao;

import model.MatchBet;
import model.MatchEvent;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Access Object (DAO) for matchBet-objects.
 * Contains methods to read and write matchBet-objects to the database.
 * Singleton-class.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser         Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 26.12.2015
 */
public class MatchBetDao {

    private static MatchBetDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 22.12.2015
     */
    public static MatchBetDao getInstance() {
        if (instance == null) {
            instance = new MatchBetDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets all matchBets for a specific matchEvent from the database and returns them.
     * @return A List of matchBets
     * @since 26.12.2015
     */
    public List<MatchBet> getMatchBets(Long matchEventId) {
        MatchEvent matchEvent = entityManager.find(MatchEvent.class, matchEventId);

        Query query = entityManager.createQuery("SELECT mb FROM " + MatchBet.TABLE_NAME + " mb WHERE mb.matchEventId = :matchEventId");
        query.setParameter("matchEventId", matchEvent);
        List<MatchBet> matchBets = query.getResultList();
        return matchBets;
    }

    /**
     * Gets a single matchBet by id
     * @return A single matchBet
     * @since 27.12.2015
     */
    public MatchBet findMatchBetById(Long matchBetId) {
        return entityManager.find(MatchBet.class, matchBetId);
    }

    public void createMatchBet(MatchBet newMatchBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(newMatchBet);
        entityManager.getTransaction().commit();
    }
}
