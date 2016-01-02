package validators;

import model.MatchBet;
import model.MatchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods to validate match events actions.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  02.01.2016  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 02.01.2016
 */
public class MatchEventValidator {

    /**
     *
     * @param scoreHome
     * @param scoreAway
     * @return
     * @since 02.01.2016
     */
    public List<ValidationFault> validateFinishMatchEvent(String scoreHome, String scoreAway) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate score home
        String scoreHomeName = "scoreHome";
        if (scoreHome == null || scoreHome.isEmpty()) {
            validationFaults.add(new ValidationFault(scoreHomeName, ValidationFault.EMTPY_CODE));
        } else if (!isInteger(scoreHome)) {
            validationFaults.add(new ValidationFault(scoreHomeName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        //Validate score away
        String scoreAwayName = "scoreAway";
        if (scoreAway == null || scoreAway.isEmpty()) {
            validationFaults.add(new ValidationFault(scoreAwayName, ValidationFault.EMTPY_CODE));
        } else if (!isInteger(scoreAway)) {
            validationFaults.add(new ValidationFault(scoreAwayName, ValidationFault.INCORRECT_CHAR_CODE));
        }

        return validationFaults;
    }

    /**
     * Checks if a string is an integer number or not.
     * True = integer number, false = not an integer number
     * @param stringToCheck String to check.
     * @return True = integer number, false = not an integer number
     * @since 02.01.2015
     */
    private static boolean isInteger(String stringToCheck) {
        return stringToCheck.matches("^\\d+$");
    }
}
