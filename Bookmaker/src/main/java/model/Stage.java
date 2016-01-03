package model;

import javax.persistence.*;

/**
 * Model for stage-objects. The different stages in the euro for examples are the group stage, quarter finals, etc.
 * Every match assigned to a stage.
 * Is a jpa entity (jpa annotations) to get, store and update user in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	25.12.2015	Michael Fankhauser  Class created.
 * </pre>
 *
 * @author Michael Fankhauser
 * @version 1.0
 * @since 25.12.2015
 */
@Entity
public class Stage {

    public static final String TABLE_NAME = "Stage";
    public static final String COLUMN_NAME_NAME_EN = "nameEn";
    public static final String COLUMN_NAME_NAME_DE = "nameDe";
    public static final String COLUMN_NAME_NAME_FR = "nameFr";
    public static final String COLUMN_NAME_NAME_IT = "nameIt";

    @GeneratedValue
    @Id
    private Long id;
    @Basic
    @Column(name= COLUMN_NAME_NAME_EN)
    private String nameEn;
    @Basic
    @Column(name= COLUMN_NAME_NAME_DE)
    private String nameDe;
    @Basic
    @Column(name= COLUMN_NAME_NAME_FR)
    private String nameFr;
    @Basic
    @Column(name= COLUMN_NAME_NAME_IT)
    private String nameIt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameIt() {
        return nameIt;
    }

    public void setNameIt(String nameIt) {
        this.nameIt = nameIt;
    }
}
