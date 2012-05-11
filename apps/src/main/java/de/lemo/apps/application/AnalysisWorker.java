package de.lemo.apps.application;

import java.util.Date;
import java.util.List;

import de.lemo.apps.entities.Course;

public interface AnalysisWorker {
	
	public List usageAnalysis(Course course, Date endDate, final int dateRange, Integer dateMultiplier);
	
	public List usageAnalysis(Course course, Date beginDate, Date endDate);

}
