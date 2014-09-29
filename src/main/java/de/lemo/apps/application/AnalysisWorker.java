/**
 * File ./src/main/java/de/lemo/apps/application/AnalysisWorker.java
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

/**
	 * File AnalysisWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.Date;
import java.util.List;

import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

public interface AnalysisWorker {

	List<ResourceRequestInfo> usageAnalysisExtended(Course course, Date beginDate, Date endDate,
			List<String> resourceTypes, List<GenderEnum> genderList);

	List<ResourceRequestInfo> learningObjectUsage(Course course, Date beginDate, Date endDate,
			List<Long> selectedUsers, List<String> resourceTypes, List<GenderEnum> genderList, final List<Long> learningList);
/*
	ResultListRRITypes usageAnalysisExtendedDetails(Course course, Date beginDate, Date endDate,
			Integer resolution, List<String> resourceTypes,  List<GenderEnum> genderList);
*/
	List<List<XYDateDataItem>> usageAnalysis(Course course, Date endDate, final int dateRange,
			Integer dateMultiplier, List<String> resourceTypes);

	List<List<XYDateDataItem>> usageAnalysis(Course course, Date beginDate, Date endDate,
			List<String> resourceTypes);

}
