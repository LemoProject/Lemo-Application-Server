package de.lemo.apps.application.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

/**
 * TODO this is very much based on the DMS implementation using the standard servlet api and is probably not done in the
 * tapestry way
 * 
 * @author Leonard Kappe
 * 
 */
public class ServletContextListener implements javax.servlet.ServletContextListener {

    private Logger logger;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ServerConfiguration config = ServerConfiguration.getInstance();

        logger = Logger.getLogger(getClass());
        logger.info("Context initialized");
        logger.info("ServerInfo:  " + servletContext.getServerInfo());
        logger.info("ContextPath: " + servletContext.getContextPath());

        config.loadConfig(servletContext.getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Context destroyed");
    }

}