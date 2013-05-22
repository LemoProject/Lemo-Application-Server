package de.lemo.apps.restws.client;

import java.util.List;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.SCConnector;
import de.lemo.apps.restws.entities.SCConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectors;

/**
 * interface to get the versionnumbers and other information from the dms
 *
 */
public interface Information {
	
	String getDMSVersion() throws RestServiceCommunicationException ;
	
	String getDMSDBVersion() throws RestServiceCommunicationException;
	
	ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) throws RestServiceCommunicationException;
	
	List<SCConnector> getConnectorList() throws RestServiceCommunicationException;
	
	SCConnectorManagerState getConnectorState() throws RestServiceCommunicationException;
	
	Boolean startUpdate(Long connectorId) throws RestServiceCommunicationException;
	

}
