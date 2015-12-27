package beans;

import dao.MatchBetDao;
import dao.MatchEventDao;
import model.MatchBet;
import model.MatchEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Fanky on 26.12.15.
 */
@ManagedBean(name = "matchBetBean")
@ViewScoped
public class MatchBetBean implements Serializable {
    private Long matchEventId;
    private String descriptionEn;
    private String descriptionDe;
    private String descriptionFr;
    private String descriptionIt;
    private Double odds;
    private List<MatchBet> matchBets;

    public Long getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(Long matchEventId) {
        this.matchEventId = matchEventId;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
    }

    public String getDescriptionIt() {
        return descriptionIt;
    }

    public void setDescriptionIt(String descriptionIt) {
        this.descriptionIt = descriptionIt;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public List<MatchBet> getMatchBets() {
        return MatchBetDao.getInstance().getMatchBets(matchEventId);
    }

    public void createMatchBet() {
        MatchEvent matchEvent = MatchEventDao.getInstance().findMatchEventById(matchEventId);

        MatchBet matchBet = new MatchBet();
        matchBet.setMatchEventId(matchEvent);
        matchBet.setDescriptionEn(descriptionEn);
        matchBet.setDescriptionDe(descriptionDe);
        matchBet.setDescriptionFr(descriptionFr);
        matchBet.setDescriptionIt(descriptionIt);
        matchBet.setOdds(odds);

        MatchBetDao.getInstance().createMatchBet(matchBet);
    }
}
