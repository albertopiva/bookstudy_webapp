package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.Conference;
import it.unipd.dei.webapp.resources.ConferenceBook;
import it.unipd.dei.webapp.resources.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the ConferenceBook in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class ConferenceBookDAO extends AbstractDAO{

    /**
     * Method that insert a user conference booking.
     *
     * @param conn the connection to the database.
     * @param b the {@code ConferenceBook} object to insert in the database.
     * @return 1 if the insert goes well, otherwise 0.
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static int insertNewConferenceReservation(Connection conn, ConferenceBook b) throws SQLException {

        PreparedStatement stmnt = null;
        int result = -1;
        String query = "INSERT INTO libraryapp.conferenceBook values(?,?);";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, b.getUserID());
            stmnt.setString(2, b.getConferenceID());

            result = stmnt.executeUpdate();
            if(result == 1)
                return 1;

            return 0;

        }finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Return the list of all the reservation made by an user for any conference.
     *
     * @param conn the connection to the database.
     * @param userID the identifier of an User in the database.
     * @return the List of {@code Conference} by a given {@param userID}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<Conference> getAllReservationByUser(Connection conn, String userID) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select alphanumericCode, date, title, description, organizerID, conferenceRoomID " +
                        "from libraryapp.conferenceBook AS CB INNER JOIN " +
                        "libraryapp.conference AS C ON C.alphanumericCode = CB.conferenceID " +
                        "where CB.userid = ? order by C.date;";
        String query2= "select conferenceID from libraryapp.conferenceBook where userID = ?;";

        ArrayList<Conference> conf = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, userID);

            result = stmnt.executeQuery();
            conf = new ArrayList<>();

            while (result.next()) {
                //Conference c = ConferenceDAO.getConferenceByCode(conn,result.getString(1));
                String code = result.getString("alphanumericCode");
                Date date = result.getDate("date");
                String title = result.getString("title");
                String description = result.getString("description");
                String organizerID = result.getString("organizerID");
                String slotid = result.getString("conferenceRoomID");
                conf.add(new Conference(code,date,title,description,organizerID,slotid));
            }
            return conf;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Return the list of all the user booked for a conference.
     *
     * @param conn the connection to the database.
     * @param conferenceID the identifier of a Conference.
     * @return the list of all {@code User} that booked a reservation for a given Conference.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static ArrayList<User> getAllUserByConference(Connection conn, String conferenceID) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "select U.phoneNumber, U.password, U.salt, U.name, U.surname, U.role " +
                "from libraryapp.conferenceBook AS CB INNER JOIN " +
                "libraryapp.user AS U ON U.phoneNumber = CB.userID " +
                "where CB.conferenceID = ?;";

        ArrayList<User> users = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, conferenceID);

            result = stmnt.executeQuery();
            users = new ArrayList<>();

            while (result.next()) {

                String phoneNumber = result.getString("phoneNumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String role = result.getString("role");
                users.add(new User(phoneNumber, password, salt, name, surname, role));

            }
            return users;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }


}
