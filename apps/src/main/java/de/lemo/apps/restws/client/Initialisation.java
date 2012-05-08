package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;

public interface Initialisation {
	
	public Date getStartTime();
	
	public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution);
	
	public CourseObject getCourseDetails(Long id);
	
	public ResultListCourseObject getCoursesDetails(List<Long> ids);

}
