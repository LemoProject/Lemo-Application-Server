/**
 * File ./de/lemo/apps/restws/proxies/service/ServiceCourseDetails.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.restws.proxies.service;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * @author johndoe
 */
public interface ServiceCourseDetails {

	@GET
	@Path("{cid}")
	@Produces(MediaType.APPLICATION_JSON)
	public CourseObject getCourseDetails(
			@PathParam("cid") Long id);

	@GET
	// @Path("courses")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListCourseObject getCoursesDetails(
			@QueryParam(MetaParam.COURSE_IDS) List<Long> courseIds);

}
