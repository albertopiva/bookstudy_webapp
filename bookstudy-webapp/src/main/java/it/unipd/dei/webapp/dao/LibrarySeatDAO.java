package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.LibrarySeat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the Library Seat in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class LibrarySeatDAO extends AbstractDAO {

    /**
     * Get the list of the identifiers of the seats.
     *
     * @param conn the connection to the database.
     * @return List of {@code LibrarySeat} ID.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<Integer> getSeatIDList(Connection conn) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT id FROM libraryapp.libraryseat;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<Integer> idList = new ArrayList<>();

            while (result.next()) {
                idList.add(result.getInt("id"));
            }
            return idList;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the list of room.
     *
     * @param conn the connection to the database.
     * @return List of {@code LibrarySeat} Room.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<String> getSeatRoomList(Connection conn) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT room FROM libraryapp.libraryseat;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> roomList = new ArrayList<>();

            while (result.next()) {
                roomList.add(result.getString("room"));
            }
            return roomList;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    public static LibrarySeat getLibrarySeat(Connection conn,long id) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT id,room FROM libraryapp.libraryseat WHERE id = ?;";

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setLong(1,id);
            result = stmnt.executeQuery();

            if (result.next()) {
                return new LibrarySeat(result.getLong("id"),result.getString("room"));
            }
            return null;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }
}