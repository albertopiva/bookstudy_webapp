package it.unipd.dei.webapp.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter to manage the User, association_admin and association_member protected resources.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class CommonFilter extends AbstractFilter{

    /**
     * Get the logger resource.
     */
    static Logger logger = LogManager.getLogger(CommonFilter.class);


    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/jsp/login.jsp";
        String unauthorizedPage = req.getContextPath() + "/jsp/unauthorized.jsp";

        boolean loggedIn = session != null && session.getAttribute("phoneNumber") != null;

        if (loggedIn) {
            if (session.getAttribute("role").equals("association_admin")||
                    session.getAttribute("role").equals("association_member") ||
                            session.getAttribute("role").equals("user")) {
                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                chain.doFilter(req, res); // User is logged in, just continue request.
            } else {
                logger.info("user " + session.getAttribute("phoneNumber") +
                        " with role " + session.getAttribute("role") +
                        " tried to access the admin page");
                res.sendRedirect(unauthorizedPage); //Not authorized, show the proper page
            }
        } else {
            res.sendRedirect(loginURI); // Not logged in, show login page.
        }
    }
}
