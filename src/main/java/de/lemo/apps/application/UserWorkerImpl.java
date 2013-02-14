/**
	 * File UserWorkerImpl.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.application;

import javax.servlet.http.HttpServletRequest;
import org.apache.tapestry5.ioc.annotations.Inject;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;

public class UserWorkerImpl implements UserWorker {

	@Inject
	UserDAO userDAO;

	@Inject
	private HttpServletRequest request;

	public User getCurrentUser() {
		final User user = this.userDAO.getUser(this.request.getRemoteUser());
		return user;
	}

}
