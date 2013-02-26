/**
 * File Initialize.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.pages.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.Start;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;

public class Register {

	@Inject
	private Initialisation init;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private HttpServletRequest request;

	@Inject
	private Logger logger;

	@Inject
	UserDAO userDAO;

	@Persist
	private Long dmsUserId;

	@Persist
	private String dmsUserName;

	Object onActivate() {
		if (dmsUserId == null) {
			return Start.class;
		}
		return true;
	}

	void cleanupRender() {

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

		if (userCourseIds != null) {
			logger.debug("Loading  " + userCourseIds.size() + " courses");
			for (Long courseId : userCourseIds) {

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
			userDAO.save(user);

		} else {
			logger.debug("Could not find any courses for this user.");
		}
		// Sleep 2 seconds to simulate a long-running operation
		this.sleep(10000);

		return Start.class;

	}

	public String getStatusBar() {
		return "20%";
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
