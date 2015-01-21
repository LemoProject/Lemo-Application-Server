/**
 * File ./src/main/java/de/lemo/apps/application/AnalysisWorkerImpl.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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

/**
 * File AnalysisWorkerImpl.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.slf4j.Logger;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

public class AnalysisWorkerImpl implements AnalysisWorker {

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;
	
	@Inject
	private VisualisationHelperWorker visWorker;

	@Inject
	private Analysis analysis;

	@Override
	public List<ResourceRequestInfo> usageAnalysisExtended(final Course course, final Date beginDate,
			final Date endDate, final List<String> resourceTypes, final List<GenderEnum> genderList) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			List<Long> gender = this.visWorker.getGenderIds(genderList);

			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis");
			final ResultListResourceRequestInfo results = this.analysis.computeCourseActivityExtended(courses,
					beginStamp,
					endStamp, resourceTypes,gender,null);
			this.logger.debug("Extended Analysis: " + results);
			if ((results != null) && (results.getResourceRequestInfos() != null)
					&& (results.getResourceRequestInfos().size() > 0)) {
				for (int i = 0; i < results.getResourceRequestInfos().size(); i++) {
					final ResourceRequestInfo res = results.getResourceRequestInfos().get(i);
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
			final List<Long> selectedUsers, final List<String> resourceTypes, final List<GenderEnum> genderList, final List<Long> learningList) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			List<String> resourceTypesNames = null;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			List<Long> gender = this.visWorker.getGenderIds(genderList);

			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis");
			final ResultListResourceRequestInfo results = this.analysis.computeLearningObjectUsage(courses,
					selectedUsers,
					resourceTypes, beginStamp, endStamp, gender, learningList);
			this.logger.debug("Extended Analysis: " + results);
			if ((results != null) && (results.getResourceRequestInfos() != null)
					&& (results.getResourceRequestInfos().size() > 0)) {
				for (int i = 0; i < results.getResourceRequestInfos().size(); i++) {
					final ResourceRequestInfo res = results.getResourceRequestInfos().get(i);
				}
				return results.getResourceRequestInfos();
			}
		} else {
			this.logger.debug("Extended Analysis Result is null!");
		}
		return new ArrayList<ResourceRequestInfo>();
	}

	/*
	@Override
	public ResultListRRITypes usageAnalysisExtendedDetails(final Course course, final Date beginDate,
			final Date endDate, final Integer resolution, final List<String> resourceTypes, final List<GenderEnum> genderList) {

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;

			if (endDate != null) {

				endStamp = new Long(endDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			List<Long> gender = this.visWorker.getGenderIds(genderList);

			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " ");

			this.logger.debug("Starting Extended Analysis Details");
			final ResultListRRITypes results = this.analysis.computeCourseActivityExtendedDetails(courses, beginStamp,
					endStamp,
					resolution.longValue(), resourceTypes, gender, null);
			this.logger.debug("Extended Analysls Details: " + results);
			if (results != null) {
				if (results.getTaskRRI() != null) {
					final List<ResourceRequestInfo> ass = results.getTaskRRI();
					if (ass.size() > 0) {
						for (int i = 0; i < ass.size(); i++) {
							final ResourceRequestInfo res = ass.get(i);
							this.logger.debug("ASS ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- "
									+ res.getResourcetype() + " ------ " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getLearningObjectRRI() != null) {
					final List<ResourceRequestInfo> cou = results.getLearningObjectRRI();
					if (cou.size() > 0) {
						for (int i = 0; i < cou.size(); i++) {
							final ResourceRequestInfo res = cou.get(i);
							this.logger.debug("Cou ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- "
									+ res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getLearningObjectRRI() != null) {
					final List<ResourceRequestInfo> ress = results.getLearningObjectRRI();
					if (ress.size() > 0) {
						for (int i = 0; i < ress.size(); i++) {
							final ResourceRequestInfo res = ress.get(i);
							this.logger.debug("Res ResourceRequest " + res.getId() + " ----- " + res.getTitle()
									+ " ----- "
									+ res.getResourcetype() + " ------- " + res.getRequests() + " ----- "
									+ res.getResolutionSlot());
						}
					}
				}

				if (results.getLearningObjectRRI() != null) {
					final List<ResourceRequestInfo> ress = results.getCollaborativeObjectRRI();
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
*/
	@Override
	public List<List<XYDateDataItem>> usageAnalysis(final Course course, final Date endDate, final int dateRange,
			final Integer dateMultiplier, final List<String> resourceTypes) {

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
			final List<String> resourceTypes) {

		final List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
		final List<XYDateDataItem> list1 = CollectionFactory.newList();
		long resolution = 0;

		if ((beginDate != null) && (endDate != null)) {
			resolution = this.dateWorker.daysBetween(beginDate, endDate);
		}

		if ((course != null) && (course.getId() != null)) {
			Long endStamp = 0L;
			Long beginStamp = 0L;

			if (endDate != null) {
				endStamp = new Long(endDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}

			if (beginDate != null) {

				beginStamp = new Long(beginDate.getTime() / DateWorkerImpl.MILLISEC_MULTIPLIER);
			}


			
			final List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}
			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + resolution);
			final Map<Long, ResultListLongObject> results = this.analysis.computeCourseActivity(courses, null,
					beginStamp, endStamp, resolution, resourceTypes, null, null);
			ResultListLongObject uniqueResult = null;

			if (results != null) {
				uniqueResult = results.get(course.getCourseId());
			} else {
				uniqueResult = null;
			}

			final Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);

			// checking if result size matches resolution
			if ((uniqueResult != null) && (uniqueResult.getElements() != null)) {
				for (int i = 0; i < resolution; i++) {

					beginCal.add(Calendar.DAY_OF_MONTH, 1);
					list1.add(new XYDateDataItem(beginCal.getTime(), uniqueResult.getElements().get(i)));

				}
			} // if there was an error while retrieving result data from dms ... one single null result is added
				// because jqplot cant handle resultlist without an entry
			else {
				list1.add(new XYDateDataItem(beginCal.getTime(), 0));
			}
		}
		dataList.add(list1);
		return dataList;
	}

}
