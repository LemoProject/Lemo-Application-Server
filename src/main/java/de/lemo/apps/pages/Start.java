/**
	 * File Start.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages;

import java.util.Date;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SymbolConstants;
import org.slf4j.Logger;
import org.tynamo.security.services.PageService;
import org.tynamo.security.services.SecurityService;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.data.Register;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLongObject;

/**
 * Start page of application test.
 */
public class Start {

	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@Inject
	@Path("../images/Nutzungsanalyse.png")
	@Property
	private Asset carusselOne;

	@Inject
	@Path("../images/Pfadvisualisierung.png")
	@Property
	private Asset carusselTwo;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private Messages messages;
	
	@Inject
	private Initialisation init;
	
	@Inject UserDAO userDAO;
	
	@InjectPage
	private Register registerPage;

	@Inject
	private SecurityService securityService;

	@SuppressWarnings("deprecation")
	@Inject
	private PageService pageService;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String username;

	@Property
	private String password;

	@AfterRender
	public void afterRender() {
		this.javaScriptSupport.addScript(
					"$('.carousel').carousel({ " +
							"interval: 8000" +
							"})"
				);
	}

	@Component
	private Form loginForm;

	@Persist
	@Property
	private int clickCount;

	@Inject
	private AlertManager alertManager;

	public Date getCurrentTime() {
		return new Date();
	}

	public Object onSubmitFromLoginForm() {

		try {
			final Subject currentUser = this.securityService.getSubject();

			if (currentUser == null) {
				throw new IllegalStateException("Error during login. Can't obtain user from security service.");
			}

			final UsernamePasswordToken token = new UsernamePasswordToken(this.username, this.password);
			this.logger.info("Prepare Logintoken. Username: " + this.username);
			currentUser.login(token);

		} catch (AuthenticationException ex) {
			this.logger.info("Login unsuccessful.");
			this.loginForm.recordError(this.messages.get("error.login"));
			this.alertManager.info("Login or password not correct.");
			
			/*
			 * If user authentification fails we will start a lookup to check whether this username is known by the dms
			 */
			if (!userDAO.doExist(this.username)) {
				ResultListLongObject result = null;
				try {
					
						result = init.identifyUserName(this.username);
				
				} catch (RestServiceCommunicationException e) {
					logger.error(e.getMessage());
				}
				
				if (result != null && result.getElements()!=null && result.getElements().size() > 0) {
					
					Long dmsUserId = result.getElements().get(result.getElements().size()-1); 
			
					logger.debug("Corresponding LeMo user ID : "+dmsUserId);
					
					registerPage.setDmsUserId(dmsUserId);
					registerPage.setDmsUserName(this.username);
			
					return registerPage;
					
				} else {
					return Start.class;
				}
			} else return Start.class;
		} 

		return this.pageService.getSuccessPage();
	}

}
