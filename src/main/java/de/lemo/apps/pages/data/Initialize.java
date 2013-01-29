/**
 * File ./de/lemo/apps/pages/data/Initialize.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.pages.data;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ioc.annotations.Inject;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;

/**
 * @author johndoe
 */
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

	public String getUserName() {
		return this.request.getRemoteUser();

	}

	public Object onProgressiveDisplay() {

		final User user = this.ud.getUser(this.getUserName());

		final List<Long> userCourses = user.getMyCourses();
		if (userCourses != null)
		{
			for (int i = 0; i < userCourses.size(); i++) {

				if (!this.courseDAO.doExistByForeignCourseId(userCourses.get(i))) {
					final CourseObject courseObject = this.init.getCourseDetails(userCourses.get(i));
					this.courseDAO.save(courseObject);
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
