package dao;

import model.User;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Data Access Object (DAO) for user-objects.
 * Contains methods to read and write user-objects to the database.
 * Singleton-class.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	12.11.2015	Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 12.11.2015
 */
public class UserDao {

    private static UserDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 12.11.2015
     */
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets the user with the given email and password from the database and return the user.
     * If no user exists, returns null.
     * @return The user with the given email and password. If no user exists, returns null.
     * @since 12.11.2015
     */
    public User getUserByEmailPassword(String email, String password) {
        Query query = entityManager.createQuery("SELECT u FROM " + User.TABLE_NAME + " u WHERE u." + User.COLUMN_NAME_EMAIL + " = :email AND u." + User.COLUMN_NAME_PASSWORD + " = :password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<User> users = query.getResultList();
        User user = null;
        if (users.size() > 0) {
            user = users.get(0);
        }
        return user;
    }
}
