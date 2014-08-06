/**
 * File ./src/main/java/de/lemo/apps/pages/admin/DashboardAdmin.java
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
import de.lemo.apps.restws.entities.EConnectorManagerState;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.SCConnector;
import de.lemo.apps.restws.entities.SCConnectorManagerState;
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

	
	void setupRender() {

		this.connectorManagerState = getConnectorManagerState();
		if(this.connectorManagerState!= null && this.connectorManagerState.getState() != null)
			this.connectorState = this.connectorManagerState.getState();
		else this.connectorState = "UNDEFINED";
				
		if(this.connectorManagerState!= null && this.connectorManagerState.getUpdatingConnector() != null)
			this.updatingConnector = this.connectorManagerState.getUpdatingConnector().getName();
		else this.updatingConnector = "UNDEFINED";
	}

	
	@Persist
	private SCConnectorManagerState connectorManagerState;
	
	@Property
	@Persist
	private String connectorState;
	
	@Property
	@Persist
	private String updatingConnector;
	
	
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
    	connectorModel.add("update",null);
    }
	
	public List<User> getAllUser(){
		return userDAO.getAllUser();
	}
	
	@Cached
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
	
	public Object onActionFromStartUpdate(Long connectorId){
		if (connectorId != null){
			try {
				info.startUpdate(connectorId);
			} catch (RestServiceCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this;
	}
	
	
	public SCConnector getUpdatingConnector(){
		return  getConnectorManagerState().getUpdatingConnector();
	}
	
	
	public Boolean getConnectorReady(){
		if(this.connectorState.equals(EConnectorManagerState.READY.toString()) )
			return true;
		else return false;
	}
	
	public SCConnectorManagerState getConnectorManagerState(){
		SCConnectorManagerState state;
		try {
			state = info.getConnectorState();
			logger.debug("State: "+ state.getState());
			return state;
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new SCConnectorManagerState();
	}
	

}
