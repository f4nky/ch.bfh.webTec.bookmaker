package beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * Bean for the language switcher.
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

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    /**
     * Is executed if a user changes the language in the view.
     * Sets the current language of the web application to the language the user selected/clicked.
     *
     * @param language Shortcode of the language to set as the current language (e.g. "de", "en", etc.).
     * @since 28.12.2015
     */
    public void changeLanguage(String language) {
        setLanguage(language);
    }
}
