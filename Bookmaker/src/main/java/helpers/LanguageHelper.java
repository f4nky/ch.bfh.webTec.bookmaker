package helpers;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Contains helper methods for the language handling:
 * - set the language from the selected link in the view
 * - read strings from the translation file of the current language.
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
public class LanguageHelper implements ActionListener {

    /**
     * Base name of the language resource bundle (see faces-config.xml)
     */
    private static final String RESOURCE_BUNDLE_BASE_NAME = "language.messages";

    /**
     * Is executed if a user changes the language in the view.
     * Sets the current language of the web application to the language the user selected/clicked.
     *
     * @param event Executed action event. Tells the method the clicked language-link.
     * @throws AbortProcessingException
     * @since 23.12.2015
     */
    public void processAction(ActionEvent event) throws AbortProcessingException {
        String langShortcut = event.getComponent().getId();
        FacesContext context = FacesContext.getCurrentInstance();
        Locale newLanguageLocale = new Locale(langShortcut);
        context.getViewRoot().setLocale(newLanguageLocale);
    }

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
}
