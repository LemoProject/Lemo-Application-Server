package de.lemo.apps.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;

@RequiresAuthentication
public class Search {
	
	@Persist
	private String searchQuery;
	
	@Inject
	private CourseDAO courseDAO;
	
	@Property
	private List<Course> allCourses;
	
	@Inject
	private Logger logger;
	
	@Property
	@Persist
	private Course courseItem;
	
	public void setSearchQuery(String string) {
		this.searchQuery = string;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	
	public void onActivate() {
		allCourses = this.courseDAO.searchCourses(Arrays.asList(searchQuery.split(" ")));	
	}

	
}

