package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.Conference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that allow to do operations about Conference in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class ConferenceDAO extends AbstractDAO {

    /**
     * Method that insert a conference.
     *
     * @param conn the connection to the database.
     * @param c the {@code Conference} to insert in the database.
     * @return 1 if the insert goes well, otherwise 0.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int insertNewConference(Connection conn, Conference c) throws SQLException {

        PreparedStatement stmnt = null;

        int result = -1;

        String query = "INSERT INTO libraryapp.conference values(?,?,?,?,?,?);";

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, c.getAlphanumericCode());
            stmnt.setDate(2, c.getDate());
            stmnt.setString(3, c.getTitle());
            stmnt.setString(4, c.getDescription());
            stmnt.setString(5, c.getOrganizerID());
            stmnt.setString(6, c.getConferenceRoom());

            result = stmnt.executeUpdate();

            if (result == 1)
                return 1;

            return 0;

        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Return the list of all conferences.
     *
     * @param conn the connection to the database.
     * @return the list of {@code Conference}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static ArrayList<Conference> getConferenceList(Connection conn) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select * from libraryapp.conference order by date;";

        ArrayList<Conference> conf = null;

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            conf = new ArrayList<>();

            while (result.next()) {
                String code = result.getString("alphanumericCode");
                Date date = result.getDate("date");
                String title = result.getString("title");
                String description = result.getString("description");
                String organizerID = result.getString("organizerID");
                String conferenceRoomID = result.getString("conferenceRoomID");

                conf.add(new Conference(code, date, title, description, organizerID, conferenceRoomID));

            }
            return conf;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Return the conference giving the alphanumericCode.
     *
     * @param conn the connection to the database.
     * @param alphanumericCode the identifier of the conference to return.
     * @return the {@code Conference} given its {@param alphanumericCode}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static Conference getConferenceByCode(Connection conn, String alphanumericCode) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select * from libraryapp.conference where alphanumericcode = ?;";
        Logger logger = LogManager.getLogger(ConferenceDAO.class);
        Conference conf = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1,alphanumericCode);
            logger.debug(query);
            logger.debug(alphanumericCode);
            result = stmnt.executeQuery();
            if (result.next()) {

                Date date = result.getDate("date");
                String title = result.getString("title");
                String description = result.getString("description");
                String organizerID = result.getString("organizerID");
                String conferenceRoomID = result.getString("conferenceRoomID");

                conf = new Conference(alphanumericCode, date, title, description, organizerID, conferenceRoomID);
            }
            return conf;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Return the conference by a given date.
     *
     * @param conn the connection to the database.
     * @param date the date in which searching for a Conference.
     * @return the list of {@code Conference}s for the given date.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<Conference> getConferenceByDate(Connection conn, Date date) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select * from libraryapp.conference where date = ?;";

        ArrayList confList = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1,date);

            result = stmnt.executeQuery();

            confList = new ArrayList<>();

            while (result.next()) {

                String alphanumericCode = result.getString("alphanumericCode");
                String title = result.getString("title");
                String description = result.getString("description");
                String organizerID = result.getString("organizerID");
                String conferenceRoomID = result.getString("conferenceRoomID");

                confList.add(new Conference(alphanumericCode, date, title, description, organizerID, conferenceRoomID));
            }
            return confList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Delete a conference giving the alphanumericCode. It delete also on cascade all the reservation
     * made for this conference.
     *
     * @param conn the connection to the database.
     * @param alphanumericCode the identifier of the conference to delete.
     * @return 1 if the conference was successfully delete, otherwise 0.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int deleteConference(Connection conn, String alphanumericCode) throws SQLException {

        PreparedStatement stmnt = null;
        String query = "delete from libraryapp.conference where alphanumericcode = ?;";
        int result = -1;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1,alphanumericCode);

            result = stmnt.executeUpdate();

            if (result==1) {
                //all gone well, conference deleted with all reservation on cascade
                return 1;
            }
            return 0;
        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

}
