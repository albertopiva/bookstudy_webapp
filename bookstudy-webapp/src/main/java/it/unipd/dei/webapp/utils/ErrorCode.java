package it.unipd.dei.webapp.utils;

import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Contains the enumeration of the error code needed in the servlets.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public enum ErrorCode {

    /**
     * Wrong format code of the request.
     */
    WRONG_FORMAT(-100, HttpServletResponse.SC_BAD_REQUEST,"Wrong format of the request."),

    /**
     * One or more input fields are empty.
     */
    EMPTY_INPUT_FIELDS(-102, HttpServletResponse.SC_BAD_REQUEST, "One or more input fields are empty."),

    /**
     * Submitted credentials are wrong.
     */
    WRONG_CREDENTIALS(-105, HttpServletResponse.SC_UNAUTHORIZED, "Submitted credentials are wrong"),

    /**
     * Unrecognized role.
     */
    UNRECOGNIZED_ROLE(-109, HttpServletResponse.SC_BAD_REQUEST, "Unrecognized role"),

    /**
     * Wrong rest request format.
     */
    WRONG_REST_FORMAT(-113, HttpServletResponse.SC_BAD_REQUEST, "Wrong rest request format."),

    /**
     * Conference not found.
     */
    CONFERENCE_NOT_FOUND(-116, HttpServletResponse.SC_NOT_FOUND, "Conference not found."),

    /**
     * User not found.
     */
    USER_NOT_FOUND(-117, HttpServletResponse.SC_NOT_FOUND, "User not found."),

    /**
     * The input json is in the wrong format.
     */
    BADLY_FORMATTED_JSON(-120,  HttpServletResponse.SC_BAD_REQUEST, "The input json is in the wrong format."),

    /**
     * The user is already booked for this conference.
     */
    CONFERENCE_ALREADY_BOOKED(-130,  HttpServletResponse.SC_CONFLICT, "You are already booked for this conference."),

    /**
     * The user has already booked a seat for this slotID.
     */
    SEAT_RESERVATION_ALREADY_BOOKED(-140,  HttpServletResponse.SC_CONFLICT, "You are already booked a seat for this slotID."),

    /**
     * The alphanumericCode is not present in the request.
     */
    ALPHANUMERIC_CODE_MISSING(-141,  HttpServletResponse.SC_NOT_FOUND, "The alphanumericCode is not present in the request."),

    /**
     * The phoneNumber of the user is not present in the request.
     */
    PHONE_NUMBER_MISSING(-142,  HttpServletResponse.SC_NOT_FOUND, "The phoneNumber of the user is not present in the request."),

    /**
     * The entry time is already set for this SeatReservation.
     */
    ENTRY_TIME_ALREADY_SET(-143,  HttpServletResponse.SC_CONFLICT, "The entry time is already set for this SeatReservation."),

    /**
     * The exit time is already set for this SeatReservation.
     */
    EXIT_TIME_ALREADY_SET(-144,  HttpServletResponse.SC_CONFLICT, "The exit time is already set for this SeatReservation."),

    /**
     * Operation unknown.
     */
    OPERATION_UNKNOWN(-200, HttpServletResponse.SC_BAD_REQUEST, "Operation unknown."),

    /**
     * Method of the request not allowed.
     */
    METHOD_NOT_ALLOWED(500, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "The method is not allowed"),

    /**
     * SQL Exception.
     */
    SQL_ERROR(-998, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Exception."),

    /**
     * Internal Error.
     */
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
;
    /**
     * My own code of the error.
     */
    private final int errorCode;

    /**
     * The HTTP code of the error.
     */
    private final int httpCode;

    /**
     * The error message.
     */
    private final String errorMessage;

    /**
     * The constructore of the ErrorCode.
     *
     * @param errorCode my own code of the error.
     * @param httpCode the HTTP code of the error.
     * @param errorMessage the error message.
     */
    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Get my own error code.
     *
     * @return my own error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get the HTTP error code.
     *
     * @return the HTTP error code.
     */
    public int getHTTPCode() {
        return httpCode;
    }


    /**
     * Get the message of the error.
     *
     * @return the message of the error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Create a JSON Object of the ErrorCode.
     *
     * @return JSONObject of the ErrorCode.
     */
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}