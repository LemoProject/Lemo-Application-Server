/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/service/ServiceCourseDetails.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
	 * File ServiceCourseDetails.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.restws.proxies.service;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * @author Andreas Pursian
 */
public interface ServiceCourseDetails {

	@GET
	@Path("{cid}")
	@Produces(MediaType.APPLICATION_JSON)
	CourseObject getCourseDetails(
			@PathParam("cid") Long id);

	@GET
	@Path("multi")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListCourseObject getCoursesDetails(
			@QueryParam(MetaParam.COURSE_IDS) List<Long> courseIds);

}
