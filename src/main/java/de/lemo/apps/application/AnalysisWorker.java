package de.lemo.apps.application;

import java.util.Date;
import java.util.List;

import de.lemo.apps.entities.Course;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

public interface AnalysisWorker {
	
	public List<ResourceRequestInfo> usageAnalysisExtended(Course course, Date beginDate, Date endDate, List<EResourceType> resourceTypes);
	
	public List<ResourceRequestInfo> learningObjectUsage(Course course, Date beginDate, Date endDate, List<Long> selectedUsers, List<EResourceType> resourceTypes);
	
	public ResultListRRITypes usageAnalysisExtendedDetails(Course course, Date beginDate, Date endDate, Integer resolution, List<EResourceType> resourceTypes);
	
	public List<List<XYDateDataItem>> usageAnalysis(Course course, Date endDate, final int dateRange, Integer dateMultiplier, List<EResourceType> resourceTypes  );
	
	public List<List<XYDateDataItem>> usageAnalysis(Course course, Date beginDate, Date endDate, List<EResourceType> resourceTypes );
		
}
