package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import de.lemo.apps.entities.User;
import de.lemo.apps.integration.UserDAO;
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
    
	@Inject
	private Messages messages;
    
	void onPrepareForRender(){
		username = userItem.getUsername();
		fullname = userItem.getFullname();
		email = userItem.getEmail();
		accountLocked = userItem.isAccountLocked();
		credentialsExpired = userItem.isCredentialsExpired();		
	}
	
    void onValidateFromAccountform() {   	
    
        if (password == null || !password.equals(passwordConfirmation)) {
            form.recordError(passwordField, "Password doesnt match.");
        }else{
    		userItem.setFullname(fullname);
    		userItem.setEmail(email);
    		userItem.setAccountLocked(accountLocked);
    		userItem.setCredentialsExpired(credentialsExpired);      
    		userItem.setPassword(password);
        }
    }
	public String getDeleteString() {
		return messages.format("sureToDelete", userItem.getFullname());
	}
	
	Object onActionFromDelete() {
	    this.userDAO.remove(this.userItem);
	    return Dashboard.class;
	}
}
