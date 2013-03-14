/**
	 * File Initialize.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import de.lemo.apps.entities.Roles;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;


@RequiresAuthentication
public class Initialize {
	private static final int THOU = 1000;
	
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
				if (this.courseDAO.courseNeedsUpdate(userCourses.get(i))) {
					CourseObject updateObject = null;
					try {
						updateObject = this.init.getCourseDetails(userCourses.get(i));
					} catch (RestServiceCommunicationException e) {
						logger.error(e.getMessage());
					}
					if (updateObject != null) {
						logger.debug("ID of updated object is: "+ updateObject.getId() 
								+ "  ----  "+updateObject.getTitle()+ "  ----  "+ updateObject.getDescription() );
						this.courseDAO.update(updateObject);
					}
				}
			}
		}

		// Sleep 2 seconds to simulate a long-running operation
		this.sleep(THOU);
//		if (user.getRoles().contains(Roles.ADMIN)){
//			return DashboardAdmin.class;
//		} else return Dashboard.class;
		return user.getRoles().contains(Roles.ADMIN) ? DashboardAdmin.class : Dashboard.class;
	//	return Dashboard.class;
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
