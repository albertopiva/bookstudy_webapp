package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resources.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Class to manage the Member in the database.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class MemberDAO extends AbstractDAO{


    final static Logger logger = LogManager.getLogger(MemberDAO.class);

    /**
     * Get the list of the member phoneNumber.
     *
     * @param conn the connection to the database.
     * @return List of {@code Member} phone Numbers.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<String> getUsersPhoneList(Connection conn)
            throws SQLException {

        //Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        String query = "SELECT phoneNumber FROM libraryapp.userData;";

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
     * Get the Member object from its phoneNumber
     *
     * @param conn the connection to the database.
     * @param phoneNumber the phoneNumber of a member.
     * @return the {@code Member} object by its {@param phoneNumber}.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static Member getUserDataByID(Connection conn, String phoneNumber) throws SQLException{

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.userData AS UD " +
                "INNER JOIN libraryapp.user AS U ON U.phonenumber = UD.userID " +
                "WHERE phonenumber=?;";
        Member userData = null;

        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumber);

            result = stmnt.executeQuery();

            if (result.next()) {
                String phone = result.getString("phoneNumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String role = result.getString("role");
                String city = result.getString("city");
                String region = result.getString("region");
                String homeTown = result.getString("hometown");
                String bornRegion = result.getString("bornRegion");
                Date date = result.getDate("birthday");
                userData = new Member(phoneNumber,password,salt,name,surname,role,city,region,homeTown,bornRegion,date);
            }
            return userData;
        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Get the list of Member in the database.
     *
     * @param conn the connection to the database.
     * @return List of {@code Member}s.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static List<Member> getUserDataList(Connection conn) throws SQLException {

        PreparedStatement stmnt = null;
        ResultSet result = null;

        String query = "SELECT * FROM libraryapp.userData AS UD" +
                "INNER JOIN libraryapp.user AS U ON U.phoneNumber = UD.phoneNumber;";

        try {
            stmnt = conn.prepareStatement(query);

            result = stmnt.executeQuery();

            List<Member> membersData = new ArrayList<>();

            while (result.next()) {
                String phoneNumber = result.getString("phoneNumber");
                String password = result.getString("password");
                String salt = result.getString("salt");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String role = result.getString("role");
                String city = result.getString("city");
                String region = result.getString("region");
                String homeTown = result.getString("hometown");
                String bornRegion = result.getString("bornRegion");
                Date date = result.getDate("birthday");
                membersData.add(new Member(phoneNumber,password,salt,name,surname,role,city,region,homeTown,bornRegion,date));
            }
            return membersData;

        } finally {
            cleaningOperations(stmnt, result, conn);
        }
    }

    /**
     * Method that insert the data of the user that it was update to "associate" user.
     *
     * @param conn the connection to the database.
     * @param u the {@code Member} data that will be insert in the database.
     * @return 1 if the insert goes well, 0 otherwise.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int insertNewUserData(Connection conn, Member u) throws SQLException {

        PreparedStatement stmnt = null;
        int result;
        String query = "INSERT INTO libraryapp.userData VALUES (?,?,?,?,?,?)";

        try{
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, u.getPhoneNumber());
            stmnt.setString(2, u.getHometown());
            stmnt.setString(3, u.getBornRegion());
            stmnt.setDate(4,u.getBrithday());
            stmnt.setString(5, u.getCity());
            stmnt.setString(6, u.getRegion());

            logger.debug(stmnt.toString());

            result = stmnt.executeUpdate();
            if(result == 1)
                return 1;

            return 0;
        } finally {
            cleaningOperations(stmnt, null, conn);
        }
    }

    /**
     * Method that delete the data of the user.
     *
     * @param conn the connection to the database.
     * @param phoneNumber phonenumber of the user whose data is being deleted.
     * @return 1 if the delete goes well, 0 otherwise.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int deleteUserData(Connection conn, String phoneNumber) throws SQLException {
        int result =0;
        PreparedStatement stmnt = null;
        String query = "DELETE FROM libraryapp.userData WHERE userID = ?";
        try {
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumber);
            logger.debug(stmnt.toString());
            result = stmnt.executeUpdate();
            if(result==1)
                return 1;
            return 0;
        }finally {
            cleaningOperations(stmnt, null, conn);
        }
    }

    /**
     * Insert data in the enroll table.
     *
     * @param conn the connection to the database.
     * @param phoneNumberDo the phoneNumber of the user that do the update.
     * @param phoneNumberUpdated the phoneNumber of the user whose role is updated.
     * @return 1 if the insert goes well.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int insertEnroll(Connection conn, String phoneNumberDo, String phoneNumberUpdated)
            throws SQLException {
        PreparedStatement stmnt = null;
        String query = "INSERT INTO libraryapp.enroll VALUES (?,?)";
        try {
            //update role of the user
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumberDo);
            stmnt.setObject(2, phoneNumberUpdated);
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

    /**
     * Update data in the enroll table.
     *
     * @param conn the connection to the database.
     * @param phoneNumberDo the phoneNumber of the user that do the update.
     * @param phoneNumberUpdated the phoneNumber of the user whose role is updated.
     * @return 1 if the insert goes well.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    public static int updateEnroll(Connection conn, String phoneNumberDo, String phoneNumberUpdated)
            throws SQLException {
        PreparedStatement stmnt = null;
        String query = "UPDATE libraryapp.enroll SET userID = ? WHERE newPerson = ?;";
        try {
            //update role of the user
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, phoneNumberDo);
            stmnt.setObject(2, phoneNumberUpdated);
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