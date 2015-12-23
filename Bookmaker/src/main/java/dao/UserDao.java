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
 * 1.1  23.12.2015  Joel Holzer         Added method to create a user in the database. Added method to check if an user
 *                                      already exists.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.1
 * @since 23.12.2015
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

    /**
     * Checks if already exists an user with the given email address.
     *
     * @param email Email to check if an user exists in the database.
     * @return True if an other user exists, false if not.
     * @since 23.12.2015
     */
    public boolean checkIfUserWithEmailExists(String email) {
        Query query = entityManager.createQuery("SELECT u FROM " + User.TABLE_NAME + " u WHERE u." + User.COLUMN_NAME_EMAIL + " = :email");
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (users.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Creates a new record in the database for the given user.
     *
     * @param userToCreate User to create the record in the database.
     * @since 12.11.2015
     */
    public void createUser(User userToCreate) {
        entityManager.getTransaction().begin();
        entityManager.persist(userToCreate);
        entityManager.getTransaction().commit();
    }
}
