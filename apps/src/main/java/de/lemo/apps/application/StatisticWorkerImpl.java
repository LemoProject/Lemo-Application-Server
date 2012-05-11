/**
 * 
 */
package de.lemo.apps.application;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

/**
 * @author johndoe
 *
 */
public class StatisticWorkerImpl implements StatisticWorker{
	
	
	public Long getAverageRequest(List<List<XYDateDataItem>> dataItemList){
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		innerList = dataItemList.get(0);
		Long requestAvg = 0L;
		if(innerList!= null){
			requestAvg = getOverallRequest(dataItemList) / innerList.size();
		}	
		return requestAvg;
	}
	
	public Long getOverallRequest(List<List<XYDateDataItem>> dataItemList){
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		innerList = dataItemList.get(0);
		Long requestCounter = 0L;
		if(innerList!= null){
			for(int i = 0;i<innerList.size();i++){
				requestCounter = requestCounter + innerList.get(i).getYValue().longValue();
			}
		}
		return requestCounter;
	}
	
	public Long getMaxRequest(List<List<XYDateDataItem>> dataItemList){
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		innerList = dataItemList.get(0);
		Long requestMax = 0L;
		if(innerList!= null){
			for(int i = 0;i<innerList.size();i++){
				if(innerList.get(i).getYValue().longValue() > requestMax)
					requestMax = innerList.get(i).getYValue().longValue();
			}
		}
		return requestMax;
	}
	
	public Date getMaxRequestDate(List<List<XYDateDataItem>> dataItemList){
		List<XYDateDataItem> innerList = CollectionFactory.newList();
		innerList = dataItemList.get(0);
		Long requestMax = 0L;
		Date requestMaxDate = new Date();
		if(innerList!= null){
			for(int i = 0;i<innerList.size();i++){
				if(innerList.get(i).getYValue().longValue() > requestMax){
					requestMax = innerList.get(i).getYValue().longValue();
					requestMaxDate = innerList.get(i).getXValue();
				}
			}
		}
		return requestMaxDate;
	}

}
