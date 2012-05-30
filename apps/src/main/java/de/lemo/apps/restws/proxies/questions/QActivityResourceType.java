package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;





public interface QActivityResourceType {

	
	@GET
	@Path("activityresourcetype")
	@Produces("application/json")
	public ResultListResourceRequestInfo compute(@QueryParam("course_ids") List<Long> courses, @QueryParam("starttime") long startTime, @QueryParam("endtime") long endTime, @QueryParam("types") List<EResourceType> resourceTypes);
	    

}