package dao;

import beans.SessionBean;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import model.MatchBet;
import model.MatchEvent;
import model.User;
import model.UserBet;

import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for userBet-objects.
 * Contains methods to read and write userBet-objects to the database.
 * Singleton-class.
 * <br/><br/>
 * <p>
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser          Class created.
 * 1.1  29.12.2015  Joel Holzer                 Added the following methods: {@link #addUserBet}, {@link #getUserBetsByMatchEvent}
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.1
 * @since 29.12.2015
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
     * Gets all bets placed by the given user.
     *
     * @param user User to get all placed bets.
     * @return A list of UserBets
     * @since 27.12.2015
     */
    public List<UserBet> getAllUserBets(User user) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " +
                "WHERE ub." + UserBet.COLUMN_NAME_USER_ID + " = :userId" + " ORDER BY ub." + UserBet.COLUMN_NAME_MATCH_BET_ID + " ASC");
        query.setParameter("userId", user);

        List<UserBet> userBets = query.getResultList();
        return userBets;
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
     *
     * @param userBet
     * @since 29.12.2015
     */
    public void addUserBet(UserBet userBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(userBet);
        entityManager.getTransaction().commit();
    }

    /**
     *
     * @param user
     * @param matchEvent
     * @return
     * @since 29.12.2015
     */
    public List<UserBet> getUserBetsByMatchEvent(User user, MatchEvent matchEvent) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " + "LEFT JOIN ub." + UserBet.COLUMN_NAME_MATCH_BET_ID +
                " mb WHERE ub." + UserBet.COLUMN_NAME_USER_ID + " = :userId" + " AND mb." + MatchBet.COLUMN_NAME_MATCH_EVENT_ID + " = :matchEventId" + " ORDER BY ub." + UserBet.COLUMN_NAME_MATCH_BET_ID + " ASC");
        query.setParameter("userId", user);
        query.setParameter("matchEventId", matchEvent);

        List<UserBet> userBets = query.getResultList();
        return userBets;
    }
}
