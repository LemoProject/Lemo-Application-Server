/**
 * File ./de/lemo/apps/exceptions/ObjectNotSavedException.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.exceptions;

import org.apache.log4j.Logger;

/**
 * @author Andreas Pursian
 */
public class ObjectNotSavedException extends Exception {

	private final Logger logger = Logger.getLogger(ObjectNotSavedException.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -4576186737274820490L;

	public ObjectNotSavedException() {

	}

	public ObjectNotSavedException(final String objectId) {
		this.logger.error("Object with id: " + objectId + " could not be saved.");
	}
}
