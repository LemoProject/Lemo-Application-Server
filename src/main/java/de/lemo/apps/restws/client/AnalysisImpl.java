/**
 * 
 */
package de.lemo.apps.restws.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.slf4j.Logger;
import de.lemo.apps.application.config.ServerConfiguration;
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

	private static ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
	// TODO set better pool size
	private static HttpClient httpClient = new DefaultHttpClient(connectionManager);
	private static ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

	private final QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
			AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

	@Override
	public Map<Long, ResultListLongObject> computeCourseActivity(
			final List<Long> courses,
			final List<Long> users,
			final Long starttime,
			final Long endtime,
			final Long resolution,
			final List<String> resourceTypes) {

		try {

			if (init.defaultConnectionCheck()) {

				final ClientResponse<String> response = qcourseActivity.compute(courses, users, starttime, endtime,
						resolution, resourceTypes);

				String result = response.getEntity();

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
			final List<String> resourceTypes) {

		try {

			if (init.defaultConnectionCheck()) {

				final QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qActivityResourceType != null) {

					final ClientResponse<ResultListResourceRequestInfo> response = qActivityResourceType.compute(
							courses, startTime, endTime, resourceTypes);
					ResultListResourceRequestInfo result = response.getEntity();

					return result;
				}
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
			final Long resolution, final List<String> resourceTypes) {

		try {
			if (init.defaultConnectionCheck()) {

				final QActivityResourceTypeResolution qActivityResourceType = ProxyFactory.create(
						QActivityResourceTypeResolution.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qActivityResourceType != null) {

					if ((resourceTypes != null) && (resourceTypes.size() > 0)) {
						for (int i = 0; i < resourceTypes.size(); i++) {
							this.logger.info("Course Activity Request - CA Selection: " + resourceTypes.get(i));
						}
					} else {
						this.logger.info("Course Activity Request - CA Selection: NO Items selected ");
					}

					final ClientResponse<ResultListRRITypes> response = qActivityResourceType.compute(courses,
							startTime, endTime,
							resolution, resourceTypes);
					ResultListRRITypes result = response.getEntity();

					return result;
				}
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
			final List<String> types, final Long startTime, final Long endTime) {

		try {

			if (init.defaultConnectionCheck()) {

				final QLearningObjectUsage qLOUsage = ProxyFactory.create(QLearningObjectUsage.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qLOUsage != null) {
					if ((types != null) && (types.size() > 0)) {
						for (int i = 0; i < types.size(); i++) {
							this.logger.debug("LO Request - LO Selection: " + types.get(i));
						}
					} else {
						this.logger.debug("LO Request - LO Selection: NO Items selected ");
					}

					final ClientResponse<ResultListResourceRequestInfo> response = qLOUsage.compute(courseIds, userIds,
							types, startTime,
							endTime);
					ResultListResourceRequestInfo result = response.getEntity();

					return result;
				}
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return new ResultListResourceRequestInfo();
	}

	@Override
	public ResultListLongObject computeCourseUsers(
			final List<Long> courseIds,
			final Long startTime,
			final Long endTime) {
		ResultListLongObject result = null;

		final QCourseUsers analysis = ProxyFactory.create(QCourseUsers.class, AnalysisImpl.QUESTIONS_BASE_URL,
				clientExecutor);
		if (analysis != null) {
			ClientResponse<ResultListLongObject> response = analysis.compute(courseIds, startTime, endTime);
			result = response.getEntity();
			try {

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		if (result == null) {
			result = new ResultListLongObject();
		}

		return result;
	}

	@Override
	public String computeUserPathAnalysis(
			final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types,
			final Boolean considerLogouts,
			final Long startTime,
			final Long endTime) {

		final QUserPathAnalysis analysis = ProxyFactory.create(QUserPathAnalysis.class,
				AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
		if (analysis != null) {
			final ClientResponse<String> response = analysis.compute(courseIds, userIds, types, considerLogouts,
					startTime, endTime);
			String result = response.getEntity();
			try {

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			return result;
		}
		return "{}";
	}

	@Override
	public String computeCourseUserPaths(final List<Long> courseIds, final Long startTime, final Long endTime) {

		try {

			if (init.defaultConnectionCheck()) {

				final QCourseUserPaths qUserPath = ProxyFactory.create(QCourseUserPaths.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);

				ClientResponse<String> response = qUserPath.compute(courseIds, startTime, endTime);
				String result = response.getEntity();

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
			final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types,
			final Long minLength,
			final Long maxLength,
			final Double minSup,
			final Boolean sessionWise,
			final Long startTime,
			final Long endTime) {
		logger.info("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				final QFrequentPathsBIDE qFrequentPath = ProxyFactory.create(QFrequentPathsBIDE.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qFrequentPath != null) {
					final ClientResponse<String> response = qFrequentPath.compute(courseIds, userIds, types, minLength,
							maxLength, minSup, sessionWise, startTime, endTime);
					String result = response.getEntity();

					return result;
				}
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("Gebe leere Resultlist zurueck");
		return "{}";
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
			final Long endTime) {
		logger.info("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				final QFrequentPathsViger qFrequentPath = ProxyFactory.create(QFrequentPathsViger.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qFrequentPath != null) {
					final ClientResponse<String> response = qFrequentPath.compute(courseIds, userIds, types, minLength,
							maxLength, minSup, sessionWise, startTime, endTime);
					String result = response.getEntity();
					logger.info("BIDE result: " + result);

					return result;
				}
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

				final QCumulativeUserAccess qCumulativeAnalysis = ProxyFactory.create(QCumulativeUserAccess.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qCumulativeAnalysis != null) {
					final ClientResponse<String> response = qCumulativeAnalysis.compute(courseIds, types, departments,
							degrees,
							startTime, endTime);

					String result = response.getEntity();
					this.logger.debug("CumulativeUserAnalysis result: " + result);

					return result;
				}
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
			final Long endTime) {
		this.logger.debug("Starting Performance histogram Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				final QPerformanceHistogram qPerformanceHistogram = ProxyFactory.create(QPerformanceHistogram.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qPerformanceHistogram != null) {
					List<Long> result;
					final ClientResponse<ResultListLongObject> response = qPerformanceHistogram.compute(courses, users,
							quizzes, resolution, startTime, endTime);
					if (response == null) {
						result = new ArrayList<Long>();
					} else {
						result = response.getEntity().getElements();
					}

					return result;
				}
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
			final Long endTime) {
		this.logger.debug("Starting Performance Cumulative Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				final QPerformanceBoxPlot qPerformanceBoxPlot = ProxyFactory.create(QPerformanceBoxPlot.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qPerformanceBoxPlot != null) {
					final ClientResponse<String> response = qPerformanceBoxPlot.compute(courses, users, quizzes,
							resolution, startTime, endTime);
					String result = response.getEntity();

					this.logger.debug("Performance Cumulative result: " + result);

					return result;
				}
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
			final Long endTime) {
		this.logger.debug("Starting Performance user test Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				final QPerformanceUserTest qPerformanceUserTest = ProxyFactory.create(QPerformanceUserTest.class,
						AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qPerformanceUserTest != null) {
					List<Long> result;
					final ClientResponse<ResultListLongObject> response = qPerformanceUserTest.compute(courses, users,
							quizzes, resolution, startTime, endTime);
					if (response == null) {
						result = new ArrayList<Long>();
					} else {
						result = response.getEntity().getElements();
					}

					return result;
				}
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
			final Long endTime) {
		this.logger.debug("Starting Performance user test Boxplot Analysis ... ");
		try {

			if (init.defaultConnectionCheck()) {

				final QPerformanceUserTestBoxPlot qPerformanceUserTestBoxPlot = ProxyFactory.create(
						QPerformanceUserTestBoxPlot.class, AnalysisImpl.QUESTIONS_BASE_URL, clientExecutor);
				if (qPerformanceUserTestBoxPlot != null) {
					String result;
					final ClientResponse<String> response = qPerformanceUserTestBoxPlot.compute(courses, users,
							quizzes, resolution, startTime, endTime);

					if (response == null) {
						result = "";
					} else {
						result = response.getEntity();
					}

					return result;
				}
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "";
	}

}
