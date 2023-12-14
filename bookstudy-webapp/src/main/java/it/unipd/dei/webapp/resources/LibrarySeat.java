package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about a LibrarySeat.
 * The LibrarySeat is a space of a table in the library where a member or a normal person can study or read books.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */

public class LibrarySeat extends Resource {

    /**
     * The seatID (identifier) of the LibrarySeat.
     */
    final private long seatID;

    /**
     * The room of the library where is the seat.
     */
    final private String room;

    /**
     * The constructor of the LibrarySeat.
     *
     * @param seatID the seatID of the LibrarySeat.
     * @param room the room of the library where is the seat.
     */
    public LibrarySeat(long seatID, String room){
        this.seatID = seatID;
        this.room = room;
    }

    /**
     * Get the seatID of the LibrarySeat.
     *
     * @return the seatID of the LibrarySeat.
     */
    public long getSeatID() {return seatID;};

    /**
     * Get the roomID of the LibrarySeat.
     *
     * @return the roomID of the LibrarySeat.
     */
    public String getRoom() {return room;};

    /**
     * Write a {@code LibrarySeat} object in a JSON format.
     * @param out  the stream to which the JSON representation of the {@code LibrarySeat} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("LibrarySeat");
        jg.writeStartObject();
        jg.writeStringField("seatID", String.valueOf(seatID));
        jg.writeStringField("Room", room);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code LibrarySeat} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code LibrarySeat} object.
     * @return the {@code LibrarySeat} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static LibrarySeat fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        long jSeatID=-1;
        String jRoom = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "LibrarySeat".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no LibrarySeat object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "seatID":
                        jp.nextToken();
                        jSeatID = jp.getIntValue(); //check if exception
                        break;
                    case "room":
                        jp.nextToken();
                        jRoom = jp.getText();
                        break;
                }
            }
        }
        return new LibrarySeat(jSeatID,jRoom);
    }

    @Override
    public String toString() {
        return "LibrarySeat{" +
                "seatID=" + seatID +
                ", room='" + room + '\'' +
                '}';
    }
}