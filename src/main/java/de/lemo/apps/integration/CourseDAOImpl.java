/**
 * File ./src/main/java/de/lemo/apps/integration/CourseDAOImpl.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/


/**
	 * File CourseDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */

package de.lemo.apps.integration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

/**
 * DAO for the course object
 */
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

	public List<Course> findAllByOwner(final User user, boolean needUpdate) {
		if (user.getMyCourses().isEmpty()) {
			return new ArrayList<Course>();
		}
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.in("courseId", user.getMyCourseIds()));
		criteria.add(Restrictions.eq("needUpdate", needUpdate));
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
	
	public List<Course> searchCourses(final List<String> searchStrings) {
		List<Long> searchLongs = new ArrayList<Long>();
		for (String s : searchStrings) {
			try {
				searchLongs.add(Long.valueOf(s).longValue());
			}
			catch (NumberFormatException e){
				
			}
		}
		Criteria criteria = this.session.createCriteria(Course.class);

		Junction res = Restrictions.disjunction();
		for (String s : searchStrings) {
			res.add(Restrictions.like("courseName", "%"+s+"%"));
			res.add(Restrictions.like("courseDescription", "%"+s+"%"));
		}
		if(!searchLongs.isEmpty()) res.add(Restrictions.in("courseId", searchLongs));
		criteria.add(res);
		final List<Course> results = criteria.list();
		if (results.size() == 0) {
			return new ArrayList<Course>();
		}
		return results;
	}

	public Course getCourse(final String coursename) {
		Criteria criteria = this.session.createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseDescription", coursename));
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
