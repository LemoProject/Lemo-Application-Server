/**
 * File ./src/main/java/de/lemo/apps/application/UserWorkerImpl.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
	 * File UserWorkerImpl.java
	 *
	 * Date Feb 14, 2013 
	 *
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
		return this.userDAO.getUser(this.request.getRemoteUser());
	}

}
