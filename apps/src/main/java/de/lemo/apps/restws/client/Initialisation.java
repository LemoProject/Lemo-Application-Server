package de.lemo.apps.restws.client;

import java.util.List;

import de.lemo.apps.restws.entities.ResultList;

public interface Initialisation {
	
	public String getStartTime();
	
	public ResultList computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution);

}
