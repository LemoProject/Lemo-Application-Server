/**
 * 
 */
package de.lemo.apps.restws.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ProxyFactory;
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

	@Override
	public HashMap<Long, ResultListLongObject> computeCourseActivity(
			final List<Long> courses,
			final List<Long> roles,
			final List<Long> users,
			final Long starttime,
			final Long endtime,
			final Long resolution,
			final List<String> resourceTypes) {

		try {

			if (init.defaultConnectionCheck()) {

				final QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qcourseActivity != null) {

					final String resultString = qcourseActivity.compute(courses, roles, users, starttime, endtime,
							resolution,
							resourceTypes);

					return datahelper.convertJSONStringToResultListHashMap(resultString);
				}
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new HashMap<Long, ResultListLongObject>();
	}

	@Override
	public ResultListResourceRequestInfo computeCourseActivityExtended(final List<Long> courses, final Long startTime,
			final Long endTime,
			final List<String> resourceTypes) {

		try {

			if (init.defaultConnectionCheck()) {

				final QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qActivityResourceType != null) {

					final ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime,
							endTime,
							resourceTypes);

					return result;
				}
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new ResultListResourceRequestInfo();
	}

	@Override
	public ResultListRRITypes computeCourseActivityExtendedDetails(final List<Long> courses, final Long startTime,
			final Long endTime,
			final Long resolution, final List<String> resourceTypes) {

		try {
			if (init.defaultConnectionCheck()) {

				final QActivityResourceTypeResolution qActivityResourceType = ProxyFactory
						.create(QActivityResourceTypeResolution.class,
								AnalysisImpl.QUESTIONS_BASE_URL);
				if (qActivityResourceType != null) {

					if ((resourceTypes != null) && (resourceTypes.size() > 0)) {
						for (int i = 0; i < resourceTypes.size(); i++) {
							this.logger.info("Course Activity Request - CA Selection: " + resourceTypes.get(i));
						}
					} else {
						this.logger.info("Course Activity Request - CA Selection: NO Items selected ");
					}

					final ResultListRRITypes result = qActivityResourceType.compute(courses, startTime, endTime,
							resolution,
							resourceTypes);

					return result;
				}
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new ResultListRRITypes();
	}

	@Override
	public ResultListResourceRequestInfo computeLearningObjectUsage(final List<Long> courseIds,
			final List<Long> userIds,
			final List<String> types, final Long startTime, final Long endTime) {

		try {

			if (init.defaultConnectionCheck()) {

				final QLearningObjectUsage qLOUsage = ProxyFactory.create(QLearningObjectUsage.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qLOUsage != null) {
					if ((types != null) && (types.size() > 0)) {
						for (int i = 0; i < types.size(); i++) {
							this.logger.debug("LO Request - LO Selection: " + types.get(i));
						}
					} else {
						this.logger.debug("LO Request - LO Selection: NO Items selected ");
					}

					final ResultListResourceRequestInfo result = qLOUsage.compute(courseIds, userIds, types, startTime,
							endTime);
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
		return new ResultListResourceRequestInfo();
	}

	@Override
	public ResultListLongObject computeCourseUsers(
			final List<Long> courseIds,
			final Long startTime,
			final Long endTime) {
		ResultListLongObject result = null;
		final QCourseUsers analysis = ProxyFactory.create(QCourseUsers.class, AnalysisImpl.QUESTIONS_BASE_URL);
		if (analysis != null) {
			result = analysis.compute(courseIds, startTime, endTime);
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

		final QUserPathAnalysis analysis = ProxyFactory
				.create(QUserPathAnalysis.class, AnalysisImpl.QUESTIONS_BASE_URL);
		if (analysis != null) {
			final String result = analysis.compute(courseIds, userIds, types, considerLogouts, startTime, endTime);
			System.out.println("PATH result: " + result);
			return result;
		}
		return "{}";
	}

	@Override
	public String computeCourseUserPaths(final List<Long> courseIds, final Long startTime, final Long endTime) {

		try {

			if (init.defaultConnectionCheck()) {

				final QCourseUserPaths qUserPath = ProxyFactory.create(QCourseUserPaths.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qUserPath != null) {
					final String result = qUserPath.compute(courseIds, startTime, endTime);
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
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
		System.out.println("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				final QFrequentPathsBIDE qFrequentPath = ProxyFactory.create(QFrequentPathsBIDE.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qFrequentPath != null) {
					final String result = qFrequentPath.compute(courseIds, userIds, types, minLength, maxLength,
							minSup,
							sessionWise, startTime, endTime);
					System.out.println("BIDE result: " + result);
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
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
		System.out.println("Starte BIDE Request");
		try {

			if (init.defaultConnectionCheck()) {

				final QFrequentPathsViger qFrequentPath = ProxyFactory.create(QFrequentPathsViger.class,
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qFrequentPath != null) {
					final String result = qFrequentPath.compute(courseIds, userIds, types, minLength, maxLength,
							minSup,
							sessionWise, startTime, endTime);
					System.out.println("BIDE result: " + result);
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurueck");
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
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qCumulativeAnalysis != null) {
					final String result = qCumulativeAnalysis.compute(courseIds, types, departments, degrees,
							startTime, endTime);
					this.logger.debug("CumulativeUserAnalysis result: " + result);
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
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
						AnalysisImpl.QUESTIONS_BASE_URL);
				if (qPerformanceHistogram != null) {
					List<Long> result;
					final ResultListLongObject tmpresult = qPerformanceHistogram.compute(courses, users, quizzes,
							resolution,
							startTime, endTime);
					if (tmpresult == null) {
						result = new ArrayList<Long>();
					} else {
						result = tmpresult.getElements();
					}
					return result;
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
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

				final QPerformanceBoxPlot qPerformanceBoxPlot = ProxyFactory
						.create(QPerformanceBoxPlot.class, AnalysisImpl.QUESTIONS_BASE_URL);
				if (qPerformanceBoxPlot != null) {
					final String result = qPerformanceBoxPlot.compute(courses, users, quizzes, resolution, startTime,
							endTime);
					this.logger.debug("Performance Cumulative result: " + result);
					return result;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "{}";
	}

}
