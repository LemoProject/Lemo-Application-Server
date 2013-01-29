/**
 * File ./de/lemo/apps/application/AnalysisWorkerImpl.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

/**
 * @author johndoe
 */
public class AnalysisWorkerImpl implements AnalysisWorker {

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private Analysis analysis;

	@Override
	public List<ResourceRequestInfo> usageAnalysisExtended(final Course course, final Date beginDate,
			final Date endDate, final List<EResourceType> resourceTypes) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			List<String> resourceTypesNames = null;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / 1000);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / 1000);
			}

			if ((resourceTypes != null) && (resourceTypes.size() >= 1)) {
				resourceTypesNames = new ArrayList<String>();
				for (int i = 0; i < resourceTypes.size(); i++) {
					resourceTypesNames.add(resourceTypes.get(i).toString());
					this.logger.debug("Resource Typ: " + resourceTypes.get(i).toString());
				}
			}

			new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis");
			final ResultListResourceRequestInfo results = this.analysis.computeCourseActivityExtended(courses,
					beginStamp, endStamp, resourceTypesNames);
			this.logger.debug("Extended Analysis: " + results);
			if ((results != null) && (results.getResourceRequestInfos() != null)
					&& (results.getResourceRequestInfos().size() > 0)) {
				for (int i = 0; i < results.getResourceRequestInfos().size(); i++) {
					results.getResourceRequestInfos().get(i);
				}
				return results.getResourceRequestInfos();
			}
		} else {
			this.logger.debug("Extended Analysis Result is null!");
		}
		return new ArrayList<ResourceRequestInfo>();
	}

	@Override
	public List<ResourceRequestInfo> learningObjectUsage(final Course course, final Date beginDate, final Date endDate,
			final List<Long> selectedUsers, final List<EResourceType> resourceTypes) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			List<String> resourceTypesNames = null;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / 1000);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / 1000);
			}

			if ((resourceTypes != null) && (resourceTypes.size() >= 1)) {
				resourceTypesNames = new ArrayList<String>();
				for (int i = 0; i < resourceTypes.size(); i++) {
					resourceTypesNames.add(resourceTypes.get(i).toString());
					this.logger.debug("Resource Typ: " + resourceTypes.get(i).toString());
				}
			}

			new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis");
			final ResultListResourceRequestInfo results = this.analysis.computeLearningObjectUsage(courses,
					selectedUsers, resourceTypesNames, beginStamp, endStamp);
			this.logger.debug("Extended Analysis: " + results);
			if ((results != null) && (results.getResourceRequestInfos() != null)
					&& (results.getResourceRequestInfos().size() > 0)) {
				for (int i = 0; i < results.getResourceRequestInfos().size(); i++) {
					results.getResourceRequestInfos().get(i);
				}
				return results.getResourceRequestInfos();
			}
		} else {
			this.logger.debug("Extended Analysis Result is null!");
		}
		return new ArrayList<ResourceRequestInfo>();
	}

	@Override
	public ResultListRRITypes usageAnalysisExtendedDetails(final Course course, final Date beginDate,
			final Date endDate, final Integer resolution, final List<EResourceType> resourceTypes) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			List<String> resourceTypesNames = null;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / 1000);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / 1000);
			}

			if ((resourceTypes != null) && (resourceTypes.size() >= 1)) {
				resourceTypesNames = new ArrayList<String>();
				for (int i = 0; i < resourceTypes.size(); i++) {
					resourceTypesNames.add(resourceTypes.get(i).toString());
				}
			}

			new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis Details");
			final ResultListRRITypes results = this.analysis.computeCourseActivityExtendedDetails(courses, beginStamp,
					endStamp, resolution.longValue(), resourceTypesNames);
			this.logger.debug("Extended Analysls Details: " + results);
			if (results != null) {
				if (results.getAssignmentRRI() != null) {
					final List<ResourceRequestInfo> ass = results.getAssignmentRRI();
					if (ass.size() > 0) {
						for (int i = 0; i < ass.size(); i++) {
							final ResourceRequestInfo res = ass.get(i);
							this.logger.debug("ASS ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- " + res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getCoursesRRI() != null) {
					final List<ResourceRequestInfo> cou = results.getCoursesRRI();
					if (cou.size() > 0) {
						for (int i = 0; i < cou.size(); i++) {
							final ResourceRequestInfo res = cou.get(i);
							this.logger.debug("Cou ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- " + res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getResourcesRRI() != null) {
					final List<ResourceRequestInfo> ress = results.getResourcesRRI();
					if (ress.size() > 0) {
						for (int i = 0; i < ress.size(); i++) {
							final ResourceRequestInfo res = ress.get(i);
							this.logger.debug("Res ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- " + res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getResourcesRRI() != null) {
					final List<ResourceRequestInfo> ress = results.getForumRRI();
					if (ress.size() > 0) {
						for (int i = 0; i < ress.size(); i++) {
							final ResourceRequestInfo res = ress.get(i);
							this.logger.debug("Forum ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- " + res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				return results;
			}

		} else {
			this.logger.debug("Extended Analysis Details Result is null!");
		}

		return new ResultListRRITypes();
	}

	@Override
	public List usageAnalysis(final Course course, final Date endDate, final int dateRange,
			final Integer dateMultiplier, final List<EResourceType> resourceTypes) {

		Date beginDate = endDate;

		if ((endDate != null) && (dateMultiplier != null)) {
			final Calendar cal = Calendar.getInstance();
			this.logger.debug("EndDate : " + endDate);
			this.logger.debug("BeginDate before computation: " + beginDate);
			cal.setTime(beginDate);

			cal.add(dateRange, dateMultiplier);
			beginDate = cal.getTime();
			this.logger.debug("BeginDate after computation: " + beginDate);
		}
		return this.usageAnalysis(course, beginDate, endDate, resourceTypes);
	}

	@Override
	public List<List<XYDateDataItem>> usageAnalysis(final Course course, final Date beginDate, final Date endDate,
			final List<EResourceType> resourceTypes) {

		final List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
		final List<XYDateDataItem> list1 = CollectionFactory.newList();
		Integer resolution = 0;

		if ((beginDate != null) && (endDate != null)) {
			resolution = this.dateWorker.daysBetween(beginDate, endDate);
		}

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			List<String> resourceTypesNames = null;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / 1000);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / 1000);
			}

			if ((resourceTypes != null) && (resourceTypes.size() >= 1)) {
				resourceTypesNames = new ArrayList<String>();
				for (int i = 0; i < resourceTypes.size(); i++) {
					resourceTypesNames.add(resourceTypes.get(i).toString());
				}
			}

			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + resolution);
			final HashMap<Long, ResultListLongObject> results = this.analysis.computeCourseActivity(courses, roles,
					null, beginStamp, endStamp, resolution, resourceTypesNames);
			ResultListLongObject uniqueResult = null;

			if (results != null) {
				uniqueResult = results.get(course.getCourseId());
			} else {
				uniqueResult = null;
			}

			final Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);

			// checking if result size matches resolution
			if ((uniqueResult != null) && (uniqueResult.getElements() != null))
			{
				for (int i = 0; i < resolution; i++) {

					beginCal.add(Calendar.DAY_OF_MONTH, 1);
					list1.add(new XYDateDataItem(beginCal.getTime(), uniqueResult.getElements().get(i)));

				}
			} // if there was an error while retrieving result data from dms ... one single null result is added
			else {
				list1.add(new XYDateDataItem(beginCal.getTime(), 0));
			}
		}
		dataList.add(list1);
		return dataList;
	}

}
