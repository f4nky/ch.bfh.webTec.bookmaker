package dao;

import model.MatchBet;
import model.MatchEvent;
import model.User;
import model.UserBet;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for userBet-objects.
 * Contains methods to read and write userBet-objects to the database.
 * Singleton-class.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser          Class created.
 * 1.1  29.12.2015  Joel Holzer                 Added the following methods: {@link #addUserBet}, {@link #getUserBetsByMatchEvent}
 * 1.2  02.01.2015  Joel Holzer                 Added the following methods: {@link #getUserBetsByMatchEvent}, {@link #getUserBetsByMatchBet}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.2
 * @since 02.01.2015
 */
public class UserBetDao {

    private static UserBetDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 12.11.2015
     */
    public static UserBetDao getInstance() {
        if (instance == null) {
            instance = new UserBetDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets all outstanding bets placed by the given user. A bet is outstanding as long as no match result has been entered.
     *
     * @param user User to get all pending bets.
     * @return A list of pending UserBets
     * @since 27.12.2015
     */
    public List<UserBet> getOutstandingUserBets(User user) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " +
                "WHERE ub.userId = :userId AND ub.matchBetId.matchEventId.scoreTeamHome IS NULL AND ub.matchBetId.matchEventId.scoreTeamAway IS NULL");
        query.setParameter("userId", user);
        return query.getResultList();
    }

    /**
     * Gets all finished bets placed by the given user. A bet is finished when the match result has been entered and the start date of the match is in the past.
     *
     * @param user User to get all finished bets.
     * @return A list of finished UserBets
     * @since 31.12.2015
     */
    public List<UserBet> getFinishedUserBets(User user) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " +
                "WHERE ub.userId = :userId AND ub.matchBetId.matchEventId.matchEventDateTime <= :dateNow AND ub.matchBetId.matchEventId.scoreTeamHome IS NOT NULL AND ub.matchBetId.matchEventId.scoreTeamAway IS NOT NULL");
        query.setParameter("userId", user);
        query.setParameter("dateNow", new Date());
        return query.getResultList();
    }

    /**
     * Adds the given user bet to the database (creates a userbet-record).
     *
     * @param userBet User bet to add to the database.
     * @since 29.12.2015
     */
    public void addUserBet(UserBet userBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(userBet);
        entityManager.getTransaction().commit();
    }

    /**
     * Gets all user bets for the given match event and the given user from the database. That means every
     * bets a user sets money for a specific match.
     *
     * @param user User which user bets for a specific match wants to select from the database.
     * @param matchEvent Match event which user bets should select.
     * @return The user bets which the given user made for the given match.
     * @since 29.12.2015
     */
    public List<UserBet> getUserBetsByMatchEvent(User user, MatchEvent matchEvent) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " + "LEFT JOIN ub." + UserBet.COLUMN_NAME_MATCH_BET_ID +
                " mb WHERE ub." + UserBet.COLUMN_NAME_USER_ID + " = :userId" + " AND mb." + MatchBet.COLUMN_NAME_MATCH_EVENT_ID + " = :matchEventId" + " ORDER BY ub." + UserBet.COLUMN_NAME_MATCH_BET_ID + " ASC");
        query.setParameter("userId", user);
        query.setParameter("matchEventId", matchEvent);
        return query.getResultList();
    }

    /**
     * Gets the user bets from every user for the given match event from the database.
     * That means all user bets for a specific match.
     *
     * @param matchEvent Match event which user bets should select.
     * @return The user bets for the given match.
     * @since 02.01.2016
     */
    public List<UserBet> getUserBetsByMatchEvent(MatchEvent matchEvent) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub WHERE ub.matchBetId.matchEventId = :matchEvent");
        query.setParameter("matchEvent", matchEvent);
        return query.getResultList();
    }

    /**
     * Gets all user bets from the given match bet from the database. This means, this method returns all the amounts
     * which gamblers set for a specific bet, e.g for the bet "France wins again Switzerland".
     * Every match bet can have 0 or multiple user bets, dependent the number of users who set money for this match bet.
     *
     * @param matchBet Match bet which user bets should select.
     * @return The user bets for the given match bet.
     * @since 02.01.2016
     */
    public List<UserBet> getUserBetsByMatchBet(MatchBet matchBet) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub WHERE ub.matchBetId = :matchBet");
        query.setParameter("matchBet", matchBet);
        return query.getResultList();
    }
}
