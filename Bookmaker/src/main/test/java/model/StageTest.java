package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for the stage orm.model class.
 * Tests the crud db-operations (create, read, update, delete).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	25.12.2015	Michael Fankhauser  Class created.
 * 1.1  25.12.2015  Michael Fankhauser  Added methods to test read, update and delete.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 25.12.2015
 */
public class StageTest {

    /**
     * Tests the creation of a stage in the database.
     * @since 25.12.2015
     */
    @Test
    public void testCreate() throws ParseException {
        Stage stage = new Stage();
        stage.setNameEn("Group stage");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();
        em.persist(stage);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a stage from the database.
     * @since 25.12.2015
     */
    @Test
    public void testRead() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Stage> stages = em.createQuery("select s from Stage s").getResultList();
        Stage stage = stages.get(0);
        assertEquals(stage.getNameEn(), "Group stage");
    }

    /**
     * Tests the update of a stage in the database.
     * @since 25.12.2015
     */
    @Test
    public void testUpdate() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Stage> stages = em.createQuery("select s from Stage s").getResultList();
        Stage stage = stages.get(0);

        em.getTransaction().begin();
        {
            stage.setNameEn("Round of 16");
        }
        em.getTransaction().commit();

        assertTrue(stage.getNameEn().equals("Round of 16"));
    }

    /**
     * Tests the deletion of a stage in the database.
     * @since 25.12.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<Stage> stages = em.createQuery("select s from Stage s").getResultList();
        Stage stage = stages.get(0);

        em.getTransaction().begin();
        {
            em.remove(stage);
        }
        em.getTransaction().commit();
    }
}
