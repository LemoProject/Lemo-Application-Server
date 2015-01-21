/**
 * File ./src/main/java/de/lemo/apps/services/QaModule.java
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

package de.lemo.apps.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * This module is automatically included as part of the Tapestry IoC Registry if <em>tapestry.execution-mode</em>
 * includes <code>qa</code> ("quality assurance").
 */
public class QaModule {

	public static void bind(final ServiceBinder binder) {
		// Bind any services needed by the QA team to produce their reports
	}

	public static void contributeApplicationDefaults(
			final MappedConfiguration<String, Object> configuration) {
		// The factory default is true but during the early stages of an application
		// overriding to false is a good idea. In addition, this is often overridden
		// on the command line as -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, false);

		// The application version number is incorprated into URLs for some
		// assets. Web browsers will cache assets because of the far future expires
		// header. If existing assets are changed, the version number should also
		// change, to force the browser to download new versions.
		configuration.add(SymbolConstants.APPLICATION_VERSION, "0.0.1-SNAPSHOT-QA");
	}
}
