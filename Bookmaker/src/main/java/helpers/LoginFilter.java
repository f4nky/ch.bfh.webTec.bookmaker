package helpers;

import beans.NavigationBean;
import beans.SessionBean;
import model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This filter is used to redirect logged in users (users and managers) to their home page when the call the root
 * url of the web application.
 * For example: If the user is logged in as a manager and he call the root url http://localhost:8080/bookmaker, he
 * is redirected to the manager home page (http://localhost:8080/bookmaker/managerViews/managerHome.xhtml)
 * Same for the normal user (gambler).
 * <br/><br/>
 * <p>
 * <b>History:</b>
 * <pre>
 * 1.0  17.12.2015  Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 17.12.2015
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Redirect logged in users (users and managers) to their home page when the call the root
     * url of the web application. If the user calls another url, the user is redirected to the called url.
     * <p>
     * For example: If the user is logged in as a manager and he call the root url http://localhost:8080/bookmaker, he
     * is redirected to the manager home page (http://localhost:8080/bookmaker/managerViews/managerHome.xhtml)
     * Same for the normal user (gambler).
     * <p>
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

        String requestURI = servletRequest.getRequestURI();
        if (session != null && session.getAttribute(SessionBean.USER_KEY) != null) {
            //Manager is logged in
            if (((User) session.getAttribute(SessionBean.USER_KEY)).getIsManager()) {
                //Start page request --> start manager home page
                if (requestURI.equals(NavigationBean.START_PAGE_WITHOUT_INDEX)) {
                    servletResponse.sendRedirect(servletRequest.getContextPath() + NavigationBean.MANAGER_HOME_PAGE);
                } else {
                    chain.doFilter(request, response);
                }
            }
            //User is logged in
            else {
                //Start page request --> start manager home page
                if (requestURI.equals(NavigationBean.START_PAGE_WITHOUT_INDEX)) {
                    servletResponse.sendRedirect(servletRequest.getContextPath() + NavigationBean.USER_HOME_PAGE);
                } else {
                    chain.doFilter(request, response);
                }
            }
        } else {
            //Anonymous user. Load given site
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

