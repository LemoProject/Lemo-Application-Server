/**
	 * File UserDAO.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.integration;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.User;

public interface UserDAO {

	boolean doExist(User user);

	User getUser(String username);
	
	@CommitAfter
	void toggleFavoriteCourse(final Long courseId, final Long userId);

	@CommitAfter
	void update(User user);

	@CommitAfter
	void save(User user);

	User login(String username, String password);

}
