package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QPerformanceUserTestBoxPlot {

	/**
	 * @param courses
	 *            (optional) List of course-ids that shall be included
	 * @param users
	 *            (optional) List of user-ids
	 * @param quizzes
	 *            (mandatory) List of learning object ids (the ids have to start with the type specific prefix (11 for
	 *            "assignment", 14 for "quiz", 17 for "scorm"))
	 * @param resolution
	 *            (mandatory)
	 * @param startTime
	 *            (mandatory)
	 * @param endTime
	 *            (mandatory)
	 * @return
	 */

	@POST
	@Path("performanceUserTestBoxPlot")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) final List<Long> courses,
			@FormParam(MetaParam.USER_IDS) final List<Long> users,
			@FormParam(MetaParam.QUIZ_IDS) final List<Long> quizzes,
			@FormParam(MetaParam.RESOLUTION) final Long resolution,
			@FormParam(MetaParam.START_TIME) final Long startTime,
			@FormParam(MetaParam.END_TIME) final Long endTime);

}
