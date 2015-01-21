/**
 * File ./src/main/java/de/lemo/apps/integration/CourseDAO.java
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
	 * File CourseDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.integration;

import java.util.List;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.restws.entities.CourseObject;

/**
 * DAO class for courses
 */
public interface CourseDAO {

	List<Course> findAllByOwner(User user, boolean needUpdate);

	List<Course> findAll();

	List<Course> findFavoritesByOwner(User user);

	boolean doExist(Course course);

	boolean doExistByForeignCourseId(Long courseId);
	
	boolean courseNeedsUpdate(final Long courseId);
	
	List<Course> searchCourses(final List<String> searchStrings);

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
