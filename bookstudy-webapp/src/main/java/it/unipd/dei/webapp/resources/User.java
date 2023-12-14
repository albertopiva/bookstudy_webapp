package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Represents the data about an User.
 * 
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class User extends Resource{

    /**
     * The phoneNumber (identifier) of the User.
     */
    final private String phoneNumber;

    /**
     * The password of the User.
     */
    final private String password;

    /**
     * The salt (to encrypt the password) of the User.
     */
    final private String salt;

    /**
     * The name of the User.
     */
    final private String name;

    /**
     * The surname of the User.
     */
    final private String surname;

    /**
     * The role of the User.
     */
    final private String role;

    /**
     * The constructor for the User.
     *
     * @param phoneNumber the phoneNumber (identifier) of the User.
     * @param password the password of the User.
     * @param salt the salt (to encrypt the password) of the User.
     * @param name the name of the User.
     * @param surname the surname of the User.
     * @param role the role of the User.
     */
    public User(String phoneNumber, String password, String salt, String name, String surname, String role) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    /**
     * Get the phoneNumber of the User.
     *
     * @return the phoneNumber of the User.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the password of the User.
     *
     * @return the password of the User.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the salt of the User.
     *
     * @return the salt of the User.
     */
    public String getSalt() { return salt; }

    /**
     * Get the name of the User.
     *
     * @return the name of the User.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the surname of the User.
     *
     * @return the surname of the User.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Get the role of the User.
     *
     * @return the role of the User.
     */
    public String getRole() { return role; }

    /**
     * Get a string representation of the user.
     *
     * @return the string representation of the user.
     */
    @Override
    public String toString(){
        return "PhoneNumber: "+phoneNumber+", name: "+name+", surname: "+surname+", role: "+role;
    }

    /**
     * Write a {@code User} object in a JSON format.
     * @param out  the stream to which the JSON representation of the {@code User} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("user");
        jg.writeStartObject();
        jg.writeStringField("phoneNumber", getPhoneNumber());
        jg.writeStringField("name", getName());
        jg.writeStringField("surname", getSurname());
        jg.writeStringField("role", getRole());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code User} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code User} object.
     * @return the {@code User} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static User fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jPhone = null;
        String jSurname = null;
        String jName = null;
        String jPassword = null;
        String jSalt = null;
        String jRole = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "user".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no User object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                switch (jp.getCurrentName()) {
                    case "phoneNumber":
                        jp.nextToken();
                        jPhone = jp.getText();
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
                    case "role":
                        jp.nextToken();
                        jRole = jp.getText();
                        break;
                }
            }
        }

        return new User(jPhone, jPassword, jSalt, jName, jSurname, jRole);
    }
}
