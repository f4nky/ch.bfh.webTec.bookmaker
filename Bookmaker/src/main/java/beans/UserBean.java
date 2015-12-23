package beans;

import dao.UserDao;
import helpers.LanguageHelper;
import model.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

/**
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	12.11.2015	Joel Holzer  Class created.
 * 1.1  23.12.2015  Joel Holzer  Added register fields (member variables) and methods. Added validation and translation for login.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.1
 * @since 23.12.2015
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

    private User user;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordRepetition;
    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;
    private String loginValidationMessage = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepetition() {
        return passwordRepetition;
    }

    public void setPasswordRepetition(String passwordRepetition) {
        this.passwordRepetition = passwordRepetition;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public String getLoginValidationMessage() {
        return loginValidationMessage;
    }

    public void setLoginValidationMessage(String loginValidationMessage) {
        this.loginValidationMessage = loginValidationMessage;
    }

    /**
     *
     * @return
     * @since 23.12.2015
     */
    public String register() {
        //Validate data
        //TODO validate data and return error messages if not correct

        user = new User(email, firstName, lastName, password, false);
        UserDao.getInstance().createUser(user);

        return "todo";
    }

    /**
     * Logs in the user or the manager and redirect to the home page of the manager or user depends on the role of the
     * logged in user.
     * Before the login is done, the user with the given email and password is got from the database.
     * If the user does not exist in the database, the user data are incorrect and the user is not logged in and a
     * validation error is displayed.
     *
     * @since 12.11.2015, Updated: 23.12.2015
     */
    public void login() throws IOException {
        user = UserDao.getInstance().getUserByEmailPassword(email, password);

        if (user != null) {
            HttpSession session = SessionBean.getSession();
            session.setAttribute(SessionBean.USER_KEY, user);

            if (user.getIsManager()) {
                navigationBean.redirectToManagerHome();
            } else {
                navigationBean.redirectToUserHome();
            }
        }
        loginValidationMessage = LanguageHelper.getTranslation("error_login_incorrect");
    }

    /**
     * Logs out the logged in user (invalidate the session).
     *
     * @return The path of the page to redirect after log out.
     * @since 12.11.2015
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return navigationBean.getStartPageWithIndexForRedirect();
    }
}
