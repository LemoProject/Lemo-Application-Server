/**
	 * File CourseIdValueEncoderWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;


public class CourseIdValueEncoderWorker implements CourseIdValueEncoder {

	
	private CourseDAO courseDAO;
	@Inject
	private Logger logger;

	public CourseIdValueEncoderWorker(CourseDAO courseDAO){
		System.out.println("CourseIdValueEncoderWorker");
		this.courseDAO = courseDAO;
	}
	
	
	public String toClient(final Course value) {
		System.out.println("toClient");
		return String.valueOf(value.getId());
	}

	
	public Course toValue(final String id) {
		System.out.println("toValue");
		return this.courseDAO.getCourse(Long.parseLong(id));
	}

	public ValueEncoder<Course> create(final Class<Course> arg0) {
		System.out.println("create");
		return this;
	}

}