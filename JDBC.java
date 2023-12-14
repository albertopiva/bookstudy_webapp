import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class libraryapp {
    /**
     * The JDBC driver to be used
     */
    private static final String DRIVER = "org.postgresql.Driver";

    /**
     * The URL of the database to be accessed
     */
    private static final String DATABASE = "jdbc:postgresql://localhost/appdb";

    /**
     * The username for accessing the database
     */
    private static final String USER = "studyteam";

    /**
     * The password for accessing the database
     */
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {

        // the connection to the DBMS
        Connection con = null;
        // the statement to be executed
        Statement stmt = null;
        // the results of the statements execution
        ResultSet rs = null;
        ResultSet rs2 = null;
        // start time of a statement
        long start;
        // end time of a statement
        long end;

        // "data structures" for the data to be read from the database

        try {
            // register the JDBC driver
            Class.forName(DRIVER);
            System.out.printf("Driver %s successfully registered.%n", DRIVER);

        } catch (ClassNotFoundException e) {
            System.out.printf("Driver %s not found: %s.%n", DRIVER, e.getMessage());
            // terminate with a generic error code
            System.exit(-1);
        }



        try {
            // connect to the database
            start = System.currentTimeMillis();
            con = DriverManager.getConnection(DATABASE, USER, PASSWORD);
            end = System.currentTimeMillis();
            System.out.printf("Connection to database %s successfully established in %,d milliseconds.%n",DATABASE, end - start);

            // create the statement to execute the query
            start = System.currentTimeMillis();
            stmt = con.createStatement();
            end = System.currentTimeMillis();
            System.out.printf("Statement successfully created in %,d milliseconds.%n",end - start);

            //get all the parks
            String sql="SELECT memberID, name, surname from libraryapp.memberdata;";
            start = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);

            System.out.printf("%nEvent List:%n");

            String memberID;
            String name;
            String surname;

            // cycle on the query results
            while (rs.next()) {
                // read member identifier (phone number)
                memberID = rs.getString("memberID");

                // read name member
                name = rs.getString("name");

                // read surname member
                surname = rs.getString("surname");

                System.out.printf("%nPhone number: %s,\t\tName: %s,\t\tSurname: %s%n",memberID, name, surname);
            }

            sql = "((SELECT r.date, sl.id as slotID, se.id as seatID, se.room, r.alphanumericcode " +
                    "FROM libraryapp.slot AS sl " +
                    "INNER JOIN libraryapp.seat AS se ON se.id=sl.seat "+
                    "INNER JOIN libraryapp.reservation AS r ON r.mslotid=sl.id "+
                    "INNER JOIN libraryapp.member AS m ON  m.phonenumber=r.memberid "+
                    "INNER JOIN libraryapp.memberdata AS md ON r.memberid=m.phonenumber " +
                    "ORDER BY r.date ) "+
                    "UNION " +
                    "(SELECT r.date, sl.id as slotID, se.id as seatID, se.room, r.alphanumericcode " +
                    "FROM libraryapp.slot AS sl " +
                    "INNER JOIN libraryapp.seat AS se ON se.id=sl.seat "+
                    "INNER JOIN libraryapp.reservation AS r ON r.uslotid=sl.id "+
                    "INNER JOIN libraryapp.user AS u ON u.phonenumber=r.userid " +
                    "ORDER BY r.date ASC))";

            rs2 = stmt.executeQuery(sql);
            String Date;
            String Slot_id;
            String Seat_id;
            String Room;
            String Alphanumericcode;

            if(!rs2.isBeforeFirst()){ //if there are no reservation
                System.out.printf("%nThere are no reservation yet!%n");
            }
            else {
                System.out.printf("\n\n\tDATE\t\t\tSLOT_ID\t\tSEAT_ID\t\tROOM\tAlphanumeric Code\n");
                while (rs2.next()){	//if there is at least one reservation
                    Date = rs2.getString("date");
                    Slot_id = rs2.getString("slotID");
                    Seat_id = rs2.getString("seatID");
                    Room = rs2.getString("room");
                    Alphanumericcode = rs2.getString("alphanumericcode");

                    System.out.printf("\t%s\t\t%s\t\t%s\t\t%s\t\t%s%n", Date, Slot_id, Seat_id, Room, Alphanumericcode);
                }
            }



            //close all the pointer and connection
            rs.close();
            rs2.close();
            stmt.close();
            con.close();
            end=System.currentTimeMillis();
            System.out.printf("%nData correctly exctracted and visualized in %d milliseconds.%n",end - start);
        }catch (SQLException e){
            System.out.printf("%nDatabase access error:%n");

            while (e != null) {
                System.out.printf("- Message: %s%n", e.getMessage());
                System.out.printf("- SQL status code: %s%n", e.getSQLState());
                System.out.printf("- SQL error code: %s%n", e.getErrorCode());
                System.out.printf("%n");
                e = e.getNextException();
            }
        }finally{
            try {
                // close the used resources
                if (!rs.isClosed()) {
                    start = System.currentTimeMillis();

                    rs.close();
                    end = System.currentTimeMillis();

                    System.out.printf("%nResult set successfully closed in finally block in %,d milliseconds.%n", end - start);
                }

                if (!rs2.isClosed()) {
                    start = System.currentTimeMillis();

                    rs2.close();
                    end = System.currentTimeMillis();

                    System.out.printf("Result set successfully closed in finally block in %,d milliseconds.%n", end - start);
                }

                if (!stmt.isClosed()) {
                    start = System.currentTimeMillis();
                    stmt.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Statement successfully closed in finally block in %,d milliseconds.%n", end - start);
                }

                if (!con.isClosed()) {
                    start = System.currentTimeMillis();
                    con.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Connection successfully closed in finally block in %,d milliseconds.%n", end - start);
                }
            }catch (SQLException e) {
                System.out.printf("Error while releasing resources in finally block:%n");
                // cycle in the exception chain
                while (e != null) {
                    System.out.printf("- Message: %s%n", e.getMessage());
                    System.out.printf("- SQL status code: %s%n", e.getSQLState());
                    System.out.printf("- SQL error code: %s%n", e.getErrorCode());
                    System.out.printf("%n");
                    e = e.getNextException();
                }
            }finally {
                // release resources to the garbage collector
                rs = null;
                stmt = null;
                con = null;
                System.out.printf("Resources released to the garbage collector.%n");
            }

        }

        System.out.printf("Program end.%n");
    }
}