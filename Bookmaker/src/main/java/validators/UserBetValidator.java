package validators;

import beans.SessionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods to validate user bets created by form inputs.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  29.12.2015  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 29.12.2015
 */
public class UserBetValidator {

    /**
     * To add a user bet its just required to enter the amount which wants to set for a bet.
     * This method validates the entered amount.
     * The entered amount is validated as follows:
     * - not empty
     * - is a number
     * - greater than 0
     * - not greater than the saldo of the user.
     *
     * @param amountOfUserBet The entered amount (amount which wants to set for the bet).
     * @return List of the occurred validation faults. Empty list if no validation fault occurred.
     * @since 29.12.2015
     */
    public List<ValidationFault> validateAddUserBet(String amountOfUserBet) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate amount
        String amountName = "amount";
        if (amountOfUserBet == null || amountOfUserBet.isEmpty() || (isNumeric(amountOfUserBet) && Integer.parseInt(amountOfUserBet) <= 0)) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.EMTPY_CODE));
        } else if (!isNumeric(amountOfUserBet)) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (Double.parseDouble(amountOfUserBet) > SessionBean.getUser().getSaldo()) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.TO_SHORT_CODE));
        }
        return validationFaults;
    }

    /**
     * Checks if a string is a number or not.
     * True = number, false = no number.
     *
     * @param stringToCheck String to check.
     * @return True = number, false = no number.
     * @since 29.12.2015
     */
    private static boolean isNumeric(String stringToCheck) {
        return stringToCheck.matches("-?\\d+(\\.\\d+)?");
    }
}
