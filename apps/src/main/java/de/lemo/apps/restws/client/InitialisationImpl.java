package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;

import de.lemo.apps.restws.entities.ResultList;
import de.lemo.apps.restws.proxies.QCourseActivity;
import de.lemo.apps.restws.proxies.Starttime;

public class InitialisationImpl implements Initialisation {
	
	//@Inject
	//private Logger logger;

	public InitialisationImpl(){
		//Initialise the Rest session
		//logger.info("Register Rest Service");
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}
	
	
	public String getStartTime(){
		//Create resource delegate
		//logger.info("Getting Server Starttime");
		try {
			ClientRequest request = new ClientRequest("http://localhost:4443/starttime");
			
			ClientResponse<Starttime> response;
			
				response = request.get(Starttime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			Starttime starttime = ProxyFactory.create(Starttime.class,
			"http://localhost:4443/starttime");
			if (starttime != null){
				return starttime.startTimeHtml();
			}
			
		} catch (ClientProtocolException e) {
		 
		e.printStackTrace();
 
		} catch (IOException e) {
 
			e.printStackTrace();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Keine Antwort!";
	}
		
		
	public ResultList computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution) {
			//Create resource delegate
			//logger.info("Getting Server Starttime");
			try {
				ClientRequest request = new ClientRequest("http://localhost:4443/starttime");
				
				ClientResponse<Starttime> response;
				
					response = request.get(Starttime.class);
				
					
				
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
				}
				
				QCourseActivity qcourseActivity = ProxyFactory.create(QCourseActivity.class,
				"http://localhost:4443/courseactivity");
				if (qcourseActivity != null){
//					starttime = 1108968800L;
//					endtime= 1334447632L;
//					resolution = 30;
//					roles = new ArrayList<Long>();
//					courses = new ArrayList<Long>();
//					courses.add(2100L);
//					courses.add(2200L);
					return qcourseActivity.compute(courses, roles, starttime, endtime, resolution);
				}
				
			} catch (ClientProtocolException e) {
			 
			e.printStackTrace();
	 
			} catch (IOException e) {
	 
				e.printStackTrace();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Gebe leere Resultlist zurück");
			return new ResultList();
		}
		
		
}
