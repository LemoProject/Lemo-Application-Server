/**
	 * File QPerformanceBoxPlot.java
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
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QPerformanceBoxPlot {

	@POST
	@Path("performanceboxplot")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.USER_IDS) List<Long> users,
			@FormParam(MetaParam.QUIZ_IDS) List<Long> quizzes,
			@FormParam(MetaParam.RESOLUTION) Long resolution,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime);

}
