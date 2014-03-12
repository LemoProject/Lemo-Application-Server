/**
 * File ./src/main/java/de/lemo/apps/pages/account/ServerAdministration.java
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
	 * File ServerAdministration.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.account;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.client.Information;
import de.lemo.apps.restws.client.Initialisation;

/**
 * Represents the information for the dms 
 *
 */
@RequiresAuthentication
@BreadCrumbReset
public class ServerAdministration {

	@Inject
	private Initialisation init;
	
	@Inject
	private Information info;

	@Component(id = "serverform")
	private Form form;

	public String getStartTime() {
		try {
			if (this.init.getStartTime() != null) {
				return this.init.getStartTime().toLocaleString();
			} else {
				return "Offline";
			}
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			return "Offline";
		}
	}

	public Boolean getServerOnline() {
		try {
			if (this.init.defaultConnectionCheck() != null) {
				return true;
			} else {
				return false;
			}
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public String getDMSVersion(){
		try {
			return info.getDMSVersion();
		} catch (RestServiceCommunicationException e) {
			return "Information not available!";
		}
	}
	
	
	public String getDMSDBVersion(){
		try {
			return info.getDMSDBVersion();
		} catch (RestServiceCommunicationException e) {
			return "Information not available!";
		}
	}
	
	
	

}
