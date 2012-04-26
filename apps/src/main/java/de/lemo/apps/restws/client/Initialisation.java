package de.lemo.apps.restws.client;

import java.util.Date;
import java.util.List;

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourse;
import de.lemo.apps.restws.entities.ResultListLong;

public interface Initialisation {
	
	public Date getStartTime();
	
	public ResultListLong computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution);
	
	public CourseObject getCourseDetails(Long id);
	
	public ResultListCourse getCoursesDetails(List<Long> ids);

}
