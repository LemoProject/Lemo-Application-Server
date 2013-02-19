package de.lemo.apps.restws.client;

import de.lemo.apps.exceptions.RestServiceCommunicationException;

public interface Information {
	
	String getDMSVersion() throws RestServiceCommunicationException ;
	
	String getDMSDBVersion() throws RestServiceCommunicationException;

}
