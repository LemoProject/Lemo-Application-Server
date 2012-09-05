package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;

import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultList;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.proxies.questions.QCourseActivity;
import de.lemo.apps.restws.proxies.service.ServiceCourseDetails;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;
import de.lemo.apps.restws.proxies.service.ServiceUserInformation;

public class InitialisationImpl implements Initialisation {
	
	private static final String SERVICE_STARTTIME_URL = "http://localhost:8080/dms/services/starttime";
	private static final String SERVICE_COURSE_URL = "http://localhost:8080/dms/services/courses";
    //private static final String QUESTIONS_BASE_URL = "http://localhost:8080/dms//questions";

	public InitialisationImpl(){
		//Initialise the Rest session
		//logger.info("Register Rest Service");
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}
	
	
	public Date getStartTime(){
		//Create resource delegate
		//logger.info("Getting Server Starttime");
		try {
			ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			ServiceStartTime starttime = ProxyFactory.create(ServiceStartTime.class,SERVICE_STARTTIME_URL);
			if (starttime != null){
				return new Date(starttime.startTimeJson().getTime());
			}
			
		} catch (ClientProtocolException e) {
		 
		e.printStackTrace();
 
		} catch (IOException e) {
 
			e.printStackTrace();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
		
		
	
		
		public ResultListCourseObject getCoursesDetails(List<Long> ids){
			try {
				ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
				
				ClientResponse<ServiceStartTime> response;
				
					response = request.get(ServiceStartTime.class);
				
					
				
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
				}
				
				ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class,SERVICE_COURSE_URL);
				if (courseDetails != null){

					ResultListCourseObject result = courseDetails.getCoursesDetails(ids);
					return result;
				}
				
			} catch (ClientProtocolException e) {
			 
			e.printStackTrace();
	 
			} catch (IOException e) {
	 
				e.printStackTrace();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Gebe leere Resultlist zurueck");
			return new ResultListCourseObject();
		}
		
		
		
		public CourseObject getCourseDetails(Long id){
			try {
				ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
				
				ClientResponse<ServiceStartTime> response;
				
					response = request.get(ServiceStartTime.class);
				
					
				
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
				}
				
				ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class,SERVICE_COURSE_URL);
				if (courseDetails != null){

					CourseObject result = courseDetails.getCourseDetails(id);
					return result;
				}
				
			} catch (ClientProtocolException e) {
			 
			e.printStackTrace();
	 
			} catch (IOException e) {
	 
				e.printStackTrace();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Gebe leere Resultlist zurueck");
			return new CourseObject();
		}
		
}
