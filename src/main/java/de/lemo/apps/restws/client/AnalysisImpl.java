/**
 * 
 */
package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListBoxPlot;
import de.lemo.apps.restws.entities.ResultListHashMapObject;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.proxies.questions.QActivityResourceType;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeResolution;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeString;
import de.lemo.apps.restws.proxies.questions.QCourseActivity;
import de.lemo.apps.restws.proxies.questions.QCourseActivityString;
import de.lemo.apps.restws.proxies.questions.QCourseUserPaths;
import de.lemo.apps.restws.proxies.questions.QCourseUsers;
import de.lemo.apps.restws.proxies.questions.QCumulativeUserAccess;
import de.lemo.apps.restws.proxies.questions.QFrequentPathsBIDE;
import de.lemo.apps.restws.proxies.questions.QLearningObjectUsage;
import de.lemo.apps.restws.proxies.questions.QUserPathAnalysis;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

/**
 * @author johndoe
 * 
 */
public class AnalysisImpl implements Analysis {

//	  Pre Maven DMS connection String
//    private static final String SERVICE_STARTTIME_URL = "http://localhost:4443/starttime";
//    private static final String QUESTIONS_BASE_URL = "http://localhost:4443/questions";
	
	@Inject
    private Logger logger;
    
    private static final String SERVICE_STARTTIME_URL = "http://localhost:8081/clix/dms/services/starttime";
    private static final String QUESTIONS_BASE_URL = "http://localhost:8081/clix/dms/questions";

    @Override
    public HashMap<Long, ResultListLongObject> computeCourseActivity(List<Long> courses, List<Long> roles, List<Long> users,  Long starttime, Long endtime,
            int resolution, List<String> resourceTypes) {
        // Create resource delegate
        // logger.info("Getting Server Starttime");
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
                                                                  QUESTIONS_BASE_URL);
            if(qcourseActivity != null) {

            	String resultString = qcourseActivity.compute(courses, roles, users, starttime, endtime, resolution,resourceTypes);
            	ResultListHashMapObject resultObject = new ResultListHashMapObject();
            	logger.debug("CourseActivity ResultList: "+resultString);
            	
            	 ObjectMapper mapper = new ObjectMapper();
                 JsonNode jsonObj = mapper.readTree(resultString);
                 JsonNode entries = jsonObj.get("entries");
                 JsonNode keys = jsonObj.get("keys");
                 HashMap<Long, ResultListLongObject> resultList = new HashMap<Long, ResultListLongObject>();
                 if (entries != null && keys !=null) {
                	 logger.debug("Entries: "+jsonObj.get("entries").toString());
                	 // if more than one course result is returned
                	 if (entries.isArray() && keys.isArray()){
                		Iterator<JsonNode> kit = keys.getElements();
                		Iterator<JsonNode> eit = entries.getElements();
                		List<Long> entryList = new ArrayList<Long>();
                		while (kit.hasNext()) {
                			Long keyValue = Long.parseLong(kit.next().getValueAsText());
                			JsonNode entryNodes = eit.next();
                			JsonNode entryNodesArray = entryNodes.get("elements");
                			logger.debug("EntryNodes Complete:"+entryNodesArray);
                			if (entryNodesArray.isArray()){
                				logger.debug("Entries is Array ....");
                				entryList = new ArrayList<Long>();
                				Iterator<JsonNode> enit = entryNodesArray.getElements();
                				while (enit.hasNext()) {
                					Long enitValue = enit.next().getValueAsLong(0L);
                					//logger.debug("Entry Values: "+enitValue);
                					entryList.add(enitValue);
                				}
                			} else logger.debug("Entries is NO Array ....");
                			ResultListLongObject entryValues = new ResultListLongObject(entryList);
                			resultList.put(keyValue, entryValues);
                			
                			logger.debug("Course Key: "+keyValue);
                		}
                		// if only one course result is returned
                	 } else {
                		Long keyValue = Long.parseLong(keys.getValueAsText()); 
                		JsonNode entryNodesArray = entries.get("elements");
                		List<Long> entryList = new ArrayList<Long>();
             			logger.debug("EntryNodes :"+entryNodesArray);
             			
             			if (entryNodesArray.isArray()){
            				logger.debug("Entries is Array ....");
            				Iterator<JsonNode> enit = entryNodesArray.getElements();
            				while (enit.hasNext()) {
            					Long enitValue = enit.next().getValueAsLong(0L);
            					logger.debug("Entry Values: "+enitValue);
            					entryList.add(enitValue);
            				}
            			} else logger.debug("Entries is NO Array ....");
            			ResultListLongObject entryValues = new ResultListLongObject(entryList);
            			resultList.put(keyValue, entryValues);
            			
            			logger.debug("Course Key: "+keyValue);
                		 
                	 }
                 } else logger.debug("Entries not found!");
            	
            	
//            	if(resultObject!=null && resultObject.getElements()!=null){
//                	Set<Long> keySet =  resultObject.getElements().keySet();
//                	Iterator<Long> it = keySet.iterator();
//                	while(it.hasNext()){
//                		Long learnObjectTypeName = it.next();
//                		logger.debug("Result Course IDs: "+learnObjectTypeName);
//                	}
//            
//                 } else logger.debug("Empty resultset !!!");
                 
     		    
     		    // ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
                 // List<Long> resultList = new ArrayList<Long>();
                 // for (int i=0; i<result.getElements().size();i++) {
                 // Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
                 // resultList.add(i, value);
                //
                // return new ResultList(resultList);
                // }
                
     		    
     		    return resultList;//resultObject.getElements();
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zur√ºck");
        return new HashMap<Long, ResultListLongObject>();
    }
    
    
//    @Override
//    public String computeQ1JSON(List<Long> courses, List<Long> roles, Long starttime, Long endtime,
//            int resolution, List<String> resourceTypes) {
//        // Create resource delegate
//        // logger.info("Getting Server Starttime");
//        try {
//            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
//
//            ClientResponse<ServiceStartTime> response;
//
//            response = request.get(ServiceStartTime.class);
//
//            if(response.getStatus() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
//            QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
//                                                                  QUESTIONS_BASE_URL);
//            if(qcourseActivity != null) {
//
//                String result = qcourseActivity.compute(courses, roles, starttime, endtime, resolution,resourceTypes);
//                // if(result!=null && result.getElements()!=null){
//                // ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//                // List<Long> resultList = new ArrayList<Long>();
//                // for (int i=0; i<result.getElements().size();i++) {
//                // Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
//                // resultList.add(i, value);
//                // }
//                //
//                // return new ResultList(resultList);
//                // }
//                return result;
//            }
//
//        } catch (ClientProtocolException e) {
//
//            e.printStackTrace();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("Gebe leere Resultlist zur√ºck");
//        return "";
//    }
    
    
    

    @Override
    public ResultListResourceRequestInfo computeCourseActivityExtended(List<Long> courses, Long startTime, Long endTime,
            List<String> resourceTypes) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
                                                                              QUESTIONS_BASE_URL);
            if(qActivityResourceType != null) {

                ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime, endTime,
                                                                                     resourceTypes);

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

    
//    @Override
//    public String computeQ1ExtendedJSON(List<Long> courses, Long startTime, Long endTime,
//            List<String> resourceTypes) {
//
//        try {
//            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
//
//            ClientResponse<ServiceStartTime> response;
//
//            response = request.get(ServiceStartTime.class);
//
//            if(response.getStatus() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
//            QActivityResourceTypeString qActivityResourceType = ProxyFactory.create(QActivityResourceTypeString.class,
//                                                                              QUESTIONS_BASE_URL);
//            if(qActivityResourceType != null) {
//
//                String result = qActivityResourceType.compute(courses, startTime, endTime,resourceTypes);
//
//                return result;
//            }
//
//        } catch (ClientProtocolException e) {
//
//            e.printStackTrace();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("Gebe leere Resultlist zurück");
//        return "";
//    }
//    
    
    
    @Override
    public ResultListRRITypes computeCourseActivityExtendedDetails(List<Long> courses, Long startTime, Long endTime,
            Long resolution, List<String> resourceTypes) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QActivityResourceTypeResolution qActivityResourceType = ProxyFactory
                    .create(QActivityResourceTypeResolution.class,
                            QUESTIONS_BASE_URL);
            if(qActivityResourceType != null) {
            	
            	if(resourceTypes!=null && resourceTypes.size()>0)
    	    		for(int i=0; i<resourceTypes.size();i++){
    	    			logger.info("Course Activity Request - CA Selection: "+resourceTypes.get(i));
    	    		}
    	    	else logger.info("Course Activity Request - CA Selection: NO Items selected ");

                ResultListRRITypes result = qActivityResourceType.compute(courses, startTime, endTime, resolution,
                                                                          resourceTypes);

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
    
    
    @Override
    public ResultListResourceRequestInfo computeLearningObjectUsage(List<Long> courseIds, List<Long> userIds, List<String> types, Long startTime, Long endTime) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QLearningObjectUsage qLOUsage = ProxyFactory.create(QLearningObjectUsage.class, QUESTIONS_BASE_URL);
            if(qLOUsage != null) {
            	if(types!=null && types.size()>0)
            		for(int i=0; i<types.size();i++){
            			logger.debug("LO Request - LO Selection: "+types.get(i));
            		}
            	else logger.debug("LO Request - LO Selection: NO Items selected ");
            	
            	ResultListResourceRequestInfo result = qLOUsage.compute(courseIds, userIds, types, startTime, endTime);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return new ResultListResourceRequestInfo();
    }

    @Override
    public ResultListLongObject computeCourseUsers(
            List<Long> courseIds,
            Long startTime,
            Long endTime) {
        ResultListLongObject result = null;
        QCourseUsers analysis = ProxyFactory.create(QCourseUsers.class, QUESTIONS_BASE_URL);
        if(analysis != null) 
            result = analysis.compute(courseIds, startTime, endTime);
        if(result == null)
            result = new ResultListLongObject();
        return result;
    }

    @Override
    public String computeUserPathAnalysis(
            List<Long> courseIds,
            List<Long> userIds,
            List<String> types,
            Boolean considerLogouts,
            Long startTime,
            Long endTime) {

        QUserPathAnalysis analysis = ProxyFactory.create(QUserPathAnalysis.class, QUESTIONS_BASE_URL);
        if(analysis != null) {
            String result = analysis.compute(courseIds, userIds, types, considerLogouts, startTime, endTime);
            System.out.println("PATH result: "+result);
        	return result;
        }
        return "{}";
    }

    @Override
    public String computeCourseUserPaths(List<Long> courseIds, Long startTime, Long endTime) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QCourseUserPaths qUserPath = ProxyFactory.create(QCourseUserPaths.class, QUESTIONS_BASE_URL);
            if(qUserPath != null) {
                String result = qUserPath.compute(courseIds, startTime, endTime);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return "{}";
    }
    
    @Override
    public String computeQFrequentPathBIDE(
	    		List<Long> courseIds, 
	    		List<Long> userIds,
	    		List<String> types,
	    		Long minLength,
	    		Long maxLength,
	    		Double minSup, 
	    		Boolean sessionWise,
	    		Long startTime,
	    		Long endTime)  {
    	System.out.println("Starte BIDE Request");
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QFrequentPathsBIDE qFrequentPath = ProxyFactory.create(QFrequentPathsBIDE.class, QUESTIONS_BASE_URL);
            if(qFrequentPath != null) {
                String result = qFrequentPath.compute(courseIds, userIds, types, minLength, maxLength, minSup, sessionWise, startTime, endTime);
                System.out.println("BIDE result: "+result);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return "{}";
    }


	@Override
	public String computeCumulativeUserAccess(
				List<Long> courseIds,
				List<String> types, 
				List<Long> departments, 
				List<Long> degrees,
				Long startTime, 
				Long endTime) {
		 logger.debug("Starting CumulativeUserAnalysis ... ");
		 try {
	            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
	            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

	            if(response.getStatus() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	            }

	            QCumulativeUserAccess qCumulativeAnalysis = ProxyFactory.create(QCumulativeUserAccess.class, QUESTIONS_BASE_URL);
	            if(qCumulativeAnalysis != null) {
	                String result = qCumulativeAnalysis.compute(courseIds, types, departments, degrees, startTime, endTime);
	                logger.debug("CumulativeUserAnalysis result: "+result);
	                return result;
	            }

	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        logger.debug("Error while during communication with DMS. Empty resultset returned");
	        return "{}";
	}

}
