package validators;

import model.User;

/**
 * Model class for a validation fault.
 * Every validation fault has a form field, where the validation fault occurred and a fault code (which error occurred).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  23.12.2015  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 23.12.2015
 */
public class ValidationFault {

    private String field;
    /**
     * 0 = field is empty
     * 1 = value is to short
     * 2 = value is to long
     * 3 = value has incorrect characters
     * 4 = value already exist in database
     * 5 = wrong number (just for credit card)
     */
    private byte faultCode;

    public static final byte EMTPY_CODE = 0;
    public static final byte TO_SHORT_CODE = 1;
    public static final byte TO_LONG_CODE = 2;
    public static final byte INCORRECT_CHAR_CODE = 3;
    public static final byte ALREADY_EXISTS_CODE = 4;
    public static final byte WRONG_NUMBER = 5;

    public String getField() {
        return field;
    }

    public byte getFaultCode() {
        return faultCode;
    }

    public ValidationFault(String field, byte faultCode) {
        this.field = field;
        this.faultCode = faultCode;
    }
}
