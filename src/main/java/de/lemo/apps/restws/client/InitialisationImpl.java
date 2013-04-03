package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.restws.proxies.service.ServiceCourseDetails;
import de.lemo.apps.restws.proxies.service.ServiceLoginAuthentification;
import de.lemo.apps.restws.proxies.service.ServiceRatedObjects;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;
import de.lemo.apps.restws.proxies.service.ServiceTeacherCourses;
import de.lemo.apps.restws.proxies.service.ServiceUserInformation;

public class InitialisationImpl implements Initialisation {

	public static final String DMS_BASE_URL = ServerConfiguration.getInstance().getDMSBaseUrl();
	private static final String SERVICE_BASE_URL = "/services";
	public static final String SERVICE_PREFIX_URL = InitialisationImpl.DMS_BASE_URL
			+ InitialisationImpl.SERVICE_BASE_URL;
	private static final String SERVICE_STARTTIME_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/starttime";
	private static final String SERVICE_COURSE_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/courses";
	private static final String SERVICE_RATED_OBJECTS_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/ratedobjects";
	private static final String SERVICE_AUTH_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/authentification";
	private static final String SERVICE_USER_COURSES_URL = InitialisationImpl.SERVICE_PREFIX_URL + "/teachercourses";
	private static final int STATUS_ID = 200;

	private Logger logger;

	public InitialisationImpl(Logger logger) {
		// Initialise the Rest session
		this.logger = logger;
		this.logger.info("Register Rest Service");
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	}

	private static ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
	// TODO set better pool size
	private static HttpClient httpClient = new DefaultHttpClient(connectionManager);
	private static ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

	public Boolean defaultConnectionCheck() throws RestServiceCommunicationException {

		try {
			ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL, clientExecutor);
			ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != STATUS_ID) {
				throw new ClientProtocolException("Default Connection Check: Failed : HTTP error code : "
						+ response.getStatus());
			}
			response.releaseConnection();
			return true;
		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}

	}

	public Date getStartTime() throws RestServiceCommunicationException {

		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != STATUS_ID) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final ServiceStartTime serviceProxy = ProxyFactory.create(ServiceStartTime.class,
					InitialisationImpl.SERVICE_STARTTIME_URL);
			response.releaseConnection();
			if (serviceProxy != null) {
				return new Date(serviceProxy.startTimeJson().getTime());
			}
			return null;
		} catch (final ClientProtocolException e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		} catch (final IOException e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}

	}

	public ResultListCourseObject getCoursesDetails(final List<Long> ids) throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceCourseDetails serviceProxy = ProxyFactory.create(ServiceCourseDetails.class,
						InitialisationImpl.SERVICE_COURSE_URL);
				if (serviceProxy != null) {
					ResultListCourseObject result = serviceProxy.getCoursesDetails(ids);
				}
			}
			logger.info("No course details found. Returning empty resultset.");
			return new ResultListCourseObject();
		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}
	}

	public CourseObject getCourseDetails(final Long id) throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceCourseDetails serviceProxy = ProxyFactory.create(ServiceCourseDetails.class,
						InitialisationImpl.SERVICE_COURSE_URL);
				if (serviceProxy != null) {

					return serviceProxy.getCourseDetails(id);

				}
			}

			logger.info("No course details found. Returning empty resultset.");
			return new CourseObject();

		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}
	}

	public ResultListStringObject getRatedObjects(final List<Long> courseIds) throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceRatedObjects serviceProxy = ProxyFactory
						.create(ServiceRatedObjects.class, InitialisationImpl.SERVICE_RATED_OBJECTS_URL);
				if (serviceProxy != null) {

					return serviceProxy.getRatedObjects(courseIds);

				}
			}
			logger.info("No Rated objects found. Returning empty resultset.");
			return new ResultListStringObject();
		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}
	}

	public ResultListLongObject identifyUserName(final String login) throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceLoginAuthentification serviceProxy = ProxyFactory.create(
						ServiceLoginAuthentification.class, InitialisationImpl.SERVICE_AUTH_URL);
				if (serviceProxy != null) {
					return serviceProxy.authentificateUser(login);
				}
			}

		} catch (final Exception e) {

			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());

		}

		logger.info("User could not be identified. Returning empty resultset.");
		return new ResultListLongObject();

	}

	public ResultListLongObject getUserCourses(Long userId) throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceTeacherCourses serviceProxy = ProxyFactory.create(ServiceTeacherCourses.class,
						InitialisationImpl.SERVICE_USER_COURSES_URL);
				if (serviceProxy != null) {
					return serviceProxy.getTeachersCourses(userId);
				}
			}

		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}

		logger.info("Courses could not be loaded. Returning empty resultset.");
		return new ResultListLongObject();

	}

	public ResultListCourseObject getUserCourses(Long userId, Long amount, Long offset)
			throws RestServiceCommunicationException {

		try {

			if (defaultConnectionCheck()) {
				final ServiceUserInformation serviceProxy = ProxyFactory.create(ServiceUserInformation.class,
						InitialisationImpl.SERVICE_PREFIX_URL);
				if (serviceProxy != null) {
					return serviceProxy.getCoursesByUser(userId, amount, offset);
				}
			}

		} catch (final Exception e) {
			throw new RestServiceCommunicationException(this.toString() + " " + e.getLocalizedMessage());
		}

		logger.info("Courses could not be loaded. Returning empty resultset.");
		return new ResultListCourseObject();

	}

}
