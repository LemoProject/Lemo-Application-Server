package de.lemo.apps.pages.account;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;

import de.lemo.apps.restws.client.Initialisation;

@RequiresAuthentication
@BreadCrumb(titleKey="myAccountTitle")
public class ServerAdministration {
	
	@Inject
	private Initialisation init;
	
	@Component(id = "serverform")
	private Form form;
	
	public String getStartTime(){
		if(init.getStartTime()!=null) return init.getStartTime().toLocaleString();
		else return "Offline";
	}
	
	public Boolean getServerOnline(){
		if(init.getStartTime()!=null) return true;
		else return false;
	}

}
