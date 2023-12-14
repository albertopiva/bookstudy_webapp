package it.unipd.dei.webapp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.webapp.rest.*;
import it.unipd.dei.webapp.utils.ErrorCode;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Servlet that manage the rest call.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class RestDispatcherServlet extends AbstractDatabaseServlet{


    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String op = req.getRequestURI();
        try {
            if (processSeatReservation(req, res)) {
                return;
            }
            if (processConference(req, res)) {
                return;
            }
            if(processMember(req,res)){
                return;
            }
            if(processUserExistence(req,res)){
                return;
            }
            if(processAuthentication(req,res)){
                return;
            }
            if(processAllConference(req,res)){
                return;
            }
            writeError(res, ErrorCode.OPERATION_UNKNOWN);
            logger.warn("requested op " + op);
        }catch(SQLException s){
            logger.error("SQL Exception: "+s.getMessage());
            writeError(res,ErrorCode.INTERNAL_ERROR);
        }
    }

    private boolean processConference(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        if(!op.contains("conference"))
            return false;
        op = op.substring(op.lastIndexOf("conference"));
        String[] tokens = op.split("/");
        //the first token will always be "conference";
        //the second can be "view" or "manage";

        if(tokens[1].equals("view")){
            switch (req.getMethod()) {
                case "GET":
                    if (tokens.length < 3) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else {
                        new ConferenceRest(req, res, getDataSource()).getAttendanceForGivenConference();
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("list")){
            switch (req.getMethod()) {
                case "GET":
                    new ConferenceRest(req, res, getDataSource()).getAttendance();
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("room")) {
            switch (req.getMethod()) {
                case "GET":
                    new RoomRest(req, res, getDataSource()).getRooms();
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("manage")){
            switch (req.getMethod()) {
                //insert new conference
                case "POST":
                    new ConferenceRest(req, res, getDataSource()).insertConference();
                    break;
                //delete an existing one
                case "DELETE":
                    if (tokens.length < 3) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else{
                        new ConferenceRest(req, res, getDataSource()).deleteConference();
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else{
            return false;
        }
        return true;
    }

    private boolean processAllConference(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        if(!op.contains("allConference"))
            return false;
        op = op.substring(op.lastIndexOf("allConference"));
        String[] tokens = op.split("/");

        //the first token will always be "allConference";

        switch (req.getMethod()) {
            case "GET":
                //only the date so I return only the seat
                new ConferenceRest(req,res,getDataSource()).getAllConference();
                break;
            default:
                writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
        }
        return true;
    }

    private boolean processSeatReservation(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        if(!op.contains("seatReserv"))
            return false;
        op = op.substring(op.lastIndexOf("seatReserv"));
        String[] tokens = op.split("/");

        //the first token will always be "seatReserv";

        //the second can be "free" or "view" or "insert" or "update";

        if (tokens[1].equals("free")) {
            switch (req.getMethod()) {
                //
                case "GET":
                    logger.debug(tokens.length);
                    if( tokens.length==3){
                        //only the date so I return only the seat
                        logger.debug("primo if ");
                        new SeatReservationRest(req, res, getDataSource()).getSlot();

                    }else {
                        if (tokens.length == 4) {
                            new SeatReservationRest(req, res, getDataSource()).getFreeSeat();
                        } else {
                            logger.error("GET FREE SEAT LESS THAN 4 token");
                            writeError(res, ErrorCode.WRONG_REST_FORMAT);
                        }
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if (tokens[1].equals("view") && tokens[2].equals("user") ) {
            switch (req.getMethod()) {
                //view reservation
                case "GET":
                    if (tokens.length < 4) {
                        new SeatReservationRest(req, res, getDataSource()).getAllReservationByUser(req.getSession().getAttribute("phoneNumber").toString());
                    } else {
                        new SeatReservationRest(req, res, getDataSource()).getAllReservationByUser(tokens[3]);
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if (tokens[1].equals("view") && tokens[2].equals("date") ) {
            switch (req.getMethod()) {
                //view conference by date
                case "GET":
                    if (tokens.length < 4) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else {
                        new SeatReservationRest(req, res, getDataSource()).getFreeSeat();
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if (tokens[1].equals("insert") ) {
            switch (req.getMethod()) {
                //insert new reservation
                case "POST":
                    new SeatReservationRest(req, res, getDataSource()).insertReservation();

                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if (tokens[1].equals("update") ) {
            switch (req.getMethod()) {
                //update reservation
                case "UPDATE":
                    if (tokens.length < 4) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else {
                        if(tokens[2].equals("entry"))
                            new SeatReservationRest(req, res, getDataSource()).updateReservation(tokens[3],false);
                        else if(tokens[2].equals("exit"))
                            new SeatReservationRest(req, res, getDataSource()).updateReservation(tokens[3],true);
                        else
                            writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else{
            return false;
        }
        return true;
    }

    private boolean processMember(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        logger.debug(op);
        if (!op.contains("member"))
            return false;
        String[] tokens = op.split("/");
        //the first token will always be
        //the second can be
        switch (req.getMethod()) {
            case "GET":
                if (tokens.length < 2) {
                    writeError(res, ErrorCode.PHONE_NUMBER_MISSING);
                } else new MemberRest(req, res, getDataSource()).getMemberData();

                return true;
            default:
                writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
        }
        return false;
    }

    private boolean processUserExistence(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        logger.debug(op);
        if (!op.contains("userCheck"))
            return false;
        String[] tokens = op.split("/");
        //the first token will always be
        // the second can be
        //logger.debug("0: "+tokens[0]+" 1: "+tokens[1]+" 2: "+tokens[2]+" 3: "+tokens[3]);
        switch (req.getMethod()) {
            case "GET":
                if (tokens.length < 2) {
                    writeError(res, ErrorCode.PHONE_NUMBER_MISSING);
                } else new MemberRest(req, res, getDataSource()).existUser();
                return true;
            default:
                writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
        }
        return false;
    }

    private boolean processAuthentication(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String op = req.getRequestURI();
        if(!op.contains("auth"))
            return false;
        op = op.substring(op.lastIndexOf("auth"));
        String[] tokens = op.split("/");
        //the first token will always be "auth";
        //the second can be "user" or "organizer";

        if(tokens[1].equals("user")){
            switch (req.getMethod()) {
                case "POST":
                    if (tokens.length > 2) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else {
                        new AuthenticationRest(req, res, getDataSource()).authenticateUser();
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else if(tokens[1].equals("organizer")){
            switch (req.getMethod()) {
                case "POST":
                    if (tokens.length > 2) {
                        writeError(res, ErrorCode.WRONG_REST_FORMAT);
                    } else {
                        new AuthenticationRest(req, res, getDataSource()).authenticateOrganizer();
                    }
                    break;
                default:
                    writeError(res, ErrorCode.METHOD_NOT_ALLOWED);
            }
        }else{
            return false;
        }
        return true;
    }


}
