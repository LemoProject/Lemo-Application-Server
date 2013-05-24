/**
 * File ServiceCourseTitleSearch.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;;

/**
 * @author Andreas Pursian
 */
public interface ServiceCourseTitleSearch {
	
	@GET
	@Path("coursesbytext")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListCourseObject getCoursesByText(
			@QueryParam(MetaParam.SEARCH_TEXT) final String text,
			@QueryParam(MetaParam.RESULT_AMOUNT) final Long count,
			@QueryParam(MetaParam.OFFSET) final Long offset );

}
