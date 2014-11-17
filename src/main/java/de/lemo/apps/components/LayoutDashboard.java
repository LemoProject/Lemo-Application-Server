/**
 * File ./src/main/java/de/lemo/apps/components/LayoutDashboard.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

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
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;

import de.lemo.apps.annotation.Exclude;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.Start;
import de.lemo.apps.pages.data.Dashboard;
import de.lemo.apps.pages.data.Search;

/**
 * Layout for the dashboard
 */
@Exclude(stylesheet = { "core" })
@ImportJQueryUI({ "jquery.ui.mouse", "jquery.ui.slider", "jquery.ui.draggable", "jquery.ui.sortable" })
@Import(
		library = {
				"../js/bootstrap-alert.js",
				"../js/excanvas.js",
				"../js/apps.js",
				"../js/spin.js",
				"../js/d3/libs/d3.js",
				"../js/d3/libs/BoxPlot_Lib.js",
				"../js/d3/libs/nv.d3.js",
				"../js/d3/libs/jquery.tipsy.js",
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
				"../js/csvExport.js",
				"../js/calHeatmap/cal-heatmap.min.js"
		},
		stylesheet = {
				"../css/bootstrap-responsive.css",
				"../css/bootstrap.css",
				"../css/jquery.jqplot.css",
				"../css/nv.d3.css",
				"../css/jquery-ui-1.8.16.bootstrap.css",
				"../css/apps.css",
				"../js/calHeatmap/cal-heatmap.css"
		})
public class LayoutDashboard {

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
	private Search searchPage;

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
	
	@Property
	private String searchStringCourse;

	@Component
	private ActionLink logoutLink;
	
	@Component
	private Form searchForm;
	
	@Inject
	private Logger logger;

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
	
	public Object onSelectedFromSearchCourses() {
		this.searchPage.setSearchQuery(searchStringCourse);
		return this.searchPage;
	}

	@Cached
	public List<Course> getFavoriteCourses() {
		return this.userWorker.getCurrentUser().getFavoriteCourses();
	}

	@Cached
	public List<Course> getAllCourses() {
		return this.userWorker.getCurrentUser().getMyCourses();
	}

}
