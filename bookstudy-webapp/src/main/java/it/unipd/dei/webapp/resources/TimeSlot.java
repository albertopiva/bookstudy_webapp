package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

/**
 * Represent the data about a time slot.
 * TimeSlot: period during which a seat may be occupied.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */

public class TimeSlot extends Resource {

    /**
     * The slotID (identifier) of the TimeSlot.
     */
    final private long slotID;

    /**
     * The hour_range of the TimeSlot (it will be morning,afternoon or evening).
     */
    final private String hour_range;

    /**
     * The date of the TimeSlot.
     */
    final private Date date;



    /**
     * The constructor of the TimeSlot.
     *
     * @param slotID the slotID (identifier) of the TimeSlot.
     * @param hour_range the hour_range of the TimeSlot (it will be morning,afternoon or evening).
     * @param date the date of the TimeSlot.
     */
    public TimeSlot(long slotID, String hour_range, Date date){
        this.slotID = slotID;
        this.hour_range = hour_range;
        this.date = date;
    }

    /**
     * Get the slotID of the TimeSlot.
     *
     * @return the slotID of the TimeSlot.
     */
    public long getSlotID() {
        return slotID;
    }

    /**
     * Get the hour_range of the TimeSlot.
     *
     * @return the hour_range of the TimeSlot.
     */
    public String getHour_range() {
        return hour_range;
    }

    /**
     * Get the date of the TimeSlot.
     *
     * @return the date of the TimeSlot.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Write a {@code TimeSlot} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code TimeSlot} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("TimeSlot");
        jg.writeStartObject();
        jg.writeStringField("slotID", String.valueOf(slotID));
        jg.writeStringField("hour_range", hour_range);
        jg.writeStringField("date", date.toString());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code TimeSlot} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code TimeSlot} object.
     * @return the {@code TimeSlot} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static TimeSlot fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jSlotID = -1;
        String jHourRange = null;
        Date jDate = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "TimeSlot".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no TimeSlot object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "slotID":
                        jp.nextToken();
                        jSlotID = Integer.parseInt(jp.getText());
                        break;
                    case "hour_range":
                        jp.nextToken();
                        jHourRange = jp.getText();
                        break;
                    case "date":
                        jp.nextToken();
                        jDate = Date.valueOf(jp.getValueAsString());
                        break;
                }
            }
        }
        return new TimeSlot(jSlotID,jHourRange,jDate);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "slotID=" + slotID +
                ", hour_range='" + hour_range + '\'' +
                ", date=" + date +
                '}';
    }
}