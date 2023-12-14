package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about a ConferenceRoom.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class ConferenceRoom extends Resource {

    /**
     * The ID (identifier) of the ConferenceRoom.
     */
    final private String ID;

    /**
     * The name of the ConferenceRoom.
     */
    final private String name;

    /**
     * The constructor of the conference room.
     *
     * @param ID the ID of the ConferenceRoom.
     * @param name the name of the ConferenceRoom.
     */
    public ConferenceRoom(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    /**
     * Get the roomID of the conference.
     *
     * @return the roomID of the conference.
     */
    public String getRoomID() {
        return ID;
    }

    /**
     * Get the name of the conference.
     *
     * @return the name of the conference.
     */

    public String getRoomName() {
        return name;
    }

    /**
     * Write a {@code ConferenceRoom} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code ConferenceRoom} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("conferenceRoom");
        jg.writeStartObject();
        jg.writeStringField("ID", ID);
        jg.writeStringField("name", name);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code ConferenceRoom} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code ConferenceRoom} object.
     * @return the {@code ConferenceRoom} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static ConferenceRoom fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jID = null;
        String jName = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || ("conferenceRoom").equals(jp.getCurrentName()) == false) {

            // there are no more conference room
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no conferenceRoom object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "ID":
                        jp.nextToken();
                        jID = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                }
            }
        }

        return new ConferenceRoom(jID, jName);
    }

}
