/**
 * File ./de/lemo/apps/restws/proxies/questions/QFrequentPathsViger.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
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

/**
 * @author Andreas Pursian
 */
public interface QFrequentPathsViger {

	@POST
	@Path("frequentPathsViger")
	@Produces(MediaType.APPLICATION_JSON)
	public String compute(
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
