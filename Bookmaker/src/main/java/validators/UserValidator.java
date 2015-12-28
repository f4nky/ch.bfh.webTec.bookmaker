package validators;

import dao.UserDao;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods to validate {@link User}-objects created by form inputs, e.g. registration.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  23.12.2015  Joel Holzer         Class created.
 * 1.1  28.12.2015  Joel Holzer         Added validation method for profile site.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 28.12.2015
 */
public class UserValidator {

    private static final int MAX_VARCHAR_LENGTH = 255;

    /**
     * Validates an {@link User}-object for the registration.
     * Validates the email, first name, last name, password and password repetition.
     *
     * The email is validated as follows:
     * - not empty
     * - contains @ and .
     * - is longer than 4 chars
     * - is not longer than 255 chars
     * - is not exists for an other user
     *
     * First name and last name are validated as follows:
     * - not empty
     * - not longer than 255 chars
     *
     * Password and password repetition are validated as follows:
     * - not empty
     * - not longer than 255 chars
     * - longer than 5 chars
     * - password and password repetition are equals.
     *
     * @param userToValidate User to validate the fields.
     * @param passwordRepetition Password repetition to compare with the password.
     * @return List of the occurred validation faults. Empty if no validation fault occurred.
     * @since 23.12.2015
     */
    public List<ValidationFault> validateRegister(User userToValidate, String passwordRepetition) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate email
        String email = userToValidate.getEmail();
        String emailName = "email";
        if (email == null || email.isEmpty()) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.EMTPY_CODE));
        } else if (!email.contains("@") && !email.contains(".")) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (email.length() < 5) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_SHORT_CODE));
        } else if (UserDao.getInstance().checkIfUserWithEmailExists(email)) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.ALREADY_EXISTS_CODE));
        } else if (email.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_LONG_CODE));
        }

        //Validate first name
        String firstName = userToValidate.getFirstName();
        String firstNameName = "firstname";
        if (firstName == null || firstName.isEmpty()) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.EMTPY_CODE));
        } else if (firstName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.TO_LONG_CODE));
        }

        //Validate last name
        String lastName = userToValidate.getLastName();
        String lastNameName = "lastname";
        if (lastName == null || lastName.isEmpty()) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.EMTPY_CODE));
        } else if (lastName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.TO_LONG_CODE));
        }

        //Validate password
        String password = userToValidate.getPassword();
        String passwordName = "password";
        if (password == null || password.isEmpty()) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.EMTPY_CODE));
        } else if (password.length() < 6) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_SHORT_CODE));
        } else if (password.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_LONG_CODE));
        }

        //Validate password repetition
        String passwordRepetitionName = "password2";
        if (passwordRepetition == null || passwordRepetition.isEmpty()) {
            validationFaults.add(new ValidationFault(passwordRepetitionName, ValidationFault.EMTPY_CODE));
        } else if (!passwordRepetition.equals(password)) {
            validationFaults.add(new ValidationFault(passwordRepetitionName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        return validationFaults;
    }

    /**
     * Validates an {@link User}-object for the profil update.
     * Validates the email, first name, last name, password and password repetition.
     *
     * The email is validated as follows:
     * - not empty
     * - contains @ and .
     * - is longer than 4 chars
     * - is not longer than 255 chars
     * - is not exists for an other user
     *
     * First name and last name are validated as follows:
     * - not empty
     * - not longer than 255 chars
     *
     * Password and password repetition are validated as follows:
     * - not longer than 255 chars
     * - longer than 5 chars
     * - password and password repetition are equals.
     *
     * @param userToValidate User to validate the fields.
     * @param passwordRepetition Password repetition to compare with the password.
     * @return List of the occurred validation faults. Empty if no validation fault occurred.
     * @since 28.12.2015
     */
    public List<ValidationFault> validateProfile(User userToValidate, String passwordRepetition) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate email
        String email = userToValidate.getEmail();
        String emailName = "email";
        if (email == null || email.isEmpty()) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.EMTPY_CODE));
        } else if (!email.contains("@") && !email.contains(".")) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (email.length() < 5) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_SHORT_CODE));
        } else if (email.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_LONG_CODE));
        }  else if (UserDao.getInstance().checkIfOtherUserWithEmailExists(userToValidate.getId(), email)) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.ALREADY_EXISTS_CODE));
        }

        //Validate first name
        String firstName = userToValidate.getFirstName();
        String firstNameName = "firstname";
        if (firstName == null || firstName.isEmpty()) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.EMTPY_CODE));
        } else if (firstName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.TO_LONG_CODE));
        }

        //Validate last name
        String lastName = userToValidate.getLastName();
        String lastNameName = "lastname";
        if (lastName == null || lastName.isEmpty()) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.EMTPY_CODE));
        } else if (lastName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.TO_LONG_CODE));
        }

        //Validate password and password repetition
        String password = userToValidate.getPassword();
        String passwordName = "password";
        String passwordRepetitionName = "password2";
        if (password != null && !password.isEmpty()) {
            if (password.length() < 6) {
                validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_SHORT_CODE));
            } else if (password.length() > MAX_VARCHAR_LENGTH) {
                validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_LONG_CODE));
            }

           if (!passwordRepetition.equals(password)) {
                validationFaults.add(new ValidationFault(passwordRepetitionName, ValidationFault.INCORRECT_CHAR_CODE));
            }
        }
        return validationFaults;
    }
}
