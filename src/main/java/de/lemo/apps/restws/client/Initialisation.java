package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListStringObject;

public interface Initialisation {
	
	public Boolean defaultConnectionCheck() throws RestServiceCommunicationException;

	public Date getStartTime() throws RestServiceCommunicationException;

	public CourseObject getCourseDetails(Long id) throws RestServiceCommunicationException;

	public ResultListCourseObject getCoursesDetails(List<Long> ids) throws RestServiceCommunicationException;

	public ResultListStringObject getRatedObjects(List<Long> courseIds) throws RestServiceCommunicationException;

}
