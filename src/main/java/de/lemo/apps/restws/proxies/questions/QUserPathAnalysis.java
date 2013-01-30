package de.lemo.apps.restws.proxies.questions;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.COURSE_IDS;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.END_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.LOGOUT_FLAG;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.START_TIME;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.TYPES;
import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.USER_IDS;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QUserPathAnalysis {

	@POST
	@Path("userpathanalysis")
	@Produces(MediaType.APPLICATION_JSON)
	public String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courseIds,
			@FormParam(MetaParam.USER_IDS) List<Long> userIds,
			@FormParam(MetaParam.TYPES) List<String> types,
			@FormParam(MetaParam.LOGOUT_FLAG) Boolean considerLogouts,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime);

}
