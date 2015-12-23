package validators;

/**
 * Created by holzer on 23.12.2015.
 */
public class ValidationFault {

    private String field;
    /**
     * 0 = empty
     * 1 = to short
     * 2 = to long
     * 3 = incorrect characters
     * 4 = already exist
     */
    private byte faultCode;

    public static final byte EMTPY_CODE = 0;
    public static final byte TO_SHORT_CODE = 1;
    public static final byte TO_LONG_CODE = 2;
    public static final byte INCORRECT_CHAR_CODE = 3;
    public static final byte ALREADY_EXISTS_CODE = 4;

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
