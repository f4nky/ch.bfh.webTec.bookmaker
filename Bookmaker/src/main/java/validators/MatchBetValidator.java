package validators;

import model.MatchBet;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods to validate {@link model.MatchBet}-objects created by form inputs.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  28.12.2015  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 28.12.2015
 */
public class MatchBetValidator {

    private static final int MAX_VARCHAR_LENGTH = 255;

    /**
     * Validates an {@link MatchBet}-object when the match bet will be added.
     * Validates the description de, description en, description fr, description it and the odds.
     *
     * All descriptions are validated as follows:
     * - not empty
     * - is not longer than 255 chars
     *
     * Odds is validated as follows:
     * - not empty
     * - correct format (decimal, like 5.4)
     *
     * @param matchBetToValidate Match bet to validate the fields.
     * @param odds Odds to validate. The odds is given as String because the input in the form field is a string. When
     *             the input in the form field is a double, it is not possible to validate with this validator and
     *             return the validation faults with ajax...
     * @return List of the occurred validation faults. Empty if no validation fault occurred.
     * @since 28.12.2015
     */
    public List<ValidationFault> validateAddMatchBet(MatchBet matchBetToValidate, String odds) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate Description EN
        String descriptionEn = matchBetToValidate.getDescriptionEn();
        String desctriptionEnName = "descEn";
        if (descriptionEn == null || descriptionEn.isEmpty()) {
            validationFaults.add(new ValidationFault(desctriptionEnName, ValidationFault.EMTPY_CODE));
        } else if (descriptionEn.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(desctriptionEnName, ValidationFault.TO_LONG_CODE));
        }

        //Validate Description DE
        String descriptionDe = matchBetToValidate.getDescriptionDe();
        String descriptionDeName = "descDe";
        if (descriptionDe == null || descriptionDe.isEmpty()) {
            validationFaults.add(new ValidationFault(descriptionDeName, ValidationFault.EMTPY_CODE));
        } else if (descriptionDe.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(descriptionDeName, ValidationFault.TO_LONG_CODE));
        }

        //Validate Description FR
        String descriptionFr = matchBetToValidate.getDescriptionFr();
        String descriptionFrName = "descFr";
        if (descriptionFr == null || descriptionFr.isEmpty()) {
            validationFaults.add(new ValidationFault(descriptionFrName, ValidationFault.EMTPY_CODE));
        } else if (descriptionFr.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(descriptionFrName, ValidationFault.TO_LONG_CODE));
        }

        //Validate Description IT
        String descriptionIt = matchBetToValidate.getDescriptionIt();
        String descriptionItName = "descIt";
        if (descriptionIt == null || descriptionIt.isEmpty()) {
            validationFaults.add(new ValidationFault(descriptionItName, ValidationFault.EMTPY_CODE));
        } else if (descriptionIt.length() > MAX_VARCHAR_LENGTH) {
            validationFaults.add(new ValidationFault(descriptionItName, ValidationFault.TO_LONG_CODE));
        }

        //Validate odds
        String oddsName = "odds";
        if (odds == null || odds.isEmpty()) {
            validationFaults.add(new ValidationFault(oddsName, ValidationFault.EMTPY_CODE));
        } else if (!isNumeric(odds)) {
            validationFaults.add(new ValidationFault(oddsName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        return validationFaults;
    }

    /**
     * Checks if a string is a decimal number or not.
     * True = decimal number, false = no decimal number.
     *
     * @param stringToCheck String to check.
     * @return True = decimal number, false = no decimal number.
     * @since 28.12.2015
     */
    private static boolean isNumeric(String stringToCheck) {
        return stringToCheck.matches("-?\\d+(\\.\\d+)?");
    }
}
