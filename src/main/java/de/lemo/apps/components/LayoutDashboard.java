package de.lemo.apps.components;

import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.annotation.Exclude;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.Start;
import de.lemo.apps.pages.data.Dashboard;


@Exclude(stylesheet={"core"})  //remove the Tapestry css
@ImportJQueryUI({"jquery.ui.core", "jquery.ui.slider", "jquery.ui.mouse", "jquery.ui.draggable", "jquery.ui.sortable"})
@Import(library={"../js/bootstrap-alert.js",
				"../js/excanvas.js",
				"../js/apps.js",
				"../js/d3/d3.v2.js",
				"../js/d3/d3_BoxPlot_Lib.js",
				"../js/d3/nv.d3.js",
				"../js/d3/jquery.tipsy.js",
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
						"../css/jquery.jqplot.css",
						"../css/nv.d3.css",
						//"../css/jquery-ui-1.8.16.bootstrap.css",
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
    
    @Inject
    private CourseDAO courseDAO;
    
    @Inject
    private UserWorker userWorker;
    
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
	
	@Property
	private Course favoriteCourse;
	
	@Property
	private Course allCourse;
	
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
      //javaScriptSupport.importJavaScriptLibrary(dashboardJS);
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
	
	@Cached
	public List<Course> getFavoriteCourses(){
		return courseDAO.findFavoritesByOwner(userWorker.getCurrentUser());
	}
	
	@Cached
	public List<Course> getAllCourses(){
		return courseDAO.findAllByOwner(userWorker.getCurrentUser());
	}


}
