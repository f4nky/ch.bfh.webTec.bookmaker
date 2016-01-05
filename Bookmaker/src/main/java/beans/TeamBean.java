package beans;

import dao.TeamDao;
import model.Team;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

/**
 * Bean to obtain a list of teams to fill dropdown lists.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	05.01.2016	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 05.01.2016
 */
@ManagedBean(name = "teamBean")
@RequestScoped
public class TeamBean {

    private List<Team> teams;

    /**
     * Gets all the teams from the database and returns them.
     *
     * @return A list of teams
     * @since 05.01.2016
     */
    @PostConstruct
    public List<Team> getTeams() {
        return TeamDao.getInstance().getTeams();
    }
}
