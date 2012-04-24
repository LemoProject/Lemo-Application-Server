package de.lemo.apps.restws.proxies;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lemo.apps.restws.entities.ResultList;
import de.lemo.apps.restws.entities.ResultListLong;



public interface QCourseActivity {
	
	@GET
	@Path("compute")
	@Produces("application/json")
    public ResultListLong compute(@QueryParam("course_ids") List<Long> courses, @QueryParam("role_ids") List<Long> roles,
    						  @QueryParam("starttime") Long starttime, @QueryParam("endtime") Long endtime, 
    						  @QueryParam("resolution") int resolution);

}
