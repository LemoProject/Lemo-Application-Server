/**
 * File ./de/lemo/apps/application/StatisticWorker.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.application;

import java.util.Date;
import java.util.List;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

public interface StatisticWorker {

	public Long getAverageRequest(List<List<XYDateDataItem>> dataItemList);

	public Long getOverallRequest(List<List<XYDateDataItem>> dataItemList);

	public Long getMaxRequest(List<List<XYDateDataItem>> dataItemList);

	public Date getMaxRequestDate(List<List<XYDateDataItem>> dataItemList);

}
