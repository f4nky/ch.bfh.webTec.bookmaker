package helpers;

import validators.ValidationFault;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Contains helper methods for the language handling:
 * - read strings from the translation file of the current language.
 * - read the validation fault messages from the translation file of the current language.
 * - creates the output for the validation error message for a list of {@link ValidationFault}-objects.
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
public class LanguageHelper {

    /**
     * Base name of the language resource bundle (see faces-config.xml)
     */
    private static final String RESOURCE_BUNDLE_BASE_NAME = "language.messages";

    /**
     * Returns the string with the given translation key from the language file of the current language.
     * Is used to access the strings in the language files from java code.
     *
     * @param translationKey Translation key of the string to select from the language file.
     * @return Content of the given translation key from the language file.
     * @since 23.12.2015
     */
    public static String getTranslation(String translationKey) {
        FacesContext context = FacesContext.getCurrentInstance();
        String translatedText = "";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, context.getViewRoot().getLocale());
            translatedText = bundle.getString(translationKey);
        } catch (NullPointerException nullPointerException) {
            translatedText = "Key " + translationKey + " is null";
        } catch (MissingResourceException missingResourceException) {
            translatedText = "Key " + translationKey + " does not exist in language file";
        } catch (ClassCastException classCastException) {
            translatedText = "Object for the Key " + translationKey + " in the language file is not a string";
        }
        return translatedText;
    }

    /**
     * Returns the validation fault message for the given form, field and error code from the translation file of the
     * current language.
     * The translation keys of the validation faults has the following structure:
     * form_[formName]_[formField]_[errorCode].
     *
     * e.g:
     * form_register_email_0
     *
     * The following fault codes exists:
     * 0 = field is empty
     * 1 = value is to short
     * 2 = value is to long
     * 3 = value has incorrect characters
     * 4 = value already exist in database
     *
     * @param formName Name of the form. Ist the part [formName] of the translation key. E.g. register or login.
     * @param field Field where the validation fault occurred. E.g. email, password
     * @param errorCode Fault code.
     * @return The translated fault message. The format is [Field]: [Message], for example
     * Password: Must not be empty
     * @since 23.12.2015
     */
    private static String getValidationFaultTranslation(String formName, String field, byte errorCode) {
        FacesContext context = FacesContext.getCurrentInstance();
        String translatedText = "";
        String translationKeyField = "form_" + formName + "_" + field;
        String translationKeyError = "form_" + formName + "_" + field + "_" + errorCode;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, context.getViewRoot().getLocale());
            translatedText = "<strong>"+bundle.getString(translationKeyField) + ":</strong> " + bundle.getString(translationKeyError);
        } catch (NullPointerException nullPointerException) {
            translatedText = "No translation for " + translationKeyField + " or " + translationKeyError;
        } catch (MissingResourceException missingResourceException) {
            translatedText = "Key " + translationKeyError + " or " + translationKeyField + " does not exist in language file";
        } catch (ClassCastException classCastException) {
            translatedText = "Object for the Key " + translationKeyError + " or " + translationKeyField + " in the language file is not a string";
        }
        return translatedText;
    }

    /**
     * Returns a string with all the translated validation faults for the given list of {@link ValidationFault}-objects.
     * Every validation fault in the string is displayed on a new line and the fault message of a specific fault is
     * generated with the method {@link LanguageHelper#getValidationFaultTranslation(String, String, byte)}.
     *
     * @param formName Name of the form where the validation faults occured. E.g. register or login.
     * @param validationFaults List with the validation faults.
     * @return String with the translated validation faults. If no validation faults, returns an empty string.
     * @since 23.12.2015
     */
    public static String createValidationFaultOutput(String formName, List<ValidationFault> validationFaults) {
        String errorMessage = "";
        for(ValidationFault validationFault : validationFaults) {
            errorMessage += LanguageHelper.getValidationFaultTranslation(formName, validationFault.getField(), validationFault.getFaultCode()) + "<br/>";
        }
        return errorMessage;
    }
}
