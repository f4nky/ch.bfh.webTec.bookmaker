package beans;

import dao.UserDao;
import model.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
            return "home";
        }
        return "login";
    }

    /**
     *
     * @return
     * @since 12.11.2015
     */
    public String logout() {
        //TODO
        return  "login";
    }

}
