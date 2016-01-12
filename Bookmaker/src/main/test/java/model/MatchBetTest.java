package model;

import beans.MatchBetBean;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for the matchBet orm.model class.
 * Tests the crud db-operations (create, read, update, delete).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser  Class created.
 * 1.1  26.12.2015  Michael Fankhauser  Added methods to test read, update and delete.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 26.12.2015
 */
public class MatchBetTest {

    /**
     * Tests the creation of a matchBet in the database.
     * @since 26.12.2015
     */
    @Test
    public void testCreate() throws ParseException, IOException {
        Date tmpDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("10.06.2016 21:00");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();

        MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventNr("1");
        matchEvent.setMatchEventDateTime(tmpDate);

        em.persist(matchEvent);

        MatchBetBean matchBet = new MatchBetBean();
        matchBet.setMatchEventId(1L);
        matchBet.setDescriptionEn("Switzerland leads after 90'");
        matchBet.setOdds("3.0");

        matchBet.createMatchBet();

        //em.persist(matchBet);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a matchBet from the database.
     * @since 26.12.2015
     */
    @Test
    public void testRead() throws ParseException, IOException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchBet> matchBets = em.createQuery("select mb from MatchBet mb").getResultList();
        MatchBet matchBet = matchBets.get(0);
        assertEquals(matchBet.getDescriptionEn(), "Switzerland leads after 90'");
    }

    /**
     * Tests the update of a matchBet in the database.
     * @since 26.12.2015
     */
    @Test
    public void testUpdate() throws ParseException, IOException  {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchBet> matchBets = em.createQuery("select mb from MatchBet mb").getResultList();
        MatchBet matchBet = matchBets.get(0);

        em.getTransaction().begin();
        {
            matchBet.setDescriptionEn("Spain wins");
        }
        em.getTransaction().commit();

        assertTrue(matchBet.getDescriptionEn().equals("Spain wins"));
    }

    /**
     * Tests the deletion of a matchBet in the database.
     * @since 26.12.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<MatchBet> matchBets = em.createQuery("select mb from MatchBet mb").getResultList();
        MatchBet matchBet = matchBets.get(0);

        em.getTransaction().begin();
        {
            em.remove(matchBet);
        }
        em.getTransaction().commit();
    }
}
