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
 * 1.1  29.12.2015  Joel Holzer                 Added methods {@link #getMatchBets(MatchEvent, boolean)} and
 *                                              {@link #createMatchBet(MatchBet)}
 * 1.2  02.01.2016  Joel Holzer                 Added method {@link #updateIsActive(MatchBet)}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.2
 * @since 02.01.2016
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
     *
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
     * Gets all active (occurred) or not active (not occurred) match bets from the given match event.
     *
     * @param matchEvent Match event which active or not active match bets should select.
     * @param isActiveMatchBet True, when only the active match bets (match bets which occurred) should select, false
     *                         when only the not active match bets (match bets which not occurred) should select.
     * @return All active (occurred) or not active (not occurred) match bets from the given match event.
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
     * Gets a single matchBet by id.
     *
     * @return A single matchBet
     * @since 27.12.2015
     */
    public MatchBet findMatchBetById(Long matchBetId) {
        return entityManager.find(MatchBet.class, matchBetId);
    }

    /**
     * Adds the given match bet to the database (creates a new matchbet-record).
     *
     * @param newMatchBet Match bet to add to the database.
     * @since 29.12.2015
     */
    public void createMatchBet(MatchBet newMatchBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(newMatchBet);
        entityManager.getTransaction().commit();
    }

    /**
     * Deletes the given match bet from the database.
     *
     * @param matchBetToDelete Match bet to delete from the database.
     * @since 29.12.2015
     */
    public void deleteMatchBet(MatchBet matchBetToDelete) {
        MatchBet matchBet = entityManager.find(MatchBet.class, matchBetToDelete.getId());
        entityManager.getTransaction().begin();
        entityManager.remove(matchBet);
        entityManager.getTransaction().commit();
    }

    /**
     * Updates the is active value of the given match bet in the database.
     * The is active value is set, when the manager marks a match bet as occurred.
     *
     * @param matchBetToUpdate Match bet to update the is active value. This match bet contains the new is active value.
     * @since 02.01.2016
     */
    public void updateIsActive(MatchBet matchBetToUpdate) {
        MatchBet matchBet = entityManager.find(MatchBet.class, matchBetToUpdate.getId());
        entityManager.getTransaction().begin();
        matchBet.setIsActive(matchBetToUpdate.getIsActive());
        entityManager.getTransaction().commit();
    }
}
