package de.lemo.apps.restws.proxies.questions;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QPrediction {

	@POST
	@Path("prediction")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courseIds,
			@FormParam(MetaParam.USER_IDS) List<Long> userIds,
			@FormParam(MetaParam.TYPES) List<String> types,
			@FormParam(MetaParam.LOGOUT_FLAG) Boolean considerLogouts,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime,
			@FormParam(MetaParam.GENDER) List<Long> gender,
			@FormParam(MetaParam.LEARNING_OBJ_IDS) List<Long> learningObjects);

}
