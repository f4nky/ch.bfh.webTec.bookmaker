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
 * 1.1  23.12.2015  Joel Holzer         Added method {@link #checkIfUserWithEmailExists}
 * 1.2  28.12.2015  Joel Holzer         Added methods {@link #updateUser} and
 * 1.3  01.01.2016  Joel Holzer         Added method {@link #updateSaldo}
 * 1.4  02.01.2016  Joel Holzer         Added method {@link #addAmountToSaldo}
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.4
 * @since 02.01.2016
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
     * Checks if another user (another than the user with the given id) with the given email address exists.
     *
     * @param id All other users than the user with this id will check against the email address.
     * @param email Email address to check with the other users.
     * @return True if another user with the given email address exists, false if not.
     * @since 28.12.2015
     */
    public boolean checkIfOtherUserWithEmailExists(long id, String email) {
        Query query = entityManager.createQuery("SELECT u FROM " + User.TABLE_NAME + " u WHERE u." + User.COLUMN_ID + " != :id AND u." + User.COLUMN_NAME_EMAIL + " = :email");
        query.setParameter("id", id);
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

    /**
     * Updates the user with the new data of the given user. The user to update is identified with the id of the given
     * user. The given user contains the new data of the user.
     *
     * @param userToUpdate User with the new data. Id must be the same as by the old user.
     * @since 28.12.2015
     */
    public void updateUser(User userToUpdate) {
        User user = entityManager.find(User.class, userToUpdate.getId());
        entityManager.getTransaction().begin();
        user.setEmail(userToUpdate.getEmail());
        user.setFirstName(userToUpdate.getFirstName());
        user.setLastName(userToUpdate.getLastName());
        user.setPassword(userToUpdate.getPassword());
        entityManager.getTransaction().commit();
    }

    /**
     * Updates the saldo of the given user in the database. The given user contains the new saldo.
     *
     * @param userToUpdateSaldo User with the new saldo. Id must be the same as by the user to update.
     * @since 01.01.2016
     */
    public void updateSaldo(User userToUpdateSaldo) {
        User user = entityManager.find(User.class, userToUpdateSaldo.getId());
        entityManager.getTransaction().begin();
        user.setSaldo(userToUpdateSaldo.getSaldo());
        entityManager.getTransaction().commit();
    }

    /**
     * Add up the given amount to the saldo of the user with the given userId.
     * @param userId Id of the user to add up his saldo.
     * @param amountToAdd Amount to add to the saldo of the user.
     * @since 02.01.2016
     */
    public void addAmountToSaldo(long userId, double amountToAdd) {
        User user = entityManager.find(User.class, userId);
        entityManager.getTransaction().begin();
        user.setSaldo(user.getSaldo() + amountToAdd);
        entityManager.getTransaction().commit();
    }
}
