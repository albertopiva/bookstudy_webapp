package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to manage the Seat Reservation in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class SeatReservationDAO extends AbstractDAO {

    /**
     * The timezone.
     */
    public static final Calendar TZUTC = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));

    static Logger logger = LogManager.getLogger(SeatReservationDAO.class);

    /**
     * Method that insert a new user reservation.
     *
     * @param conn the connection to the database.
     * @param r the {@code SeatReservation} to insert in the database.
     * @return 1 if the {@code SeatReservation} was inserted.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int insertNewUserReservation(Connection conn, SeatReservation r) throws SQLException {
        PreparedStatement stmnt = null;
        int result = -1;
        String query = "INSERT INTO libraryapp.seatReservation values(?,?,null,null,?,?,?);";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, r.getAlphanumericCode());
            stmnt.setDate(2, r.getDate());
            stmnt.setLong(3, r.getSlot().getSlotID());
            stmnt.setLong(4, r.getSeat().getSeatID());
            stmnt.setString(5, r.getUserID());

            result = stmnt.executeUpdate();
            if (result == 1)
                return 1;

            return 0;

        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Return the SeatReservation giving the alphanumericcode of the reservation.
     *
     * @param conn the connection to the database.
     * @param alphanumericCode the identifier of the reservation.
     * @return the {@code SeatReservation} given the {@param alphanumericCode}
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static SeatReservation getReservationByCode(Connection conn, String alphanumericCode) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select alphanumericcode,s.date as date,entrytime as entrytime,exittime as exittime, " +
                "userid,ti.id as slotid,ti.hour_range as hour_range, " +
                "l.id as seatid, l.room as room " +
                "from libraryapp.seatReservation as s  " +
                "join libraryapp.timeSlot as ti on s.slotid = ti.id " +
                "join libraryapp.libraryseat as l on s.seatid = l.id " +
                "WHERE alphanumericcode=?;" ;
        SeatReservation reser = null;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, alphanumericCode);

            result = stmnt.executeQuery();

            if (result.next()) {
                String code = result.getString("alphanumericcode");
                Date date = result.getDate("date");
                Time entry = result.getTime("entrytime", Calendar.getInstance(TimeZone.getTimeZone("Rome")));
                Time exit = result.getTime("exittime", Calendar.getInstance(TimeZone.getTimeZone("Rome")));
                String userID = result.getString("userid");
                long slotid = result.getLong("slotid");
                TimeSlot ts = new TimeSlot(slotid,result.getString("hour_range"),date);
                long seatid = result.getLong("seatid");
                LibrarySeat ls = new LibrarySeat(seatid,result.getString("room"));
                reser = new SeatReservation(code, date, entry, exit, userID, ts, ls);

            }
            return reser;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * The list of all the reservation made by a specific user.
     *
     * @param conn the connection to the database.
     * @param userID the identifier of the user to get all its reservations.
     * @return the list of of {@code SeatReservation}.
     * @throws SQLException the exception thrown if something goes wrong in SQL.
     * @throws ParseException the exception thrown if something goes wrong in time parsing.
     */
    public static ArrayList<SeatReservation> getAllReservationByUser(Connection conn, String userID)
            throws SQLException,ParseException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select alphanumericcode,s.date as date,entrytime as entrytime,exittime as exittime, " +
                "userid,ti.id as slotid,ti.hour_range as hour_range, " +
                "l.id as seatid, l.room as room " +
                "from libraryapp.seatReservation as s  " +
                "join libraryapp.timeSlot as ti on s.slotid = ti.id " +
                "join libraryapp.libraryseat as l on s.seatid = l.id " +
                "where userid = ? order by date;" ;

        ArrayList<SeatReservation> reser = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, userID);

            result = stmnt.executeQuery();

            reser = new ArrayList<>();

            while (result.next()) {
                String code = result.getString("alphanumericcode");
                Date date = result.getDate("date");

                //set the formatter for read the time
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

                //get the entry and exit time as string
                String strEntry = result.getString("entrytime");
                String strExit = result.getString("exittime");

                //convert to time
                Time entry = null;
                if(strEntry != null && !strEntry.equals(""))
                    entry = new Time(formatter.parse(strEntry).getTime());

                Time exit = null;
                if(strExit !=null && !strExit.equals(""))
                    exit = new Time(formatter.parse(strExit).getTime());
                String userid = result.getString("userid");
                long slotid = result.getLong("slotid");
                TimeSlot ts = new TimeSlot(slotid,result.getString("hour_range"),date);
                long seatid = result.getLong("seatid");
                LibrarySeat ls = new LibrarySeat(seatid,result.getString("room"));
                reser.add(new SeatReservation(code, date, entry, exit, userid, ts, ls));
            }

            return reser;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the list of all reservation made in a given date.
     *
     * @param conn the connection to the database.
     * @param date the date to search the reservations.
     * @return the list of {@code SeatReservation}.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static ArrayList<SeatReservation> getAllReservationByDate(Connection conn, Date date) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select alphanumericcode,s.date as date,entrytime as entrytime,exittime as exittime, " +
                "userid,ti.id as slotid,ti.hour_range as hour_range, " +
                "l.id as seatid, l.room as room " +
                "from libraryapp.seatReservation as s  " +
                "join libraryapp.timeSlot as ti on s.slotid = ti.id " +
                "join libraryapp.libraryseat as l on s.seatid = l.id " +
                "where date = ?;" ;
        ArrayList<SeatReservation> reser = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1, date);

            result = stmnt.executeQuery();

            reser = new ArrayList<>();

            while (result.next()) {

                String code = result.getString("alphanumericcode");
                Time entry = result.getTime("entrytime", Calendar.getInstance(TimeZone.getTimeZone("Rome")));
                Time exit = result.getTime("exittime", Calendar.getInstance(TimeZone.getTimeZone("Rome")));
                String userid = result.getString("userid");
                long slotid = result.getLong("slotid");
                TimeSlot ts = new TimeSlot(slotid,result.getString("hour_range"),date);
                long seatid = result.getLong("seatid");
                LibrarySeat ls = new LibrarySeat(seatid,result.getString("room"));
                reser.add(new SeatReservation(code, date, entry, exit, userid, ts, ls));

            }
            return reser;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }


    /**
     * Update the entry_time by a given reservation alphanumericCode.
     *
     * @param conn the connection to the database.
     * @param alphanumericCode the identifier of the reservation to set the entry time.
     * @return 1 if the reservation was updated.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int setEntryTime(Connection conn, String alphanumericCode) throws SQLException {
        PreparedStatement stmnt = null;
        int result = -1;
        String query = "UPDATE libraryapp.seatReservation set entrytime = now() WHERE alphanumericcode = ?;";
        SeatReservation reser = null;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, alphanumericCode);

            result = stmnt.executeUpdate();
            if (result == 1)
                return 1;
            return 0;

        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Update the exit_time by a given reservation alphanumericCode.
     *
     * @param conn the connection to the database.
     * @param alphanumericCode the identifier of the reservation to set the exit time.
     * @return 1 if the reservation was updated.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int setExitTime(Connection conn, String alphanumericCode) throws SQLException {
        PreparedStatement stmnt = null;
        int result = -1;
        String query = "UPDATE libraryapp.seatReservation set exittime = now() WHERE alphanumericcode = ?;";
        SeatReservation reser = null;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, alphanumericCode);
            System.out.println(stmnt.toString());
            System.out.println(query);
            result = stmnt.executeUpdate();
            if (result == 1)
                return 1;
            return 0;

        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Get the list of the free Seat for a given date and slot.
     *
     * @param conn the connection to the database.
     * @param date the date to search free seats.
     * @param slot the slot of the day to search free seats.
     * @return the list of {@code LibrarySeat} available for that day and that slot.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static ArrayList<LibrarySeat> getListFreeSeatByGivenSlotAndData(Connection conn, Date date, long slot)
            throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "WITH freeSeat as (select libraryapp.libraryseat.id from libraryapp.libraryseat " +
                "EXCEPT " +
                "(select libraryapp.seatreservation.seatid from libraryapp.seatreservation " +
                "where date = ? and slotid = ?)) " +
                "select * from freeSeat " +
                "natural join libraryapp.libraryseat as L;";
        //String query = "select * from libraryapp.seatReservation where userid = ? order by date;";

        ArrayList<LibrarySeat> freeSeat = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setDate(1, date);
            stmnt.setLong(2, slot);

            result = stmnt.executeQuery();


            freeSeat = new ArrayList<>();

            while (result.next()) {
                int seatID = result.getInt("id");
                String room = result.getString("room");
                freeSeat.add(new LibrarySeat(seatID, room));

            }
            return freeSeat;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }
}

