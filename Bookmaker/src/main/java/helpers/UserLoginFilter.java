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
public class UserLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            HttpSession session = servletRequest.getSession(false);

            if (session != null && session.getAttribute(SessionBean.USER_KEY) != null) {
                //User is logged in
                if (!((User)session.getAttribute(SessionBean.USER_KEY)).getIsManager()) {
                    //User is logged in
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
        catch(Throwable t) {
            System.out.println( t.getMessage());
        }
    }

    @Override
    public void destroy() {

    }
}

