/**
	 * File Dashbaord.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.UsageStatisticsContainer;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

/**
 * implementation of the dashboard view
 */
@RequiresAuthentication
@BreadCrumb(titleKey = "dashboardTitle")
@BreadCrumbReset
public class Dashboard {

	
	@Component(parameters = { "dataItems=usageAnalysisWidget1" })
	private JqPlotLine chart1;

	@Component(parameters = { "dataItems=usageAnalysisWidget2" })
	private JqPlotLine chart2;

	@Component(parameters = { "dataItems=usageAnalysisWidget3" })
	private JqPlotLine chart3;

	@Inject
	private Logger logger;

	@Inject
	private Locale currentLocale;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private StatisticWorker statisticWorker;

	@Inject
	private AnalysisWorker analysisWorker;

	@Inject
	@Path("../../images/icons/glyphicons_019_cogwheel.png")
	@Property
	private Asset wheel;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserWorker userWorker;

	@Property
	private BreadCrumbInfo breadCrumb;

	@Inject
	private SecurityService securityService;

	@Inject
	private ApplicationStateManager applicationStateManager;

	@Inject
	private Initialisation init;

	@Inject
	private Analysis analysis;

	@Component(id = "courseForm1")
	private Form courseForm1;

	@Component(id = "courseForm2")
	private Form courseForm2;

	@Component(id = "courseForm3")
	private Form courseForm3;

	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel1;

	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel2;

	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel3;

	@Property
	private Long widgetCourse1Id;

	@Property
	private Long widgetCourse2Id;

	@Property
	private Long widgetCourse3Id;

	@Persist("Flash")
	private UsageStatisticsContainer usageStatistics;

	private Course widgetCourse1;

	private Course widgetCourse2;

	private Course widgetCourse3;

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.courseModel1 = new CourseIdSelectModel(courses);
		this.courseModel2 = new CourseIdSelectModel(courses);
		this.courseModel3 = new CourseIdSelectModel(courses);

		this.widgetCourse1 = this.courseDAO.getCourse(this.userWorker.getCurrentUser().getWidget1());
		this.widgetCourse2 = this.courseDAO.getCourse(this.userWorker.getCurrentUser().getWidget2());
		this.widgetCourse3 = this.courseDAO.getCourse(this.userWorker.getCurrentUser().getWidget3());
		if (this.widgetCourse1 != null) {
			this.widgetCourse1Id = this.widgetCourse1.getId();
		}
		if (this.widgetCourse2 != null) {
			this.widgetCourse2Id = this.widgetCourse2.getId();
		}
		if (this.widgetCourse3 != null) {
			this.widgetCourse3Id = this.widgetCourse3.getId();
		}
	}

	@Property
	@Persist
	private Integer count;

	public List<Course> getMyCourses() {
		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
	}

	void onSuccessFromCourseForm1() {
		this.logger.debug("Course ID:" + this.widgetCourse1);
		final User user = this.userWorker.getCurrentUser();
		user.setWidget1(this.widgetCourse1Id);
		this.userDAO.update(user);
	}

	void onSuccessFromCourseForm2() {
		this.logger.debug("Course ID:" + this.widgetCourse2);
		final User user = this.userWorker.getCurrentUser();
		user.setWidget2(this.widgetCourse2Id);
		this.userDAO.update(user);
	}

	void onSuccessFromCourseForm3() {
		this.logger.debug("Course ID:" + this.widgetCourse2);
		final User user = this.userWorker.getCurrentUser();
		user.setWidget3(this.widgetCourse3Id);
		this.userDAO.update(user);
	}

	@Cached
	public List getUsageAnalysisWidget3() {
		final Long id = this.userWorker.getCurrentUser().getWidget3();
		return this.getUsageAnalysis(id);
	}

	@Cached
	public List getUsageAnalysisWidget2() {
		final Long id = this.userWorker.getCurrentUser().getWidget2();
		return this.getUsageAnalysis(id);
	}

	@Cached
	public List getUsageAnalysisWidget1() {
		final Long id = this.userWorker.getCurrentUser().getWidget1();
		return this.getUsageAnalysis(id);
	}

	public List getUsageAnalysis(final Long courseId) {
		final Course course = this.courseDAO.getCourse(courseId);
		if (course != null) {
			final Date endDate = course.getLastRequestDate();
			return this.analysisWorker.usageAnalysis(course, endDate, Calendar.MONTH, -1, null);
		}
		return new ArrayList();
	}

	// TODO reduce recomputation of analysis results per widget - method should provide a container class for all
	// necessary stats data
	public UsageStatisticsContainer getUsageStatistics(final Long courseId) {
		this.logger.debug("######### Getting stats info for course Id: " + courseId);
		if ((this.usageStatistics != null) && (this.usageStatistics.getCourseId() == courseId)) {
			return this.usageStatistics;
		}
		final List<List<XYDateDataItem>> dataItemList = this.getUsageAnalysis(courseId);
		this.usageStatistics = new UsageStatisticsContainer(courseId);
		this.usageStatistics.setAverageRequest(this.statisticWorker.getAverageRequest(dataItemList));
		this.usageStatistics.setMaxRequest(this.statisticWorker.getMaxRequest(dataItemList));
		this.usageStatistics.setMaxRequestDate(this.statisticWorker.getMaxRequestDate(dataItemList));
		this.usageStatistics.setOverallRequest(this.statisticWorker.getOverallRequest(dataItemList));
		return this.usageStatistics;
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

	public String getWidgetName(final int widget) {
		switch (widget) {
			case 1:
				final Long id1 = this.userWorker.getCurrentUser().getWidget1();
				if ((id1 != null) && (this.getWidgetCourse1() != null)) {
					return this.getWidgetCourse1().getCourseDescription();
				}
			case 2:
				final Long id2 = this.userWorker.getCurrentUser().getWidget2();
				if ((id2 != null) && (this.getWidgetCourse2() != null)) {
					return this.getWidgetCourse2().getCourseDescription();
				}
			case 3:
				final Long id3 = this.userWorker.getCurrentUser().getWidget3();
				if ((id3 != null) && (this.getWidgetCourse3() != null)) {
					return this.getWidgetCourse3().getCourseDescription();
				}
			default:
				return "Widget unused";
		}
	}

	@Cached
	public Course getWidgetCourse1() {
		final Long id1 = this.userWorker.getCurrentUser().getWidget1();
		return this.courseDAO.getCourse(id1);
	}

	@Cached
	public Course getWidgetCourse2() {
		final Long id2 = this.userWorker.getCurrentUser().getWidget2();
		return this.courseDAO.getCourse(id2);
	}

	@Cached
	public Course getWidgetCourse3() {
		final Long id3 = this.userWorker.getCurrentUser().getWidget3();
		return this.courseDAO.getCourse(id3);
	}

	public String getLocalizedDate(final Date date) {
		return this.dateWorker.getLocalizedDate(date, this.currentLocale);
	}

	public String getLocalizedDateTime(final Date date) {
		return this.dateWorker.getLocalizedDateTime(date, this.currentLocale);
	}

}
