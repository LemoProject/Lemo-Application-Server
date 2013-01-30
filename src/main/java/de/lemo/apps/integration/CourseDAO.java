package de.lemo.apps.integration;

import java.util.List;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

public interface CourseDAO {

	public List<Course> findAllByOwner(User user);

	public List<Course> findAll();

	public List<Course> findFavoritesByOwner(User user);

	public boolean doExist(Course course);

	public boolean doExistByForeignCourseId(Long courseId);

	public Course getCourse(String coursename);

	public Course getCourse(Long id);

	public Course getCourseByDMSId(Long id);

	@CommitAfter
	public void toggleFavorite(Long id);

	@CommitAfter
	public void save(Course course);

	@CommitAfter
	public void save(CourseObject course);

	@CommitAfter
	public void update(Course course);

}
