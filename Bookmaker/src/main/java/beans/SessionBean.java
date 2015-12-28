package beans;

import model.User;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Bean to obtain and manage sessions for logged in users.
 *
 * <b>History:</b>
 * <pre>
 * 1.0	12.11.2015	Joel Holzer  Class created.
 * 1.1  28.12.2015  Joel Holzer  Added method {@link #setUser(User)}.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.1
 * @since 28.12.2015
 */
public class SessionBean {

    public static final String USER_KEY = "user";

    /**
     * Returns any existing session instances associated with the current request, or null if there is no such session.
     * @return Object with any existing session instances, null if there is no such session.
     * @since 12.11.2015
     */
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    /**
     * Returns the user of the session (currently logged in user) or null, if no active session exists.
     * @return Currently logged in user. Null if no active session exists.
     * @since 12.11.2015
     */
    public static User getUser() {
        HttpSession session = getSession();
        if (session != null) {
            return (User) session.getAttribute(USER_KEY);
        }
        return null;
    }

    /**
     * Saves the given user in the session.
     *
     * @param user User to save in the session.
     * @since 28.12.2015
     */
    public static void setUser(User user) {
        HttpSession session = SessionBean.getSession();
        session.setAttribute(SessionBean.USER_KEY, user);
    }
}
