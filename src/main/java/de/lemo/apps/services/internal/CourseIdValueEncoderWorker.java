/**
	 * File CourseIdValueEncoderWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;


public class CourseIdValueEncoderWorker implements CourseIdValueEncoder {

	
	private CourseDAO courseDAO;
	@Inject
	private Logger logger;

	public CourseIdValueEncoderWorker(CourseDAO courseDAO){
		this.courseDAO = courseDAO;
	}
	
	
	public String toClient(final Course value) {
		return String.valueOf(value.getId());
	}

	
	public Course toValue(final String id) {
		return this.courseDAO.getCourse(Long.parseLong(id));
	}

	public ValueEncoder<Course> create(final Class<Course> arg0) {
		return this;
	}

}