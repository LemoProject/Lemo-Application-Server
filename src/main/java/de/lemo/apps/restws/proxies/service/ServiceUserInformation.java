/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/service/ServiceUserInformation.java
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
	 * File ServiceUserInformation.java
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

/**
 * Service for user information. Provide all courses for a user
 */
public interface ServiceUserInformation {

	@GET
	@Path("{user_id}/courses")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListCourseObject getCoursesByUser(
			@QueryParam("user_id") Long id,
			@QueryParam("course_count") Long count,
			@QueryParam("course_offset") Long offset);

}
