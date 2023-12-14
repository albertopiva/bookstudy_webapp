package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.ConferenceDAO;
import it.unipd.dei.webapp.dao.ConferenceRoomDAO;
import it.unipd.dei.webapp.resources.Conference;
import it.unipd.dei.webapp.resources.ConferenceRoom;
import it.unipd.dei.webapp.resources.ResourceList;
import it.unipd.dei.webapp.utils.ErrorCode;

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
public class RoomRest extends RestResource{
    /**
     * The constructor of the Conference REST Resource
     *
     * @param req the HTTPSServletRequest object.
     * @param res the HTTPSServletResponse object.
     * @param ds the DataSource to connect to the database.
     */
    public RoomRest(HttpServletRequest req, HttpServletResponse res, DataSource ds) {
        super(req, res, ds);
    }

    /**
     * Get the list of the rooms.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void getRooms() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        try {
            ArrayList<ConferenceRoom> rooms = (ArrayList<ConferenceRoom>) ConferenceRoomDAO.getConferenceRoomList(ds.getConnection());

            if (rooms == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.CONFERENCE_NOT_FOUND);
            } else {
                ResourceList<ConferenceRoom> lists = new ResourceList<>(rooms);
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

}
