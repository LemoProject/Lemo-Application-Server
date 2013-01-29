package de.lemo.apps.restws.proxies.questions;



import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.*;

public interface QPerformanceBoxPlot {
	
	@POST
	@Path("performanceboxplot")
	@Produces(MediaType.APPLICATION_JSON)
    public String compute(
    		@FormParam(COURSE_IDS) List<Long> courses, 
    		@FormParam(USER_IDS) List<Long> users, 
    		@FormParam(QUIZ_IDS) List<Long> quizzes,
    		//@FormParam(RESOLUTION) Long resolution,
    		@FormParam(START_TIME) Long startTime,
    		@FormParam(END_TIME) Long endTime); 

}
