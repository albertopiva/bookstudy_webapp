package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.Organizer;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the Organizer in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class OrganizerDAO extends AbstractDAO {

    /**
     * Method that insert a new organizer.
     *
     * @param conn the connection to the database.
     * @param org the {@code Organizer} to insert in the database.
     * @return 1 if the organizer was created.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int newOrganizer(Connection conn, Organizer org) throws SQLException{

        PreparedStatement stmnt = null;
        int result = -1;

        String query = "INSERT INTO libraryapp.organizer VALUES(?,?,?,?,?,?);";

        try{
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, org.getPhoneNumber());
            String saltPass = org.getPassword()+org.getSalt();
            stmnt.setString(2, DigestUtils.md5Hex(saltPass).toUpperCase());
            stmnt.setString(3, org.getSalt());
            stmnt.setString(4, org.getName());
            stmnt.setString(5, org.getSurname());
            stmnt.setString(6, org.getAssociation());

            result = stmnt.executeUpdate();
            if (result==1)
                return 1;

            return 0;
        } finally {
            cleaningOperations(stmnt, null, conn);
        }

    }

    /**
     * Get the list of Organizer's phone numbers.
     *
     * @param conn the connection to the database.
     * @return the list of {@code Organizer}'s phone numbers.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<String> getOrganizerPhoneList(Connection conn) throws SQLException {


        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT phoneNumber FROM libraryapp.organizer;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> organizerList = new ArrayList<>();

            while (result.next()) {
                organizerList.add(result.getString("phoneNumber"));
            }

            return organizerList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Authenticate an Organizer.
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of the organizer to authenticate.
     * @param password the password of the organizer to authenticate.
     * @return the {@code Organizer} object.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static Organizer authenticateOrganizer(Connection conn, String phoneNumber, String password)
            throws SQLException{

        Organizer s = getOrganizerByID(conn,phoneNumber);
        if(s == null)
            return null;

        //Convert the given password
        String saltPass = password+s.getSalt();
        String encryptedPassword = DigestUtils.md5Hex(saltPass).toUpperCase();

        if (s.getPassword().equals(encryptedPassword))
            return s;
        return null;
    }

    /**
     * Function to get a {@code Organizer} by its {@param phoneNumber}.
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of the organizer to authenticate.
     * @return {@code Organizer} object if exist otherwise null
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static Organizer getOrganizerByID(Connection conn, String phoneNumber) throws SQLException{

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.organizer WHERE phoneNumber=?;";

        Organizer organizer = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumber);

            result = stmnt.executeQuery();

            if (result.next()) {
                String phoneNumb = result.getString("phoneNumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String association = result.getString("association");
                organizer = new Organizer(phoneNumb,password,salt,name,surname,association);
            }
            return organizer;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the list of the organizers in the database.
     *
     * @param conn the connection to the database.
     * @return list of {@code Organizer} objects.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<Organizer> getOrganizerList(Connection conn)  throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.organizer;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<Organizer> organizerList = new ArrayList<>();

            while (result.next()) {
                String phoneNumber = result.getString("phoneNumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String association = result.getString("association");
                organizerList.add(new Organizer(phoneNumber,password,salt,name,surname,association));
            }
            return organizerList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }
}
