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
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;

/**
 * Shows account information for the user
 * 
 */
@RequiresAuthentication
@BreadCrumbReset
public class MyAccount {

    @Inject
    private Messages messages;
    
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

    @Property
    @Persist("flash")
    private String infoMessage;

    public String getUserName() {
        return this.request.getRemoteUser();

    }

    public User getMyAccount() {
        final User user = this.ud.getUser(this.getUserName());
        if(user != null) {
            this.myAccount = user;
            return this.myAccount;
        } else {
            // TODO valid null check
            return new User();
        }
    }

    Object onSuccess() {
        this.ud.update(this.myAccount);
        infoMessage = messages.get("accountUpdateSuccess");
        return this;
    }

}
