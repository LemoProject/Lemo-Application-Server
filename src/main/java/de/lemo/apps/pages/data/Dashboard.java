/**
 * File ./de/lemo/apps/pages/data/Dashboard.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.UsageStatisticsContainer;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey = "dashboardTitle")
@BreadCrumbReset
public class Dashboard {

	// @Component(parameters = {"dataItems=testPieData"})
	// private JqPlotPie chart4 ;

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
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserWorker userWorker;

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

	// @SuppressWarnings("unused")
	// @SessionState(create = false)
	// @Property
	// private CurrentUser currentUser;
	//
	//
	// void onActivate() {
	// if (securityService.getSubject().isAuthenticated() && !applicationStateManager.exists(CurrentUser.class)) {
	// CurrentUser currentUser = applicationStateManager.get(CurrentUser.class);
	// currentUser.merge(securityService.getSubject().getPrincipal());
	// }
	//
	// }

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
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

	// @Cached
	// public List getTestPieData()
	// {
	// List<List<TextValueDataItem>> dataList = CollectionFactory.newList();
	// List<TextValueDataItem> list1 = CollectionFactory.newList();
	//
	// list1.add(new TextValueDataItem("Mozilla Firefox",12));
	// list1.add(new TextValueDataItem("Google Chrome", 9));
	// list1.add(new TextValueDataItem("Safari (Webkit)",14));
	// list1.add(new TextValueDataItem("Internet Explorer", 16));
	// list1.add(new TextValueDataItem("Opera", 2));
	//
	//
	// dataList.add(list1);
	//
	// return dataList;
	// }

	public List<Course> getMyCourses() {
		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
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
	public List<List<XYDateDataItem>> getUsageAnalysisWidget3() {
		final Long id = this.userWorker.getCurrentUser().getWidget3();
		return this.getUsageAnalysis(id);
	}

	@Cached
	public List<List<XYDateDataItem>> getUsageAnalysisWidget2() {
		final Long id = this.userWorker.getCurrentUser().getWidget2();
		return this.getUsageAnalysis(id);
	}

	@Cached
	public List<List<XYDateDataItem>> getUsageAnalysisWidget1() {
		final Long id = this.userWorker.getCurrentUser().getWidget1();
		return this.getUsageAnalysis(id);
	}

	public List<List<XYDateDataItem>> getUsageAnalysis(final Long courseId) {
		final Course course = this.courseDAO.getCourse(courseId);
		if (course != null) {
			final Date endDate = course.getLastRequestDate();
			return this.analysisWorker.usageAnalysis(course, endDate, Calendar.MONTH, -1, null);
		}
		return new ArrayList<List<XYDateDataItem>>();
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
				if ((id1 != null) && (this.getWidgetCourse1() != null))
				{
					return this.getWidgetCourse1().getCourseDescription(); // courseDAO.getCourse(id1).getCourseDescription();
				}
			case 2:
				final Long id2 = this.userWorker.getCurrentUser().getWidget2();
				if ((id2 != null) && (this.getWidgetCourse2() != null))
				{
					return this.getWidgetCourse2().getCourseDescription(); // courseDAO.getCourse(id2).getCourseDescription();
				}
			case 3:
				final Long id3 = this.userWorker.getCurrentUser().getWidget3();
				if ((id3 != null) && (this.getWidgetCourse3() != null))
				{
					return this.getWidgetCourse3().getCourseDescription(); // courseDAO.getCourse(id3).getCourseDescription();
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
