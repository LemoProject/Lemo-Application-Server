/**
	 * File CourseIdValueEncoder.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;
import de.lemo.apps.entities.Course;

public interface CourseIdValueEncoder extends ValueEncoder<Course>, ValueEncoderFactory<Course> {

//	ValueEncoder<Course> create(Class<Course> arg0);
//
//	ValueEncoder<Course> create(Class<Course> type, CourseDAO courseDAO);

}
