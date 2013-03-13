package de.lemo.apps.restws.client;

import de.lemo.apps.exceptions.RestServiceCommunicationException;

/**
 * interface to get the versionnumbers from the dms
 *
 */
public interface Information {
	
	String getDMSVersion() throws RestServiceCommunicationException ;
	
	String getDMSDBVersion() throws RestServiceCommunicationException;

}
