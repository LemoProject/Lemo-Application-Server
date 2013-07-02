/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/questions/QActivityResourceTypeResolution.java
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
 * File QActivityResourceTypeResolution.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.restws.proxies.questions;

import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * Checks which resources are used in certain courses
 * an extra parameter specifies the resolution of the data
 */
public interface QActivityResourceTypeResolution {

	@POST
	@Path("activityresourcetyperesolution")
	@Produces(MediaType.APPLICATION_JSON)
	ResultListRRITypes compute(
			@FormParam(MetaParam.COURSE_IDS) List<Long> courses,
			@FormParam(MetaParam.START_TIME) Long startTime,
			@FormParam(MetaParam.END_TIME) Long endTime,
			@FormParam(MetaParam.RESOLUTION) Long resolution,
			@FormParam(MetaParam.TYPES) List<String> resourceTypes,
			@FormParam(MetaParam.GENDER) List<Long> gender);

}