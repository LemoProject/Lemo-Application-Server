package de.lemo.apps.services.internal;




import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.services.ApplicationInitializer;
import org.apache.tapestry5.services.ApplicationInitializerFilter;
import org.apache.tapestry5.services.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.lemo.apps.entities.User;


public class AppsInitializerImpl implements ApplicationInitializerFilter {
	private Session session;

	private boolean runFirst = false;
	private boolean insertTestData = true;
	
	private AppsInitializerImpl(Session session){
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	public void initializeApplication(Context context, ApplicationInitializer applicationInitializer) {
		
		
		
		if (insertTestData) {
			
			Transaction t = session.getTransaction();
			t.begin();
		
			List<Long> courses = new ArrayList<Long>();
			courses.add(2100L);
			courses.add(2200L);
			
			final User user = new User();
			user.setEmail("johndoe@example.com");
			user.setFullname("John Doe");
			user.setPassword("john");
			user.setUsername("johndoe");
			user.setMyCourses(courses);
			
			final User user1 = new User();
			user1.setEmail("petersmith@example.com");
			user1.setFullname("Peter Smith");
			user1.setPassword("peter");
			user1.setUsername("petersmith");
			
			session.save(user);
			session.save(user1);
		
			t.commit();
		}
	}

}
