package validators;

import dao.UserDao;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holzer on 23.12.2015.
 */
public class UserValidator {

    private static final int MAX_VARCHAR_LENGTH = 255;

    public List<ValidationFault> validateRegister(User userToValidate, String passwordRepetition) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Check email
        String email = userToValidate.getEmail();
        String emailName = "email";
        if (email == null || email.isEmpty()) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.EMTPY_CODE));
        } else if (!email.contains("@")) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (email.length() < 5) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_SHORT_CODE));
        } else if (UserDao.getInstance().checkIfUserWithEmailExists(email)) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.ALREADY_EXISTS_CODE));
        } else if (email.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(emailName, ValidationFault.TO_LONG_CODE));
        }

        //Check first name
        String firstName = userToValidate.getFirstName();
        String firstNameName = "firstname";
        if (firstName == null || firstName.isEmpty()) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.EMTPY_CODE));
        } else if (firstName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(firstNameName, ValidationFault.TO_LONG_CODE));
        }

        //Check last name
        String lastName = userToValidate.getLastName();
        String lastNameName = "lastname";
        if (lastName == null || lastName.isEmpty()) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.EMTPY_CODE));
        } else if (lastName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(lastNameName, ValidationFault.TO_LONG_CODE));
        }

        //Check password
        String password = userToValidate.getPassword();
        String passwordName = "password";
        if (password == null || password.isEmpty()) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.EMTPY_CODE));
        } else if (password.length() < 6) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_SHORT_CODE));
        } else if (password.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(passwordName, ValidationFault.TO_LONG_CODE));
        }

        //Check password repetition
        String passwordRepetitionName = "password2";
        if (passwordRepetition == null || passwordRepetition.isEmpty()) {
            validationFaults.add(new ValidationFault(passwordRepetitionName, ValidationFault.EMTPY_CODE));
        } else if (!passwordRepetition.equals(password)) {
            validationFaults.add(new ValidationFault(passwordRepetitionName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        return validationFaults;
    }
}
