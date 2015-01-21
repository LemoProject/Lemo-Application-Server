/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/questions/QCumulativeUserAccess.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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
 * File QCumulativeUserAccess .java
 * Date Feb 14, 2013
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
 * Accumulates the requests of the users to the objects over a period
 */
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
			@FormParam(MetaParam.END_TIME) Long endtime,
			@FormParam(MetaParam.LEARNING_OBJ_IDS) List<Long> learningObjects);

}
