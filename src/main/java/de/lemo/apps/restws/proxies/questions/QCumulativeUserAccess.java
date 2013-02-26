/**
	 * File  QCumulativeUserAccess .java
	 *
	 * Date Feb 14, 2013 
	 *
	 * 
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
package de.lemo.apps.restws.proxies.questions;


import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

public interface QCumulativeUserAccess {

	@POST
	@Path("cumulative")
	@Produces(MediaType.APPLICATION_JSON)
	String compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.TYPES) List<String> types,
			@FormParam(MetaParam.DEPARTMENT) List<Long> departments,
			@FormParam(MetaParam.DEGREE) List<Long> degrees,
			@FormParam(MetaParam.START_TIME) Long starttime,
			@FormParam(MetaParam.END_TIME) Long endtime);

}
