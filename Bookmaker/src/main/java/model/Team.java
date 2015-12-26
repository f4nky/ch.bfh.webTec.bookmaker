package model;

import javax.persistence.*;

/**
 * Model for user-objects.
 * Is a jpa entity (jpa annotations) to get, store and update user in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	22.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 22.12.2015
 */
@Entity
public class Team {

    public static final String TABLE_NAME = "Team";
    public static final String COLUMN_NAME_TEAM_NR = "teamNr";
    public static final String COLUMN_NAME_NAME_EN = "nameEn";
    public static final String COLUMN_NAME_NAME_DE = "nameDe";
    public static final String COLUMN_NAME_NAME_FR = "nameFr";
    public static final String COLUMN_NAME_NAME_IT = "nameIt";
    public static final String COLUMN_NAME_COUNTRY_CODE = "countryCode";

    @GeneratedValue
    @Id
    private Long id;
    @Basic
    @Column(name = COLUMN_NAME_TEAM_NR)
    private String teamNr;
    @Basic
    @Column(name=COLUMN_NAME_NAME_EN)
    private String nameEn;
    @Basic
    @Column(name=COLUMN_NAME_NAME_DE)
    private String nameDe;
    @Basic
    @Column(name=COLUMN_NAME_NAME_FR)
    private String nameFr;
    @Basic
    @Column(name=COLUMN_NAME_NAME_IT)
    private String nameIt;
    @Basic
    @Column(name=COLUMN_NAME_COUNTRY_CODE)
    private String countryCode;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String name_en) {
        this.nameEn = name_en;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String name_de) {
        this.nameDe = name_de;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String name_fr) {
        this.nameFr = name_fr;
    }

    public String getNameIt() {
        return nameIt;
    }

    public void setNameIt(String name_it) {
        this.nameIt = name_it;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTeamNr() {
        return teamNr;
    }

    public void setTeamNr(String teamNr) {
        this.teamNr = teamNr;
    }
}
