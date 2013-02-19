
/**
	 * File CourseDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */

package de.lemo.apps.integration;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

public class CourseDAOImpl implements CourseDAO {

	@Inject
	private Session session;

	@Inject
	private Logger logger;

	public List<Course> findAll() {
		Criteria criteria = this.session.createCriteria(Course.class);
		List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	public List<Course> findAllByOwner(final User user) {
		if (user.getMyCourses().isEmpty()) {
			return new ArrayList<Course>();
		}
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourseIds()));
		//criteria.add(Restrictions.eq("needUpdate", false));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	public List<Course> findFavoritesByOwner(final User user) {
		if (user == null || user.getMyCourses() == null || user.getMyCourses().isEmpty()) {
			return new ArrayList<Course>();
		}
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourseIds()));
		criteria.add(Restrictions.eq("favorite", true));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	public boolean doExist(final Course course) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("id", course.getId()));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	public boolean doExistByForeignCourseId(final Long courseId) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseId", courseId));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean courseNeedsUpdate(final Long courseId) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.conjunction()
				.add(Restrictions.eq("courseId", courseId))
				.add(Restrictions.eq("needUpdate", true))
		);
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	public Course getCourse(final String coursename) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("coursename", coursename));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	public Course getCourse(final Long id) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("id", id));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	public Course getCourseByDMSId(final Long id) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseId", id));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	public void save(final Course course) {
		this.session.persist(course);
	}

	public Course save(final CourseObject courseObject) {
		Course course = new Course(courseObject);
		this.session.persist(course);
		return course;
	}

	public void update(final CourseObject newCourse) {
		if(newCourse!=null){
			logger.debug("New Course:"+newCourse.getId());
			Course oldCourse = this.getCourseByDMSId(newCourse.getId());
			if (oldCourse != null){
				oldCourse.updateCourse(newCourse);
				this.session.update(oldCourse);
			}
		}
	}
	
	public void update(final Course course) {
		this.session.update(course);
	}

}
