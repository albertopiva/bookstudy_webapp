package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.ConferenceRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that allow to get information about ConferenceRoom in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class ConferenceRoomDAO extends AbstractDAO{
    /**
     * Given the list of the ID.
     *
     * @param conn the connection to the database.
     * @return List of {@code ConferenceRoom} ID.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<String> getConferenceRoomIDList(Connection conn) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT id FROM libraryapp.conferenceRoom;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> roomList = new ArrayList<>();

            while (result.next()) {
                roomList.add(result.getString("id"));
            }
            return roomList;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the ConferenceRoom by a given id.
     *
     * @param conn the connection to the database.
     * @param id the id of the conference.
     * @return {@code ConferenceRoom} object by its {@param id}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static ConferenceRoom getConferenceRoomByID(Connection conn, String id) throws SQLException{

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.conferenceRoom WHERE id=?;";

        ConferenceRoom confRoom = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, id);

            result = stmnt.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                confRoom = new ConferenceRoom(id,name);
            }
            return confRoom;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Give the lists of all ConferenceRoom.
     *
     * @param conn the connection to the database.
     * @return List of {@code ConferenceRoom}s.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<ConferenceRoom> getConferenceRoomList(Connection conn) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.conferenceRoom;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<ConferenceRoom> confRooms = new ArrayList<>();

            while (result.next()) {
                String id = result.getString("id");
                String name = result.getString("name");

                confRooms.add(new ConferenceRoom(id,name));
            }
            return confRooms;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }
}
