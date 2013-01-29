/**
 * File ./de/lemo/apps/restws/client/Analysis.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.client;

import java.util.HashMap;
import java.util.List;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

public interface Analysis {

	HashMap<Long, ResultListLongObject> computeCourseActivity(
			List<Long> courses,
			List<Long> roles,
			List<Long> users,
			Long starttime,
			Long endtime,
			int resolution,
			List<String> resourceTypes);

	// String computeQ1JSON(
	// List<Long> courses,
	// List<Long> roles,
	// Long starttime,
	// Long endtime,
	// int resolution,
	// List<String> resourceTypes);

	ResultListResourceRequestInfo computeCourseActivityExtended(
			List<Long> courses,
			Long startTime,
			Long endTime,
			List<String> resourceTypes);

	// String computeQ1ExtendedJSON(
	// List<Long> courses,
	// Long startTime,
	// Long endTime,
	// List<String> resourceTypes);

	ResultListRRITypes computeCourseActivityExtendedDetails(
			List<Long> courses,
			Long startTime,
			Long endTime,
			Long resolution,
			List<String> resourceTypes);

	ResultListResourceRequestInfo computeLearningObjectUsage(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long startTime,
			Long endTime);

	String computeCourseUserPaths(
			List<Long> courseIds,
			Long startTime,
			Long endTime);

	String computeUserPathAnalysis(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Boolean considerLogouts,
			Long startTime,
			Long endTime);

	ResultListLongObject computeCourseUsers(
			List<Long> courseIds,
			Long startTime,
			Long endTime);

	public String computeQFrequentPathBIDE(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long minLength,
			Long maxLength,
			Double minSup,
			Boolean sessionWise,
			Long startTime,
			Long endTime);

	public String computeQFrequentPathViger(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long minLength,
			Long maxLength,
			Double minSup,
			Boolean sessionWise,
			Long startTime,
			Long endTime);

	public String computeCumulativeUserAccess(
			List<Long> courseIds,
			List<String> types,
			List<Long> departments,
			List<Long> degrees,
			Long startTime,
			Long endTime);

	public List<Long> computePerformanceHistogram(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Integer resolution,
			Long startTime,
			Long endTime);

	public String computePerformanceBoxplot(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Long startTime,
			Long endTime);

}
