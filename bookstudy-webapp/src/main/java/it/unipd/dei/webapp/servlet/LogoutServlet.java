package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.OrganizerDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.Organizer;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet to logout the user.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class LogoutServlet extends AbstractDatabaseServlet{

    /**
     * Get request for the logout.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        //invalidate the user session
        req.getSession().invalidate();
        //redirect the browser to the homepage
        res.sendRedirect(req.getContextPath() + "/jsp/homepage.jsp");
    }
}
