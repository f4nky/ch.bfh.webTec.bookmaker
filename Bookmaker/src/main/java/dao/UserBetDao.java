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
import java.util.List;

/**
 * Data Access Object (DAO) for userBet-objects.
 * Contains methods to read and write userBet-objects to the database.
 * Singleton-class.
 * <br/><br/>
 * <p>
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser         Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 27.12.2015
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
     * Gets all pending bets placed by the given user. A bet is pending as long as no match result has been entered.
     *
     * @param user User to get all pending bets.
     * @return A list of pending UserBets
     * @since 27.12.2015
     */
    public List<UserBet> getUserBetsPending(User user) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " +
                "WHERE ub." + UserBet.COLUMN_NAME_USER_ID + " = :userId");
        query.setParameter("userId", user);
        List<UserBet> userBets = query.getResultList();
        return userBets;
    }

    public void addUserBet(UserBet userBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(userBet);
        entityManager.getTransaction().commit();
    }

    public List<UserBet> getUserBetsByMatchEvent(User user, MatchEvent matchEvent) {
        Query query = entityManager.createQuery("SELECT ub FROM " + UserBet.TABLE_NAME + " ub " + "LEFT JOIN ub." + UserBet.COLUMN_NAME_MATCH_BET_ID +
                " mb WHERE ub." + UserBet.COLUMN_NAME_USER_ID + " = :userId" + " AND mb." + MatchBet.COLUMN_NAME_MATCH_EVENT_ID + " = :matchEventId" + " ORDER BY ub." + UserBet.COLUMN_NAME_MATCH_BET_ID + " ASC");
        query.setParameter("userId", user);
        query.setParameter("matchEventId", matchEvent);

        List<UserBet> userBets = query.getResultList();
        return userBets;
    }
}
