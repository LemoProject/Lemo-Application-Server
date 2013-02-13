package de.lemo.apps.pages.account;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.client.Initialisation;

@RequiresAuthentication
@BreadCrumbReset
public class ServerAdministration {

	@Inject
	private Initialisation init;

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
			if (this.init.getStartTime() != null) {
				return true;
			} else {
				return false;
			}
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

}
