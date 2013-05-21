/**
	 * File Dashbaord.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
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
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.UsageStatisticsContainer;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Information;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.SCConnector;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

/**
 * Dashboard view for the administrator
 *
 */
@RequiresAuthentication
@BreadCrumb(titleKey = "dashboardTitle")
@BreadCrumbReset
public class DashboardAdmin {

	@Inject
	private Logger logger;

	@Inject
	private UserDAO userDAO;


	@Property
	private BreadCrumbInfo breadCrumb;

	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
    private Messages messages;


	@Inject
	private Information info;

	
//	void onPrepareForRender() {
//		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
//	}

	@Property
	@Persist
	private Integer count;
	
	@SuppressWarnings("unused")
	@Property
	private User userItem;
	
	@SuppressWarnings("unused")
	@Property
	private SCConnector connectorItem;


	@SuppressWarnings("unchecked")
	@Property
	private final BeanModel userModel;
    {
    	userModel = beanModelSource.createEditModel(User.class, messages);
    	userModel.include("username","fullname","email","accountlocked");
    	userModel.reorder("username","fullname","email");
    	userModel.add("view",null);
    }
    
    
    @SuppressWarnings("unchecked")
	@Property
	private final BeanModel connectorModel;
    {
    	connectorModel = beanModelSource.createDisplayModel(SCConnector.class, messages);
    	connectorModel.include("name","platformId","platformName");
    	connectorModel.reorder("name","platformId","platformName");
    	connectorModel.add("view",null);
    }
	
	
//	public List<Course> getMyCourses() {
//		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
//	}
	
	
//	public User getUser(){
//		return userDAO.getUser(this.request.getRemoteUser());
//	}
	
	public List<User> getAllUser(){
		return userDAO.getAllUser();
	}
	
	public List<SCConnector> getAllConnectors(){
		try {
			
			List<SCConnector> connectorList = info.getConnectorList();
			
			return connectorList;
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<SCConnector>();
	}
	

}
