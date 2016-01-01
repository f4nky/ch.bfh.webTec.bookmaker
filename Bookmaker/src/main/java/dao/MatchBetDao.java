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
 * 1.0	26.12.2015	Michael Fankhauser          Class created.
 * 1.1  29.12.2015  Joel Holzer                 Added method {@link #getMatchBets(MatchEvent, boolean)}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.1
 * @since 29.12.2015
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
     * @param matchEvent Match event to get the matchBets.
     * @return A List of matchBets
     * @since 26.12.2015
     */
    public List<MatchBet> getMatchBets(MatchEvent matchEvent) {
        Query query = entityManager.createQuery("SELECT mb FROM " + MatchBet.TABLE_NAME + " mb WHERE mb.matchEventId = :matchEvent");
        query.setParameter("matchEvent", matchEvent);
        List<MatchBet> matchBets = query.getResultList();
        return matchBets;
    }

    /**
     *
     * @param matchEvent
     * @param isActiveMatchBet
     * @return
     * @since 29.12.2015
     */
    public List<MatchBet> getMatchBets(MatchEvent matchEvent, boolean isActiveMatchBet) {
        Query query = entityManager.createQuery("SELECT mb FROM " + MatchBet.TABLE_NAME + " mb WHERE mb.matchEventId = :matchEvent AND mb.isActive = :isActiveMatchBet");
        query.setParameter("matchEvent", matchEvent);
        query.setParameter("isActiveMatchBet", isActiveMatchBet);
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
