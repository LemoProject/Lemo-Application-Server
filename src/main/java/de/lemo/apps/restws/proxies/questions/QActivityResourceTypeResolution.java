/**
	 * File QActivityResourceTypeResolution.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.questions;


import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * Checks which resources are used in certain courses
 * an extra parameter specifies the resolution of the data
 */
public interface QActivityResourceTypeResolution {

	@POST
	@Path("activityresourcetyperesolution")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListRRITypes compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime,
			@FormParam(MetaParam.RESOLUTION) Long resolution,
			@FormParam(MetaParam.TYPES) List<String> resourceTypes);

}