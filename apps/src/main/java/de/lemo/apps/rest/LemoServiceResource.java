package de.lemo.apps.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Question;

@Path("/lemoservice")
public class LemoServiceResource
{

	@Inject
	private Logger logger;
	
	public LemoServiceResource() {
		
	}

	@GET
	@Produces("text/html")
	public String getAllDomains()
	{
		logger.info("Printing Hello World ...");
		return "Hello LeMo User!";
	}

	@POST
	@Consumes("application/json")
	public Response post(Question domainObject)
	{
		return Response.ok().build();
	}

	@GET
	@Path("{name}")
	@Produces("text/html")
	public String getDomainObject(@PathParam("name") String name)
	{
		
		if (name == null)
		{
			throw new WebApplicationException(Response.Status.valueOf("Bitte Namen angeben."));
		}
		return "Hallo "+name+", willkommen bei LeMo!";
	}

}