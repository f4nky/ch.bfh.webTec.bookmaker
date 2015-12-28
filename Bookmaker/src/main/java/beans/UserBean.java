package beans;

import dao.UserDao;
import helpers.LanguageHelper;
import helpers.PasswordHasher;
import model.User;
import validators.UserValidator;
import validators.ValidationFault;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
    private String registerErrorMessage = null;
    private String registerSuccessMessage = null;

    private static final String REGISTER_FORM_NAME = "register";

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

    public String getRegisterErrorMessage() {
        return registerErrorMessage;
    }

    public void setRegisterErrorMessage(String registerErrorMessage) {
        this.registerErrorMessage = registerErrorMessage;
    }

    public String getRegisterSuccessMessage() {
        return registerSuccessMessage;
    }

    public void setRegisterSuccessMessage(String registerSuccessMessage) {
        this.registerSuccessMessage = registerSuccessMessage;
    }

    /**
     * Registers a new user (gambler)
     * Firstly, the given user data are validated. If a validation error occurred, the user is not saved in the database
     * and the validation errors are displayed. For validation, see {@link UserValidator}.
     * If the user data are correct, the method encrypts the given password with sha-256, saves the user in the database
     * and displays a successful-message.
     *
     * This method is called with ajax, because the register dialog is a modal form dialog and the page should not load
     * new when a validation error occurred.
     *
     * @since 23.12.2015
     */
    public void register() {
        //Validate input data
        user = new User(email, firstName, lastName, password, false);
        UserValidator userValidator = new UserValidator();
        List<ValidationFault> validationFaults = userValidator.validateRegister(user, passwordRepetition);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Encrypt password
            try {
                user.setPassword(PasswordHasher.generatePasswordHash(user.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                //TODO exception handling
            } catch (UnsupportedEncodingException e) {
                //TODO exception handling
            }
            //Save user in database and display success message
            UserDao.getInstance().createUser(user);
            registerErrorMessage = null;
            registerSuccessMessage = LanguageHelper.getTranslation("form_register_correct");
        } else {
            //Validation faults -> display validation faults
            registerErrorMessage = LanguageHelper.createValidationFaultOutput(REGISTER_FORM_NAME, validationFaults);
        }
    }

    /**
     * Logs in the user (gambler) or the manager and redirect to the home page of the manager or user depends on the role of the
     * logged in user.
     * Before the login is done, the user with the given email and password is got from the database.
     * If the user does not exist in the database, the user data are incorrect and the user is not logged in and a
     * validation error is displayed.
     *
     * This method is called with ajax, because the login dialog is a modal form dialog and the page should not load new
     * when a validation error occurred.
     *
     * @since 12.11.2015, Updated: 23.12.2015
     */
    public void login() throws IOException {
        //Encrypt password
        try {
            password = PasswordHasher.generatePasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            //TODO exception handling
        } catch (UnsupportedEncodingException e) {
            //TODO exception handling
        }
        user = UserDao.getInstance().getUserByEmailPassword(email, password);

        if (user != null) {
            //User exists in database
            HttpSession session = SessionBean.getSession();
            session.setAttribute(SessionBean.USER_KEY, user);

            if (user.getIsManager()) {
                navigationBean.redirectToManagerHome();
            } else {
                navigationBean.redirectToUserHome();
            }
        }
        //No user exists in database -> display login incorrect message
        loginValidationMessage = LanguageHelper.getTranslation("form_login_incorrect");
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
