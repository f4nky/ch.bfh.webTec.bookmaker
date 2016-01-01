package validators;

import beans.SessionBean;
import model.MatchBet;
import model.UserBet;

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
     *
     * @param amountOfUserBet
     * @return
     * @since 29.12.2015
     */
    public List<ValidationFault> validateAddUserBet(String amountOfUserBet) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate amount
        String amountName = "amount";
        if (amountOfUserBet == null || amountOfUserBet.isEmpty()) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.EMTPY_CODE));
        } else if (!isNumeric(amountOfUserBet)) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (Double.parseDouble(amountOfUserBet) > SessionBean.getUser().getSaldo()) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.TO_SHORT_CODE));
        }
        return validationFaults;
    }

    /**
     * Checks if a string is a decimal number or not.
     * True = decimal number, false = no decimal number.
     *
     * @param stringToCheck String to check.
     * @return True = decimal number, false = no decimal number.
     * @since 29.12.2015
     */
    private static boolean isNumeric(String stringToCheck) {
        return stringToCheck.matches("-?\\d+(\\.\\d+)?");
    }
}
