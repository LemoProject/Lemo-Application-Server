/**
 * File ManageUser.java
 * Date Mar 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.admin;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.BeanModelSource;
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.UsageStatisticsContainer;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.data.DashboardAdmin;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;


/**
 * @author Andreas Pursian
 *
 */
@RequiresAuthentication
public class ManageUser {
	
	@Inject
	private Messages messages;
	
	@Inject
	private Logger logger;

	@Inject
	private Locale currentLocale;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private StatisticWorker statisticWorker;

	@Inject
	private AnalysisWorker analysisWorker;

	@Inject
	@Path("../../images/icons/glyphicons_019_cogwheel.png")
	@Property
	private Asset wheel;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserWorker userWorker;

	@Property
	private BreadCrumbInfo breadCrumb;

	
	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
	}
	
	@Component(id = "accountform")
	private Form form;

	@Property
	@Persist
	private Integer count;
	
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
