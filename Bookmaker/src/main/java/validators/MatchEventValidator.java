package validators;

import model.MatchEvent;
import model.Team;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains methods to validate form inputs for match events actions, e.g. the inputs to finish a match event
 * (enter the result).
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

    private static final int MAX_VARCHAR_LENGTH = 255;

    /**
     * Validates an {@link MatchEvent}-object when the match bet will be added.
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
     * @param matchEventToValidate Match bet to validate the fields.
     * @return List of the occurred validation faults. Empty if no validation fault occurred.
     * @since 11.01.2016
     */
    public List<ValidationFault> validateAddMatchEvent(MatchEvent matchEventToValidate, String matchEventDateTime) {

        List<ValidationFault> validationFaults = new ArrayList<ValidationFault>();

        //Validate matchEventDateTime
        Date convertedDate;
        String matchEventDateTimeName = "dateTime";

        if (matchEventDateTime == null) {
            validationFaults.add(new ValidationFault(matchEventDateTimeName, ValidationFault.EMTPY_CODE));
        }

        try {
            convertedDate = stringToDate(matchEventDateTime);

            if (convertedDate.before(new Date())) {
                validationFaults.add(new ValidationFault(matchEventDateTime, ValidationFault.DATE_EXPIRED));
            }
        } catch (ParseException ex) {
            validationFaults.add(new ValidationFault(matchEventDateTimeName, ValidationFault.INVALID_DATE));
        }

        //Validate matchEventNr
        String matchEventNr = matchEventToValidate.getMatchEventNr();
        String matchEventNrName = "eventNr";
        if (matchEventNr != null) {
            if (matchEventNr.length() > MAX_VARCHAR_LENGTH) {
                validationFaults.add(new ValidationFault(matchEventNrName, ValidationFault.TO_LONG_CODE));
            }
        }

        //Validate matchEventGroup
        String matchEventGroup = matchEventToValidate.getMatchEventGroup();
        String matchEventGroupName = "eventGroup";
        if (matchEventGroup != null) {
            if (matchEventGroup.length() > MAX_VARCHAR_LENGTH) {
                validationFaults.add(new ValidationFault(matchEventGroupName, ValidationFault.TO_LONG_CODE));
            }
        }

        //Validate teams
        Team teamHome = matchEventToValidate.getTeamHome();
        Team teamAway = matchEventToValidate.getTeamAway();
        String teamAwayName = "teamAway";
        if (teamHome != null && teamAway != null) {
            if (teamHome == teamAway) {
                validationFaults.add(new ValidationFault(teamAwayName, ValidationFault.TEAM_IDENTICAL));
            }
        }

        return validationFaults;
    }

    /**
     * Validates the form inputs to finish a match event. A match event is finished by entering the score of both teams
     * (home and away).
     * Both scores are validated as follows:
     * - not empty
     * - is an integer number >= 0
     *
     * @param scoreHome The entered score of the home team.
     * @param scoreAway The entered score of the away team.
     * @return List of the occurred validation faults. Empty list if no validation fault occurred.
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
     * Checks if a string is an integer number >= 0 or not.
     * True = integer number, false = not an integer number.
     *
     * @param stringToCheck String to check.
     * @return True = integer number, false = not an integer number
     * @since 02.01.2015
     */
    private static boolean isInteger(String stringToCheck) {
        return stringToCheck.matches("^\\d+$");
    }

    /**
     * Converts a string to date.
     *
     * @param date date in string format to convert.
     * @return Date if no error occurs, otherwise throw an Exception
     * @since 11.01.2016
     */
    private Date stringToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.parse(date);
    }
}
