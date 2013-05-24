package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Produces("application/json")
public interface ServiceTaskManager {

	@GET
	@Path("{id}")
	Response taskResult(@PathParam("id") String taskId);

}
