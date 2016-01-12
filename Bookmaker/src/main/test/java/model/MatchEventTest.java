package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class MatchEventTest {

    /**
     * Tests the creation of a match in the database.
     * @since 22.12.2015
     */
    @Test
    public void testCreate() throws ParseException {
        Date tmpDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("10.06.2016 21:00");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();

        Stage stage = new Stage();
        stage.setNameEn("Group stage");

        Team teamHome = new Team();
        teamHome.setTeamNr("A1");
        teamHome.setNameEn("France");
        teamHome.setCountryCode("fra");

        Team teamAway = new Team();
        teamAway.setTeamNr("A2");
        teamAway.setNameEn("Romania");
        teamAway.setCountryCode("rou");

        em.persist(stage);
        em.persist(teamHome);
        em.persist(teamAway);

        MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventNr("1");
        matchEvent.setMatchEventDateTime(tmpDate);
        matchEvent.setStage(stage);
        matchEvent.setMatchEventGroup("A");
        matchEvent.setTeamHome(teamHome);
        matchEvent.setTeamAway(teamAway);

        em.persist(matchEvent);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a match from the database.
     * @since 22.12.2015
     */
    @Test
    public void testRead() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchEvent> matchEvents = em.createQuery("select m from MatchEvent m").getResultList();
        MatchEvent matchEvent = matchEvents.get(0);
        assert(matchEvent.getMatchEventDateTime().equals(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("10.06.2016 21:00")));
    }

    /**
     * Tests the update of a match in the database.
     * @since 22.12.2015
     */
    @Test
    public void testUpdate() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchEvent> matchEvents = em.createQuery("select m from MatchEvent m").getResultList();
        MatchEvent matchEvent = matchEvents.get(0);

        em.getTransaction().begin();
        {
            matchEvent.setMatchEventDateTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2010 23:59"));
        }
        em.getTransaction().commit();

        //assertTrue(matchEvent.getMatchDateTime().equals(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2020 23:59")));
    }

    /**
     * Tests the deletion of a match in the database.
     * @since 22.12.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchEvent> matchEvents = em.createQuery("select m from MatchEvent m").getResultList();
        MatchEvent matchEvent = matchEvents.get(0);

        em.getTransaction().begin();
        {
            em.remove(matchEvent);
        }
        em.getTransaction().commit();
    }
}
