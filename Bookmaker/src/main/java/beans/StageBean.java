package beans;

import dao.StageDao;
import model.Stage;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

/**
 * Bean to obtain a list of teams to fill dropdown lists.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	11.01.2016	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 11.01.2016
 */
@ManagedBean(name = "stageBean")
@RequestScoped
public class StageBean {

    private List<Stage> stages;

    /**
     * Gets all the stages from the database and returns them.
     *
     * @return A list of stages
     * @since 11.01.2016
     */
    @PostConstruct
    public List<Stage> getStages() {
        return StageDao.getInstance().getStages();
    }
}
