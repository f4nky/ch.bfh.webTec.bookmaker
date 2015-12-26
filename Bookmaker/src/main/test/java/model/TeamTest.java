package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for the team orm.model class.
 * Tests the crud db-operations (create, read, update, delete).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	22.12.2015	Michael Fankhauser  Class created.
 * 1.1  22.12.2015  Michael Fankhauser  Added methods to test read, update and delete.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 22.12.2015
 */
public class TeamTest {

    /**
     * Tests the creation of a team in the database.
     * @since 22.12.2015
     */
    @Test
    public void testCreate() throws ParseException {
        Team team = new Team();
        team.setNameEn("France");
        team.setCountryCode("fra");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();
        em.persist(team);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a team from the database.
     * @since 22.12.2015
     */
    @Test
    public void testRead() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Team> teams = em.createQuery("select t from Team t").getResultList();
        Team team = teams.get(0);
        assertEquals(team.getNameEn(), "France");
    }

    /**
     * Tests the update of a team in the database.
     * @since 22.12.2015
     */
    @Test
    public void testUpdate() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Team> teams = em.createQuery("select t from Team t").getResultList();
        Team team = teams.get(0);

        em.getTransaction().begin();
        {
            team.setNameEn("New Team name");
        }
        em.getTransaction().commit();

        assertTrue(team.getNameEn().equals("New Team name"));
    }

    /**
     * Tests the deletion of a team in the database.
     * @since 22.12.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Team> teams = em.createQuery("select t from Team t").getResultList();
        Team team = teams.get(0);

        em.getTransaction().begin();
        {
            em.remove(team);
        }
        em.getTransaction().commit();
    }
}
