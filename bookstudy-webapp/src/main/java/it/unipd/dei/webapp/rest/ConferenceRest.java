package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.ConferenceBookDAO;
import it.unipd.dei.webapp.dao.ConferenceDAO;
import it.unipd.dei.webapp.resources.Conference;
import it.unipd.dei.webapp.resources.Message;
import it.unipd.dei.webapp.resources.ResourceList;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;
import it.unipd.dei.webapp.utils.UtilMethods;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class ConferenceRest extends RestResource {

    /**
     * The constructor of the Conference REST Resource.
     *
     * @param req the HTTPSServletRequest object.
     * @param res the HTTPSServletResponse object.
     * @param ds the DataSource to connect to the database.
     */
    public ConferenceRest(HttpServletRequest req, HttpServletResponse res, DataSource ds) {
        super(req, res, ds);
    }

    /**
     * Get the list of conference.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getAllConference() throws IOException {

        try {

            ArrayList<Conference> conference = ConferenceDAO.getConferenceList(ds.getConnection());
            if (conference == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.CONFERENCE_NOT_FOUND);
            } else {
                ResourceList<Conference> lists = new ResourceList<>(conference);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }

        } catch (SQLException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            res.setStatus(ec.getHTTPCode());
            res.getWriter().write(ec.toJSON().toString());
            logger.error("stacktrace:", e);
        }
    }

    /**
     * Get the list of the users that had booked themselves for the given conference.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getAttendanceForGivenConference() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        try {
            String alphaCode = tokens[2];

            Conference conference = ConferenceDAO.getConferenceByCode(ds.getConnection(), alphaCode);
            if (conference == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.CONFERENCE_NOT_FOUND);
            } else {
                ArrayList<User> users = ConferenceBookDAO.getAllUserByConference(ds.getConnection(), alphaCode);
                logger.info("Conference Serve");
                if (users == null || users.size() == 0) {
                    Message m = new Message("There are no reservation for this conference yet.");

                    res.setStatus(HttpServletResponse.SC_OK);
                    m.toJSON(res.getOutputStream());
                } else {
                    ResourceList<User> lists = new ResourceList<>(users);
                    res.setStatus(HttpServletResponse.SC_OK);
                    lists.toJSON(res.getOutputStream());
                }
            }

        } catch (SQLException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            res.setStatus(ec.getHTTPCode());
            res.getWriter().write(ec.toJSON().toString());
            logger.error("stacktrace:", e);
        }
    }

    /**
     * Create a new Conference into the database.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void insertConference() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        try {
            Conference tmp = Conference.fromJSON(req.getInputStream());
            logger.debug(tmp.toString());
            String alphaCode = UtilMethods.computeAlphanumericCode(10);
            String orgID = req.getSession().getAttribute("phoneNumber").toString();
            Conference conference = new Conference(alphaCode,tmp.getDate(),tmp.getTitle(),tmp.getDescription(),orgID,tmp.getConferenceRoom());
            logger.debug(conference);
            if(conference.checkEmpty()){
                //some fields are empty
                logger.error("EMPTY FIELDS in CONFERENCE");
                ErrorCode ec = ErrorCode.EMPTY_INPUT_FIELDS;
                res.setStatus(ec.getHTTPCode());
                res.getWriter().write(ec.toJSON().toString());
            }else{
                //I can insert the conference
                int x = ConferenceDAO.insertNewConference(ds.getConnection(), conference);
                if(x == 1){
                    //right insert in the database
                    JSONObject resJSON = new JSONObject();
                    res.setStatus(HttpServletResponse.SC_OK);
                    resJSON.put("result", "successfull");
                    res.getWriter().write(resJSON.toString());
                }else{
                    //Something goes wrong
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
            }
        } catch (SQLException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            res.setStatus(ec.getHTTPCode());
            res.getWriter().write(ec.toJSON().toString());
            logger.error("stacktrace:", e);
        }
    }


    /**
     * Get the list of the users that had booked themselves for all the conference of a given organizer.
     * For this method we pick the organizer from the Session.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getAttendance() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        try {
            ArrayList<Conference> allConference = (ArrayList<Conference>) ConferenceDAO.getConferenceList(ds.getConnection());
            ArrayList<Conference> conference = new ArrayList<>();
            for(Conference c : allConference){
                if(c.getOrganizerID().equals(req.getSession().getAttribute("phoneNumber")))
                    conference.add(c);
            }
            if (conference == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.CONFERENCE_NOT_FOUND);
            } else {
                ResourceList<Conference> lists = new ResourceList<>(conference);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }

        } catch (SQLException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            res.setStatus(ec.getHTTPCode());
            res.getWriter().write(ec.toJSON().toString());
            logger.error("stacktrace:", e);
        }
    }


    /**
     * Delete a conference from the database and on cascade all the reservation done by the users
     * for that conference.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void deleteConference() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        try {
            String alphaCode = tokens[2];
            logger.debug(alphaCode);
            if(alphaCode ==null || alphaCode.equals(""))
            {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writeError(res, ErrorCode.EMPTY_INPUT_FIELDS);
            }else {
                Conference c = ConferenceDAO.getConferenceByCode(ds.getConnection(),alphaCode);

                //logger.debug(c.toString());
                if(c==null){

                    logger.debug("NULL CONFERENCE");
                    JSONObject resJSON = new JSONObject();
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    writeError(res,ErrorCode.CONFERENCE_NOT_FOUND);
                }else {
                    int x = ConferenceDAO.deleteConference(ds.getConnection(), alphaCode);

                    logger.debug("DELETIONS : "+c);
                    if (x == 1) {
                        JSONObject resJSON = new JSONObject();
                        res.setStatus(HttpServletResponse.SC_OK);
                        resJSON.put("result", "successfull");
                        res.getWriter().write(resJSON.toString());
                    } else {
                        //Something goes wrong
                        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                        logger.error("NOT FOUND");
                    }
                }
            }
        } catch (SQLException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            res.setStatus(ec.getHTTPCode());
            res.getWriter().write(ec.toJSON().toString());
            logger.error("stacktrace:", e);
        }
    }
}
