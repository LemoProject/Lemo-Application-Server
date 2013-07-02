/**
 * File ./src/main/java/de/lemo/apps/pages/data/Search.java
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

package de.lemo.apps.pages.data;

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

