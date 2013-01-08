package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.proxies.service.ServiceCourseDetails;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

public class InitialisationImpl implements Initialisation {
     
    public static final String DMS_BASE_URL = ServerConfiguration.getInstance().getDMSBaseUrl();
    public static final String SERVICE_STARTTIME_URL = DMS_BASE_URL + "/services/starttime";
    private static final String SERVICE_COURSE_URL = DMS_BASE_URL + "/services/courses";

    public InitialisationImpl() {
        // Initialise the Rest session
        // logger.info("Register Rest Service");
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    public Date getStartTime() {
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

            ServiceStartTime starttime = ProxyFactory.create(ServiceStartTime.class, SERVICE_STARTTIME_URL);
            if(starttime != null) {
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

    public ResultListCourseObject getCoursesDetails(List<Long> ids) {
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class, SERVICE_COURSE_URL);
            if(courseDetails != null) {

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

    public CourseObject getCourseDetails(Long id) {
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class, SERVICE_COURSE_URL);
            if(courseDetails != null) {

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
