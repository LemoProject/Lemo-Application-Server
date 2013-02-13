/**
 * 
 */
package de.lemo.apps.exceptions;

import org.apache.log4j.Logger;

/**
 * @author Andreas Pursian
 */
public class RestServiceCommunicationException extends Exception {

	private final Logger logger = Logger.getLogger(RestServiceCommunicationException.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -4576186737274820490L;

	public RestServiceCommunicationException() {

	}

	public RestServiceCommunicationException(final String serviceName) {
		this.logger.error("Error during REST-Service communication. Service invoked: "+serviceName);
	}
}
