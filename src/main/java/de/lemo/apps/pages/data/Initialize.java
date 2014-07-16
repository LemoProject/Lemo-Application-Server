/**
	 * File Initialize.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.data;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Roles;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.admin.DashboardAdmin;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;


@RequiresAuthentication
public class Initialize {
	
	@Inject
	private Initialisation init;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private HttpServletRequest request;
	
	@Inject
	private UserDAO userDAO;

	@Inject
	UserDAO ud;
	
	@Inject 
	ComponentResources compRessources;
	
	@Component(parameters = {"event=progress", "id=literal:progress"})
    private EventLink progress;
	
	@Inject
	Logger logger;

	@Property
	@Persist
	private long percentage;
	
	@Property
	@Persist
	private double multi;
	
	public String getUserName() {
		
		return this.request.getRemoteUser();

	}
	
	void onPrepareForRender() {
		
		this.percentage = 0;
	}
	
	void cleanupRender() {
		this.percentage = 0;
		this.multi = 0;
	}

	public Object onProgressiveDisplay() {

		User user = this.ud.getUser(this.getUserName());
		
		if(user.getCurrentLogin() != null)
			user.setLastLogin(user.getCurrentLogin());
		user.setCurrentLogin(new Date());
		this.userDAO.update(user);

		final List<Long> userCourses = user.getMyCourseIds();
		
		if (userCourses != null && userCourses.size() > 0) {
			this.multi = 100 / userCourses.size();
			
			if (userCourses != null) {
				for (int i = 0; i < userCourses.size(); i++) {
					this.percentage = Math.round((i+1)*multi);
					logger.debug("Looking if course ID:"+userCourses.get(i)+" needs update.");
					if (this.courseDAO.courseNeedsUpdate(userCourses.get(i))) {
						CourseObject updateObject = null;
						try {
							updateObject = this.init.getCourseDetails(userCourses.get(i));
						} catch (RestServiceCommunicationException e) {
							logger.debug("Couldn't obtain data for course with Id " + userCourses.get(i) + ".");
						}
						if (updateObject != null) {
							logger.debug("ID of updated object is: "+ updateObject.getId() 
									+ "  ----  "+updateObject.getTitle()+ "  ----  "+ updateObject.getDescription() );
							this.courseDAO.update(updateObject);
						}
					}
					
				}
			}
		}
		return user.getRoles().contains(Roles.ADMIN) ? DashboardAdmin.class : Dashboard.class;

	}
	
	Object onProgress(){
		JSONObject progress = new JSONObject();
		logger.info("Progress Request, progress:"+this.percentage);
		progress.append("progress", String.valueOf(this.percentage)+"%");
		return progress;
	}
	
	public String getProgressEventURI(){
		return compRessources.createEventLink("Progress").toAbsoluteURI();
	}

	public String getStatusBar() {
		return String.valueOf(this.percentage)+"%";
	}

}
