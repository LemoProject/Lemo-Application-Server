/**
 * 
 */
package de.lemo.apps.restws.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.SCConnector;
import de.lemo.apps.restws.entities.SCConnectorManagerState;
import de.lemo.apps.restws.entities.SCConnectors;
import de.lemo.apps.restws.proxies.service.ServiceConnectorManager;
import de.lemo.apps.restws.proxies.service.ServiceCourseTitleSearch;
import de.lemo.apps.restws.proxies.service.ServiceVersion;

/**
 * @author Andreas Pursian
 */
public class InformationImpl implements Information {
	
	
	private static final String SERVICE_VERSION_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/version/dms";
	private static final String SERVICE_DBVERSION_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/version/db";
	private static final String SERVICE_CONNECTOR_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/connectors";
	
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
	

	public ResultListCourseObject getCoursesByTitle(String text, Long count, Long offset) 
												throws RestServiceCommunicationException {
		
		try {

				if (init.defaultConnectionCheck()){
					final ServiceCourseTitleSearch serviceProxy = ProxyFactory.create(ServiceCourseTitleSearch.class,
																					  InitialisationImpl.SERVICE_PREFIX_URL);
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
	
	
	public List<SCConnector> getConnectorList() throws RestServiceCommunicationException {
		
		try {
				if (init.defaultConnectionCheck()){
					final ServiceConnectorManager serviceProxy = ProxyFactory.create(ServiceConnectorManager.class, SERVICE_CONNECTOR_URL);
					if (serviceProxy != null) {
						
						SCConnectors result = serviceProxy.connectorListJson();
						if(result != null) return result.getConnectors();
					}
				}
		} catch (final Exception e) {		
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());
		}
		logger.info("Connector information could not be loaded.");
		return new ArrayList<SCConnector>();
	}
	
	
	public SCConnectorManagerState getConnectorState() throws RestServiceCommunicationException {
		
		try {
				if (init.defaultConnectionCheck()){
					final ServiceConnectorManager serviceProxy = ProxyFactory.create(ServiceConnectorManager.class, SERVICE_CONNECTOR_URL);
					if (serviceProxy != null) {
						
						SCConnectorManagerState result = serviceProxy.state();
						if(result != null) return result;
					}
				}
		} catch (final Exception e) {		
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());
		}
		logger.info("Connector information could not be loaded.");
		return new SCConnectorManagerState();
	}

	
	public Boolean startUpdate(Long connectorId) throws RestServiceCommunicationException {
		
		try {
				if (init.defaultConnectionCheck()){
					final ServiceConnectorManager serviceProxy = ProxyFactory.create(ServiceConnectorManager.class, SERVICE_CONNECTOR_URL);
					if (serviceProxy != null) {
						
						Boolean result = serviceProxy.update(connectorId);
						if(result != null) return result;
					}
				}
		} catch (final Exception e) {		
				throw new RestServiceCommunicationException(this.toString()+" "+e.getLocalizedMessage());
		}
		logger.info("Connector information could not be loaded.");
		return false;
	}



}
