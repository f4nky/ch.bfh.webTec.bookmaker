package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for the userBet orm.model class.
 * Tests the crud db-operations (create, read, update, delete).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	27.12.2015	Michael Fankhauser  Class created.
 * 1.1  27.12.2015  Michael Fankhauser  Added methods to test read, update and delete.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 27.12.2015
 */
public class UserBetTest {

    /**
     * Tests the creation of a userBet in the database.
     * @since 27.12.2015
     */
    @Test
    public void testCreate() throws ParseException {
        Date tmpDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("10.06.2016 21:00");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        em.getTransaction().begin();

        MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventNr(1);
        matchEvent.setMatchEventDateTime(tmpDate);

        em.persist(matchEvent);

        MatchBet matchBet = new MatchBet();
        matchBet.setDescriptionEn("Switzerland wins.");
        matchBet.setOdds(3.0);

        em.persist(matchBet);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        em.persist(user);

        UserBet userBet = new UserBet();
        userBet.setMatchBetId(matchBet);
        userBet.setUserId(user);
        userBet.setEntryDateTime(new Date());
        userBet.setAmount(100.0);

        em.persist(userBet);
        em.getTransaction().commit();
    }

    /**
     * Tests the read (select) of a userBet from the database.
     * @since 27.12.2015
     */
    @Test
    public void testRead() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<UserBet> userBets = em.createQuery("select ub from UserBet ub").getResultList();
        UserBet userBet = userBets.get(0);
        assertEquals(userBet.getAmount(), new Double(100.0));
    }

    /**
     * Tests the update of a userBet in the database.
     * @since 27.12.2015
     */
    @Test
    public void testUpdate() throws ParseException {
        testCreate();
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<UserBet> userBets = em.createQuery("select ub from UserBet ub").getResultList();
        UserBet userBet = userBets.get(0);

        em.getTransaction().begin();
        {
            userBet.setAmount(99.0);
        }
        em.getTransaction().commit();

        assertTrue(userBet.getAmount().equals(99.0));
    }

    /**
     * Tests the deletion of a userBet in the database.
     * @since 27.12.2015
     */
    @Test
    public void testDelete() {
        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        List<UserBet> userBets = em.createQuery("select ub from UserBet ub").getResultList();
        UserBet userBet = userBets.get(0);

        em.getTransaction().begin();
        {
            em.remove(userBet);
        }
        em.getTransaction().commit();
    }
}
