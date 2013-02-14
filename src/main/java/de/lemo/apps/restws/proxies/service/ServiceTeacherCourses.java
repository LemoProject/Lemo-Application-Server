/**
 * File ServiceTeacherCourses.java
 * Date Feb 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;


/**
 * @author Andreas Pursian
 *
 */
public interface ServiceTeacherCourses {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListLongObject getTeachersCourses(
			@QueryParam(MetaParam.USER_IDS) Long userId);


}
