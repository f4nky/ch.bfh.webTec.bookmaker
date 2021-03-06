package dao;

import model.Team;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Access Object (DAO) for team-objects.
 * Contains methods to read and write team-objects to the database.
 * Singleton-class.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	22.12.2015	Michael Fankhauser         Class created.
 * 1.1  12.11.2016  Michael Fankhauser         Added method {@link #findTeamById(Long)}
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 12.01.2016
 */
public class TeamDao {

    private static TeamDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 22.12.2015
     */
    public static TeamDao getInstance() {
        if (instance == null) {
            instance = new TeamDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets all teams from the database and returns them.
     * @return A List of Teams
     * @since 22.12.2015
     */
    public List<Team> getTeams() {
        Query query = entityManager.createQuery("SELECT t FROM " + Team.TABLE_NAME + " t ORDER BY t.nameEn");
        List<Team> teams = query.getResultList();
        return teams;
    }

    /**
     * Gets a single team by id.
     *
     * @return A single team
     * @since 12.01.2016
     */
    public Team findTeamById(Long teamId) {
        return entityManager.find(Team.class, teamId);
    }
}
