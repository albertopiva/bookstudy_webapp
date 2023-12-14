package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils; //è stata aggiunta una dependecy nel pom file

/**
 * Class to manage the User in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class UserDAO extends AbstractDAO {
    /**
     * Get the list of phoneNumbers in the database.
     *
     * @param conn the connection to the database.
     * @return List of {@code User} phone Numbers
     * @throws SQLException the exception thrown if something goes wrong.
     */

    public static List<String> getUsersPhoneList(Connection conn) throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT phonenumber FROM libraryapp.user;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<String> userList = new ArrayList<>();

            while (result.next()) {
                userList.add(result.getString("phoneNumber"));
            }
            return userList;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Function that authenticate a {@code User}
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of a user.
     * @param password the password of a user.
     * @return {@code User} if it exist in the db or null.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static User authenticateUser(Connection conn, String phoneNumber, String password) throws SQLException {

        User u = getUserByID(conn, phoneNumber);
        if (u == null)
            return null;

        //Convert the given password
        String saltPass = password+u.getSalt();
        String encryptedPassword = DigestUtils.md5Hex(saltPass).toUpperCase();

        if (u.getPassword().equals(encryptedPassword))
            return u;

        return null;
    }

    /**
     * Get the user by its phoneNumber.
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of the user.
     * @return the {@code User} object by its {@param phoneNumber}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static User getUserByID(Connection conn, String phoneNumber) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT * FROM libraryapp.user WHERE phonenumber=?;";
        User user = null;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumber);

            result = stmnt.executeQuery();

            if (result.next()) {
                String phone = result.getString("phonenumber");
                String password = result.getString("password");
                String salt =result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String role = result.getObject("role").toString();
                user = new User(phone, password, salt, name, surname, role);
            }
            return user;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the user list in the database.
     *
     * @param conn the connection to the database.
     * @return the list of {@code User}s.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<User> getUserList(Connection conn) throws SQLException {
        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.user;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            ArrayList<User> users = new ArrayList<>();

            while (result.next()) {
                String phone = result.getString("phonenumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String role = result.getString("role");
                users.add(new User(phone, password, salt, name, surname, role));
            }
            return users;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Method that insert a new user.
     *
     * @param conn the connection to the database.
     * @param u the {@code User} to insert in the database.
     * @return 1 if the insert goes well, 0 otherwise.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int insertNewUser(Connection conn, User u) throws SQLException {
        PreparedStatement stmnt = null;
        int result = -1;
        String query = "INSERT INTO libraryapp.user values(?,?,?,?,?,?)";

        //User user = null;
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, u.getPhoneNumber());
            String saltPass = u.getPassword()+u.getSalt();
            stmnt.setString(2, DigestUtils.md5Hex(saltPass).toUpperCase());
            stmnt.setString(3,u.getSalt());
            stmnt.setString(4, u.getName());
            stmnt.setString(5, u.getSurname());
            stmnt.setObject(6, u.getRole(), Types.OTHER);

            result = stmnt.executeUpdate();
            if (result == 1)
                return 1;

            return 0;
        } finally {
            cleaningOperations(stmnt, conn);
        }
    }

    /**
     * Update an user's role.
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of the user to update.
     * @param role the new role of the user.
     * @return 1 if the role was updated.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int updateUser(Connection conn, String phoneNumber, String role)
            throws SQLException {
        PreparedStatement stmnt = null;
        String query = "UPDATE libraryapp.user SET role = ? where phoneNumber= ?";
        try {
            //update role of the user
            stmnt = conn.prepareStatement(query);
            stmnt.setString(2, phoneNumber);
            stmnt.setObject(1, role, Types.OTHER);
            int result = stmnt.executeUpdate();
            if (result == 1) {
                return 1;
            } else {
                return 0;
            }

        } finally {
            cleaningOperations(stmnt, conn);
        }
    }
}