package de.lemo.apps.application;

import java.util.Date;
import java.util.List;

import de.lemo.apps.entities.Course;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListRRITypes;

public interface AnalysisWorker {
	
	public List usageAnalysisExtended(Course course, Date beginDate, Date endDate, List<EResourceType> resourceTypes);
	
	public ResultListRRITypes usageAnalysisExtendedDetails(Course course, Date beginDate, Date endDate, Integer resolution, List<EResourceType> resourceTypes);
	
	public List usageAnalysis(Course course, Date endDate, final int dateRange, Integer dateMultiplier);
	
	public List usageAnalysis(Course course, Date beginDate, Date endDate);

}
