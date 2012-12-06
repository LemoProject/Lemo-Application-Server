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
    		@FormParam(MIN_INTERVAL) Long minInterval,
    		@FormParam(MAX_INTERVAL) Long maxInterval,
    		@FormParam(MIN_WHOLE_INTERVAL) Long minWholeInterval,
    		@FormParam(MAX_WHOLE_INTERVAL) Long maxWholeInterval,
    		@FormParam(MIN_SUP) double minSup, 
    		@FormParam(SESSION_WISE) boolean sessionWise,
    		@FormParam(START_TIME) long startTime,
    		@FormParam(END_TIME) long endTime);
}
