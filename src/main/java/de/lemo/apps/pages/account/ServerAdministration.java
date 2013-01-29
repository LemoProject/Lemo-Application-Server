/**
 * File ./de/lemo/apps/pages/account/ServerAdministration.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.account;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ioc.annotations.Inject;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.restws.client.Initialisation;

@RequiresAuthentication
@BreadCrumbReset
public class ServerAdministration {

	@Inject
	private Initialisation init;

	public String getStartTime() {
		if (this.init.getStartTime() != null) {
			return this.init.getStartTime().toLocaleString();
		} else {
			return "Offline";
		}
	}

	public Boolean getServerOnline() {
		if (this.init.getStartTime() != null) {
			return true;
		} else {
			return false;
		}
	}

}
