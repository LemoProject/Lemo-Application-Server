/**
 * File QPerformanceHistogram.java
 * Date Feb 14, 2013
 * 
 * @param starttime
 *            min time for the data
 * @param endtime
 *            max time for the data
 * @param users
 *            users for the request
 * @param resolution
 *            resolution for the request
 * @param quiz
 *            quiz for the request
 * @param courses
 *            courses for the request
 * @return a list with the cumulative user access to the learning objects
 */
package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * Results for the perfromance (test) of student
 */
public interface QPerformanceHistogram {

	@POST
	@Path("performanceHistogram")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListLongObject compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.USER_IDS) List<Long> users,
			@FormParam(MetaParam.QUIZ_IDS) List<Long> quizzes,
			@FormParam(MetaParam.RESOLUTION) Long resolution,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime,
			@FormParam(MetaParam.GENDER) List<Long> gender);

}
