package de.lemo.apps.restws.client;

import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.ResultListCourseObject;

/**
 * interface to get the versionnumbers from the dms
 *
 */
public interface Information {
	
	String getDMSVersion() throws RestServiceCommunicationException ;
	
	String getDMSDBVersion() throws RestServiceCommunicationException;
	
	ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) throws RestServiceCommunicationException;
	

}
