
package de.lemo.apps.restws.proxies.service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import de.lemo.apps.restws.entities.ResultListLongObject;



public interface ServiceLoginAuthentification {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("authentification")
	public ResultListLongObject authentificateUser(String login);

}
