/**
 * 
 */
package de.lemo.apps.restws.proxies.questions;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.TYPES;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.USER_IDS;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

/**
 * @author johndoe
 *
 */
public interface QLearningObjectUsage {
	
	 	@POST
	    @Path("learningobjectusage")
	    @Produces(MediaType.APPLICATION_JSON)
	    public ResultListResourceRequestInfo compute(
	            @FormParam(COURSE_IDS) List<Long> courseIds,
	            @FormParam(USER_IDS) List<Long> userIds,
			    @FormParam(TYPES) List<String> types,
			    @FormParam(START_TIME) Long startTime,
		        @FormParam(END_TIME) Long endTime);
     

}
