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

/**
 * Represents the data about a Conference.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class Conference extends Resource{

    /**
     * The alphanumericCode (identifier) of the conference.
     */
    private final String alphanumericCode;

    /**
     * The date of the conference.
     */
    private final Date date;

    /**
     * The title of the conference.
     */
    private final String title;

    /**
     * The description of the conference.
     */
    private final String description;

    /**
     * The identifier of the organizer of the conference.
     */
    private final String organizerID;

    /**
     * The identifier of the room in which the conference will be booked.
     */
    private final String conferenceRoom;

    /**
     * The constructor of the conference.
     *
     * @param alphanumericCode the identifier of the conference.
     * @param date the date of the conference.
     * @param title the title of the conference.
     * @param description the description of the conference.
     * @param organizerID the identifier of the organizer of the conference.
     * @param conferenceRoom the identifier of the room in which the conference will be booked.
     */
    public Conference(String alphanumericCode, Date date, String title, String description,
                      String organizerID, String conferenceRoom){
        this.alphanumericCode = alphanumericCode;
        this.date = date;
        this.title = title;
        this.description = description;
        this.organizerID = organizerID;
        this.conferenceRoom = conferenceRoom;
    }

    /**
     * Get the alphanumericCode of the conference.
     *
     * @return the alphanumericCode of the conference.
     */
    public String getAlphanumericCode() {
        return alphanumericCode;
    }

    /**
     * Get the date of the conference.
     *
     * @return the date of the conference.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the title of the conference.
     *
     * @return the title of the conference.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the description of the conference.
     *
     * @return the description of the conference.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the organizerID of the conference.
     *
     * @return the organizerID of the conference.
     */
    public String getOrganizerID() { return organizerID;}

    /**
     * Get the conferenceRoomID of the conference.
     *
     * @return the conferenceRoomID of the conference.
     */
    public String getConferenceRoom() { return conferenceRoom;}

    /**
     * Write a {@code Conference} object in a JSON format.
     * @param out  the stream to which the JSON representation of the {@code Conference} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("conference");
        jg.writeStartObject();
        jg.writeStringField("alphanumericCode", alphanumericCode);
        jg.writeStringField("date", String.valueOf(date));
        jg.writeStringField("title", String.valueOf(title));
        jg.writeStringField("description", String.valueOf(description));
        jg.writeStringField("organizerID",String.valueOf(organizerID));
        jg.writeStringField("conferenceRoomID",String.valueOf(conferenceRoom));
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Conference} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Conference} object.
     * @return the {@code Conference} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Conference fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jAlphanumericCode = null;
        Date jDate = null;
        String jTitle = null;
        String jDescription = null ;
        String jOrganizerdID = null;
        String jConferenceRoomID = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        Logger logger = LogManager.getLogger(Conference.class);
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "conference".equals(jp.getCurrentName()) == false) {
            logger.debug(jp.getCurrentToken() +" "+jp.getCurrentName());
            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Conference object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "alphanumericCode":
                        jp.nextToken();
                        jAlphanumericCode = jp.getText();
                        break;
                    case "date":
                        jp.nextToken();
                        jDate = Date.valueOf(jp.getValueAsString());
                        break;
                    case "title":
                        jp.nextToken();
                        jTitle = jp.getText();
                        break;
                    case "description":
                        jp.nextToken();
                        jDescription = jp.getText();
                        break;
                    case "organizerID":
                        jp.nextToken();
                        jOrganizerdID = jp.getText();
                        break;
                    case "conferenceRoomID":
                        jp.nextToken();
                        jConferenceRoomID = jp.getText();
                        break;
                }
            }
        }
        return new Conference(jAlphanumericCode, jDate, jTitle, jDescription, jOrganizerdID, jConferenceRoomID);
    }

    /**
     * Get a string representation of the conference.
     *
     * @return the string representation of the conference.
     */
    @Override
    public String toString(){
        return alphanumericCode+", "+date.toString()+", "+title+", "+description+", "+organizerID+", "+ conferenceRoom;
    }

    /**
     * Check if some fields are empty.
     *
     * @return true if some fields are empty, false otherwise.
     */
    public boolean checkEmpty(){
        boolean em = false;
        if(alphanumericCode==null || alphanumericCode.equals(""))
            em=true;
        if(title==null || title.equals(""))
            em=true;
        if(description==null || description.equals(""))
            em=true;
        if(organizerID==null || organizerID.equals(""))
            em=true;
        if(conferenceRoom ==null || conferenceRoom.equals(""))
            em=true;
        return em;
    }
}
