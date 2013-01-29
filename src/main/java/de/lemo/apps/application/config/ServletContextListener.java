/**
 * File ./de/lemo/apps/application/config/ServletContextListener.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.application.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;

/**
 * TODO this is very much based on the DMS implementation using the standard servlet api and is probably not done in the
 * tapestry way
 * 
 * @author Leonard Kappe
 */
public class ServletContextListener implements javax.servlet.ServletContextListener {

	private Logger logger;

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		final ServletContext servletContext = sce.getServletContext();
		final ServerConfiguration config = ServerConfiguration.getInstance();

		this.logger = Logger.getLogger(this.getClass());
		this.logger.info("Context initialized");
		this.logger.info("ServerInfo:  " + servletContext.getServerInfo());
		this.logger.info("ContextPath: " + servletContext.getContextPath());

		config.loadConfig(servletContext.getContextPath());
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		this.logger.info("Context destroyed");
	}

}