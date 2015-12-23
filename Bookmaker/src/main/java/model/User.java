package model;

import javax.persistence.*;

/**
 * Model for user-objects.
 * Is a jpa entity (jpa annotations) to get, store and update user in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	29.10.2015	Michael Fankhauser  Class created.
 * 1.1  12.11.2015  Joel Holzer         Format code, Added comments.
 * 1.2  23.12.2015  Joel Holzer         Added new constructors.
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.2
 * @since 23.12.2015
 */
@Entity
public class User {

    public static final String TABLE_NAME = "User";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_PASSWORD = "password";

    @GeneratedValue
    @Id
    private Long id;
    @Basic
    @Column(name = COLUMN_NAME_EMAIL)
    private String email;
    @Basic
    private String firstName;
    @Basic
    private String lastName;
    @Basic
    @Column(name = COLUMN_NAME_PASSWORD)
    private String password;
    @Basic
    private Boolean isManager;

    /**
     * Empty constructor.
     * Needed from jpa.
     * @since 23.12.2015
     */
    public User() {

    }

    /**
     * Constructor to create a user and initialize the member variables.
     * @since 23.12.2015
     */
    public User(String email, String firstName, String lastName, String password, Boolean isManager) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isManager = isManager;
    }

    /**
     * Returns the unique id of the user.
     * @return Unique id of the user.
     * @since 29.10.2015
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the email of the user.
     * @return Email of the user.
     * @since 29.10.2015
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email Email to set.
     * @since 29.10.2015
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the first name of the user.
     * @return First name of the user.
     * @since 29.10.2015
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName First name to set.
     * @since 29.10.2015
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     * @return Last name of the user.
     * @since 29.10.2015
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName Last name to set.
     * @since 29.10.2015
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the password of the user.
     * @return Password of the user.
     * @since 29.10.2015
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password Password to set.
     * @since 29.10.2015
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the isManager-Flag of the user.
     * If this flag is set to true, then the user is a manager, else the user is a normal user.
     * @return isManager-Flag of the user. true = manager user, false = normal user.
     * @since 29.10.2015
     */
    public Boolean getIsManager() {
        return isManager;
    }

    /**
     * Sets the isManager-Flag of the user.
     * If this flag is set to true, then the user is a manager, else the user is a normal user.
     * @param isManager Value to set for the isManager-Flag. true = Manager user, false = normal user.
     * @since 29.10.2015
     */
    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }
}
