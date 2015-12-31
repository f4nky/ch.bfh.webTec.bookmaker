package beans;

import dao.MatchEventDao;
import model.MatchEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	23.12.2015	Michael Fankhauser  Class created.
 * 1.1  25.12.2015  Michael Fankhauser  Methods for coming + finished matches implemented.
 * 1.2  31.12.2015  Joel Holzer Method for matches in progress implemented.
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.2
 * @since 31.12.2015
 */
@ManagedBean(name = "championship")
@RequestScoped
public class ChampionshipBean implements Serializable {
    private List<MatchEvent> matchEventsComing;
    private List<MatchEvent> matchEventsPast;

    /**
     *
     * @return
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsComing() {
        return MatchEventDao.getInstance().getMatchesComing();
    }

    /**
     *
     * @return
     * @since 31.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsInProgress() {
        return MatchEventDao.getInstance().getMatchesInProgress();
    }

    /**
     *
     * @return
     * @since 25.12.2015
     */
    @PostConstruct
    public List<MatchEvent> getMatchEventsPast() {
        return MatchEventDao.getInstance().getMatchesPast();
    }
}