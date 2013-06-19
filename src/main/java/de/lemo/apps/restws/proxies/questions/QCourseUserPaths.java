/**
 * File QCourseUserPaths.java
 * Date Feb 14, 2013
 */

package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * Computes paths for users
 */
public interface QCourseUserPaths {

	@POST
	@Path("courseuserpaths")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courseIds,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime,
			@FormParam(MetaParam.GENDER) List<Long> gender);

}
