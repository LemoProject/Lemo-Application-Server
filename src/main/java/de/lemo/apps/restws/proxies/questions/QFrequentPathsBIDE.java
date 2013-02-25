/**
	 * File QFrequentPathsBIDE.java
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

/**
 * @author Andreas Pursian
 */
public interface QFrequentPathsBIDE {

	@POST
	@Path("frequentPaths")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courseIds,
			@FormParam(MetaParam.USER_IDS) List<Long> userIds,
			@FormParam(MetaParam.TYPES) List<String> types,
			@FormParam(MetaParam.MIN_LENGTH) Long minLength,
			@FormParam(MetaParam.MAX_LENGTH) Long maxLength,
			@FormParam(MetaParam.MIN_SUP) Double minSup,
			@FormParam(MetaParam.SESSION_WISE) Boolean sessionWise,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime);
}
