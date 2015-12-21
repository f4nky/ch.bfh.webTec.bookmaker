package beans;

import dao.UserDao;
import model.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * TODO
 *
 * <b>History:</b>
 * <pre>
 * 1.0	12.11.2015	Joel Holzer  Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 12.11.2015
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private User user;
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    /**
     * TODO
     * @return
     * @since 12.11.2015
     */
    public String login() {
        user = UserDao.getInstance().getUserByEmailPassword(email, password);

        if (user != null) {
            HttpSession session = SessionBean.getSession();
            session.setAttribute(SessionBean.USER_KEY, user);
            if (user.getIsManager()) {
                return navigationBean.redirectToManagerHome();
            }
            return navigationBean.redirectToUserHome();
        }
        return navigationBean.redirectToStartPage();
    }

    /**
     *
     * @return
     * @since 12.11.2015
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return navigationBean.redirectToStartPage();
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }
}
