/**
 * File ./de/lemo/apps/exceptions/LoginUserNotFoundException.java
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
public class LoginUserNotFoundException extends Exception {

	private final Logger logger = Logger.getLogger(LoginUserNotFoundException.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1413849611873505626L;

	public LoginUserNotFoundException() {

	}

	public LoginUserNotFoundException(final String userName) {
		this.logger.error("Can't find LoginUser: " + userName);
	}

}
