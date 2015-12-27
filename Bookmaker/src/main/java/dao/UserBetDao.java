package dao;

import beans.SessionBean;
import model.UserBet;

import javax.persistence.EntityManager;
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
     * Gets all bets placed by the currently logged in user.
     *
     * @return A list of UserBets
     * @since 27.12.2015
     */
    public List<UserBet> getAllUserBets() {
        Query query = entityManager.createQuery("SELECT ub FROM " + model.UserBet.TABLE_NAME + " ub " +
                "WHERE ub." + model.UserBet.COLUMN_NAME_USER_ID + " = :userId");
        query.setParameter("userId", SessionBean.getUser());
        List<UserBet> userBets = query.getResultList();
        return userBets;
    }

    /**
     * Gets all pending bets placed by the currently logged in user. A bet is pending as long as no match result has been entered.
     *
     * @return A list of pending UserBets
     * @since 27.12.2015
     */
    public List<UserBet> getUserBetsPending() {
        Query query = entityManager.createQuery("SELECT ub FROM " + model.UserBet.TABLE_NAME + " ub " +
                "WHERE ub." + model.UserBet.COLUMN_NAME_USER_ID + " = :userId");
        query.setParameter("userId", SessionBean.getUser());
        List<UserBet> userBets = query.getResultList();
        return userBets;
    }

    public void addUserBet(UserBet userBet) {
        entityManager.getTransaction().begin();
        entityManager.persist(userBet);
        entityManager.getTransaction().commit();
    }
}
