/**
 * File ./src/main/java/de/lemo/apps/application/ApplicationInfo.java
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
	 * File ApplicationInfo.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.ResourceBundle;

/**
 * Helper class to show information about the application server 
 *
 */
public final class ApplicationInfo {

	private static final String BUNDLE_NAME = "de.lemo.apps.application.config";
	private static final ResourceBundle RESOURCEBUNDLE = ResourceBundle.getBundle(ApplicationInfo.BUNDLE_NAME);

	private ApplicationInfo() {
		// no instance needed
	}

	public static String getSystemName() {
		return ApplicationInfo.RESOURCEBUNDLE.getString("lemo.system-name");
	}

	public static String getDisplayName() {
		return ApplicationInfo.RESOURCEBUNDLE.getString("lemo.display-name");
	}
}
