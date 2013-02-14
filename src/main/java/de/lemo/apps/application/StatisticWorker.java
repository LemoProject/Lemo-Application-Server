/**
	 * File StatisticWorker.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
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
