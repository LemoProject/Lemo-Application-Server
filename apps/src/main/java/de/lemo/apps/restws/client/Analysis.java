package de.lemo.apps.restws.client;

import java.util.List;

import de.lemo.apps.restws.entities.ResultListLongObject;

public interface Analysis {
	
	public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution);
	
	

}
