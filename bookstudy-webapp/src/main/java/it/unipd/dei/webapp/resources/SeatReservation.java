package it.unipd.dei.webapp.resources;

import java.sql.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;

/**
 * Represents the data about a SeatReservation.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class SeatReservation extends Resource{

    /**
     * The alphanumericCode (identifier) of the SeatReservation.
     */
    final private String alphanumericCode;

    /**
     * The date of the SeatReservation.
     */
    final private Date date;

    /**
     * The entry time (when the user enter in the library) of the SeatReservation.
     */
    final private Time entryTime;

    /**
     * The exit time (when the user exit from the library) of the SeatReservation.
     */
    final private Time exitTime;

    /**
     * The userID (alphanumericCode) of the user that booked his seat in the library.
     */
    private final String userID;

    /**
     * The slotID of the SeatReservation.
     */
    private final TimeSlot slot;

    /**
     * The seatID of the SeatReservation.
     */
    private final LibrarySeat seat;


    /**
     * The constructor of the SeatReservation.
     *
     * @param alphanumericCode the alphanumericCode (identifier) of the SeatReservation.
     * @param date the date of the SeatReservation.
     * @param entryTime the entry time (when the user enter in the library) of the SeatReservation.
     * @param exitTime the exit time (when the user exit from the library) of the SeatReservation.
     * @param userID the userID (alphanumericCode) of the user that booked his seat in the library.
     * @param slot the slotID of the SeatReservation.
     * @param seat the seatID of the SeatReservation.
     */
    public SeatReservation(String alphanumericCode, Date date, Time entryTime, Time exitTime, String userID, TimeSlot slot, LibrarySeat seat){
        this.alphanumericCode = alphanumericCode;
        this.date = date;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.userID = userID;
        this.slot = slot;
        this.seat = seat;
    }

    /**
     * Get the alphanumericCode of the SeatReservation.
     *
     * @return the alphanumericCode of the SeatReservation.
     */
    public String getAlphanumericCode() {
        return alphanumericCode;
    }

    /**
     * Get the date of the SeatReservation.
     *
     * @return the date of the SeatReservation.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the entry time of the SeatReservation.
     *
     * @return the entry time of the SeatReservation.
     */
    public Time getEntryTime() {
        return entryTime;
    }

    /**
     * Get the exit time of the SeatReservation.
     *
     * @return the exit time of the SeatReservation.
     */
    public Time getExitTime() {
        return exitTime;
    }

    /**
     * Get the userID of the SeatReservation.
     *
     * @return the userID of the SeatReservation.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Get the slotID of the SeatReservation.
     *
     * @return the slotID of the SeatReservation.
     */
    public TimeSlot getSlot() {
        return slot;
    }

    /**
     * Get the seatID of the SeatReservation.
     *
     * @return the seatID of the SeatReservation.
     */
    public LibrarySeat getSeat() {
        return seat;
    }


    /**
     * Write a {@code SeatReservation} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code SeatReservation} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("SeatReservation");
        jg.writeStartObject();
        jg.writeStringField("alphanumericCode", alphanumericCode);
        //check if it is correct or if a proper method exists
        jg.writeStringField("date", String.valueOf(date));
        //check if it is correct or if a proper method exists
        jg.writeStringField("entryTime", String.valueOf(entryTime));
        //check if it is correct or if a proper method exists
        jg.writeStringField("exitTime", String.valueOf(exitTime));
        //write the slot
        jg.writeFieldName("TimeSlot");
        jg.writeStartObject();
        jg.writeStringField("slotID",String.valueOf(slot.getSlotID()));
        jg.writeStringField("hour_range",slot.getHour_range());
        jg.writeStringField("date",String.valueOf(slot.getDate()));
        jg.writeEndObject();
        //write the seat
        jg.writeFieldName("LibrarySeat");
        jg.writeStartObject();
        jg.writeStringField("seatID",String.valueOf(seat.getSeatID()));
        jg.writeStringField("room",seat.getRoom());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code SeatReservation} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code SeatReservation} object.
     * @return the {@code SeatReservation} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static SeatReservation fromJSON(final InputStream in) throws IOException {

        Logger logger = LogManager.getLogger(SeatReservation.class);
        // the fields read from JSON
        String jAlphanumericCode = null;
        Date jdate = null;
        Time jentryTime = null;
        Time jexitTime = null ;
        TimeSlot jSlot = null;
        LibrarySeat jSeat = null;
        String jUser = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "SeatReservation".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no SeatReservation object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "alphanumericCode":
                        jp.nextToken();
                        jAlphanumericCode = jp.getText();
                        logger.debug("jAlphanumericCode: "+jAlphanumericCode);
                        break;
                    case "date":
                        jp.nextToken();
                        jdate = Date.valueOf(jp.getValueAsString());
                        logger.debug("jDate: "+jdate);
                        //jdate = LocalDate.parse(jDate);
                        break;
                    case "entryTime":
                        jp.nextToken();
                        jentryTime = Time.valueOf(jp.getText());
                        logger.debug("jEntryTime: "+jentryTime);
                        //jentryTime = Date.parse(jEntryTime);
                        break;
                    case "exitTime":
                        jp.nextToken();
                        jexitTime = Time.valueOf(jp.getText());
                        logger.debug("jExitTime: "+jexitTime);
                        //jentryTime = Date.parse(jEntryTime);
                        break;
                    case "TimeSlot":
                        jp.nextToken();
                        long slotID = -1;
                        logger.debug("TIMESLOT: ");
                        while (jp.nextToken() != JsonToken.END_OBJECT) {
                            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                                switch (jp.getCurrentName()) {
                                    case "slotID":
                                        jp.nextToken();
                                        slotID = Long.parseLong(jp.getText());
                                        logger.debug("jSlotID: "+slotID);
                                        //jentryTime = Date.parse(jEntryTime);
                                        break;
                                }
                            }
                        }
                        jSlot = new TimeSlot(slotID,null,null);
                        //jexitTime = LocalTime.parse(jExitTime);
                        break;
                    case "LibrarySeat":
                        jp.nextToken();
                        long seatID = -1;
                        logger.debug("Libraryseat: ");
                        while (jp.nextToken() != JsonToken.END_OBJECT) {
                            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                                switch (jp.getCurrentName()) {
                                    case "seatID":
                                        jp.nextToken();
                                        seatID = Long.parseLong(jp.getText());
                                        logger.debug("SeatID: "+seatID);
                                        //jentryTime = Date.parse(jEntryTime);
                                        break;
                                }
                            }
                        }
                        jSeat = new LibrarySeat(seatID,null);
                        //jexitTime = LocalTime.parse(jExitTime);
                        break;
                    case "userID":
                        jp.nextToken();
                        jUser = jp.getText();
                        logger.debug("userID: "+jUser);
                        //jexitTime = LocalTime.parse(jExitTime);
                        break;
                }
            }
        }
        return new SeatReservation(jAlphanumericCode, jdate, jentryTime, jexitTime,jUser,jSlot,jSeat);
    }

    @Override
    public String toString() {
        return "SeatReservation{" +
                "alphanumericCode='" + alphanumericCode + '\'' +
                ", date=" + date +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", userID='" + userID + '\'' +
                ", slot=" + slot.toString() +
                ", seat=" + seat.toString() +
                '}';
    }
}
