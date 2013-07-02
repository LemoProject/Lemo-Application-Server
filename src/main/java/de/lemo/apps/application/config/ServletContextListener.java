/**
 * File ./src/main/java/de/lemo/apps/application/config/ServletContextListener.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
 * File ServerConfiguration.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.application.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;

/*
 * TODO this is very much based on the DMS implementation using the standard servlet api and is probably not done in the
 * 'tapestry way'.
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