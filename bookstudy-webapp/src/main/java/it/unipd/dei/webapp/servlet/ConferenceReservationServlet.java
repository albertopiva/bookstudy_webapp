package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.ConferenceBookDAO;
import it.unipd.dei.webapp.dao.ConferenceDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.Conference;
import it.unipd.dei.webapp.resources.ConferenceBook;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;
import it.unipd.dei.webapp.utils.UtilMethods;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet to manage the conference reservations.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class ConferenceReservationServlet extends AbstractDatabaseServlet{

    /**
     * Get some information about the conference.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String op = req.getRequestURI();
        //Parse the request
        op = op.substring(op.lastIndexOf("confReserv")+11);
        boolean error = false;
        //Call the method view confReservations
        //If op = confReserv/view/ return the list of all reservation
        if(op.contains("view")){
            op = op.substring(op.lastIndexOf("view")+4);

            if(op.equals("")){
                // the list of all reservation (maybe there will be a filter here)
            }
            else {
                //If op = confReserv/view/user/{user} return the list of all reservation of this user
                if(op.contains("user")){
                    op = op.substring(op.lastIndexOf("user")+4);
                    String userParam = req.getSession().getAttribute("phoneNumber").toString();
                    if (userParam == null) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            writeError(res,ErrorCode.INTERNAL_ERROR);
                    }
                    try {
                        //Check that this user exists
                        User user = UserDAO.getUserByID(getDataSource().getConnection(), userParam);
                        List<Conference> allReserv = null;
                        allReserv = ConferenceBookDAO.getAllReservationByUser(getDataSource().getConnection(), userParam);

                        if (user == null) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("Person: "+user+ " allReserv is null: "+(allReserv==null));
                            //writeError(res, ErrorCode.USER_NOT_FOUND);
                        }/*else if(allReserv ==null){
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("You had never made a reservation");
                            //writeError(res, ErrorCode.USER_NOT_FOUND);
                        }*/ else {
                            req.setAttribute("reservationList", allReserv);
                            req.setAttribute("reservationSize", allReserv.size());
                            req.setAttribute("user", user);
                            //res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            //forwards the control to the search-employee-result JSP
                            //req.getRequestDispatcher("${basedir}/jsp/reservation-result.jsp").forward(req, res);
                            req.getRequestDispatcher("/jsp/protected/common/conf-reservation-result.jsp").forward(req, res);
                        }
                    }
                    catch (SQLException e){
                        logger.error("stacktrace:", e);
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                    }
                }
                //If op = confReserv/view/date/{date} return the list of all reservation in this date
                else if(op.contains("date")) {
                    op = op.substring(op.lastIndexOf("date")+4);
                    String dateParam = op;

                    if (dateParam == null) {
                        error = true;
                        if (error) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("ERROR: value of user is null. Check the query");
                        }
                    }
                    try {
                        ArrayList<Conference> allConf = null;
                        Date date = Date.valueOf(dateParam);
                        allConf = (ArrayList<Conference>) ConferenceDAO.getConferenceByDate(getDataSource().getConnection(), date);

                        if (date == null) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            //res.getWriter().write("Person: "+(person==null)+ "ALLRESERV: "+(allReserv==null));
                            writeError(res, ErrorCode.WRONG_FORMAT);
                        } else {
                            req.setAttribute("reservationList", allConf);
                            req.setAttribute("date", date);
                            //res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            //forwards the control to the search-employee-result JSP
                            //req.getRequestDispatcher("${basedir}/jsp/reservation-result.jsp").forward(req, res);
                            req.getRequestDispatcher("/jsp/protected/common/reservation-result.jsp").forward(req, res);
                        }
                    } catch (SQLException e) {
                        logger.error("stacktrace:", e);
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                    }
                }
            }
        }
    }

    /**
     * Insert new reservation for a conference.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String op = req.getRequestURI();
        String error = "";
        //Parse the request
        op = op.substring(op.lastIndexOf("confReserv")+11);
        String userID = null;
        if(op.contains("insert")){
            //insert new reservation given
            try {
                int insertDone = -1;
                HttpSession session = req.getSession();
                userID = session.getAttribute("phoneNumber").toString();
                String conferenceID = req.getParameter("conferenceID");
                if(userID == null || conferenceID ==null)
                {
                    error = "true";
                    req.setAttribute("error", error);
                    req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    //res.getWriter().write(req.getRequestURI());
                    //writeError(res, ErrorCode.WRONG_FORMAT);
                }else {

                    ConferenceBook confBook = new ConferenceBook(userID, conferenceID);
                    insertDone = ConferenceBookDAO.insertNewConferenceReservation(getDataSource().getConnection(), confBook);

                    if (insertDone == -1) {
                        //Some problem, DAO not work
                        error = "true";
                        req.setAttribute("error", error);
                        req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                        //writeError(res, ErrorCode.OPERATION_UNKNOWN);
                    } else if (insertDone == 0) {
                        //Insert not done
                        error = "true";
                        req.setAttribute("error", error);
                        req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                        //writeError(res, ErrorCode.INTERNAL_ERROR);
                    } else {
                        //returned value 1
                        //Insert success
                        error = "false";
                        req.setAttribute("error", error);
                        req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                        res.setStatus(HttpServletResponse.SC_OK);
                        //res.getWriter().write("Insert done. You have new reservation for conference " + confBook.getConferenceID());

                    }
                }
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    res.setStatus(HttpServletResponse.SC_CONFLICT);
                    error = "true";
                    req.setAttribute("error", error);
                    req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                    //writeError(res, ErrorCode.CONFERENCE_ALREADY_BOOKED);
                    logger.warn("User "+userID+" is already booked for this conference.");
                } else {
                    //e.printStackTrace();
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    error = "true";
                    req.setAttribute("error", error);
                    req.getRequestDispatcher("/jsp/protected/common/insert-conf-reservation.jsp").forward(req, res);
                    //writeError(res, ErrorCode.INTERNAL_ERROR);
                    logger.error("stacktrace:", e);
                }

            } catch(NumberFormatException num){
                writeError(res, ErrorCode.WRONG_FORMAT);
            }
        }

    }
}