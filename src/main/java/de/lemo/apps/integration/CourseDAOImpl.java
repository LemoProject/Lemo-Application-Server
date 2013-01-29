/**
 * File ./de/lemo/apps/integration/CourseDAOImpl.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.integration;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

public class CourseDAOImpl implements CourseDAO {

	@Inject
	private Session session;

	@Override
	public List<Course> findAll() {
		final Criteria criteria = this.session.createCriteria(Course.class);
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	@Override
	public List<Course> findAllByOwner(final User user) {
		if (user.getMyCourses().isEmpty()) {
			return new ArrayList<Course>();
		}
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourses()));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	@Override
	public List<Course> findFavoritesByOwner(final User user) {
		if (user.getMyCourses().isEmpty()) {
			return new ArrayList<Course>();
		}
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourses()));
		criteria.add(Restrictions.eq("favorite", true));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	@Override
	public boolean doExist(final Course course) {
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("id", course.getId()));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean doExistByForeignCourseId(final Long courseId) {
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseId", courseId));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Course getCourse(final String coursename) {
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("coursename", coursename));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	@Override
	public Course getCourse(final Long id) {
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("id", id));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	@Override
	public Course getCourseByDMSId(final Long id) {
		final Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseId", id));
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	@Override
	public void toggleFavorite(final Long id) {
		System.out.println("CourseId:" + id);
		final Course favCourse = this.getCourse(id);
		Boolean favStatus = favCourse.getFavorite();
		System.out.println("FavStatus:" + favStatus);
		if (favStatus == null) {
			favStatus = true;
		} else {
			favStatus = !favStatus;
		}
		System.out.println("FavStatus2:" + favStatus);
		favCourse.setFavorite(favStatus);
		System.out.println("FavStatus3:" + favCourse.getFavorite());
		this.session.update(favCourse);
	}

	@Override
	public void save(final Course course) {
		this.session.persist(course);
	}

	@Override
	public void save(final CourseObject courseObject) {
		final Course course = new Course(courseObject);
		this.session.persist(course);
	}

	@Override
	public void update(final Course course) {
		this.session.update(course);
	}

}
