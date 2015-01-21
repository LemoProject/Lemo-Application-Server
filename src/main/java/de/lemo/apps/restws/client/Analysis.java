/**
 * File ./src/main/java/de/lemo/apps/restws/client/Analysis.java
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

package de.lemo.apps.restws.client;

import java.util.List;
import java.util.Map;

import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListStringObject;

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
			List<Long> gender,
			List<Long> learningObjects);

	ResultListResourceRequestInfo computeCourseActivityExtended(
			List<Long> courses,
			Long startTime,
			Long endTime,
			List<String> resourceTypes,
			List<Long> gender,
			List<Long> learningObjects);

	/*
	ResultListRRITypes computeCourseActivityExtendedDetails(
			List<Long> courses,
			Long startTime,
			Long endTime,
			Long resolution,
			List<String> resourceTypes,
			List<Long> gender,
			List<Long> learningObjects);
*/
	ResultListResourceRequestInfo computeLearningObjectUsage(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Long startTime,
			Long endTime,
			List<Long> gender,
			List<Long> learningObjects);

	String computeCourseUserPaths(
			List<Long> courseIds,
			Long startTime,
			Long endTime,
			List<Long> gender,
			List<Long> learningObjects);

	String computeUserPathAnalysis(
			List<Long> courseIds,
			List<Long> userIds,
			List<String> types,
			Boolean considerLogouts,
			Long startTime,
			Long endTime,
			List<Long> gender,
			List<Long> learningObjects);

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
	
	String computeQFrequentPathApriori(
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
			Long endTime,
			List<Long> learningObjects);

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
