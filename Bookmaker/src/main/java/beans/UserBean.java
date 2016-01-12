package beans;

import dao.UserDao;
import helpers.LanguageHelper;
import helpers.PasswordHasher;
import model.User;
import validators.UserValidator;
import validators.ValidationFault;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * This bean is used for actions concerning users, e.g. to login a user on the system, to logout a user, to change
 * the profile data, to register or to get all the users of the system.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	12.11.2015	Joel Holzer  Class created.
 * 1.1  23.12.2015  Joel Holzer  Added register fields (member variables) and methods. Added validation and translation for login.
 * 1.2  28.12.2015  Joel Holzer  Added fields (member variables) and methods for profile.
 * 1.3  01.01.2016  Joel Holzer  Added field for the saldo of the user account.
 * 1.4  05.01.2016  Michael Fankhauser Added method for reading all users from database.
 * </pre>
 *
 * @author Joel Holzer, Michael Fankhauser
 * @version 1.4
 * @since 05.01.2016
 */
@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordRepetition;
    private double saldo;
    private List<User> users;

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    private String loginValidationMessage = null;
    private String registerErrorMessage = null;
    private String registerSuccessMessage = null;
    private String profileErrorMessage = null;
    private String profileSuccessMessage = null;

    private static final String REGISTER_FORM_NAME = "register";
    private static final String PROFILE_FORM_NAME = "profile";

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

    public double getSaldo() {
        return saldo;
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

    public String getProfileErrorMessage() {
        return profileErrorMessage;
    }

    public void setProfileErrorMessage(String profileErrorMessage) {
        this.profileErrorMessage = profileErrorMessage;
    }

    public String getProfileSuccessMessage() {
        return profileSuccessMessage;
    }

    public void setProfileSuccessMessage(String profileSuccessMessage) {
        this.profileSuccessMessage = profileSuccessMessage;
    }

    /**
     * Constructor.
     * Initializes the member variables email, firstName, lastName, password, passwordRepetition and saldo with the
     * data of the logged in user (if a user is logged in).
     * @since 12.11.2015
     */
    public UserBean() {
        User loggedInUser = SessionBean.getUser();
        if (loggedInUser != null) {
            email = loggedInUser.getEmail();
            firstName = loggedInUser.getFirstName();
            lastName = loggedInUser.getLastName();
            password = loggedInUser.getPassword();
            passwordRepetition = loggedInUser.getPassword();
            saldo = loggedInUser.getSaldo();
        }
    }

    /**
     * Gets all registered user from the database and returns them.
     *
     * @return A list of users
     * @since 05.01.2016
     */
    @PostConstruct
    public List<User> getUsers() {
        return UserDao.getInstance().getUsers();
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
    public void register() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //Validate input data
        User user = new User(email, firstName, lastName, password, false);
        UserValidator userValidator = new UserValidator();
        List<ValidationFault> validationFaults = userValidator.validateRegister(user, passwordRepetition);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Encrypt password
            user.setPassword(PasswordHasher.generatePasswordHash(password));
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
    public void login() throws IOException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //Encrypt password
        password = PasswordHasher.generatePasswordHash(password);
        User user = UserDao.getInstance().getUserByEmailPassword(email, password);

        //User exists in database
        if (user != null) {
            SessionBean.setUser(user);

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
     * Saves the profile of the user with the input data of the profile form.
     * Before saving the profile data, the input data are validated and if a validation error occurred, the validation
     * message are displayed.
     *
     * @since 28.12.2015
     */
    public void saveProfile() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //Validate input data
        User oldUserData = SessionBean.getUser();
        User newUserData = new User(oldUserData.getId(), email, firstName, lastName, password, oldUserData.getIsManager());

        UserValidator userValidator = new UserValidator();
        List<ValidationFault> validationFaults = userValidator.validateProfile(newUserData, passwordRepetition);

        if (validationFaults.size() == 0) {
            //No validation faults
            //Encrypt password if changed
            if (password != null && !password.isEmpty()) {
                newUserData.setPassword(PasswordHasher.generatePasswordHash(password));
            } else {
                newUserData.setPassword(oldUserData.getPassword());
            }

            SessionBean.setUser(newUserData);
            //Save user in database and display success message
            UserDao.getInstance().updateUser(newUserData);
            profileErrorMessage = null;
            profileSuccessMessage = LanguageHelper.getTranslation("form_profile_correct");
        } else {
            //Validation faults -> display validation faults
            profileErrorMessage = LanguageHelper.createValidationFaultOutput(PROFILE_FORM_NAME, validationFaults);
        }
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
