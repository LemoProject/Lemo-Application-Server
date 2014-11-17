/**
 * File ./src/main/java/de/lemo/apps/rest/LemoServiceResource.java
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

package de.lemo.apps.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import de.lemo.apps.entities.Question;

@Path("/lemoservice")
public class LemoServiceResource {

	@Inject
	private Logger logger;

	public LemoServiceResource() {

	}

	@GET
	@Produces("text/html")
	public String getAllDomains() {
		return "Hello LeMo User!";
	}

	@POST
	@Consumes("application/json")
	public Response post(final Question domainObject) {
		return Response.ok().build();
	}

	@GET
	@Path("{name}")
	@Produces("application/json")
	public String getDomainObject(@PathParam("name") final String name) throws JsonGenerationException, JsonMappingException,
			IOException {

		if (name == null) {
			throw new WebApplicationException(Response.Status.valueOf("Bitte Namen angeben."));
		}
		final ObjectMapper mapper = new ObjectMapper();
		final Map<String, Object> userData = new HashMap<String, Object>();
		userData.put("name", "Hallo " + name + ", willkommen bei LeMo!");

		return mapper.writeValueAsString(userData);
	}

}