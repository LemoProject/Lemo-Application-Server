/**
	 * File Initialize.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.pages.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
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
	UserDAO ud;
	
	@Inject
	Logger logger;

	public String getUserName() {
		return this.request.getRemoteUser();

	}

	public Object onProgressiveDisplay() {

		final User user = this.ud.getUser(this.getUserName());

		final List<Long> userCourses = user.getMyCourseIds();
		if (userCourses != null) {
			for (int i = 0; i < userCourses.size(); i++) {
				
				logger.debug("Looking if course ID:"+userCourses.get(i)+" needs update.");

//				if (!this.courseDAO.doExistByForeignCourseId(userCourses.get(i))) {
//					CourseObject courseObject = null;
//					try {
//						courseObject = this.init.getCourseDetails(userCourses.get(i));
//					} catch (RestServiceCommunicationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (courseObject != null)
//						this.courseDAO.save(courseObject);
//				} else
				if (this.courseDAO.courseNeedsUpdate(userCourses.get(i))) {
					CourseObject updateObject = null;
					try {
						updateObject = this.init.getCourseDetails(userCourses.get(i));
					} catch (RestServiceCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (updateObject != null)
						logger.debug("ID of updated object is: "+ updateObject.getId()+ "  ----  "+updateObject.getTitle()+ "  ----  "+ updateObject.getDescription() );
						this.courseDAO.update(updateObject);
				}
			}
		// ResultListCourseObject courses = init.getCoursesDetails(user.getMyCourses());
		}

		// Sleep 2 seconds to simulate a long-running operation
		this.sleep(1000);

		return Dashboard.class;

	}

	public String getStatusBar() {
		return "20%";
	}

	private void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			// Ignore
		}
	}

}
