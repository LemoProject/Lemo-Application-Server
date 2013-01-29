/**
 * File ./de/lemo/apps/restws/proxies/service/ServiceStartTime.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import de.lemo.apps.restws.entities.SCTime;

public interface ServiceStartTime {

	@GET
	@Produces("application/json")
	public SCTime startTimeJson();

	@GET
	@Produces("text/html")
	public String startTimeHtml();

}
