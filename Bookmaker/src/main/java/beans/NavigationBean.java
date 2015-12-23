package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Bean to navigate from one site to another.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	17.12.2015	Joel Holzer  Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 17.12.2015
 */
@ManagedBean
@SessionScoped
public class NavigationBean {

    public static final String START_PAGE_WITHOUT_INDEX = "/bookmaker/";
    public static final String USER_HOME_PAGE = "/userViews/userHome.xhtml";
    public static final String MANAGER_HOME_PAGE = "/managerViews/managerHome.xhtml";

    private static final String START_PAGE_WITH_INDEX = "/welcome.xhtml";
    private static final String REDIRECT_POSTFIX = "?faces-redirect=true";

    private String requestContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

    /**
     * Returns the url of the start page (with the prefix /bookmaker/).
     * @return Url of the start page (with the prefix /bookmaker/).
     * @since 17.12.2015
     */
    public String getStartPageWithIndexForRedirect() {
        return START_PAGE_WITH_INDEX + REDIRECT_POSTFIX;
    }

    /**
     * Redirects to the home page of the logged in user (gambler)
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 17.12.2015
     */
    public void redirectToUserHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + USER_HOME_PAGE + REDIRECT_POSTFIX);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the home page of the logged in manager
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 17.12.2015
     */
    public void redirectToManagerHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + MANAGER_HOME_PAGE + REDIRECT_POSTFIX);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
