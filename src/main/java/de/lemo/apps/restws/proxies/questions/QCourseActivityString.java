/**
	 * File QCourseActivityString.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QCourseActivityString {

	@POST
	@Path("courseactivity")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.ROLE_IDS) List<Long> roles,
			@FormParam(MetaParam.USER_IDS) List<Long> users,
			@FormParam(MetaParam.START_TIME) Long starttime,
			@FormParam(MetaParam.END_TIME) Long endtime,
			@FormParam(MetaParam.RESOLUTION) Integer resolution,
			@FormParam(MetaParam.TYPES) List<String> resourceTypes);
}
