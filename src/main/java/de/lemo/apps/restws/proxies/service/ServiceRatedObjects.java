/**
 * File ./de/lemo/apps/restws/proxies/service/ServiceRatedObjects.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.restws.proxies.service;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.restws.proxies.questions.parameters.MetaParam;

/**
 * @author johndoe
 */
public interface ServiceRatedObjects {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultListStringObject getRatedObjects(
			@QueryParam(MetaParam.COURSE_IDS) List<Long> courses);

}
