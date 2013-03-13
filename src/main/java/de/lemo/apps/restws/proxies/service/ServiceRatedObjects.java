/**
	 * File ServiceRatedObjects.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.service;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * Service to get a list of all learning objects within the specified courses that have a grade attribute
 */
public interface ServiceRatedObjects {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	ResultListStringObject getRatedObjects(
			@QueryParam(MetaParam.COURSE_IDS) List<Long> courses);

}
