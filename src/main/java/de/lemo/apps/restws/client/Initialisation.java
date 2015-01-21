/**
 * File ./src/main/java/de/lemo/apps/restws/client/Initialisation.java
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

package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;
import org.apache.tapestry5.json.JSONObject;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListStringObject;

public interface Initialisation {

	Boolean defaultConnectionCheck() throws RestServiceCommunicationException;

	Date getStartTime() throws RestServiceCommunicationException;

	CourseObject getCourseDetails(Long id) throws RestServiceCommunicationException;

	ResultListCourseObject getCoursesDetails(List<Long> ids) throws RestServiceCommunicationException;

	ResultListStringObject getRatedObjects(List<Long> courseIds) throws RestServiceCommunicationException;

	ResultListStringObject getLearningObjects(List<Long> courseIds) throws RestServiceCommunicationException;
	
	ResultListStringObject getLearningTypes(List<Long> courseIds) throws RestServiceCommunicationException;
	
	ResultListLongObject identifyUserName(final String login) throws RestServiceCommunicationException;

	ResultListLongObject getUserCourses(Long userId) throws RestServiceCommunicationException;

	ResultListCourseObject getUserCourses(Long userId, Long amount, Long offset)
			throws RestServiceCommunicationException;

	JSONObject  taskResult(String taskId);

}
