/**
 * File ./src/main/java/de/lemo/apps/pages/admin/CreateUser.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
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
 * File CreateUser.java
 * Date Mar 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.admin;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;


/**
 * @author Andreas Pursian
 *
 */
public class CreateUser {
	
	
	@Property
	private BreadCrumbInfo breadCrumb;

	@Inject
	UserDAO userDAO;
	
	@Property
	@Persist
	private User userItem;

	
    Boolean onActivate(){
    	this.userItem = new User();
    	return false;
    }	
	
    public User getUser(){
		return this.userItem;
	}
	
	
	Object onSuccess() {
		this.userDAO.save(this.userItem);
		return DashboardAdmin.class;
	}
}
