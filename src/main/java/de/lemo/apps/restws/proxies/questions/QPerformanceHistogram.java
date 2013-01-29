package de.lemo.apps.restws.proxies.questions;


import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.ResultListLongObject;



import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.*;


public interface QPerformanceHistogram {

	/**
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
	 * @throws SQLException
	 * @throws JSONException
	 */
	@POST
	@Path("performanceHistogram")
	@Produces(MediaType.APPLICATION_JSON)
    public ResultListLongObject compute(
    		@FormParam(COURSE_IDS) List<Long> courses, 
    		@FormParam(USER_IDS) List<Long> users, 
    		@FormParam(QUIZ_IDS) List<Long> quizzes,
    		@FormParam(RESOLUTION) Integer resolution,
    		@FormParam(START_TIME) Long startTime,
    		@FormParam(END_TIME) Long endTime) ;
	
}
