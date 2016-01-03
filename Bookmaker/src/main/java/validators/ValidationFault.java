package validators;

/**
 * Model class for a validation fault.
 * Every validation fault has a form field, where the validation fault occurred and a fault code (which error occurred).
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  23.12.2015  Joel Holzer         Class created.
 * 1.1  01.01.2016  Joel Holzer         Added fault number 5 for wrong credit card number.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.1
 * @since 01.01.2016
 */
public class ValidationFault {

    private String field;
    private byte faultCode;

    public static final byte EMTPY_CODE = 0;
    public static final byte TO_SHORT_CODE = 1;
    public static final byte TO_LONG_CODE = 2;
    public static final byte INCORRECT_CHAR_CODE = 3;
    public static final byte ALREADY_EXISTS_CODE = 4;
    public static final byte WRONG_NUMBER = 5;

    /**
     * Returns the name of the field in the form where the validation fault occurred.
     *
     * @return Name of the field where the validation fault occurred.
     * @since 23.12.2015
     */
    public String getField() {
        return field;
    }

    /**
     * Returns the fault code of the occurred validation fault. The fault code tells which fault occurred.
     * The following fault codes exists:
     * 0 = field is empty
     * 1 = value is to short
     * 2 = value is to long
     * 3 = value has incorrect characters
     * 4 = value already exist in database
     * 5 = wrong number (just for credit card)
     *
     * @return Fault code which tells the occurred fault.
     * @since 23.12.2015
     */
    public byte getFaultCode() {
        return faultCode;
    }

    /**
     * Constructor. Initializes the member variables.
     * @param field Field where the validation fault occurred.
     * @param faultCode Fault code which tells which validation fault occurred. More see {@link #getFaultCode()}.
     * @since 23.12.2015
     */
    public ValidationFault(String field, byte faultCode) {
        this.field = field;
        this.faultCode = faultCode;
    }
}
