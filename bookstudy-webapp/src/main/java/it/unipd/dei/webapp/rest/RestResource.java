package it.unipd.dei.webapp.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.unipd.dei.webapp.utils.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;

/**
 * Super class of the REST resources. Represent the common object of the REST resource.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public abstract class RestResource {

    /**
     * The HTTP request
     */
    protected final HttpServletRequest req;

    /**
     * The HTTP response.
     */
    protected final HttpServletResponse res;

    /**
     * The connection to the database.
     */
    protected final DataSource ds;

    /**
     * The logger.
     */
    protected final Logger logger;
    /**
     * Creates a new REST resource.
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param ds the dataSource for the connection.
     */
    protected RestResource(final HttpServletRequest req, final HttpServletResponse res, DataSource ds) {
        this.req = req;
        this.res = res;
        this.ds = ds;
        this.logger = LogManager.getLogger(this.getClass());
    }

    /**
     * Write on the Output Streaming of the HTTP Response an {@code ErrorCode}.
     *
     * @param res the HTTP response.
     * @param ec the {@code ErrorCode} to write in the streaming output.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(ec.toJSON().toString());
    }
}