package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;

/**
 * Test class for creating all entities on database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 26.12.2015
 */
public class CreateEntitiesTest {

    /**
     * Tests the creation of every single entity in the database.
     * @since 26.12.2015
     */
    @Test
    public void testCreate() throws ParseException {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();

        User user = new User();
        Team team = new Team();
        Stage stage = new Stage();
        MatchEvent matchEvent = new MatchEvent();

        em.persist(user);
        em.persist(team);
        em.persist(stage);
        em.persist(matchEvent);

        em.persist(matchEvent);
        em.getTransaction().commit();
    }
}
