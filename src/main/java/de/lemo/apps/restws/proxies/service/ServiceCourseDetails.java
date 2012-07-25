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

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;

/**
 * @author johndoe
 *
 */
public interface ServiceCourseDetails {
	
	@GET
	@Path("{course_id}")
	@Produces("application/json")
    public CourseObject getCourseDetails(@PathParam("course_id") Long id);
	
	@GET
	@Path("courses")
	@Produces("application/json")
    public ResultListCourseObject getCoursesDetails(@QueryParam("course_id") List<Long> ids);


}
