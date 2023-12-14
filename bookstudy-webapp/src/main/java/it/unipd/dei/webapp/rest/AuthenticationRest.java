package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.ConferenceRoomDAO;
import it.unipd.dei.webapp.dao.OrganizerDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.ConferenceRoom;
import it.unipd.dei.webapp.resources.Organizer;
import it.unipd.dei.webapp.resources.ResourceList;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Manage the REST call about the conference.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class AuthenticationRest extends RestResource{
    /**
     * The constructor of the Conference REST Resource
     *
     * @param req the HTTPSServletRequest object.
     * @param res the HTTPSServletResponse object.
     * @param ds the DataSource to connect to the database.
     */
    public AuthenticationRest(HttpServletRequest req, HttpServletResponse res, DataSource ds) {
        super(req, res, ds);
    }

    public void authenticateUser() throws IOException {
        User jsonUser = User.fromJSON(req.getInputStream());
        if(jsonUser.getPhoneNumber()==null || jsonUser.getPassword() == null) {
            //error
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(res, ErrorCode.EMPTY_INPUT_FIELDS);
            logger.debug(req.getRequestURI()+ " "+jsonUser.getPhoneNumber() +" "+ jsonUser.getPassword());
        }else{
            //Take the database tuple that corresponds to username and password in the right table, switching the kind of user
            try {
                User u = UserDAO.authenticateUser(ds.getConnection(), jsonUser.getPhoneNumber(), jsonUser.getPassword());
                if(u==null){
                    //the authentication fail
                    //write the error
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    writeError(res, ErrorCode.WRONG_CREDENTIALS);
                }else{
                    res.setStatus(HttpServletResponse.SC_OK);
                    HttpSession session = req.getSession();

                    // insert in the session the email an the role
                    session.setAttribute("phoneNumber", u.getPhoneNumber());
                    session.setAttribute("name", u.getName());
                    session.setAttribute("surname", u.getSurname());
                    session.setAttribute("role", u.getRole());

                    u.toJSON(res.getOutputStream());
                }
            }catch(SQLException e){
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }
    public void authenticateOrganizer()
            throws IOException {
        Organizer jsonOrganizer = Organizer.fromJSON(req.getInputStream());
        if(jsonOrganizer.getPhoneNumber()==null || jsonOrganizer.getPassword() == null) {
            //error
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(res, ErrorCode.EMPTY_INPUT_FIELDS);
            logger.debug(req.getRequestURI()+ " "+jsonOrganizer.getPhoneNumber() +" "+ jsonOrganizer.getPassword());
        }else {
            //Take the database tuple that corresponds to username and password in the right table, switching the kind of user
            try {
                Organizer s = OrganizerDAO.authenticateOrganizer(ds.getConnection(), jsonOrganizer.getPhoneNumber(), jsonOrganizer.getPassword());
                if(s==null){
                    //the authentication fail
                    //write the error
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    writeError(res, ErrorCode.WRONG_CREDENTIALS);
                }else{
                    res.setStatus(HttpServletResponse.SC_OK);
                    HttpSession session = req.getSession();

                    // insert in the session the email an the role
                    session.setAttribute("phoneNumber", s.getPhoneNumber());
                    session.setAttribute("name", s.getName());
                    session.setAttribute("surname", s.getSurname());
                    session.setAttribute("role", "organizer");

                    // login credentials were correct: we redirect the user to the homepage
                    // now the session is active and its data can used to change the homepage

                }
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }
}
