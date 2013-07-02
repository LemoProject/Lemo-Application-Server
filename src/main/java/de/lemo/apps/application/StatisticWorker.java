/**
 * File ./src/main/java/de/lemo/apps/application/StatisticWorker.java
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
	 * File StatisticWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.Date;
import java.util.List;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

public interface StatisticWorker {

	Long getAverageRequest(List<List<XYDateDataItem>> dataItemList);

	Long getOverallRequest(List<List<XYDateDataItem>> dataItemList);

	Long getMaxRequest(List<List<XYDateDataItem>> dataItemList);

	Date getMaxRequestDate(List<List<XYDateDataItem>> dataItemList);

}
