package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.LibrarySeatDAO;
import it.unipd.dei.webapp.dao.SeatReservationDAO;
import it.unipd.dei.webapp.dao.TimeSlotDAO;
import it.unipd.dei.webapp.dao.UserDAO;
import it.unipd.dei.webapp.resources.*;
import it.unipd.dei.webapp.utils.ErrorCode;
import it.unipd.dei.webapp.utils.UtilMethods;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage the REST call about the seat Reservations.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class SeatReservationRest extends RestResource{

    /**
     * Creates a new Seat Reservation REST resource.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param ds the dataSource for the connection.
     */
    public SeatReservationRest(HttpServletRequest req, HttpServletResponse res, DataSource ds) {
        super(req, res, ds);
    }

    public void getFreeSeat() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("seatReserv"));
        String spl[] = op.split("/");
        //spl[0] is seatReserv
        //spl[1] is free
        //spl[2] is the date
        //spl[3] is the slot
        Date date = Date.valueOf(spl[2]);
        String slot = spl[3];
        if (slot == null || date == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("ERROR: value is null. Date: " + date + " slot: " + slot);
        } else {
            try {
                int slotID = Integer.parseInt(slot);
                ArrayList<LibrarySeat> freeSeat = SeatReservationDAO.getListFreeSeatByGivenSlotAndData(ds.getConnection(), date, slotID);
                if (freeSeat == null) {
                    Message m = new Message("There are no seat available.");
                    res.setStatus(HttpServletResponse.SC_OK);
                    m.toJSON(res.getOutputStream());
                }else {
                    ResourceList<LibrarySeat> lists = new ResourceList<>(freeSeat);
                    res.setStatus(HttpServletResponse.SC_OK);
                    lists.toJSON(res.getOutputStream());
                }
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }

    public void getSlot() throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("seatReserv"));
        String spl[] = op.split("/");
        //spl[0] is seatReserv
        //spl[1] is free
        //spl[2] is the date
        Date date = Date.valueOf(spl[2]);
        if (date == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("ERROR: value is null. Date: " + date);
        } else {
            try {
                //I will check if I have to insert the slots for this day
                List<TimeSlot> slots = TimeSlotDAO.getDaySlots(ds.getConnection(),date);
                boolean createSlot = slots==null;
                if(createSlot){
                    //create the slots for this day
                    int tmp = TimeSlotDAO.createDaySlots(ds.getConnection(),date);
                    if(tmp==1){
                        createSlot=false;
                        logger.debug("Slots created for day: "+date);
                    }
                }
                List<TimeSlot> tmp = TimeSlotDAO.getDaySlots(ds.getConnection(),date);
                List<TimeSlot> timeSlots = new ArrayList<>();
                for(TimeSlot ts: tmp) {
                    if (ts.getHour_range().equals("evening")) {
                        if (req.getSession().getAttribute("role").equals("association_admin") ||
                                req.getSession().getAttribute("role").equals("association_member"))
                            timeSlots.add(ts);
                    } else {
                        timeSlots.add(ts);
                    }
                }
                ResourceList<TimeSlot> lists = new ResourceList<>(timeSlots);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            } catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }

    public void getAllReservationByUser(String userParam) throws IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("seatReserv"));
        String spl[] = op.split("/");
        //spl[0] is "seatReserv"
        //spl[1] is "view"
        //spl[2] is "user"
        //spl[3] is the userID
        //String userParam = req.get;
        if (userParam == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(res,ErrorCode.PHONE_NUMBER_MISSING);
        }
        try {
            //Check that this user exists
            User user = UserDAO.getUserByID(ds.getConnection(), userParam);
            ArrayList<SeatReservation> allReserv = SeatReservationDAO.getAllReservationByUser(ds.getConnection(), userParam);

            if (user == null) {
                Message m = new Message("Some problem with the user.");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }else {
                ResourceList<SeatReservation> lists = new ResourceList<>(allReserv);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }
        } catch (SQLException e) {
            logger.error("stacktrace:", e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
        catch(ParseException e){
            writeError(res,ErrorCode.INTERNAL_ERROR);
        }
    }

    public void insertReservation() throws IOException {
        //check before insert the reservation
        //if the user had already booked a seat for that slotID he cannot booked another reservation
        try {
            SeatReservation tmp = SeatReservation.fromJSON(req.getInputStream());
            logger.debug(tmp.toString());
            boolean alreadyBooked = false;
            ArrayList<SeatReservation> previousRes = SeatReservationDAO.getAllReservationByUser(ds.getConnection(),req.getSession().getAttribute("phoneNumber").toString());
            for(SeatReservation sr : previousRes){
                if(sr.getSlot().getSlotID()==tmp.getSlot().getSlotID()){
                    //Already booked a seat for this slot.
                    alreadyBooked=true;
                    res.setStatus(HttpServletResponse.SC_CONFLICT);
                    writeError(res,ErrorCode.SEAT_RESERVATION_ALREADY_BOOKED);
                }
            }
            if(!alreadyBooked) {
                String alphaCode = UtilMethods.computeAlphanumericCode(10);
                String userID = req.getSession().getAttribute("phoneNumber").toString();
                TimeSlot completeSlot = TimeSlotDAO.getTimeSlot(ds.getConnection(),tmp.getSlot().getSlotID());
                LibrarySeat completeSeat = LibrarySeatDAO.getLibrarySeat(ds.getConnection(),tmp.getSeat().getSeatID());
                SeatReservation reservation = new SeatReservation(alphaCode, tmp.getDate(), null, null, userID, completeSlot, completeSeat);
                logger.debug(reservation);
                //Date date1 = Date.valueOf(new SimpleDateFormat("dd/MM/yyyy").parse(dateString).toString());
                int insertDone = -1;
                //long slotID = TimeSlotDAO.getIDFromDayAndSlot(ds.getConnection(),reservation.getDate(),uslot);
                insertDone = SeatReservationDAO.insertNewUserReservation(ds.getConnection(), reservation);

                if (insertDone == -1) {
                    //Some problem, DAO does not work
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
                } else if (insertDone == 0) {
                    //Insertion tried but failed
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                } else {
                    //Successful insertion
                    res.setStatus(HttpServletResponse.SC_OK);
                    reservation.toJSON(res.getOutputStream());
                    //res.getWriter().write("Insert done. You have new reservation for the day " + date.toString());
                }
            }
        } catch (SQLException e) {
            logger.error("stacktrace:", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeError(res, ErrorCode.INTERNAL_ERROR);

        }catch (ParseException e) {
            logger.error("stacktrace:", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeError(res, ErrorCode.INTERNAL_ERROR);

        } catch(NumberFormatException num){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(res, ErrorCode.WRONG_FORMAT);
        }
    }

    /**
     * Update a reservation
     * @param exit if the value of exit is true it means that I have to update exit time
     *             if the value of exit is false it means that I have to update entry time
     * @throws IOException
     */
    public void updateReservation(String alphaCode,boolean exit) throws IOException {
        if(alphaCode == null)
        {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(res,ErrorCode.ALPHANUMERIC_CODE_MISSING);
        }
        else {
            try {
                LocalTime time = LocalTime.now();
                String output = null;
                SeatReservation reservation = SeatReservationDAO.getReservationByCode(ds.getConnection(),alphaCode);
                int insertDone = -1;
                if(!exit){
                    if(reservation.getEntryTime() == null) {
                        insertDone = SeatReservationDAO.setEntryTime(ds.getConnection(), alphaCode);
                        output = "Entry time updated to " + time.toString();
                    }else{
                        //entry time already set
                        res.setStatus(HttpServletResponse.SC_CONFLICT);
                        writeError(res,ErrorCode.ENTRY_TIME_ALREADY_SET);
                    }
                }
                else {
                    if(reservation.getExitTime() == null) {
                        insertDone = SeatReservationDAO.setExitTime(ds.getConnection(), alphaCode);
                        output = "Exit time updated to " + time.toString();
                    }else{
                        //exit time already set
                        res.setStatus(HttpServletResponse.SC_CONFLICT);
                        writeError(res,ErrorCode.EXIT_TIME_ALREADY_SET);
                    }
                }
                if (insertDone == -1) {
                    //Some problem, DAO does not work
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
                }
                else if (insertDone == 0) {
                    //Insertion tried but failed
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
                else {
                    //Successful insertion
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.getWriter().write(output);
                }
            }
            catch (SQLException e) {
                logger.error("stacktrace:", e);
                writeError(res, ErrorCode.INTERNAL_ERROR);
            }
        }
    }
}
