package it.unipd.dei.webapp.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;

/**
 * Abstract filter class.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class AbstractFilter extends HttpFilter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}
