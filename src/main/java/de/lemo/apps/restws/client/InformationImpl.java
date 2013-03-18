/**
 * 
 */
package de.lemo.apps.restws.client;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.proxies.service.ServiceCourseTitleSearch;
import de.lemo.apps.restws.proxies.service.ServiceVersion;

/**
 * @author johndoe
 */
public class InformationImpl implements Information {
	
	
	private static final String SERVICE_VERSION_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/version/dms";
	private static final String SERVICE_DBVERSION_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/version/db";
	
	@Inject
	private Initialisation init;
	
	@Inject
	private Logger logger;
	
	public String getDMSVersion() throws RestServiceCommunicationException {
		
		try {

				if (init.defaultConnectionCheck()){
					final ServiceVersion serviceProxy = ProxyFactory.create(ServiceVersion.class, SERVICE_VERSION_URL);
					if (serviceProxy != null) {
						
						return serviceProxy.getDMSVersion();
					}
				}

		} catch (final Exception e) {
				
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());

		}
		
		
		logger.info("Version information could not be loaded.");
		return "";
		
	}
	
	
	
	public String getDMSDBVersion() throws RestServiceCommunicationException {
		
		try {

				if (init.defaultConnectionCheck()){
					final ServiceVersion serviceProxy = ProxyFactory.create(ServiceVersion.class, SERVICE_DBVERSION_URL);
					if (serviceProxy != null) {
						
						return serviceProxy.getDBVersion();
					}
				}

		} catch (final Exception e) {
				
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());

		}
		
		
		logger.info("DB version information could not be loaded.");
		return "";
		
	}
	
	public ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) throws RestServiceCommunicationException {
		
		try {

				if (init.defaultConnectionCheck()){
					final ServiceCourseTitleSearch serviceProxy = ProxyFactory.create(ServiceCourseTitleSearch.class, InitialisationImpl.SERVICE_PREFIX_URL);
					if (serviceProxy != null) {
						
						return serviceProxy.getCoursesByText(text, count, offset);
					}
				}

		} catch (final Exception e) {
				
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());

		}
		
		
		logger.info("Courses could not be loaded. Returning empty resultset.");
		return new ResultListCourseObject();
		
	}

	

}
