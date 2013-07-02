/**
 * File ./src/main/java/de/lemo/apps/services/internal/CourseIdSelectModel.java
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
	 * File CourseIdSelectModel.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.services.internal;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import de.lemo.apps.entities.Course;

public class CourseIdSelectModel extends AbstractSelectModel {

	private final List<Course> courses;

	public CourseIdSelectModel(final List<Course> course) {
		this.courses = course;
	}

	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	@Override
	public List<OptionModel> getOptions() {
		final List<OptionModel> options = new ArrayList<OptionModel>();
		for (final Course course : this.courses) {
			options.add(new OptionModelImpl(course.getCourseDescription(), course.getId()));
		}
		return options;
	}

}
