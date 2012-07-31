package de.lemo.apps.components;

import java.util.Locale;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.pages.Start;

@Import(library={"../js/bootstrap-alert.js",
			//	"../js/jquery-1.5.2.min.js",
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
						"../css/bootstrap.css"})
public class Layout {
	
	/**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

	
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

}