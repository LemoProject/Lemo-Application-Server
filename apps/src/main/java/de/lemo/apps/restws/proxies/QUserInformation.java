package de.lemo.apps.restws.proxies;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourse;


public interface QUserInformation {
	
	@GET
	@Path("getcoursedetails")
	@Produces("application/json")
    public CourseObject getCourseDetails(@QueryParam("course_id") Long id);
	
	@GET
	@Path("/getcoursesdetails")
	@Produces("application/json")
    public ResultListCourse getCoursesDetails(@QueryParam("course_id") List<Long> ids);
	
	@GET
	@Path("getCoursesByUser")
	@Produces("application/json")
    public ResultListCourse getCoursesByUser(@QueryParam("user_id") Long id, @QueryParam("course_count") Long count,
            @QueryParam("course_offset") Long offset);
	
	

}
