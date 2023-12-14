package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.MemberDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.Member;
import it.unipd.dei.webapp.resources.User;
import it.unipd.dei.webapp.utils.ErrorCode;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Random;

/**
 * Servlet to insert a new user, to see all users and to update the user's role.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class UserServlet extends AbstractDatabaseServlet {

    /**
     * Method to view all users or the information of a single user given the phoneNumber and the list of all users.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{

        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("user")+5);
        boolean error = false;
        String phoneNumber = null;
        //Call the method to view users
        //If op = user/view/ return the list of all users
        if(op.contains("view")){
            op = op.substring(op.lastIndexOf("view")+4);
            if(op.equals("")) {
                //I have to return the list of all users
                try {
                    List<User> users = null;
                    users = UserDAO.getUserList(getDataSource().getConnection());
                    req.setAttribute("UserList", users);
                    req.setAttribute("UsersSize", users.size());
                    req.getRequestDispatcher("/jsp/protected/assAdmin/user-result.jsp").forward(req, res);

                } catch (SQLException e) {
                    logger.error("stacktrace:", e);
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
            }

        }
        //If op = user/information return info about this user
        else if (op.contains("information")) {
            HttpSession session = req.getSession();
            phoneNumber = session.getAttribute("phoneNumber").toString();
            if (phoneNumber == null) {
                error = true;
                if (error) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("ERROR: value of user is null. Check the query");
                }
            }
            try{
                User user = UserDAO.getUserByID(getDataSource().getConnection(), phoneNumber);
                if (user == null) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("User with this " + phoneNumber + " doesn't exist");
                }
                else{
                    req.setAttribute("UserInformation", user);
                    req.getRequestDispatcher("/jsp/protected/assAdmin/user-result.jsp").forward(req, res);
                }
            }
            catch(SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }

    }

    /**
     * Method to insert a new user or update the role of an existing user.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("user")+5);
        String error = "";
        //Insert a new user
        if(op.contains("insert")) {

            String phoneNumber = "";
            String password = "";
            String name = "";
            String surname = "";
            String role = "";
            User u = null;
            String m = null;
            int x =  5;

            String salt="";
            char c;
            Random random = new Random();
            for(int i=0; i<10;i++){
                c = (char) (random.nextInt(94)+33);
                salt = salt + c;
            }

            String city = "";
            String region = "";
            String homeTown = "";
            String bornRegion = "";
            Date birthday;

            try {
                //retrieve request parameters
                name = req.getParameter("name");
                surname = req.getParameter("surname");
                phoneNumber = req.getParameter("phoneNumber");
                password = req.getParameter("password");
                role = req.getParameter("role");
                logger.info("role " + role);

                if(name==null || surname==null || phoneNumber==null || role==null){
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    error = "true";
                    req.setAttribute("error", error);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                    //writeError(res,ErrorCode.EMPTY_INPUT_FIELDS);
                }else {
                    //create new user from the request parameters
                    u = new User(phoneNumber, password, salt, name, surname, role);

                    //creates new object for accessing the database and stores user
                    x = UserDAO.insertNewUser(getDataSource().getConnection(), u);

                    //if the insert is done correctly
                    if (x == 1) {
                        logger.info("role " + role);
                        if(role.equals("librarian"))
                        {
                            logger.info("role " + role);
                            res.setStatus(HttpServletResponse.SC_OK);
                            error = "false";
                            req.setAttribute("error", error);
                            req.getRequestDispatcher("/jsp/protected/librarian/create-librarian.jsp").forward(req, res);
                        }
                        else{
                            res.setStatus(HttpServletResponse.SC_OK);
                            error = "false";
                            req.setAttribute("error", error);
                            req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                        }

                    } else { //if wrong insertion
                        if(role == "librarian")
                        {
                            res.setStatus(HttpServletResponse.SC_OK);
                            error = "true";
                            req.setAttribute("error", error);
                            req.getRequestDispatcher("/jsp/protected/librarian/create-librarian.jsp").forward(req, res);
                        }
                        else{
                            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            error = "true";
                            req.setAttribute("error", error);
                            req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                            //writeError(res, ErrorCode.INTERNAL_ERROR);
                        }

                    }
                }
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                error = "true";
                req.setAttribute("error", error);
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                //writeError(res, ErrorCode.INTERNAL_ERROR);

            }
        }
        //method to update the user information, in particual the user's role
        else if(op.contains("update")) {

            op=op.substring(op.lastIndexOf("update")+6);

            if(op.contains("UsrToMember")){
                //Session parameter, it will be used to keep track of who made this update
                HttpSession session = req.getSession();
                String sessionPhoneNumber = session.getAttribute("phoneNumber").toString();

                String phoneNumber = req.getParameter("phoneNumber");
                String typeOfUpdateRole = req.getParameter("role");

                String user_oldRole = null;

                String hometown = req.getParameter("hometown");
                String bornRegion = req.getParameter("bornRegion");
                String birthdayString = req.getParameter("birthday");
                String city = req.getParameter("city");
                String region = req.getParameter("region");

                if(phoneNumber == null || typeOfUpdateRole == null || hometown == null || bornRegion == null ||
                        birthdayString == null || city ==null || region== null){

                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writeError(res, ErrorCode.EMPTY_INPUT_FIELDS);
                } else {
                    try {

                        user_oldRole = UserDAO.getUserByID(getDataSource().getConnection(),phoneNumber).getRole();

                        String output = null;
                        int insertDone = -1;

                        insertDone = UserDAO.updateUser(getDataSource().getConnection(), phoneNumber, typeOfUpdateRole);
                        output = "User " + phoneNumber + " updated to role " + typeOfUpdateRole;

                        if (insertDone == -1) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("Internal Error");
                            logger.error("ERROR: Query not executed");
                        } else if (insertDone == 0) {
                            //Tried but unsuccessful insertion
                            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            res.getWriter().write("Internal Error");
                            logger.error("ERROR: The update method failed");
                        } else {
                            //Successful insertion
                            insertDone = -1;
                            User u = UserDAO.getUserByID(getDataSource().getConnection(),phoneNumber);
                            if(user_oldRole.equals("association_admin")||user_oldRole.equals("association_member"))
                                MemberDAO.deleteUserData(getDataSource().getConnection(), u.getPhoneNumber());

                            Date birthday = Date.valueOf(birthdayString);
                            logger.debug(birthdayString+" date: "+birthday);
                            Member memb = new Member(u.getPhoneNumber(),u.getPassword(),u.getSalt(),u.getName(),u.getSurname(),
                                    typeOfUpdateRole,city,region,hometown,bornRegion,birthday);

                            insertDone = MemberDAO.insertNewUserData(getDataSource().getConnection(), memb);
                            output = "Member's data inserted.";

                            if (insertDone == -1) {
                                UserDAO.updateUser(getDataSource().getConnection(), phoneNumber, user_oldRole);
                                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                res.getWriter().write("Internal Error");
                                logger.error("ERROR: Query not executed");
                            } else if (insertDone == 0) {
                                //Tried but unsuccessful insertion
                                UserDAO.updateUser(getDataSource().getConnection(), phoneNumber, user_oldRole);
                                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                res.getWriter().write("Internal Error");
                                logger.error("ERROR: The update method failed");
                            } else {
                                //Insert done correctly
                                //Then I put the phoneNumber in the enroll table
                                //to keep track of this update
                                int enrollDone =-1;

                                int enroll;
                                if(user_oldRole.equals("association_admin")||user_oldRole.equals("association_member"))
                                    enroll = MemberDAO.updateEnroll(getDataSource().getConnection(),sessionPhoneNumber,phoneNumber);
                                else
                                    enroll = MemberDAO.insertEnroll(getDataSource().getConnection(), sessionPhoneNumber, phoneNumber);

                                if(enroll==-1){
                                    //The query was not executed
                                    //then I delete the data just entered and restore the previous role
                                    int delete1 = -1;
                                    delete1 = MemberDAO.deleteUserData(getDataSource().getConnection(),phoneNumber);
                                    UserDAO.updateUser(getDataSource().getConnection(), phoneNumber, user_oldRole);
                                    /*if(delete1==-1||delete1==0) {
                                        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                        res.getWriter().write("Internal Error during");
                                        logger.error("ERROR");
                                    }
                                    else{
                                        res.setStatus(HttpServletResponse.SC_OK);
                                        res.getWriter().write("Delete operation in UserData and update the Role to the olderRole is ok");
                                    }*/

                                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    res.getWriter().write("Internal Error");
                                    logger.error("ERROR: the insertion method in the enroll didn't start");
                                } else if (enroll==0){
                                    //Insert failed
                                    //then I delete the data just entered and restore the previous role
                                    int delete2 = -1;
                                    delete2 = MemberDAO.deleteUserData(getDataSource().getConnection(),phoneNumber);
                                    UserDAO.updateUser(getDataSource().getConnection(), phoneNumber, user_oldRole);
                                    /*if(delete2==-1||delete2==0) {
                                        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                        res.getWriter().write("Internal Error");
                                        logger.error("ERROR");
                                    }
                                    else{
                                        res.setStatus(HttpServletResponse.SC_OK);
                                        res.getWriter().write("Delete operation in UserData and update the Role to the olderRole is ok");
                                    }*/

                                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    res.getWriter().write("Internal Error");
                                    logger.error("ERROR: the insertion method in the enroll failed");
                                }
                                else {
                                    res.setStatus(HttpServletResponse.SC_OK);
                                    res.getWriter().write("Insert operation done\n");
                                }

                                res.setStatus(HttpServletResponse.SC_OK);
                                res.getWriter().write(output);

                            }
                        }
                    } catch (SQLException e) {
                        logger.error("stacktrace:", e);
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                    }
                }
            }
        }
    }
}