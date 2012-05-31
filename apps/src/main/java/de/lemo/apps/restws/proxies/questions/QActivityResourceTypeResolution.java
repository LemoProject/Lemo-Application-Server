package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lemo.apps.restws.entities.ResultListRRITypes;





public interface QActivityResourceTypeResolution {	

	@GET
	@Path("activityresourcetyperesolution")
	@Produces("application/json")
    public ResultListRRITypes compute(@QueryParam("course_ids") List<Long> courses, @QueryParam("starttime") long startTime, @QueryParam("endtime") long endTime, @QueryParam("resolution") long resolution, @QueryParam("types") List<String> resourceTypes); 

	
}