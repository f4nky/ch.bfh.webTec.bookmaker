package model;

import model.User;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for the user orm.model class.
 * Tests the crud db-operations (create, read, update, delete).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	29.10.2015	Michael Fankhauser  Class created.
 * 1.1  12.11.2015  Joel Holzer         Added methods to test read, update and delete.
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.1
 * @since 12.11.2015
 */
public class UserTest {

    /**
     * Tests the creation of a user in the database.
     * @since 29.10.2015
     */
    @Test
    public void testCreate() throws ParseException {
        User manager = new User();
        manager.setEmail("manager@test.ch");
        manager.setFirstName("Manager");
        manager.setLastName("Doe");
        manager.setPassword("test");
        manager.setIsManager(true);

        User user = new User();
        user.setEmail("user@test.ch");
        user.setFirstName("User");
        user.setLastName("Doe");
        user.setPassword("test");
        user.setIsManager(false);

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        em.getTransaction().begin();
        em.persist(manager);
        em.persist(user);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a user from the database.
     * @since 12.11.2015
     */
    @Test
    public void testRead() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<User> users = em.createQuery("select u from User u").getResultList();
        User user = users.get(0);
        assertNotNull(user);
    }

    /**
     * Tests the update of a user in the database.
     * @since 12.11.2015
     */
    @Test
    public void testUpdate() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<User> users = em.createQuery("select u from User u").getResultList();
        User user = users.get(0);

        em.getTransaction().begin();
        {
            user.setFirstName("NewFirstName");
        }
        em.getTransaction().commit();

        assertTrue(user.getFirstName().equals("NewFirstName"));
    }

    /**
     * Tests the deletion of a user in the database.
     * @since 12.11.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<User> users = em.createQuery("select u from User u").getResultList();
        User user = users.get(0);

        em.getTransaction().begin();
        {
            em.remove(user);
        }
        em.getTransaction().commit();
    }
}
