package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import de.lemo.apps.restws.entities.SCTime;




public interface ServiceStartTime {
	
		
	@GET
	@Produces("application/json")
	public SCTime startTimeJson();
	
	@GET @Produces("text/html")
	public String startTimeHtml();

}
