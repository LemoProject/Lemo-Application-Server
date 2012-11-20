package de.lemo.apps.restws.proxies.questions;


import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListBoxPlot;

import static de.lemo.apps.restws.proxies.questions.parameters.MetaParam.*;


public interface QCumulativeUserAccess {

	/**
	 * @param starttime
	 *            min time for the data
	 * @param endtime
	 *            max time for the data
	 * @param types
	 *            list with learning objects to compute
	 * @param departments
	 *            departments for the request
	 * @param degrees
	 *            degrees for the request
	 * @param courses
	 *            courses for the request
	 * @return a list with the cumulative user access to the learning objects
	 * @throws SQLException
	 * @throws JSONException
	 */
	@POST
	@Path("cumulative")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListBoxPlot compute(
			@FormParam(START_TIME) int starttime,
			@FormParam(END_TIME) int endtime,
			@FormParam(TYPES) List<EResourceType> types, 
			@FormParam(DEPARTMENT) List<Long> departments, 
			@FormParam(DEGREE) List<Long> degrees, 
			@FormParam(COURSE_IDS) List<Long> courses) ;
	
}
