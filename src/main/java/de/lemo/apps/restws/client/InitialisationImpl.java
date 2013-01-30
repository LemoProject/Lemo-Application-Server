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
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.restws.proxies.service.ServiceCourseDetails;
import de.lemo.apps.restws.proxies.service.ServiceRatedObjects;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

public class InitialisationImpl implements Initialisation {

	public static final String DMS_BASE_URL = ServerConfiguration.getInstance().getDMSBaseUrl();
	public static final String SERVICE_STARTTIME_URL = InitialisationImpl.DMS_BASE_URL + "/services/starttime";
	private static final String SERVICE_COURSE_URL = InitialisationImpl.DMS_BASE_URL + "/services/courses";
	private static final String SERVICE_RATED_OBJECTS_URL = InitialisationImpl.DMS_BASE_URL + "/services/ratedobjects";

	public InitialisationImpl() {
		// Initialise the Rest session
		// logger.info("Register Rest Service");
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}

	public Date getStartTime() {
		// Create resource delegate
		// logger.info("Getting Server Starttime");
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final ServiceStartTime starttime = ProxyFactory.create(ServiceStartTime.class, InitialisationImpl.SERVICE_STARTTIME_URL);
			if (starttime != null) {
				return new Date(starttime.startTimeJson().getTime());
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public ResultListCourseObject getCoursesDetails(final List<Long> ids) {
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class, InitialisationImpl.SERVICE_COURSE_URL);
			if (courseDetails != null) {

				final ResultListCourseObject result = courseDetails.getCoursesDetails(ids);
				return result;
			}

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

		} catch (final IOException e) {

			e.printStackTrace();

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new ResultListCourseObject();
	}

	public CourseObject getCourseDetails(final Long id) {
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final ServiceCourseDetails courseDetails = ProxyFactory.create(ServiceCourseDetails.class, InitialisationImpl.SERVICE_COURSE_URL);
			if (courseDetails != null) {

				final CourseObject result = courseDetails.getCourseDetails(id);
				return result;
			}

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

		} catch (final IOException e) {

			e.printStackTrace();

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new CourseObject();
	}

	public ResultListStringObject getRatedObjects(final List<Long> courseIds) {
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final ServiceRatedObjects ratedObjects = ProxyFactory
					.create(ServiceRatedObjects.class, InitialisationImpl.SERVICE_RATED_OBJECTS_URL);
			if (ratedObjects != null) {

				final ResultListStringObject result = ratedObjects.getRatedObjects(courseIds);
				return result;
			}

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

		} catch (final IOException e) {

			e.printStackTrace();

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new ResultListStringObject();
	}

}
