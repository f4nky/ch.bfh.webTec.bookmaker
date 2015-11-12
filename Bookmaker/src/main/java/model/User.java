package model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model for user-objects.
 * Is a jpa entity (jpa annotations) to get, store and update user in the database.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	29.10.2015	Michael Fankhauser  Class created.
 * 1.1  12.11.2015  Joel Holzer         Format code, Added comments.
 * </pre>
 *
 * @author Michael Fankhauser, Joel Holzer
 * @version 1.1
 * @since 12.11.2015
 */
@Entity
public class User {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String email;
    @Basic
    private String firstName;
    @Basic
    private String lastName;
    @Basic
    private String password;
    @Basic
    private Boolean isAdmin;

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
     * Returns the isAdmin-Flag of the user.
     * If this flag is set to true, then the user is an admin, else the user is a normal user.
     * @return isAdmin-Flag of the user. true = admin user, false = normal user.
     * @since 29.10.2015
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets the isAdmin-Flag of the user.
     * If this flag is set to true, then the user is an admin, else the user is a normal user.
     * @param isAdmin Value to set for the isAdmin-Flag. true = Admin user, false = normal user.
     * @since 29.10.2015
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
