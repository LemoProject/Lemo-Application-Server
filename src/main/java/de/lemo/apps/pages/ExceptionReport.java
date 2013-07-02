/**
 * File ./src/main/java/de/lemo/apps/pages/ExceptionReport.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
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

package de.lemo.apps.pages;

import org.apache.tapestry5.services.ExceptionReporter;

/**
 * Controller class for the system wide Exception Template
 * 
 * @version 1.0
 * @author Andreas Pursian
 */
public class ExceptionReport implements ExceptionReporter {

	private String error;

	public void reportException(final Throwable exception) {
		this.error = exception.getLocalizedMessage();
	}

	public String getError() {
		return this.error;
	}
}