package it.unipd.dei.webapp.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract DAO object class.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class AbstractDAO {


    /**
     * Cleaning and close all the operations/variables used
     *
     * @param stmnt the PreparedStatement to close.
     * @param result the ResultSet to close.
     * @param conn the connection to the database.
     * @throws SQLException the exception thrown if something goes wrong.
     */
     static void cleaningOperations(PreparedStatement stmnt, ResultSet result, Connection conn)
             throws SQLException {

        if (stmnt!=null) {
            stmnt.close();
        }
        if (result!=null) {
            result.close();
        }
        if (conn!=null) {
            conn.close();
        }
    }

    /**
     * Cleaning and close all the operations/variables used
     * @param stmnt the PreparedStatement to close.
     * @param conn the connection to the database.
     * @throws SQLException the exception thrown if something goes wrong.
     */
    static void cleaningOperations(PreparedStatement stmnt, Connection conn) throws SQLException {
        cleaningOperations(stmnt, null, conn);
    }
}
