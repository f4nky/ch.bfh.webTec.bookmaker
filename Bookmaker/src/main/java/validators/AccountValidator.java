package validators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Every gambler has a account with money. This account has a saldo and the gambler can use this saldo for bets.
 * A gambler can charge his saldo with a credit card transaction. This class includes a method to validate the
 * form inputs from the gambler when he wants to charge his account.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  01.01.2016 Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 01.01.2016
 */
public class AccountValidator {

    /**
     * The default length of a String-type column in the database (Varchar(255)).
     */
    private static final int MAX_VARCHAR_LENGTH = 255;

    /**
     * Validates the form inputs when the gambler wants to charge his account with a credit card transaction.
     * It is just possible to charge the account with the following credit card:
     * number: 1234-1234-1234-1234
     * cvv: 234
     *
     * This method validates as follows:
     * amount:
     * - not empty
     * - number greater than 0
     *
     * credit card holder name:
     * - not empty
     * - not longer than 255 chars
     *
     * credit card number:
     * - not empty
     * - only "1234-1234-1234-1234" allowed
     *
     * cvv:
     * - not empty
     * - integer number
     * - 3 or 4 characters long
     * - only "234" allowed
     *
     * expire month:
     * - not empty / not 0
     * - not in the past
     * - integer number between 1 and 12
     *
     * expire year:
     * - not empty / not 0
     * - not in the past
     * - not greater than 5 years in the future. Maximal period of validity for credit cards is 5 years.
     * - combination of year and month should not be in the past and not greater than 5 years in the future.
     *
     * @param amount Amount he wants to charge.
     * @param cardHolderName Entered name for the credit card holder.
     * @param cardNumber Entered credit card number.
     * @param cvv Entered security code.
     * @param expireMonth Entered month when the credit card expired.
     * @param expireYear Entered year when the credit card expired.
     * @return List of the occurred validation faults. Empty if no validation fault occurred.
     * @since 01.01.2016
     */
    public List<ValidationFault> validateChargeAccount(String amount, String cardHolderName, String cardNumber, String cvv, byte expireMonth, int expireYear) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate amount
        String amountName = "amount";
        if (amount == null || amount.isEmpty()) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.EMTPY_CODE));
        } else if (!isNumeric(amount) || (isNumeric(amount) && Double.parseDouble(amount) <= 0)) {
            validationFaults.add(new ValidationFault(amountName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        //Validate card holder name
        String cardHolderNameName = "name";
        if (cardHolderName == null || cardHolderName.isEmpty()) {
            validationFaults.add(new ValidationFault(cardHolderNameName, ValidationFault.EMTPY_CODE));
        }  else if (cardHolderName.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(cardHolderNameName, ValidationFault.TO_LONG_CODE));
        }

        //Validate card number
        String cardNumberName = "cardNumber";
        if (cardNumber == null || cardNumber.isEmpty()) {
            validationFaults.add(new ValidationFault(cardNumberName, ValidationFault.EMTPY_CODE));
        } else if (!cardNumber.equals("1234-1234-1234-1234")) {
            validationFaults.add(new ValidationFault(cardNumberName, ValidationFault.WRONG_NUMBER));
        }

        //Validate expire month
        String expireMonthName = "expireMonth";
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        boolean hasMonthErrors = true;
        if (expireMonth == 0) {
            validationFaults.add(new ValidationFault(expireMonthName, ValidationFault.EMTPY_CODE));
        } else if (expireMonth < 1 || expireMonth > 12) {
            validationFaults.add(new ValidationFault(expireMonthName, ValidationFault.INCORRECT_CHAR_CODE));
        } else {
            hasMonthErrors = false;
        }

        //Validate expire year
        String expireYearName = "expireYear";
        if (expireYear == 0) {
            validationFaults.add(new ValidationFault(expireYearName, ValidationFault.EMTPY_CODE));
        } else if (expireYear < currentYear) {
            validationFaults.add(new ValidationFault(expireYearName, ValidationFault.TO_SHORT_CODE));
        } else if (expireYear > currentYear + 5) {
            validationFaults.add(new ValidationFault(expireYearName, ValidationFault.TO_LONG_CODE));
        } else if (hasMonthErrors == false && expireYear == currentYear && expireMonth < currentMonth) {
            validationFaults.add(new ValidationFault(expireMonthName, ValidationFault.TO_SHORT_CODE));
        } else if (hasMonthErrors == false && expireYear  == currentYear + 5 && expireMonth > currentMonth) {
            validationFaults.add(new ValidationFault(expireMonthName, ValidationFault.TO_LONG_CODE));
        }

        //Validate cvv
        String cvvName = "cvv";
        if (cvv == null || cvv.isEmpty()) {
            validationFaults.add(new ValidationFault(cvvName, ValidationFault.EMTPY_CODE));
        } else if (!isInteger(cvv)) {
            validationFaults.add(new ValidationFault(cvvName, ValidationFault.INCORRECT_CHAR_CODE));
        } else if (cvv.length() < 3) {
            validationFaults.add(new ValidationFault(cvvName, ValidationFault.TO_SHORT_CODE));
        } else if (cvv.length() > 4) {
            validationFaults.add(new ValidationFault(cvvName, ValidationFault.TO_LONG_CODE));
        } else if (!cvv.equals("234")) {
            validationFaults.add(new ValidationFault(cvvName, ValidationFault.WRONG_NUMBER));
        }

        return validationFaults;
    }

    /**
     * Checks if a string is a number or not.
     * True = number, false = no number.
     *
     * @param stringToCheck String to check.
     * @return True =  number, false = no number.
     * @since 01.01.2016
     */
    private static boolean isNumeric(String stringToCheck) {
        return stringToCheck.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Checks if a string is an integer number or not.
     * True = integer number, false = not an integer number.
     *
     * @param stringToCheck String to check.
     * @return True = integer number, false = not an integer number
     * @since 01.01.2015
     */
    private static boolean isInteger(String stringToCheck) {
        return stringToCheck.matches("^\\d+$");
    }
}
