package de.lemo.apps.components;

import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.pages.Start;
import de.lemo.apps.pages.data.Dashboard;


@ImportJQueryUI({"jquery.ui.core",  "jquery.ui.mouse", "jquery.ui.draggable", "jquery.ui.sortable"})
@Import(library={"../js/bootstrap-alert.js",
				"../js/dashboard.js",
				"../js/apps.js",
		 		"../js/bootstrap-transition.js",
		 		"../js/bootstrap-modal.js",
		 		"../js/bootstrap-dropdown.js",
		 		"../js/bootstrap-scrollspy.js",
		 		"../js/bootstrap-tab.js",
		 		"../js/bootstrap-tooltip.js",
		 		"../js/bootstrap-popover.js",
		 		"../js/bootstrap-button.js",
		 		"../js/bootstrap-collapse.js",
		 		"../js/bootstrap-carousel.js",
		 		"../js/bootstrap-typeahead.js"
		 },
			stylesheet={"../css/bootstrap-responsive.css",
						"../css/bootstrap.css",
						"../css/jquery-ui.css",
						"../css/apps.css"})

public class LayoutDashboard {
	
	/**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Inject @Path("../js/dashboard.js")
    private Asset dashboardJS;
    
    @Inject
    @Path("../images/icons/glyphicons_019_cogwheel_white.png")
    @Property
    private Asset wheel;
    
    @Environmental
    private JavaScriptSupport javaScriptSupport;
    
    @Property
    private String pageName;
	
	@InjectPage
	private Start startpage;
	
	@InjectPage
	@Property
	private Dashboard dashboard;
	
	@Inject
	private SecurityService securityService;
	
	@Inject
	private Request request;
	
	
	@Component
	private ActionLink logoutLink;
	
	@Inject
    private PersistentLocale persistentLocaleService;

    void onActionFromDeLocaleLink()
    {
        persistentLocaleService.set(Locale.GERMAN);
    }

    void onActionFromEnLocaleLink()
    {
        persistentLocaleService.set(Locale.ENGLISH);
    }
    
    void setupRender()
    {
      javaScriptSupport.importJavaScriptLibrary(dashboardJS);
     // javaScriptSupport.addScript("InitDashboard();+" +
     // 		"alert('Hello!');");
    }
    
    public String getGermanLocale(){
    	Locale currentLocale = persistentLocaleService.get();
    	if (currentLocale!=null && currentLocale.equals(Locale.GERMAN))
    		return "active";
    	else return "inactive";
    }
    
    public String getEnglishLocale(){
    	Locale currentLocale = persistentLocaleService.get();
    	if (currentLocale==null)
    		return "active";
    	if (currentLocale!=null && currentLocale.equals(Locale.ENGLISH))
    		return "active";
    	else return "inactive";
    }
	
	public Object onActionFromLogoutLink() {
        this.securityService.getSubject().logout();
        try {
                final Session session = this.request.getSession(false);
                if (session != null && !session.isInvalidated()) {
                        session.invalidate();
                }
        } catch (final IllegalStateException ex) {
                
        }
        return startpage;
} 


}
