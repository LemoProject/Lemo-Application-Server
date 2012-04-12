package de.lemo.apps.pages.account;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="myAccountTitle")
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
	
	public String getUserName(){
		return request.getRemoteUser();
		
	}
	
	public User getMyAccount(){
		User user = ud.getUser(getUserName());
		if (user !=null)  {
			myAccount = user;
			return myAccount;
		}
			else return new User(); //TODO valid null check
	}
	
	Object onSuccess(){
		ud.update(myAccount);
		return this;
	}

}
