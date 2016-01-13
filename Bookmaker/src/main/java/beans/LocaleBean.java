package beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * Bean for the language switcher. Is used to change the language in the view and contains also methods to get the
 * current language of the site.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	28.12.2015	Joel Holzer  Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 28.12.2015
 */
@ManagedBean
@SessionScoped
public class LocaleBean {

    private Locale locale;

    /**
     * Initializes the locale member variable of this view with the current locale of the instance.
     *
     * @since 28.12.2015
     */
    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    /**
     * Returns the current locale (locale of the current language).
     *
     * @return Current locale
     * @since 28.12.2015
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the current language.
     *
     * @return Current language
     * @since 28.12.2015
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * Sets the current language and current locale to the given language. Is used to switch the current language of
     * the site by calling this method.
     *
     * @param language Shortcode of the language to set as the current language (e.g. "de", "en", etc.).
     * @since 28.12.2015
     */
    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}
