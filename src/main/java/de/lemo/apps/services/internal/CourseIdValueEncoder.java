/**
	 * File CourseIdValueEncoder.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;

public interface CourseIdValueEncoder {

	ValueEncoder<Course> create(Class<Course> arg0);

	ValueEncoder<Course> create(Class<Course> type, CourseDAO courseDAO);

}
