package de.lemo.apps.restws.client;

import java.util.List;
import java.util.Map;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

/**
 * Interface for the analysis for the gui
 */
public interface Analysis {

	Map<Long, ResultListLongObject> computeCourseActivity(
			List<Long> courses,
			List<Long> users,
			Long starttime,
			Long endtime,
			Long resolution,
			List<String> resourceTypes,
			List<Long> gender);

	ResultListResourceRequestInfo computeCourseActivityExtended(
			List<Long> courses,
			Long startTime,
			Long endTime,
			List<String> resourceTypes,
			List<Long> gender);

	ResultListRRITypes computeCourseActivityExtendedDetails(
			List<Long> courses,
			Long startTime,
			Long endTime,
			Long resolution,
			List<String> resourceTypes,
			List<Long> gender);

	ResultListResourceRequestInfo computeLearningObjectUsage(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computeCourseUserPaths(
			List<Long> courseIds,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computeUserPathAnalysis(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Boolean considerLogouts,
			Long startTime,
			Long endTime,
			List<Long> gender);

	ResultListLongObject computeCourseUsers(
			List<Long> courseIds,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computeQFrequentPathBIDE(
			Long userId,
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long minLength,
			Long maxLength,
			Double minSup,
			Boolean sessionWise,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computeQFrequentPathViger(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long minLength,
			Long maxLength,
			Double minSup,
			Boolean sessionWise,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computeCumulativeUserAccess(
			List<Long> courseIds,
			List<String> types,
			List<Long> departments,
			List<Long> degrees,
			Long startTime,
			Long endTime);

	List<Long> computePerformanceHistogram(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Long resolution,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computePerformanceBoxplot(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Long resolution,
			Long startTime,
			Long endTime,
			List<Long> gender);

	List<Long> computePerformanceUserTest(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Long resolution,
			Long startTime,
			Long endTime,
			List<Long> gender);

	String computePerformanceUserTestBoxPlot(
			List<Long> courses,
			List<Long> users,
			List<Long> quizzes,
			Long resolution,
			Long startTime,
			Long endTime,
			List<Long> gender);

}
