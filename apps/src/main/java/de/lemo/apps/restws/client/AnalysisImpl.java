/**
 * 
 */
package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.proxies.questions.QActivityResourceType;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeResolution;
import de.lemo.apps.restws.proxies.questions.QCourseActivity;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

/**
 * @author johndoe
 *
 */
public class AnalysisImpl implements Analysis{
	
	public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution) {
		//Create resource delegate
		//logger.info("Getting Server Starttime");
		try {
			ClientRequest request = new ClientRequest("http://localhost:4443/starttime");
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QCourseActivity qcourseActivity = ProxyFactory.create(QCourseActivity.class,
			"http://localhost:4443/questions");
			if (qcourseActivity != null){

				ResultListLongObject result = qcourseActivity.compute(courses, roles, starttime, endtime, resolution);
//				if(result!=null && result.getElements()!=null){
//					ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//					List<Long> resultList = new ArrayList<Long>();
//					for (int i=0; i<result.getElements().size();i++)  { 
//						Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
//						resultList.add(i, value);
//					}
//					 
//					return new ResultList(resultList);
//				}
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
		System.out.println("Gebe leere Resultlist zurück");
		return new ResultListLongObject();
	}
	
	
	public ResultListResourceRequestInfo computeQ1Extended(List<Long> courses, Long startTime, Long endTime,  List<String> resourceTypes) {

		try {
			ClientRequest request = new ClientRequest("http://localhost:4443/starttime");
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
			"http://localhost:4443/questions");
			if (qActivityResourceType != null){

				ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime, endTime, resourceTypes);

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
		System.out.println("Gebe leere Resultlist zurück");
		return new ResultListResourceRequestInfo();
	}
	
	public ResultListRRITypes computeQ1ExtendedDetails(List<Long> courses, Long startTime, Long endTime, Long resolution, List<String> resourceTypes) {

		try {
			ClientRequest request = new ClientRequest("http://localhost:4443/starttime");
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QActivityResourceTypeResolution qActivityResourceType = ProxyFactory.create(QActivityResourceTypeResolution.class,
			"http://localhost:4443/questions");
			if (qActivityResourceType != null){

				ResultListRRITypes result = qActivityResourceType.compute(courses, startTime, endTime, resolution, resourceTypes);

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
		System.out.println("Gebe leere Resultlist zurück");
		return new ResultListRRITypes();
	}

}
