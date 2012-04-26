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
	
	public User getCurrentUser(){
		User user = userDAO.getUser(request.getRemoteUser());
		return user;
	}

}
