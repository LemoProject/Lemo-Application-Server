/**
	 * File CourseDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.integration;

import java.util.List;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

public interface CourseDAO {

	List<Course> findAllByOwner(User user);

	List<Course> findAll();

	List<Course> findFavoritesByOwner(User user);

	boolean doExist(Course course);

	boolean doExistByForeignCourseId(Long courseId);
	
	boolean courseNeedsUpdate(final Long courseId);

	Course getCourse(String coursename);

	Course getCourse(Long id);

	Course getCourseByDMSId(Long id);

	@CommitAfter
	void save(Course course);

	@CommitAfter
	Course save(CourseObject course);
	
	@CommitAfter
	void update(CourseObject newCourse);

	@CommitAfter
	void update(Course course);

}
