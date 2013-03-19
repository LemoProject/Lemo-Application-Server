/**
	 * File ServiceUserInformation.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListCourseObject;

/**
 * Service for user information. Provide all courses for a user
 */
public interface ServiceUserInformation {

	@GET
	@Path("{user_id}/courses")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListCourseObject getCoursesByUser(
			@QueryParam("user_id") Long id,
			@QueryParam("course_count") Long count,
			@QueryParam("course_offset") Long offset);

}
