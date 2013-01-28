/**
 * 
 */
package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.*;


import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * @author Andreas Pursian
 *
 */
public interface QFrequentPathsViger {
	
	@POST
    @Path("frequentPathsViger")
    @Produces(MediaType.APPLICATION_JSON)
	public String compute(
			@FormParam(COURSE_IDS) List<Long> courseIds, 
    		@FormParam(USER_IDS) List<Long> userIds, 
    		@FormParam(TYPES) List<String> types,
    		@FormParam(MIN_LENGTH) Long minLength,
    		@FormParam(MAX_LENGTH) Long maxLength,
    		@FormParam(MIN_SUP) Double minSup, 
    		@FormParam(SESSION_WISE) Boolean sessionWise,
    		@FormParam(START_TIME) Long startTime,
    		@FormParam(END_TIME) Long endTime);
	
	
}
