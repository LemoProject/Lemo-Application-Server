/**
 * File ./src/main/java/de/lemo/apps/restws/proxies/service/ServiceConnectorManager.java
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

package de.lemo.apps.restws.proxies.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.EConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectors;

/**
 * Connector platform update state.
 * 
 * @see EConnectorManagerState
 * @author Leonard Kappe
 */
public interface ServiceConnectorManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SCConnectors connectorListJson();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String connectorListHtml();

	@GET
	@Path("/state")
	@Produces(MediaType.APPLICATION_JSON)
	public SCConnectorManagerState state();

	@POST
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean update(@PathParam("id") final Long connectorId);

}