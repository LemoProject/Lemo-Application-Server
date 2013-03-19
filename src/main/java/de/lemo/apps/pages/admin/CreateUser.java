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
import de.lemo.apps.pages.data.DashboardAdmin;


/**
 * @author Andreas Pursian
 *
 */
public class CreateUser {
	
	@Component(id = "accountform")
	private Form form;
	
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
	
	Object onSelectedFromDashboard() {
		this.userDAO.save(this.userItem);
    	return DashboardAdmin.class;
	}
	
	Object onSelectedFromNew() {
		this.userDAO.save(this.userItem);
    	return this;
}

}
