/**
 * 
 */
package de.lemo.apps.pages.data;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ioc.annotations.Inject;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;

/**
 * @author johndoe
 *
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
	
	public String getUserName(){
		return request.getRemoteUser();
		
	}

	public Object onProgressiveDisplay() {
		
		User user = ud.getUser(getUserName());
		
		
		List<Long> userCourses = user.getMyCourses();
		if(userCourses!=null)
			for (int i = 0; i < userCourses.size();i++ ){
				
				if (!courseDAO.doExistByForeignCourseId(userCourses.get(i))) {
					CourseObject courseObject = init.getCourseDetails(userCourses.get(i));
					courseDAO.save(courseObject);
				}
			}
		//ResultListCourseObject courses = init.getCoursesDetails(user.getMyCourses());	
			
		
		
		// Sleep 2 seconds to simulate a long-running operation
		sleep(1000);
		
		return Dashboard.class;
		
	}
	
	public String getStatusBar(){
		return "20%";
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}
	
	
}
