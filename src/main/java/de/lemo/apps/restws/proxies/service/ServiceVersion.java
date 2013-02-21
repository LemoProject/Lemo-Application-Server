/**
 * File ServiceVersion.java
 * Date Feb 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * @author Andreas Pursian
 *
 */
public interface ServiceVersion {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	String getDMSVersion();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	String getDBVersion();

}
