package model;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.text.ParseException;

public class UserTest {

    @Test
    public void test() throws ParseException {
        User user = new User();
        user.setEmail("test@test.ch");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("test");

        EntityManager em = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
}
