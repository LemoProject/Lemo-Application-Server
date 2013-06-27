/**
	 * File ServiceCourseDetails.java
	 *
	 * Date Feb 14, 2013 
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
 * @author Andreas Pursian
 */
public interface ServiceCourseDetails {

	@GET
	@Path("{cid}")
	@Produces(MediaType.APPLICATION_JSON)
	CourseObject getCourseDetails(
			@PathParam("cid") Long id);

	@GET
	@Path("multi")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListCourseObject getCoursesDetails(
			@QueryParam(MetaParam.COURSE_IDS) List<Long> courseIds);
	
	@GET
	@Path("{cid}/hash")
	@Produces(MediaType.APPLICATION_JSON)
	public Long getCourseHash(@PathParam("cid") final Long id);
	
	/**
	 * Checks whether a course should be filtered by gender attribute based on k-anonymity.
	 * 
	 * @param id  Course identifier.
	 * 
	 * @return	A Boolean value.
	 */
	@GET
	@Path("{cid}/genderSupport")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean getGenderSupport(@PathParam("cid") final Long id);

}
