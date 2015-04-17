/**
 * File ./src/main/java/de/lemo/apps/pages/Start.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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
	 * File Start.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SymbolConstants;
import org.slf4j.Logger;
import org.tynamo.security.internal.ModularRealmAuthenticator;
import org.tynamo.security.services.PageService;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.data.Register;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;

/**
 * Start page of application test.
 */
@Secure
public class Start {

	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@Inject
	@Path("../images/Nutzungsanalyse.png")
	@Property
	private Asset carusselOne;

	@Inject
	@Path("../images/Pfadvisualisierung.png")
	@Property
	private Asset carusselTwo;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private Messages messages;
	
	@Inject
	private Initialisation init;
	
	@Inject 
	UserDAO userDAO;
	
	@Inject
	private CourseDAO courseDAO;
	
	@Property
	@Persist
	private User userItem;
	
	@InjectPage
	private Register registerPage;

	@Inject
	private SecurityService securityService;
	
	@Inject
	private ModularRealmAuthenticator modularRealmAuthenticator;

	@SuppressWarnings("deprecation")
	@Inject
	private PageService pageService;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String username;

	@Property
	private String password;

	@AfterRender
	public void afterRender() {
		this.javaScriptSupport.addScript(
					"$('.carousel').carousel({ " +
							"interval: 8000" +
							"})"
				);
	}

	@Component
	private Form loginForm;

	@Persist
	@Property
	private int clickCount;

	@Inject
	private AlertManager alertManager;

	public Date getCurrentTime() {
		return new Date();
	}

	public Object onSubmitFromLoginForm() {

		try {
			AuthenticationStrategy authenticationStrategy = new AtLeastOneSuccessfulStrategy();
			this.modularRealmAuthenticator.setAuthenticationStrategy(authenticationStrategy);
			final Subject currentUser = this.securityService.getSubject();

			if (currentUser == null) {
				throw new IllegalStateException("Error during login. Can't obtain user from security service.");
			}

			final UsernamePasswordToken token = new UsernamePasswordToken(this.username, this.password);
			this.logger.debug("Prepare Logintoken. Username: " + this.username);
			currentUser.login(token);
			if(!userDAO.doExist(this.username))
			{
				try{
					this.logger.debug("Login: The user " + username + " doesn't exist locally. And will be created.");
					addCourses();
				} catch(Exception e){
					logger.debug("Login: Can't create user. " + e.getMessage());
				}
			}

		} catch (AuthenticationException ex) {
			this.logger.info("Login unsuccessful.");
			this.loginForm.recordError(this.messages.get("error.login"));
			this.alertManager.info("Login or password not correct.");
			
			/*
			 * If user authentification fails we will start a lookup to check whether this username is known by the dms
			 */
			if (!userDAO.doExist(this.username)) {
				ResultListLongObject result = null;
				try {
					
						result = init.identifyUserName(this.username);
				
				} catch (RestServiceCommunicationException e) {
					logger.error(e.getMessage());
				}
				
				if (result != null && result.getElements()!=null && result.getElements().size() > 0) {
					
					Long dmsUserId = result.getElements().get(result.getElements().size()-1); 
			
					logger.debug("Corresponding LeMo user ID : "+dmsUserId);
					
					registerPage.setDmsUserId(dmsUserId);
					registerPage.setDmsUserName(this.username);
			
					return registerPage;
					
				} else {
					return Start.class;
				}
			} else return Start.class;
		} 

		return this.pageService.getSuccessPage();
	}
	
	private void addCourses(){
		User user = new User(username, username, username + "@localhost");
		
		userDAO.save(user);
		
		ResultListLongObject result;
		Long dmsUserId = null;
		try {
			result = init.identifyUserName(this.username);
			dmsUserId = result.getElements().get(result.getElements().size()-1);
		} catch (RestServiceCommunicationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		List<Long> userCourseIds = null;
		try {

			userCourseIds = init.getUserCourses(dmsUserId).getElements();
			logger.debug("Course received for user "+dmsUserId+": "+userCourseIds.size());
		} catch (RestServiceCommunicationException e1) {
			logger.error(e1.getMessage());
		}
		
		
		if (userCourseIds != null && userCourseIds.size() > 0) {
			double multi = 100 / userCourseIds.size();
			logger.debug("Loading  " + userCourseIds.size() + " courses");
			for (int i = 0; i < userCourseIds.size(); i++) {
				Long courseId =  userCourseIds.get(i);
				double percentage = Math.round((i+1)*multi);
				if (!this.courseDAO.doExistByForeignCourseId(courseId)) {
					CourseObject courseObject = null;
					try {
						courseObject = this.init.getCourseDetails(courseId);
					} catch (RestServiceCommunicationException e) {
						logger.error(e.getMessage());
					}
					if (courseObject != null) {
						logger.debug("New Course with Id: " + courseId + " added.");
						Course savedCourse = this.courseDAO.save(courseObject);
						user.getMyCourses().add(savedCourse);
						logger.debug("Current sourse Amount: " + user.getMyCourses().size() + " .");
					} else {
						logger.debug("Course with Id: " + courseId + " could not be retrieved.");
					}
				} else {
					logger.debug("Course with Id: " + courseId + " is already cached.");
					user.getMyCourses().add(this.courseDAO.getCourseByDMSId(courseId));
				}
			}
			

		} else {
			logger.info("Could not find any courses for this user.");
		}
		
		userDAO.save(user);
	}
}
