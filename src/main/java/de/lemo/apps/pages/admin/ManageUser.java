/**
 * File ManageUser.java
 * Date Mar 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.admin;

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
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;


/**
 * @author Andreas Pursian
 *
 */
@RequiresAuthentication
public class ManageUser {
	
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

	
    Boolean onActivate(User user){
    	this.userItem = user;
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
	


}
