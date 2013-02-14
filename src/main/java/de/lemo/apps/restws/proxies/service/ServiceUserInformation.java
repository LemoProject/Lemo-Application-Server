/**
	 * File ServiceUserInformation.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import de.lemo.apps.restws.entities.ResultListCourseObject;

public interface ServiceUserInformation {

	@GET
	@Path("{user_id}/courses")
	@Produces("application/json")
	public ResultListCourseObject getCoursesByUser(@QueryParam("user_id") Long id,
			@QueryParam("course_count") Long count,
			@QueryParam("course_offset") Long offset);

}
