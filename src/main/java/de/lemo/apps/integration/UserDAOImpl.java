/**
 * File ./src/main/java/de/lemo/apps/integration/UserDAOImpl.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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
	 * File UserDAOImpl.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */

package de.lemo.apps.integration;

import java.util.Date;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;

/**
 * implementation of the DAO for the user
 * @author Boris Wenzlaff
 *
 */
public class UserDAOImpl implements UserDAO {

	@Inject
	private Session session;

	@Inject
	private Logger logger;
	
	@Inject
	private CourseDAO courseDAO;
	
	
	public List<User> getAllUser(){
		final Criteria criteria = this.session.createCriteria(User.class);
		final List<User> results = criteria.list();
		
		return results;
	}

	public boolean doExist(final User user) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", user.getId()));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean doExist(final String username) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return false;
		}
		return true;
	}

	public User getUser(final String username) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}
	
	public User getUser(final Long userId) {
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", userId));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		return results.get(0);
	}
	
	public boolean toggleFavoriteCourse(final Long courseId, final Long userId) {
		logger.debug("Setting favorite course courseId:" + courseId);

		Course course = this.courseDAO.getCourse(courseId); 
		User user = this.getUser(userId);
		boolean isFavorite;
		if (user.getFavoriteCourses().contains(course)) {
			user.getFavoriteCourses().remove(course);
			isFavorite = false;
		} else {
			user.getFavoriteCourses().add(course);
			isFavorite = true;
		}
		this.session.update(user);
		return isFavorite;
	}
	

	public void save(final User user) {
		this.session.persist(user);
	}

	public void update(final User user) {
		this.session.update(user);
	}

	public User login(final String username, final String password) {
		this.logger.info("Check login credentials with username " + username);
		final Criteria criteria = this.session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		final List<User> results = criteria.list();
		if (results.size() == 0) {
			return null;
		}
		User user = results.get(0);

		return user;
	}
	
	public void remove(final User user) {
		this.session.delete(user);
	}

}

