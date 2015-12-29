package model;

import javax.persistence.*;

/**
 * Model for matchBet-objects.
 * Is a jpa entity (jpa annotations) to get, store and update user in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	26.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 26.12.2015
 */
@Entity
public class MatchBet {

    public static final String TABLE_NAME = "MatchBet";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_MATCH_EVENT_ID = "matchEventId";
    public static final String COLUMN_NAME_DESC_EN = "descriptionEn";
    public static final String COLUMN_NAME_DESC_DE = "descriptionDe";
    public static final String COLUMN_NAME_DESC_FR = "descriptionFr";
    public static final String COLUMN_NAME_DESC_IT = "descriptionIt";
    public static final String COLUMN_NAME_ODDS = "odds";
    public static final String COLUMN_NAME_IS_ACTIVE = "isActive";

    @GeneratedValue
    @Id
    @Column(name = COLUMN_NAME_ID)
    private Long id;
    @ManyToOne
    @JoinColumn(name = COLUMN_NAME_MATCH_EVENT_ID)
    private MatchEvent matchEventId;
    @Basic
    @Column(name = COLUMN_NAME_DESC_EN)
    private String descriptionEn;
    @Basic
    @Column(name = COLUMN_NAME_DESC_DE)
    private String descriptionDe;
    @Basic
    @Column(name = COLUMN_NAME_DESC_FR)
    private String descriptionFr;
    @Basic
    @Column(name = COLUMN_NAME_DESC_IT)
    private String descriptionIt;
    @Basic
    @Column(name = COLUMN_NAME_ODDS)
    private Double odds;
    @Basic
    @Column(name = COLUMN_NAME_IS_ACTIVE)
    private Boolean isActive = false;

    @Transient
    private double setUserAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchEvent getMatchEventId() {
        return matchEventId;
    }

    public void setMatchEventId(MatchEvent matchEventId) {
        this.matchEventId = matchEventId;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String description) {
        this.descriptionEn = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public String getDescriptionIt() {
        return descriptionIt;
    }

    public void setDescriptionIt(String descriptionIt) {
        this.descriptionIt = descriptionIt;
    }

    public double getSetUserAmount() {
        return setUserAmount;
    }

    public void setSetUserAmount(double setUserAmount) {
        this.setUserAmount = setUserAmount;
    }
}
