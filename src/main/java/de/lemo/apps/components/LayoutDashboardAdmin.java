package de.lemo.apps.components;

import java.util.List;
import java.util.Locale;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Cached;
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
import de.lemo.apps.annotation.Exclude;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.Start;
import de.lemo.apps.pages.data.Dashboard;

/**
 * Layout for the admin dashboard
 */
@Exclude(stylesheet = { "core" })
// remove the Tapestry css
@ImportJQueryUI( { "jquery.ui.mouse", "jquery.ui.slider", "jquery.ui.draggable", "jquery.ui.sortable" })
@Import(library = { "../js/bootstrap-alert.js",

				"../js/excanvas.js",
				"../js/apps.js",
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
				"../js/bootstrap-typeahead.js",
				"../js/csvExport.js"},

stylesheet = { "../css/bootstrap-responsive.css",
						"../css/bootstrap.css",
						"../css/jquery.jqplot.css",
						// "../css/jquery-ui-1.8.16.bootstrap.css",
		"../css/apps.css" })

public class LayoutDashboardAdmin {

	/**
	 * The page title, for the <title> element and the <h1>element.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Inject
	@Path("../js/dashboard.js")
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

	void onActionFromDeLocaleLink() {
		this.persistentLocaleService.set(Locale.GERMAN);
	}

	void onActionFromEnLocaleLink() {
		this.persistentLocaleService.set(Locale.ENGLISH);
	}

	public String getGermanLocale() {
		final Locale currentLocale = this.persistentLocaleService.get();
		if ((currentLocale != null) && currentLocale.equals(Locale.GERMAN)) {
			return "active";
		} else {
			return "inactive";
		}
	}

	public String getEnglishLocale() {
		final Locale currentLocale = this.persistentLocaleService.get();
		if (currentLocale == null) {
			return "active";
		}
		if ((currentLocale != null) && currentLocale.equals(Locale.ENGLISH)) {
			return "active";
		} else {
			return "inactive";
		}
	}

	public Object onActionFromLogoutLink() {
		this.securityService.getSubject().logout();
		try {
			final Session session = this.request.getSession(false);
			if ((session != null) && !session.isInvalidated()) {
				session.invalidate();
			}
		} catch (final IllegalStateException ex) {

		}
		return this.startpage;
	}

}
