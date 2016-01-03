package helpers;

import beans.SessionBean;
import model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This filter is used to protect manager sites from access from not allowed users (not logged in or gambler).
 * All the manager sites are located in the package managerViews.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0  17.12.2015  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 17.12.2015
 */
public class ManagerLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Protects manager sites from access from not allowed users (not logged in or gambler).
     * All the manager sites are located in the package managerViews.
     * If a not allowed user calls a manager site, he is redirected to the start page
     * This method is called for every request.
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @since 17.12.2015
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession(false);

        if (session != null && session.getAttribute(SessionBean.USER_KEY) != null) {
            //Manager is logged in
            if (((User) session.getAttribute(SessionBean.USER_KEY)).getIsManager()) {
                chain.doFilter(request, response);
            } else {
                //Load start page
                servletResponse.sendRedirect(servletRequest.getContextPath());
            }
        } else {
            //Anonymous user. Load start page
            servletResponse.sendRedirect(servletRequest.getContextPath());
        }
    }

    @Override
    public void destroy() {

    }
}
