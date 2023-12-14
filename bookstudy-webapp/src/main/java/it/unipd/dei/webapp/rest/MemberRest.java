package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.MemberDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.Member;
import it.unipd.dei.webapp.resources.Message;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;


/**
    * Manage the REST call about the Member.
    *
    * @author BookStudy Group
    * @version 1.0
    * @since 1.0
*/

public class MemberRest extends RestResource{

    /**
     * Creates a new Member REST resource.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param ds the dataSource for the connection.
     */
    public MemberRest(HttpServletRequest req, HttpServletResponse res, DataSource ds) {
        super(req, res, ds);
    }

    public void getMemberData() throws IOException {

        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("member"));
        String spl[] = op.split("/");
        //spl[0] is member
        //spl[1] is phoneNumber
        //String phoneNumber = req.getParameter("phoneNumber");
        String phoneNumber = spl[1];
        if (phoneNumber == null ) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("ERROR: phoneNumber value is null.");
        } else {
            try {
                Member memb = MemberDAO.getUserDataByID(ds.getConnection(), phoneNumber);
                if (memb == null) {
                    Message m = new Message("There are no member data.");
                    logger.debug("phone number retrieved: "+phoneNumber);
                    res.setStatus(HttpServletResponse.SC_OK);
                    m.toJSON(res.getOutputStream());
                }else {
                    res.setStatus(HttpServletResponse.SC_OK);
                    memb.toJSON(res.getOutputStream());
                }
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }

    public void existUser() throws IOException {

        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("userCheck"));
        String spl[] = op.split("/");
        //spl[0] is checkUser
        //spl[1] is phoneNumber
        //String phoneNumber = req.getParameter("phoneNumber");
        String phoneNumber = spl[1];

        if (phoneNumber == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("ERROR: phoneNumber value is null.");
        } else {
            try {
                User user = UserDAO.getUserByID(ds.getConnection(), phoneNumber);
                if (user == null) {
                    Message m = new Message("There are no member data.");
                    //logger.debug("phone number retrieved: " + phoneNumber);
                    res.setStatus(HttpServletResponse.SC_OK);
                    m.toJSON(res.getOutputStream());
                } else {
                    res.setStatus(HttpServletResponse.SC_OK);
                    user.toJSON(res.getOutputStream());
                }
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }
}
