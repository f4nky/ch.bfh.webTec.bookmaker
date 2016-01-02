package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Bean to navigate from one site to another.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	17.12.2015	Joel Holzer     Class created.
 * 1.1  28.12.2015  Joel Holzer     Added method {@link #redirectToManagerMatchDetail(long)}
 * 1.2  29.12.2015  Joel Holzer     Added method {@link #redirectToUserMatchDetail(long)}
 * 1.3  01.01.2016  Joel Holzer     Added method {@link #redirectToUserAccount()}
 * 1.4  02.01.2016  Joel Holzer     Added method {@link #redirectToManagerMatchDetailsFinished(long)}
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.4
 * @since 02.01.2016
 */
@ManagedBean
@SessionScoped
public class NavigationBean {

    public static final String START_PAGE_WITHOUT_INDEX = "/bookmaker/";
    public static final String USER_BETS = "/userViews/userDashboardBets.xhtml";
    public static final String MANAGER_MATCHES = "/managerViews/managerMatches.xhtml";
    public static final String MANAGER_MATCH_DETAIL = "/managerViews/managerMatchDetail.xhtml";
    public static final String MANAGER_MATCH_DETAIL_FINISHED = "/managerViews/managerMatchDetailFinished.xhtml";
    public static final String USER_MATCH_DETAIL = "/userViews/userMatchDetailComing.xhtml";
    public static final String USER_ACCOUNT = "/userViews/userDashboardAccount.xhtml";

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
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + USER_BETS + REDIRECT_POSTFIX);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the home page of the logged in manager
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 17.12.2015
     */
    public void redirectToManagerHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + MANAGER_MATCHES + REDIRECT_POSTFIX);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the match detail page (manager-view) of the match event with the given id. Only for future match.
     *
     * @param matchEventId ID of the match event which details should shown.
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 28.12.2015
     */
    public void redirectToManagerMatchDetail(long matchEventId) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + MANAGER_MATCH_DETAIL + "?matchEventId=" + matchEventId);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the match detail page (gambler-view) of the match event with the given id.
     *
     * @param matchEventId ID of the match event which details should shown.
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 29.12.2015
     */
    public void redirectToUserMatchDetail(long matchEventId) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + USER_MATCH_DETAIL + "?matchEventId=" + matchEventId);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the account page of the user (gambler).
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 01.01.2016
     */
    public void redirectToUserAccount() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + USER_ACCOUNT);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Redirects to the match detail page (manager-view) of the match event with the given id. Only for past match.
     *
     * @param matchEventId ID of the match event which details should shown.
     * @throws IOException Thrown when something with the redirect was wrong.
     * @since 02.01.2016
     */
    public void redirectToManagerMatchDetailsFinished(long matchEventId) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(requestContextPath + MANAGER_MATCH_DETAIL_FINISHED + "?matchEventId=" + matchEventId);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
