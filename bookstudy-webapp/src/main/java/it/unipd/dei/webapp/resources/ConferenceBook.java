package it.unipd.dei.webapp.resources;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;


/**
 * Represents the data about a ConferenceBook.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */

public class ConferenceBook extends Resource {

    /**
     * The userID (phoneNumber) of the user that booked for the conference.
     */
    private final String userID;

    /**
     * The conferenceID (alphanumericCode) of the conference.
     */
    private final String conferenceID;

    /**
     * The constructor for the ConferenceBook.
     *
     * @param userID the userID (phoneNumber) of the user that booked for the conference.
     * @param conferenceID the conferenceID (alphanumericCode) of the conference.
     */
    public ConferenceBook(String userID, String conferenceID) {
        this.userID = userID;
        this.conferenceID = conferenceID;
    }

    /**
     * Get the userID of the ConferenceBook.
     *
     * @return the userID of the ConferenceBook.
     */
    public String getUserID() { return userID; }

    /**
     * Get the conferenceID of the ConferenceBook.
     *
     * @return the conferenceID of the ConferenceBook.
     */
    public String getConferenceID() { return conferenceID; }

    /**
     * Write a {@code ConferenceBook} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code ConferenceBook} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("ConferenceBook");
        jg.writeStartObject();
        jg.writeStringField("userID", userID);
        jg.writeStringField("conferenceID", conferenceID);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code ConferenceBook} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code ConferenceBook} object.
     * @return the {@code ConferenceBook} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static ConferenceBook fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jUserID = null;
        String jConferenceID = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME ||
                "ConferenceBook".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no ConferenceBook object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "userID":
                        jp.nextToken();
                        jUserID = jp.getText();
                        break;
                    case "conferenceID":
                        jp.nextToken();
                        jConferenceID = jp.getText();
                        break;
                }
            }
        }
        return new ConferenceBook(jUserID, jConferenceID);
    }


}
