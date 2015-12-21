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
 * Created by holzer on 17.12.2015.
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            HttpSession session = servletRequest.getSession(false);

            String requestURI = servletRequest.getRequestURI();
            if (session != null && session.getAttribute(SessionBean.USER_KEY) != null) {
                //Manager is logged in
                if (((User)session.getAttribute(SessionBean.USER_KEY)).getIsManager()) {
                    //User is logged in
                    //Start page request --> start user home page
                    if (requestURI.equals(NavigationBean.START_PAGE_WITHOUT_INDEX)) {
                        servletResponse.sendRedirect(servletRequest.getContextPath() + NavigationBean.MANAGER_HOME_PAGE);
                    } else {
                        chain.doFilter(request, response);
                    }
                } else {
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
        catch(Throwable t) {
            System.out.println( t.getMessage());
        }
    }

    @Override
    public void destroy() {

    }
}

