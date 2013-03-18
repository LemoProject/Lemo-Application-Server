
/**
 * File ManageUser.java
 * Date Mar 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.data.DashboardAdmin;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;


/**
 * @author Andreas Pursian
 *
 */
@RequiresAuthentication
public class ManageUser {
	
	@Inject
	private Initialisation init;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Messages messages;

	@Inject
	@Path("../../images/icons/glyphicons_019_cogwheel.png")
	@Property
	private Asset wheel;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Property
	private BreadCrumbInfo breadCrumb;

	@Component(id = "accountform")
	private Form form;
	
	@Property
	@Persist
	private User userItem;
	
	@Property
	@Persist
	private Course courseItem;
	
	@Property
	@Persist
	private List<Course> searchCoursesList;

	
    Boolean onActivate(User user){
    	this.userItem = user;
    	searchCoursesList = null;
    	return false;
    }
    
//    Object onActivate(){
//    	if (this.userItem == null) {
//    		return DashboardAdmin.class; 
//    	} else return this;
//    }
    
	
	public List<Course> getUserCourses() {
		return this.courseDAO.findAllByOwner(userItem, false);
	}
	
	
	public User getUser(){
		return this.userItem;
	}
	
	Object onSuccess() {
		this.userDAO.update(this.userItem);
		return this;
	}
	
	public String getDeleteString() {
		return messages.format("sureToDelete", userItem.getFullname());
	}
	
	Object onActionFromDelete() {
	    	this.userDAO.remove(this.userItem);
	    	return DashboardAdmin.class;
	}
	
	void onActionFromSearch() {
		//input abfragen ob long oder string
		searchCoursesList = new ArrayList<Course>();
		ArrayList<Long> lA = new ArrayList<Long>();
		lA.add(112200L);
		ResultListCourseObject rs = new ResultListCourseObject();
		try {
			rs = this.init.getCoursesDetails(lA);
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (rs!=null) {
			List<CourseObject> cd = rs.getElements();
			if (cd!=null) {
				for (CourseObject co : cd) {
					searchCoursesList.add(new Course(co));
				}
			}
		}
	}
	
	public List<Course> getSearchCourseList() {
		return this.searchCoursesList;
	}
	
	public boolean getSearchCourses() {
		if (searchCoursesList!=null) {
			return true;
		}
		else {
			return false;
		}
	}

}
