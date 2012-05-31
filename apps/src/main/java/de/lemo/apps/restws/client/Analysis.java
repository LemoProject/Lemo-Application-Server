package de.lemo.apps.restws.client;

import java.util.List;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

public interface Analysis {
	
	public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution);
	
	public ResultListResourceRequestInfo computeQ1Extended(List<Long> courses, Long startTime, Long endTime,  List<String> resourceTypes);
	
	public ResultListRRITypes computeQ1ExtendedDetails(List<Long> courses, Long startTime, Long endTime, Long resolution, List<String> resourceTypes);

}
