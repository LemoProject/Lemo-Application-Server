/**
 * File ./src/main/java/de/lemo/apps/exceptions/LoginUserNotFoundException.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
	 * File LoginUserNotFoundException.java
	 *
	 * Date Feb 14, 2013 
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
