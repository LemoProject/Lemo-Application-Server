/**
 * File ./src/main/java/de/lemo/apps/restws/client/Information.java
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
	
	ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) 
										throws RestServiceCommunicationException;
	
	List<SCConnector> getConnectorList() throws RestServiceCommunicationException;
	
	SCConnectorManagerState getConnectorState() throws RestServiceCommunicationException;
	
	Boolean startUpdate(Long connectorId) throws RestServiceCommunicationException;
	

}
