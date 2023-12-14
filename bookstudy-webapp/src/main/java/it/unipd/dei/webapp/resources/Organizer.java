package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about an Organizer.
 *
 * @author BookStudy Group Group
 * @version 1.0
 * @since 1.0
 */
public class Organizer extends Resource{

    /**
     * The phoneNumber (identifier) of the Organizer.
     */
    final private String phoneNumber;

    /**
     * The password of the Organizer.
     */
    final private String password;

    /**
     * The salt (to encrypt the password) of the Organizer.
     */
    final private String salt;

    /**
     * The name of the Organizer.
     */
    final private String name;

    /**
     * The surname of the Organizer.
     */
    final private String surname;

    /**
     * The association of the Organizer.
     */
    final private String association;


    /**
     * The constructor for the Organizer.
     *
     * @param phoneNumber the phoneNumber (identifier) of the Organizer.
     * @param password the password of the Organizer.
     * @param salt the salt (to encrypt the password) of the Organizer.
     * @param name the name of the Organizer.
     * @param surname the surname of the Organizer.
     * @param association the association of the Organizer.
     */
    public Organizer(String phoneNumber, String password, String salt, String name, String surname, String association) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
        this.association = association;
    }

    /**
     * Get the phoneNumber of the Organizer.
     *
     * @return the phoneNumber of the Organizer.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the password of the Organizer.
     *
     * @return the password of the Organizer.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the salt of the Organizer.
     *
     * @return the salt of the Organizer.
     */
    public String getSalt() { return salt; }

    /**
     * Get the name of the Organizer.
     *
     * @return the name of the Organizer.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the surname of the Organizer.
     *
     * @return the surname of the Organizer.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Get the association of the Organizer.
     *
     * @return the association of the Organizer.
     */
    public String getAssociation() {
        return association;
    }

    /**
     * Get a string representation of the user.
     *
     * @return the string representation of the user.
     */
    @Override
    public String toString() {
        return "PhoneNumber: " + phoneNumber + ", name: " + name + ", surname: " + surname + ", association: " + association;
    }

    /**
     * Write a {@code Organizer} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code Organizer} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("organizer");
        jg.writeStartObject();
        jg.writeStringField("phoneNumber", getPhoneNumber());
        jg.writeStringField("password", getPassword());
        jg.writeStringField("salt", getSalt());
        jg.writeStringField("name", getName());
        jg.writeStringField("surname", getSurname());
        jg.writeStringField("association", getAssociation());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Organizer} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Organizer} object.
     * @return the {@code Organizer} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Organizer fromJSON(final InputStream in) throws IOException {

        // the fields save from JSON
        String jPhoneNumber = null;
        String jSurname = null;
        String jName = null;
        String jPassword = null;
        String jSalt = null;
        String jAssociation = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || ("organizer").equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Organizer object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "phoneNumber":
                        jp.nextToken();
                        jPhoneNumber = jp.getText();
                        break;
                    case "password":
                        jp.nextToken();
                        jPassword = jp.getText();
                        break;
                    case "salt":
                        jp.nextToken();
                        jSalt = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                        break;
                    case "surname":
                        jp.nextToken();
                        jSurname = jp.getText();
                        break;
                    case "association":
                        jp.nextToken();
                        jAssociation = jp.getText();
                        break;
                }
            }
        }

        return new Organizer(jPhoneNumber, jPassword, jSalt, jName, jSurname, jAssociation);
    }
}

