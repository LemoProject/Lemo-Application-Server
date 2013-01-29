/**
 * File ./de/lemo/apps/components/LayoutAccount.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.components;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.tynamo.security.services.SecurityService;
import de.lemo.apps.annotation.Exclude;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.Start;

@Exclude(stylesheet = { "core" })
// remove the Tapestry css
@ImportJQueryUI({ "jquery.ui.core", "jquery.ui.mouse", "jquery.ui.draggable", "jquery.ui.sortable" })
@Import(library = { "../js/bootstrap-alert.js",
		"../js/excanvas.js",
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
		stylesheet = { "../css/bootstrap-responsive.css",
				"../css/bootstrap.css",
				"../css/jquery.jqplot.css",
				// "../css/jquery-ui-1.8.16.bootstrap.css",
				"../css/apps.css" })
public class LayoutAccount {

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String subpage;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserWorker userWorker;

	@InjectPage
	private Start startpage;

	@Inject
	private SecurityService securityService;

	@Inject
	private Request request;

	@Inject
	private HttpServletRequest httpRequest;

	@Inject
	private PersistentLocale persistentLocaleService;

	void onActionFromDeLocaleLink()
	{
		this.persistentLocaleService.set(Locale.GERMAN);
	}

	void onActionFromEnLocaleLink()
	{
		this.persistentLocaleService.set(Locale.ENGLISH);
	}

	void setupRender()
	{
		// javaScriptSupport.importJavaScriptLibrary(dashboardJS);
		// javaScriptSupport.addScript("InitDashboard();+" +
		// "alert('Hello!');");
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

	@Cached
	public List<Course> getFavoriteCourses() {
		return this.courseDAO.findFavoritesByOwner(this.userWorker.getCurrentUser());
	}

	@Cached
	public List<Course> getAllCourses() {
		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
	}

	public String getUserName() {
		return this.httpRequest.getRemoteUser();

	}

	public Integer getCourseAmount() {
		final List<Course> userCourses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
		if (userCourses != null) {
			return userCourses.size();
		} else {
			return 0;
		}
	}

	public String getActivePage(final String navName) {
		if ((this.subpage != null) && this.subpage.equals(navName)) {
			return "active";
		} else {
			return "not-active";
		}
	}

}
