/**
	 * File ServiceLoginAuthentification.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.restws.proxies.service;



import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;



public interface ServiceLoginAuthentification {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListLongObject authentificateUser(
			@QueryParam(MetaParam.USER_NAME) String login);

}
