package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.EConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectors;

/**
 * Connector platform update state.
 * 
 * @see EConnectorManagerState
 * @author Leonard Kappe
 */
public interface ServiceConnectorManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SCConnectors connectorListJson();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String connectorListHtml();

	@GET
	@Path("/state")
	@Produces(MediaType.APPLICATION_JSON)
	public SCConnectorManagerState state();

	@POST
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean update(@PathParam("id") final Long connectorId);

}