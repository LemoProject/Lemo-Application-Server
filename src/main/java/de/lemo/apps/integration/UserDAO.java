package de.lemo.apps.integration;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import de.lemo.apps.entities.User;

public interface UserDAO {

	public boolean doExist(User user);

	public User getUser(String username);
	
	@CommitAfter
	public void toggleFavoriteCourse(final Long courseId, final Long userId);

	@CommitAfter
	public void update(User user);

	@CommitAfter
	public void save(User user);

	public User login(String username, String password);

}
