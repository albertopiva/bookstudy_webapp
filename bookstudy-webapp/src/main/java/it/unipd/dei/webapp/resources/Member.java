package it.unipd.dei.webapp.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

/**
 * Represents the data about a Member.
 * A Member is a special type of a User, in fact this class extends {@code User} class.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class Member extends User{

    /**
     * The city where the Member live.
     */
    final private String city;

    /**
     * The region where the Member live.
     */
    final private String region;

    /**
     * The city where the Member was born.
     */
    final private String hometown;

    /**
     * The region where the Member was born.
     */
    final private String bornRegion;

    /**
     * The birthday of the Member.
     */
    final private Date birthday;

    /**
     * The constructor of the member.
     *
     * @param phoneNumber the phoneNumber (identifier) of the member.
     * @param password the password of the member.
     * @param salt the salt (to encrypt the password) of the member.
     * @param name the name of the member.
     * @param surname the surname of the member.
     * @param role the role of the member.
     * @param city the city where the member live.
     * @param region the region where the member live.
     * @param hometown the city where the member was born.
     * @param bornRegion the region where the member was born.
     * @param birthday the birthday of the member.
     */
    public Member(String phoneNumber, String password, String salt, String name, String surname, String role,
                  String city, String region, String hometown, String bornRegion, Date birthday) {

        super(phoneNumber, password, salt, name, surname, role);
        this.city = city;
        this.region = region;
        this.hometown = hometown;
        this.bornRegion = bornRegion;
        this.birthday = birthday;
    }

    /**
     * Get the city where the member lives.
     *
     * @return the city where the member lives.
     */
    public String getCity() { return city; }

    /**
     * Get the region where the member lives.
     *
     * @return the region where the member lives.
     */
    public String getRegion() { return region; }

    /**
     * Get the city where the member was born.
     *
     * @return the city where the member was born.
     */
    public String getHometown() { return hometown; }


    /**
     * Get the region where the member was born.
     *
     * @return the region where the member was born.
     */
    public String getBornRegion() { return  bornRegion; }

    /**
     * Get the birthday of the member.
     *
     * @return the birthday of the member.
     */
    public Date getBrithday() { return birthday; }

    /**
     * Write a {@code Member} object in a JSON format.
     * @param out the stream to which the JSON representation of the {@code Member} has to be written.
     *
     * @throws IOException the exception thrown if something goes wrong.
     */
    @Override
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("Member");
        jg.writeStartObject();
        jg.writeStringField("phoneNumber", getPhoneNumber());
        jg.writeStringField("password", getPassword());
        jg.writeStringField("salt",getSalt());
        jg.writeStringField("name", getName());
        jg.writeStringField("surname", getSurname());
        jg.writeStringField("role", getRole());
        jg.writeStringField("city", getCity());
        jg.writeStringField("region", getRegion());
        jg.writeStringField("hometown", getHometown());
        jg.writeStringField("bornRegion", getBornRegion());
        jg.writeStringField("birthday", String.valueOf(getBrithday()));
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Read a {@code Member} object from an InputStream.
     *
     * @param in the {@code InputStream} where it's suppose to be a {@code Member} object.
     * @return the {@code Member} object found in the {@code InputStream}.
     * @throws IOException the exception thrown if something goes wrong.
     */
    public static Member fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jPhoneNumber = null;
        String jPassword = null;
        String jSalt = null;
        String jName = null;
        String jSurname = null;
        String jRole = null;
        String jCity = null;
        String jRegion = null;
        String jHometown = null;
        String jBornRegion = null;
        Date jBirth = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "Member".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no Member object found.");
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
                    case "salt":
                        jp.nextToken();
                        jSalt = jp.getText();
                    case "name":
                        jp.nextToken();
                        jName = jp.getText();
                    case "surname":
                        jp.nextToken();
                        jSurname = jp.getText();
                    case "role":
                        jp.nextToken();
                        jRole = jp.getText();
                    case "city":
                        jp.nextToken();
                        jCity = jp.getText();
                        break;
                    case "region":
                        jp.nextToken();
                        jRegion = jp.getText();
                        break;
                    case "hometown":
                        jp.nextToken();
                        jHometown = jp.getText();
                        break;
                    case "bornRegion":
                        jp.nextToken();
                        jBornRegion = jp.getText();
                        break;
                    case "birthday":
                        jp.nextToken();
                        jBirth = Date.valueOf(jp.getValueAsString());
                }
            }
        }

        return new Member(jPhoneNumber,jPassword, jSalt, jName, jSurname, jRole, jCity, jRegion, jHometown, jBornRegion,jBirth);
    }
}
