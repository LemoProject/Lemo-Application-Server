/**
 * File ./src/main/java/de/lemo/apps/application/StatisticWorkerImpl.java
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
	 * File StatisticWorkerImpl.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.Date;
import java.util.List;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;


public class StatisticWorkerImpl implements StatisticWorker {

	public Long getAverageRequest(final List<List<XYDateDataItem>> dataItemList) {
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		Long requestAvg = 0L;
		if ((dataItemList != null) && (dataItemList.size() >= 1)) {
			innerList = dataItemList.get(0);

			if ((innerList != null) && (innerList.size() != 0)) {
				requestAvg = this.getOverallRequest(dataItemList) / innerList.size();
			}
		}
		return requestAvg;
	}

	public Long getOverallRequest(final List<List<XYDateDataItem>> dataItemList) {
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		Long requestCounter = 0L;
		if ((dataItemList != null) && (dataItemList.size() >= 1)) {
			innerList = dataItemList.get(0);

			if (innerList != null) {
				for (int i = 0; i < innerList.size(); i++) {
					requestCounter = requestCounter + innerList.get(i).getYValue().longValue();
				}
			}
		}
		return requestCounter;
	}

	public Long getMaxRequest(final List<List<XYDateDataItem>> dataItemList) {
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		Long requestMax = 0L;
		if ((dataItemList != null) && (dataItemList.size() >= 1)) {
			innerList = dataItemList.get(0);

			if (innerList != null) {
				for (int i = 0; i < innerList.size(); i++) {
					if (innerList.get(i).getYValue().longValue() > requestMax) {
						requestMax = innerList.get(i).getYValue().longValue();
					}
				}
			}
		}
		return requestMax;
	}

	public Date getMaxRequestDate(final List<List<XYDateDataItem>> dataItemList) {
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		Date requestMaxDate = new Date();
		if ((dataItemList != null) && (dataItemList.size() >= 1)) {
			innerList = dataItemList.get(0);
			Long requestMax = 0L;

			if (innerList != null) {
				for (int i = 0; i < innerList.size(); i++) {
					if (innerList.get(i).getYValue().longValue() > requestMax) {
						requestMax = innerList.get(i).getYValue().longValue();
						requestMaxDate = innerList.get(i).getXValue();
					}
				}
			}
		}
		return requestMaxDate;
	}

}
