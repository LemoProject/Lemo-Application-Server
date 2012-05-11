package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;

public interface Initialisation {
	
	public Date getStartTime();
	
	public CourseObject getCourseDetails(Long id);
	
	public ResultListCourseObject getCoursesDetails(List<Long> ids);

}
