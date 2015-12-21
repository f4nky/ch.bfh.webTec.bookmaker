package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 * Created by holzer on 17.12.2015.
 */
@ManagedBean
@SessionScoped
public class NavigationBean {

    public static final String START_PAGE_WITHOUT_INDEX = "/bookmaker/";
    public static final String USER_HOME_PAGE = "/userViews/userHome.xhtml";
    public static final String MANAGER_HOME_PAGE = "/managerViews/managerHome.xhtml";

    private static final String START_PAGE_WITH_INDEX = "/welcome.xhtml";
    private static final String REDIRECT_POSTFIX = "?faces-redirect=true";

    public String redirectToStartPage() {
        return START_PAGE_WITH_INDEX + REDIRECT_POSTFIX;
    }

    public String redirectToUserHome() {
        return USER_HOME_PAGE + REDIRECT_POSTFIX;
    }

    public String redirectToManagerHome() {
        return MANAGER_HOME_PAGE + REDIRECT_POSTFIX;
    }
}
