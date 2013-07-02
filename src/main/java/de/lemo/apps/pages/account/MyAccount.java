/**
 * File ./src/main/java/de/lemo/apps/pages/account/MyAccount.java
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
	 * File MyAccount.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.account;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;

/**
 * Shows account information for the user
 *
 */
@RequiresAuthentication
@BreadCrumbReset
public class MyAccount {

	@Property
	private BreadCrumbInfo breadCrumb;

	@Component(id = "accountform")
	private Form form;

	@Inject
	private HttpServletRequest request;

	@Inject
	UserDAO ud;

	@Persist("Flash")
	private User myAccount;

	public String getUserName() {
		return this.request.getRemoteUser();

	}

	public User getMyAccount() {
		final User user = this.ud.getUser(this.getUserName());
		if (user != null) {
			this.myAccount = user;
			return this.myAccount;
		} else {
			// TODO valid null check
			return new User();
		}
	}

	Object onSuccess() {
		this.ud.update(this.myAccount);
		return this;
	}

}
