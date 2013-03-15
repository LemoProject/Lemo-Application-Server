package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;
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
	
	ResultListLongObject identifyUserName(final String login) throws RestServiceCommunicationException;
	
	ResultListLongObject getUserCourses(Long userId) throws RestServiceCommunicationException;
	
	ResultListCourseObject getUserCourses(Long userId, Long amount, Long offset) throws RestServiceCommunicationException;

	ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) throws RestServiceCommunicationException;
		
}
