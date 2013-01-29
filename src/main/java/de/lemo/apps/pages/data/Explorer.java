/**
 * File ./de/lemo/apps/pages/data/Explorer.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;

@RequiresAuthentication
@BreadCrumb(titleKey = "myCourses")
public class Explorer {

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private JavaScriptSupport jsSupport;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private StatisticWorker statisticWorker;

	@Inject
	private AnalysisWorker analysisWorker;

	@Inject
	private Locale currentLocale;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserWorker userWorker;

	@Property
	private Course course;

	@Property
	@Persist
	private Course initCourse;

	@InjectComponent
	private Zone courseZone;

	/**
	 * Preparing the Grid Model for the
	 */
	@Property(write = false)
	@Retain
	private BeanModel coursesGridModel;
	{
		this.coursesGridModel = this.beanModelSource.createDisplayModel(Course.class,
				this.componentResources.getMessages());
		this.coursesGridModel.include("coursename", "lastRequestDate");
		// coursesGridModel.add("compare",null);

	}

	public Object onActivate(final Course course) {
		if (course != null) {
			this.initCourse = course;
		}
		return true;
	}

	/**
	 * Inserting a some js to deal with tab changes (e.g. Summary, Last Month, etc.) after the page rendering is
	 * finished
	 */
	@AfterRender
	void afterRender() {
		this.jsSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function() { "
				+ " $('.tabs a:first').tab('show');"
				+ "});", this.courseZone.getClientId());
	}

	@Cached
	public List<Course> getCourses() {
		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
	}

	public Course getCurrentCourse() {
		if (this.initCourse != null) {
			return this.initCourse;
		} else {
			return this.getCourses().get(0);
		}
	}

	// TODO Fix Problem with jqplot and zone updates
	Object onActionFromShow(final Long id)
	{
		this.initCourse = this.courseDAO.getCourse(id);
		// TODO Till problem with chart painting and zone updates is not solved, incremental page rendering is off
		// return new MultiZoneUpdate("courseZone", courseZone.getBody()).add("courseLastMonthZone",
		// courseLastMonthZone.getBody())
		// .add("analysisSelectZone", analysisSelectZone.getBody());
		// .add("courseVisZone", courseVisZone.getBody());
		return this;
	}

	Object onActionFromFavorite(final Long id) {
		this.courseDAO.toggleFavorite(id);
		this.initCourse = this.courseDAO.getCourse(id);
		return this;
	}

	public String getFirstRequestDate() {
		return this.dateWorker.getLocalizedDateTime(this.getCurrentCourse().getFirstRequestDate(), this.currentLocale);
	}

	public String getLastRequestDate() {
		return this.dateWorker.getLocalizedDateTime(this.getCurrentCourse().getLastRequestDate(), this.currentLocale);
	}

	@Cached
	public List getUsageAnalysisLastMonth() {

		final Date endDate = this.getCurrentCourse().getLastRequestDate();
		return this.analysisWorker.usageAnalysis(this.getCurrentCourse(), endDate, Calendar.MONTH, -1, null);
	}

	// TODO Scheinbar greift ein Template Property noch auf diese Funktion zu ... auch wen dem nicht so sein sollte ->
	// die korrekte Methode lautet ...LastMonth
	@Cached
	public List getUsageAnalysis() {
		final Date endDate = this.getCurrentCourse().getLastRequestDate();
		return this.analysisWorker.usageAnalysis(this.getCurrentCourse(), endDate, Calendar.MONTH, -1, null);
	}

	@Cached
	public List getUsageAnalysisOverall() {

		final Date endDate = this.getCurrentCourse().getLastRequestDate();
		final Date beginDate = this.getCurrentCourse().getFirstRequestDate();
		return this.analysisWorker.usageAnalysis(this.course, beginDate, endDate, null);
	}

	public Long getAverageRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getAverageRequest(dataItemList);
	}

	public Long getOverallRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getOverallRequest(dataItemList);
	}

	public Long getMaxRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getMaxRequest(dataItemList);
	}

	public Date getMaxRequestDate(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getMaxRequestDate(dataItemList);
	}

	public String getLocalizedDate(final Date date) {
		return this.dateWorker.getLocalizedDate(date, this.currentLocale);
	}

	public String getLocalizedDateTime(final Date date) {
		return this.dateWorker.getLocalizedDateTime(date, this.currentLocale);
	}

}
