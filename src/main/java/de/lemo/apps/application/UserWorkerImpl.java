/**
 * File ./de/lemo/apps/application/UserWorkerImpl.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
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

	@Override
	public User getCurrentUser() {
		final User user = this.userDAO.getUser(this.request.getRemoteUser());
		return user;
	}

}
