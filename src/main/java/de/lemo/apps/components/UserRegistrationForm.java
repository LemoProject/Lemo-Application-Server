package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.admin.DashboardAdmin;
import de.lemo.apps.pages.data.Dashboard;

public class UserRegistrationForm {
	
	@Component(id = "accountform")
	private Form form;

	@Property
	private String username;
	@Property
	private String fullname;
	@Property
	private String email;	
	@Property
	private String password;
	@Property
	private String passwordConfirmation;
	@Property
	private boolean accountLocked;
	@Property
	private boolean credentialsExpired;

    @Component(id = "password")
    private PasswordField passwordField;

    @Component(id = "passwordConfirmation")
    private PasswordField passwordConfirmationField;
    
    @Parameter(required=true)
    @Property
	private User userItem;
    
	@Inject
	private UserDAO userDAO;
    
    @Parameter(required=true)
    @Property
	private boolean authorization;
    
    @Parameter(required=true)
    @Property
	private boolean create;
    
	@Inject
	private Messages messages;
	
    @Property
    @Persist("flash")
    private String infoMessage;
    
	void onPrepareForRender(){
		username = userItem.getUsername();
		fullname = userItem.getFullname();
		email = userItem.getEmail();		
	}
	
    void onValidateFromAccountform() {   	
		userItem.setFullname(fullname);
		if(create){
			userItem.setUsername(username);
		}
		userItem.setEmail(email);
		if(password != null){
	        if (!password.equals(passwordConfirmation)) {
	            form.recordError(passwordField, "Password doesnt match.");
	        }else{     
	    		userItem.setPassword(password);
	        }			
		}
    }
    
    void onSuccess(){
    	if(!create){
    		infoMessage = messages.get("accountUpdateSuccess");
    	}        
    }
    
	public String getDeleteString() {
		return messages.format("sureToDelete", userItem.getFullname());
	}
	
	Object onActionFromDelete() {
	    this.userDAO.remove(this.userItem);
	    return Dashboard.class;
	}
	
	Object onActionFromCancel() {
	    if(authorization){
	    	return DashboardAdmin.class;
	    }else{
	    	return Dashboard.class;
	    }	    
	}
}
