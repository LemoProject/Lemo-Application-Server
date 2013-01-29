/**
 * File ./de/lemo/apps/restws/client/AnalysisImpl.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

/**
 * 
 */
package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import de.lemo.apps.restws.entities.ResultListHashMapObject;
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
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

/**
 * @author johndoe
 */
public class AnalysisImpl implements Analysis {

	private static final String QUESTIONS_BASE_URL = InitialisationImpl.DMS_BASE_URL + "/questions";
	@Inject
	private Logger logger;

	@Override
	public HashMap<Long, ResultListLongObject> computeCourseActivity(final List<Long> courses, final List<Long> roles,
			final List<Long> users, final Long starttime, final Long endtime,
			final int resolution, final List<String> resourceTypes) {
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

			final QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qcourseActivity != null) {

				final String resultString = qcourseActivity.compute(courses, roles, users, starttime, endtime,
						resolution, resourceTypes);
				final ResultListHashMapObject resultObject = new ResultListHashMapObject();
				// logger.debug("CourseActivity ResultList: "+resultString);

				final ObjectMapper mapper = new ObjectMapper();
				final JsonNode jsonObj = mapper.readTree(resultString);
				final JsonNode entries = jsonObj.get("entries");
				final JsonNode keys = jsonObj.get("keys");
				final HashMap<Long, ResultListLongObject> resultList = new HashMap<Long, ResultListLongObject>();
				if ((entries != null) && (keys != null)) {
					this.logger.debug("Entries: " + jsonObj.get("entries").toString());
					// if more than one course result is returned
					if (entries.isArray() && keys.isArray()) {
						final Iterator<JsonNode> kit = keys.getElements();
						final Iterator<JsonNode> eit = entries.getElements();
						List<Long> entryList = new ArrayList<Long>();
						while (kit.hasNext()) {
							final Long keyValue = Long.parseLong(kit.next().getValueAsText());
							final JsonNode entryNodes = eit.next();
							final JsonNode entryNodesArray = entryNodes.get("elements");
							// logger.debug("EntryNodes Complete:"+entryNodesArray);
							if (entryNodesArray.isArray()) {
								this.logger.debug("Entries is Array ....");
								entryList = new ArrayList<Long>();
								final Iterator<JsonNode> enit = entryNodesArray.getElements();
								while (enit.hasNext()) {
									final Long enitValue = enit.next().getValueAsLong(0L);
									// logger.debug("Entry Values: "+enitValue);
									entryList.add(enitValue);
								}
							} else {
								this.logger.debug("Entries is NO Array ....");
							}
							final ResultListLongObject entryValues = new ResultListLongObject(entryList);
							resultList.put(keyValue, entryValues);

							this.logger.debug("Course Key: " + keyValue);
						}
						// if only one course result is returned
					} else {
						final Long keyValue = Long.parseLong(keys.getValueAsText());
						final JsonNode entryNodesArray = entries.get("elements");
						final List<Long> entryList = new ArrayList<Long>();
						// logger.debug("EntryNodes :"+entryNodesArray);

						if (entryNodesArray.isArray()) {
							this.logger.debug("Entries is Array ....");
							final Iterator<JsonNode> enit = entryNodesArray.getElements();
							while (enit.hasNext()) {
								final Long enitValue = enit.next().getValueAsLong(0L);
								// logger.debug("Entry Values: "+enitValue);
								entryList.add(enitValue);
							}
						} else {
							this.logger.debug("Entries is NO Array ....");
						}
						final ResultListLongObject entryValues = new ResultListLongObject(entryList);
						resultList.put(keyValue, entryValues);

						this.logger.debug("Course Key: " + keyValue);

					}
				} else {
					this.logger.debug("Entries not found!");
				}

				// if(resultObject!=null && resultObject.getElements()!=null){
				// Set<Long> keySet = resultObject.getElements().keySet();
				// Iterator<Long> it = keySet.iterator();
				// while(it.hasNext()){
				// Long learnObjectTypeName = it.next();
				// logger.debug("Result Course IDs: "+learnObjectTypeName);
				// }
				//
				// } else logger.debug("Empty resultset !!!");

				// ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
				// List<Long> resultList = new ArrayList<Long>();
				// for (int i=0; i<result.getElements().size();i++) {
				// Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
				// resultList.add(i, value);
				//
				// return new ResultList(resultList);
				// }

				return resultList;// resultObject.getElements();
			}

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

		} catch (final IOException e) {

			e.printStackTrace();

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zurück");
		return new HashMap<Long, ResultListLongObject>();
	}

	// @Override
	// public String computeQ1JSON(List<Long> courses, List<Long> roles, Long starttime, Long endtime,
	// int resolution, List<String> resourceTypes) {
	// // Create resource delegate
	// // logger.info("Getting Server Starttime");
	// try {
	// ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
	//
	// ClientResponse<ServiceStartTime> response;
	//
	// response = request.get(ServiceStartTime.class);
	//
	// if(response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
	// QUESTIONS_BASE_URL);
	// if(qcourseActivity != null) {
	//
	// String result = qcourseActivity.compute(courses, roles, starttime, endtime, resolution,resourceTypes);
	// // if(result!=null && result.getElements()!=null){
	// // ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
	// // List<Long> resultList = new ArrayList<Long>();
	// // for (int i=0; i<result.getElements().size();i++) {
	// // Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
	// // resultList.add(i, value);
	// // }
	// //
	// // return new ResultList(resultList);
	// // }
	// return result;
	// }
	//
	// } catch (ClientProtocolException e) {
	//
	// e.printStackTrace();
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("Gebe leere Resultlist zurück");
	// return "";
	// }

	@Override
	public ResultListResourceRequestInfo computeCourseActivityExtended(final List<Long> courses, final Long startTime,
			final Long endTime,
			final List<String> resourceTypes) {

		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			final QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qActivityResourceType != null) {

				final ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime, endTime,
						resourceTypes);

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
		System.out.println("Gebe leere Resultlist zur�ck");
		return new ResultListResourceRequestInfo();
	}

	// @Override
	// public String computeQ1ExtendedJSON(List<Long> courses, Long startTime, Long endTime,
	// List<String> resourceTypes) {
	//
	// try {
	// ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
	//
	// ClientResponse<ServiceStartTime> response;
	//
	// response = request.get(ServiceStartTime.class);
	//
	// if(response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// QActivityResourceTypeString qActivityResourceType = ProxyFactory.create(QActivityResourceTypeString.class,
	// QUESTIONS_BASE_URL);
	// if(qActivityResourceType != null) {
	//
	// String result = qActivityResourceType.compute(courses, startTime, endTime,resourceTypes);
	//
	// return result;
	// }
	//
	// } catch (ClientProtocolException e) {
	//
	// e.printStackTrace();
	//
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("Gebe leere Resultlist zurück");
	// return "";
	// }
	//

	@Override
	public ResultListRRITypes computeCourseActivityExtendedDetails(final List<Long> courses, final Long startTime,
			final Long endTime,
			final Long resolution, final List<String> resourceTypes) {

		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);

			ClientResponse<ServiceStartTime> response;

			response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

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

		} catch (final ClientProtocolException e) {

			e.printStackTrace();

		} catch (final IOException e) {

			e.printStackTrace();

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gebe leere Resultlist zur�ck");
		return new ResultListRRITypes();
	}

	@Override
	public ResultListResourceRequestInfo computeLearningObjectUsage(final List<Long> courseIds,
			final List<Long> userIds, final List<String> types, final Long startTime, final Long endTime) {

		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

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

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QCourseUserPaths qUserPath = ProxyFactory.create(QCourseUserPaths.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qUserPath != null) {
				final String result = qUserPath.compute(courseIds, startTime, endTime);
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QFrequentPathsBIDE qFrequentPath = ProxyFactory.create(QFrequentPathsBIDE.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qFrequentPath != null) {
				final String result = qFrequentPath.compute(courseIds, userIds, types, minLength, maxLength, minSup,
						sessionWise, startTime, endTime);
				System.out.println("BIDE result: " + result);
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QFrequentPathsViger qFrequentPath = ProxyFactory.create(QFrequentPathsViger.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qFrequentPath != null) {
				final String result = qFrequentPath.compute(courseIds, userIds, types, minLength, maxLength, minSup,
						sessionWise, startTime, endTime);
				System.out.println("BIDE result: " + result);
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QCumulativeUserAccess qCumulativeAnalysis = ProxyFactory.create(QCumulativeUserAccess.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qCumulativeAnalysis != null) {
				final String result = qCumulativeAnalysis.compute(courseIds, types, departments, degrees, startTime,
						endTime);
				this.logger.debug("CumulativeUserAnalysis result: " + result);
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final Integer resolution,
			final Long startTime,
			final Long endTime) {
		this.logger.debug("Starting Performance histogram Analysis ... ");
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QPerformanceHistogram qPerformanceHistogram = ProxyFactory.create(QPerformanceHistogram.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qPerformanceHistogram != null) {
				List<Long> result;
				final ResultListLongObject tmpresult = qPerformanceHistogram.compute(courses, users, quizzes,
						resolution, startTime, endTime);
				if (tmpresult == null) {
					result = new ArrayList<Long>();
				} else {
					result = tmpresult.getElements();
				}
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
			final Long startTime,
			final Long endTime) {
		this.logger.debug("Starting Performance Cumulative Analysis ... ");
		try {
			final ClientRequest request = new ClientRequest(InitialisationImpl.SERVICE_STARTTIME_URL);
			final ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			final QPerformanceBoxPlot qPerformanceBoxPlot = ProxyFactory.create(QPerformanceBoxPlot.class,
					AnalysisImpl.QUESTIONS_BASE_URL);
			if (qPerformanceBoxPlot != null) {
				final String result = qPerformanceBoxPlot.compute(courses, users, quizzes, startTime, endTime);
				this.logger.debug("Performance Cumulative result: " + result);
				return result;
			}

		} catch (final ClientProtocolException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.logger.debug("Error while during communication with DMS. Empty resultset returned");
		return "{}";
	}

}
