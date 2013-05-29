/**
 * File Initialize.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.pages.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.Start;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;

/**
 * Registration for an user and lookup for the courses
 * of the user
 *
 */
public class Register {

	@Inject
	private Initialisation init;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private HttpServletRequest request;
	
	@Inject 
	ComponentResources compRessources;

	@Inject
	private Logger logger;
	
	@Component(parameters = {"event=progress", "id=literal:progress"})
    private EventLink progress;

	@Inject
	UserDAO userDAO;

	@Persist
	private Long dmsUserId;

	@Persist
	private String dmsUserName;
	
	@Property
	@Persist
	private long percentage;
	
	@Property
	@Persist
	private double multi;

	Object onActivate() {
		if (dmsUserId == null) {
			return Start.class;
		}
		return true;
	}

	void onPrepareForRender() {
		
		this.percentage = 0;
	}
	
	void cleanupRender() {
		this.percentage = 0;
		this.multi = 0;
	}

	public String getUserName() {
		return this.request.getRemoteUser();

	}

	public Object onProgressiveDisplay() {

		User user = new User(dmsUserName, dmsUserName, dmsUserName + "@localhost", "Lem0#Lem0");

		List<Long> userCourseIds = null;
		try {

			userCourseIds = init.getUserCourses(dmsUserId).getElements();

		} catch (RestServiceCommunicationException e1) {
			logger.error(e1.getMessage());
		}
		
		
		if (userCourseIds != null && userCourseIds.size() > 0) {
			this.multi = 100 / userCourseIds.size();
			logger.info("Loading  " + userCourseIds.size() + " courses");
			for (int i = 0; i < userCourseIds.size(); i++) {
				Long courseId =  userCourseIds.get(i);
				this.percentage = Math.round((i+1)*multi);
				if (!this.courseDAO.doExistByForeignCourseId(courseId)) {
					CourseObject courseObject = null;
					try {
						courseObject = this.init.getCourseDetails(courseId);
					} catch (RestServiceCommunicationException e) {
						logger.error(e.getMessage());
					}
					if (courseObject != null) {
						logger.info("New Course with Id: " + courseId + " added.");
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
			userDAO.save(user);

		} else {
			logger.info("Could not find any courses for this user.");
		}

		return Start.class;

	}

	public String getProgressEventURI(){
		return compRessources.createEventLink("Progress").toAbsoluteURI();
	}

	public String getStatusBar() {
		return String.valueOf(this.percentage)+"%";
	}
	
	Object onProgress(){
		JSONObject progress = new JSONObject();
		logger.info("Prgress Request, progress:"+this.percentage);
		progress.append("progress", String.valueOf(this.percentage)+"%");
		return progress;
	}

	/**
	 * @return the dmsUserId
	 */
	public Long getDmsUserId() {
		return dmsUserId;
	}

	/**
	 * @param dmsUserId
	 *            the dmsUserId to set
	 */
	public void setDmsUserId(Long dmsUserId) {
		this.dmsUserId = dmsUserId;
	}

	/**
	 * @return the dmsUserName
	 */
	public String getDmsUserName() {
		return dmsUserName;
	}

	/**
	 * @param dmsUserName
	 *            the dmsUserName to set
	 */
	public void setDmsUserName(String dmsUserName) {
		this.dmsUserName = dmsUserName;
	}

	private void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			// Ignore
		}
	}

}
