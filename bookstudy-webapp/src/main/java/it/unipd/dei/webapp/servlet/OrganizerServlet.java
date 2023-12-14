package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.OrganizerDAO;
import it.unipd.dei.webapp.resources.Organizer;
import it.unipd.dei.webapp.utils.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Servlet to insert a new Organizer and to see all organizers.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class OrganizerServlet extends AbstractDatabaseServlet{

    /**
     * Method to view all organizers or the information of a single organizer given the phoneNumber.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{

        String op = req.getRequestURI();
        //parse the request
        op = op.substring(op.lastIndexOf("organizer") + 9);
        boolean error = false;
        String phoneNumber = null;
        //Call the methods to view users
        //If op = organizer/view/ return the list of all organizers
        if(op.contains("view")){
            try {
                ArrayList<Organizer> orgs = null;
                orgs = (ArrayList<Organizer>) OrganizerDAO.getOrganizerList(getDataSource().getConnection());
                req.setAttribute("OrgnizersList", orgs);
                req.setAttribute("OrgnizersSize", orgs.size());
                req.getRequestDispatcher("/jsp/protected/culturalOffice/organizer-result.jsp").forward(req, res);

            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
        //If op = organizer/information return info about this organizer
        else if (op.contains("information")) {
            phoneNumber = req.getParameter("phoneNumber");
            if (phoneNumber == null) {
                error = true;
                if (error) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("ERROR: value of organizer is null. Check the query");
                }
            }
            try{
                Organizer org = OrganizerDAO.getOrganizerByID(getDataSource().getConnection(), phoneNumber);
                if (org == null) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("Organizer with this " + phoneNumber + " doesn't exist");
                }
                else{
                    //res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    //res.getWriter().write("User with this " + phoneNumber + " exist");
                    req.setAttribute("OrganizerInformation", org);
                    req.getRequestDispatcher("/jsp/protected/organizer/organizer-result.jsp").forward(req, res);
                }
            }
            catch(SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }


    /**
     * Method that allow to create a new organizer.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("organizer") + 9);
        String error = "";

        //insert a new organizer
        if (op.contains("insert")) {

            String phoneNumber = "";
            String password = "";
            String name = "";
            String surname = "";
            String association = "";
            Organizer org = null;
            String m = null;
            int x = 5;

            String salt = "";
            char c;
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                c = (char) (random.nextInt(94) + 33);
                salt = salt + c;
            }

            try {
                //retrieve request parameters
                name = req.getParameter("name");
                surname = req.getParameter("surname");
                phoneNumber = req.getParameter("phoneNumber");
                password = req.getParameter("password");
                association = req.getParameter("association");

                if(name==null || surname==null || phoneNumber == null || password == null || association==null){
                    //some field are empty
                    error = "true";
                    req.setAttribute("error", error);
                    req.getRequestDispatcher("/jsp/protected/culturalOffice/create-organizer.jsp").forward(req, res);
                    //writeError(res, ErrorCode.EMPTY_INPUT_FIELDS);
                }else {
                    //create new organizer from the request parameters
                    org = new Organizer(phoneNumber, password, salt, name, surname, association);

                    //creates new object for accessing the database and store the organizer
                    x = OrganizerDAO.newOrganizer(getDataSource().getConnection(), org);

                    if (x == 1) {
                        res.setStatus(HttpServletResponse.SC_OK);
                        error = "false";
                        req.setAttribute("error", error);
                        req.getRequestDispatcher("/jsp/protected/culturalOffice/create-organizer.jsp").forward(req, res);
                        //res.getWriter().write("Inserimento corretto di:\n" + org.toString());
                    } else {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        error = "true";
                        req.setAttribute("error", error);
                        req.getRequestDispatcher("/jsp/protected/culturalOffice/create-organizer.jsp").forward(req, res);
                        //res.getWriter().write("Query not executed");
                    }
                }
            } catch (SQLException e) {
                error = "true";
                req.setAttribute("error", error);
                req.getRequestDispatcher("/jsp/protected/culturalOffice/create-organizer.jsp").forward(req, res);
                logger.error("stacktrace:", e);
                //writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }
}
