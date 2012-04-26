package de.lemo.apps.services.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

import de.lemo.apps.entities.Course;

public class CourseIdSelectModel extends AbstractSelectModel {
	private List<Course> courses;

	public CourseIdSelectModel(List<Course> course) {
		this.courses = course;
	}
	
	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	@Override
	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for (Course course : courses) {
			options.add(new OptionModelImpl(course.getCourseDescription(), course.getId()));
		}
		return options;
	}
	
}
