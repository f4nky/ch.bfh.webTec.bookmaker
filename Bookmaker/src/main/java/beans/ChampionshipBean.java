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
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.1
 * @since 23.12.2015
 */
@ManagedBean(name = "championship")
@RequestScoped
public class ChampionshipBean implements Serializable {
    private List<MatchEvent> matchEventsComing;
    private List<MatchEvent> matchEventsPast;

    @PostConstruct
    public List<MatchEvent> getMatchEventsComing() {
        return MatchEventDao.getInstance().getMatchesComing();
    }

    @PostConstruct
    public List<MatchEvent> getMatchEventsPast() {
        return MatchEventDao.getInstance().getMatchesPast();
    }
}