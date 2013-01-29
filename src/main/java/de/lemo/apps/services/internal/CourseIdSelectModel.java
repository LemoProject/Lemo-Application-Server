/**
 * File ./de/lemo/apps/services/internal/CourseIdSelectModel.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
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
