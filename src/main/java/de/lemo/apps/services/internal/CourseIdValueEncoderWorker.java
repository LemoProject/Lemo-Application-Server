/**
 * File ./de/lemo/apps/services/internal/CourseIdValueEncoderWorker.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;

/**
 * @author johndoe
 */
public class CourseIdValueEncoderWorker implements ValueEncoder<Course>, ValueEncoderFactory<Course>,
		CourseIdValueEncoder {

	@Inject
	private CourseDAO courseDAO;

	@Override
	public String toClient(final Course value) {
		// return the given object's ID
		return String.valueOf(value.getId());
	}

	@Override
	public Course toValue(final String id) {
		// find the color object of the given ID in the database
		return this.courseDAO.getCourse(Long.parseLong(id));
	}

	// let this ValueEncoder also serve as a ValueEncoderFactory
	@Override
	public ValueEncoder<Course> create(final Class<Course> type, final CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
		return this;
	}

	@Override
	public ValueEncoder<Course> create(final Class<Course> arg0) {
		return this;
	}

}