package it.unipd.dei.webapp.dao;


import it.unipd.dei.webapp.resources.TimeSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to get information about time slots.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class TimeSlotDAO extends AbstractDAO{

    private static Logger logger = LogManager.getLogger("TIMESLOT");

    /**
     * Get the list of the slotID.
     *
     * @param conn the connection to the database.
     * @return the list of {@code TimeSlot} IDs.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<String> getSlotIDList(Connection conn)
        throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT id FROM libraryapp.slot;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> idList = new ArrayList<>();

            while (result.next()) {
                idList.add(result.getString("id"));
            }
            return idList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     *
     * Get the hour ranges.
     *
     * @param conn the connection to the database.
     * @return the list of {@code timeSlot} hour_range.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<String> getDaySlot(Connection conn)
        throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT hour_range FROM libraryapp.timeSlot;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> hour_rangeList = new ArrayList<>();

            while (result.next()) {
                hour_rangeList.add(result.getString("hour_range"));
            }
            return hour_rangeList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    public static List<TimeSlot> getDaySlots(Connection conn, Date date) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT * FROM libraryapp.timeSlot WHERE date = ?;";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1, date);

            result = stmnt.executeQuery();
            List<TimeSlot> slots = null;

            while (result.next()) {
                if(slots==null)
                    slots = new ArrayList<>();
                slots.add(new TimeSlot(result.getInt("id"), result.getString("hour_range"), result.getDate("date")));
            }
            return slots;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }

    }
    public static int getIDFromDayAndSlot(Connection conn, Date date, String hour_range) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT id FROM libraryapp.timeSlot WHERE date = ? AND hour_range = ?;";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1, date);
            stmnt.setObject(2, hour_range, Types.OTHER);
            logger.debug(stmnt.toString());
            result = stmnt.executeQuery();


            if (result.next()) {
                return result.getInt(1);
            }
            return -1;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }

    }

    public static TimeSlot getTimeSlot(Connection conn, long id) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT id,hour_range,date FROM libraryapp.timeSlot WHERE id = ?;";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setLong(1, id);
            logger.debug(stmnt.toString());
            result = stmnt.executeQuery();

            if (result.next()) {
                return new TimeSlot(result.getInt("id"),result.getString("hour_range"),result.getDate("date"));
            }
            return null;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }

    }
    public static int createDaySlots(Connection conn, Date date) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        int result = -1;
        String query = "INSERT INTO libraryapp.timeSlot values (DEFAULT , 'morning',?),(DEFAULT, 'afternoon',?),(DEFAULT, 'evening',?);";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1, date);
            stmnt.setDate(2, date);
            stmnt.setDate(3, date);

            result = stmnt.executeUpdate();
            if (result == 1)
                return 1;

            return 0;

        } finally {
            cleaningOperations(stmnt, conn);
        }

    }
}