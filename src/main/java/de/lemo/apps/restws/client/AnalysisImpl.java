/**
 * File ./src/main/java/de/lemo/apps/restws/client/AnalysisImpl.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
 * 
 */
package de.lemo.apps.restws.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.slf4j.Logger;
import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.proxies.questions.QActivityResourceType;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeResolution;
import de.lemo.apps.restws.proxies.questions.QCourseActivityString;
import de.lemo.apps.restws.proxies.questions.QCourseUserPaths;
import de.lemo.apps.restws.proxies.questions.QCourseUsers;
import de.lemo.apps.restws.proxies.questions.QCumulativeUserAccess;
import de.lemo.apps.restws.proxies.questions.QFrequentPathsBIDE;
import de.lemo.apps.restws.proxies.questions.QFrequentPathsViger;
import de.lemo.apps.restws.proxies.questions.QLearningObjectUsage;
import de.lemo.apps.restws.proxies.questions.QPerformanceBoxPlot;
import de.lemo.apps.restws.proxies.questions.QPerformanceHistogram;
import de.lemo.apps.restws.proxies.questions.QPerformanceUserTest;
import de.lemo.apps.restws.proxies.questions.QPerformanceUserTestBoxPlot;
import de.lemo.apps.restws.proxies.questions.QUserPathAnalysis;

/**
 * @author Andreas Pursian
 */
public class AnalysisImpl implements Analysis {

	@Inject
	private Initialisation init;

	@Inject
	private DataHelper datahelper;

	@Inject
	private Logger logger;

	private static final String QUESTIONS_BASE_URL = ServerConfiguration.getInstance().getDMSBaseUrl() + "/questions";

	private PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
	private HttpClient httpClient = new DefaultHttpClient(connectionManager);
	{
		HttpParams params = this.httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 20000);
	}

	private ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

	private QCourseActivityString qcourseActivity =
			ProxyFactory.create(QCourseActivityString.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QActivityResourceType qActivityResourceType =
			ProxyFactory.create(QActivityResourceType.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QActivityResourceTypeResolution qActivityResourceTypeResolution =
			ProxyFactory.create(QActivityResourceTypeResolution.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QLearningObjectUsage qLOUsage =
			ProxyFactory.create(QLearningObjectUsage.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QCourseUsers courseUsers =
			ProxyFactory.create(QCourseUsers.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QUserPathAnalysis userPathAnalysis =
			ProxyFactory.create(QUserPathAnalysis.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QCourseUserPaths qUserPath =
			ProxyFactory.create(QCourseUserPaths.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QFrequentPathsBIDE qFrequentPathBide =
			ProxyFactory.create(QFrequentPathsBIDE.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QFrequentPathsViger qFrequentPathViger =
			ProxyFactory.create(QFrequentPathsViger.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QCumulativeUserAccess qCumulativeAnalysis =
			ProxyFactory.create(QCumulativeUserAccess.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QPerformanceHistogram qPerformanceHistogram =
			ProxyFactory.create(QPerformanceHistogram.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QPerformanceBoxPlot qPerformanceBoxPlot =
			ProxyFactory.create(QPerformanceBoxPlot.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QPerformanceUserTest qPerformanceUserTest =
			ProxyFactory.create(QPerformanceUserTest.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	private QPerformanceUserTestBoxPlot qPerformanceUserTestBoxPlot =
			ProxyFactory.create(QPerformanceUserTestBoxPlot.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	@Override
	public Map<Long, ResultListLongObject> computeCourseActivity(
			final List<Long> courses,
			final List<Long> users,
			final Long starttime,
			final Long endtime,
			final Long resolution,
			final List<String> resourceTypes,
			final List<Long> gender) {

		try {

			if (init.defaultConnectionCheck()) {

				String result = qcourseActivity.compute(courses, users, starttime, endtime,
						resolution, resourceTypes, gender);

				return datahelper.convertJSONStringToResultListHashMap(result);

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return new HashMap<Long, ResultListLongObject>();
	}

	@Override
	public ResultListResourceRequestInfo computeCourseActivityExtended(final List<Long> courses, final Long startTime,
			final Long endTime,
			final List<String> resourceTypes,
			final List<Long> gender) {

		try {

			if (init.defaultConnectionCheck()) {
				ResultListResourceRequestInfo result = qActivityResourceType.compute(
						courses, startTime, endTime, resourceTypes, gender);
				return result;
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return new ResultListResourceRequestInfo();
	}

	@Override
	public ResultListRRITypes computeCourseActivityExtendedDetails(final List<Long> courses, final Long startTime,
			final Long endTime,
			final Long resolution, final List<String> resourceTypes,
			final List<Long> gender) {

		try {
			if (init.defaultConnectionCheck()) {

				if ((resourceTypes != null) && (resourceTypes.size() > 0)) {
					for (int i = 0; i < resourceTypes.size(); i++) {
						this.logger.info("Course Activity Request - CA Selection: " + resourceTypes.get(i));
					}
				} else {
					this.logger.info("Course Activity Request - CA Selection: NO Items selected ");
				}

				ResultListRRITypes result = qActivityResourceTypeResolution.compute(courses,
						startTime, endTime, resolution, resourceTypes, gender);

				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return new ResultListRRITypes();
	}

	@Override
	public ResultListResourceRequestInfo computeLearningObjectUsage(final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types, final Long startTime, final Long endTime,
			final List<Long> gender) {

		try {

			if (init.defaultConnectionCheck()) {

				if ((types != null) && (types.size() > 0)) {
					for (int i = 0; i < types.size(); i++) {
						this.logger.debug("LO Request - LO Selection: " + types.get(i));
					}
				} else {
					this.logger.debug("LO Request - LO Selection: NO Items selected ");
				}

				ResultListResourceRequestInfo result = qLOUsage.compute(courseIds, userIds, types, startTime,
						endTime, gender);

				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Returning empty result set.");
		return new ResultListResourceRequestInfo();
	}

	@Override
	public ResultListLongObject computeCourseUsers(
			final List<Long> courseIds,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		ResultListLongObject result = new ResultListLongObject();
		
		try {
			if (init.defaultConnectionCheck()) {
			
				result = courseUsers.compute(courseIds, startTime, endTime, gender);
				
			}
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Could not execute user query.");
		return result;
	}

	@Override
	public String computeUserPathAnalysis(
			final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types,
			final Boolean considerLogouts,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {

		String result = userPathAnalysis.compute(courseIds, userIds, types, considerLogouts, startTime, endTime, gender);
		return result;
	}

	@Override
	public String computeCourseUserPaths(final List<Long> courseIds, final Long startTime, final Long endTime,
										final List<Long> gender) {

		try {

			if (init.defaultConnectionCheck()) {
				String result = qUserPath.compute(courseIds, startTime, endTime, gender);
				return result;
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return "{}";
	}

	@Override
	public String computeQFrequentPathBIDE(
			final Long lemoUserId,
			final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types,
			final Long minLength,
			final Long maxLength,
			final Double minSup,
			final Boolean sessionWise,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		logger.info("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				Response response = qFrequentPathBide.compute(lemoUserId, courseIds, userIds, types, minLength,
						maxLength, minSup, sessionWise, startTime, endTime, gender);

				if (response.getStatus() == HttpStatus.SC_CREATED) {
					logger.debug("BIDE future result created.");

					// return the id of the result for polling
					// TODO use some random path instead of insecure user id
					@SuppressWarnings("unchecked")
					ClientResponse<String> clientResponse = (ClientResponse<String>) response;
					String resultPath = clientResponse.getEntity(String.class);

					// XXX why is this even quoted?
					resultPath = StringUtils.strip(resultPath, "\"");

					return resultPath;
				} 
				// TODO do something on failure
				logger.warn("BIDE invalid response: Status code " + response.getStatus());
				return "null";
			}

		} catch (final Exception e) {
			logger.error("Bide failed", e);
		}
		logger.info("Gebe leere Resultlist zurueck");
		return "null";
	}

	@Override
	public String computeQFrequentPathViger(
			final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types,
			final Long minLength,
			final Long maxLength,
			final Double minSup,
			final Boolean sessionWise,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		logger.info("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				String result = qFrequentPathViger.compute(courseIds, userIds, types, minLength,
						maxLength, minSup, sessionWise, startTime, endTime, gender);
				logger.info("BIDE result: " + result);

				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return "{}";
	}

	@Override
	public String computeCumulativeUserAccess(
			final List<Long> courseIds,
			final List<String> types,
			final List<Long> departments,
			final List<Long> degrees,
			final Long startTime,
			final Long endTime) {
		this.logger.debug("Starting CumulativeUserAnalysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				String result = qCumulativeAnalysis.compute(courseIds, types, departments, degrees, startTime,
						endTime);

				this.logger.debug("CumulativeUserAnalysis result: " + result);

				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "{}";
	}

	@Override
	public List<Long> computePerformanceHistogram(
			final List<Long> courses,
			final List<Long> users,
			final List<Long> quizzes,
			final Long resolution,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		this.logger.debug("Starting Performance histogram Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				List<Long> result;
				ResultListLongObject response = qPerformanceHistogram.compute(courses, users,
						quizzes, resolution, startTime, endTime, gender);
				if (response == null) {
					result = new ArrayList<Long>();
				} else {
					result = response.getElements();
				}

				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return new ArrayList<Long>();
	}

	@Override
	public String computePerformanceBoxplot(
			final List<Long> courses,
			final List<Long> users,
			final List<Long> quizzes,
			final Long resolution,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		this.logger.debug("Starting Performance Cumulative Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				String result = qPerformanceBoxPlot.compute(courses, users, quizzes, resolution, startTime,
						endTime, gender);

				this.logger.debug("Performance Cumulative result: " + result);

				return result;

			}
		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "{}";
	}

	@Override
	public List<Long> computePerformanceUserTest(
			final List<Long> courses,
			final List<Long> users,
			final List<Long> quizzes,
			final Long resolution,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		this.logger.debug("Starting Performance user test Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				List<Long> result;
				ResultListLongObject response = qPerformanceUserTest.compute(courses, users, quizzes, resolution,
						startTime, endTime, gender);
				if (response == null) {
					result = new ArrayList<Long>();
				} else {
					result = response.getElements();
				}

				return result;
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return new ArrayList<Long>();
	}

	@Override
	public String computePerformanceUserTestBoxPlot(
			final List<Long> courses,
			final List<Long> users,
			final List<Long> quizzes,
			final Long resolution,
			final Long startTime,
			final Long endTime,
			final List<Long> gender) {
		this.logger.debug("Starting Performance user test Boxplot Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				String result = qPerformanceUserTestBoxPlot.compute(courses, users,
						quizzes, resolution, startTime, endTime, gender);

				if (result == null) {
					result = "";
				}
				return result;

			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "";
	}

}
