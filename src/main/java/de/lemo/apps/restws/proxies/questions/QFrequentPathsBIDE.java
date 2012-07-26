/**
 * 
 */
package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.USER_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.MIN_SUP;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.SESSION_WISE;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * @author Andreas Pursian
 *
 */
public interface QFrequentPathsBIDE {
	
	@POST
    @Path("frequentPaths")
    @Produces(MediaType.APPLICATION_JSON)
	public String compute(
    		@FormParam(COURSE_IDS) List<Long> courseIds, 
    		@FormParam(USER_IDS) List<Long> userIds, 
    		@FormParam(MIN_SUP) double minSup, 
    		@FormParam(SESSION_WISE) boolean sessionWise,
    		@FormParam(START_TIME) long startTime,
    		@FormParam(END_TIME) long endTime);
}
