package dao;

import model.Stage;

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
 * 1.0	11.01.2016	Michael Fankhauser         Class created.
 * 1.1  12.01.2016  Michael Fankhauser         Added method {@link #findStageById(Long)}
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 12.01.2016
 */
public class StageDao {

    private static StageDao instance;
    private EntityManager entityManager;

    /**
     * Returns the active instance of this class. If no instance exists, create a new one.
     * Singleton pattern, only one instance of this class can exists.
     *
     * @return Active instance.
     * @since 22.12.2015
     */
    public static StageDao getInstance() {
        if (instance == null) {
            instance = new StageDao();
            instance.entityManager = Persistence.createEntityManagerFactory("BMUnit").createEntityManager();
        }
        return instance;
    }

    /**
     * Gets all stages from the database and returns them.
     * @return A List of Stages
     * @since 11.01.2016
     */
    public List<Stage> getStages() {
        Query query = entityManager.createQuery("SELECT s FROM " + Stage.TABLE_NAME + " s");
        List<Stage> stages = query.getResultList();
        return stages;
    }

    /**
     * Gets a single stage by id.
     *
     * @return A single stage
     * @since 12.01.2016
     */
    public Stage findStageById(Long stageId) {
        return entityManager.find(Stage.class, stageId);
    }
}
