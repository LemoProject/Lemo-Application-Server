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
 *
 */
public class CourseIdValueEncoderWorker implements ValueEncoder<Course>, ValueEncoderFactory<Course>, CourseIdValueEncoder { 

    @Inject
    private CourseDAO courseDAO;

    @Override
    public String toClient(Course value) {
        // return the given object's ID
        return String.valueOf(value.getId()); 
    }

    @Override
    public Course toValue(String id) { 
        // find the color object of the given ID in the database
        return courseDAO.getCourse(Long.parseLong(id)); 
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    public ValueEncoder<Course> create(Class<Course> type, CourseDAO courseDAO) {
    	this.courseDAO = courseDAO;
        return this; 
    }

	@Override
	public ValueEncoder<Course> create(Class<Course> arg0) {
		return this; 
	}

	
}