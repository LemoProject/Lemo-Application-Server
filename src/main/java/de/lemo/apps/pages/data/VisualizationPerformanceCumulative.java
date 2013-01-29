/**
 * File ./de/lemo/apps/pages/data/VisualizationPerformanceCumulative.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.BoxPlot;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

@RequiresAuthentication
@BreadCrumb(titleKey = "visualizationTitle")
@Import(library = { "../../js/d3/d3_custom_PerformanceBoxPlot.js" })
public class VisualizationPerformanceCumulative {

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private AnalysisWorker analysisWorker;

	@Inject
	private Initialisation init;

	@Inject
	private CourseIdValueEncoder courseValueEncoder;

	@Inject
	private Analysis analysis;

	@Inject
	private UserWorker userWorker;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private Locale currentlocale;

	@Inject
	private Messages messages;

	@Inject
	private TypeCoercer coercer;

	@Property
	private BreadCrumbInfo breadCrumb;

	@InjectComponent
	private Zone formZone;

	@Component(id = "customizeForm")
	private Form customizeForm;

	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel;

	@Property
	@Persist
	private Course course;

	@Property
	@Persist
	private Long courseId;

	@Component(id = "beginDate")
	private DateField beginDateField;

	@Component(id = "endDate")
	private DateField endDateField;

	@Persist
	@Property
	private Date beginDate;

	@Persist
	@Property
	private Date endDate;

	@Property
	@Persist
	Integer resolution;

	@Property
	@Persist
	private List<Course> courses;

	// Value Encoder for activity multi-select component
	@Property(write = false)
	private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(this.coercer,
			EResourceType.class);

	// Select Model for activity multi-select component
	@Property(write = false)
	private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, this.messages);

	@Property
	@Persist
	private List<EResourceType> selectedActivities;

	@Inject
	@Property
	private LongValueEncoder userIdEncoder, quizEncoder;

	@Property
	@Persist
	private List<Long> userIds, quizIds;

	@Property
	@Persist
	private List<Long> selectedUsers, selectedQuizzes;

	public List<Long> getUsers() {
		final List<Long> courses = new ArrayList<Long>();
		courses.add(this.course.getCourseId());
		final List<Long> elements = this.analysis.computeCourseUsers(courses, this.beginDate.getTime() / 1000,
				this.endDate.getTime() / 1000).getElements();
		this.logger.info("          ----        " + elements);
		return elements;
	}

	public Object onActivate(final Course course) {
		this.logger.debug("--- Bin im ersten onActivate");
		final List<Long> allowedCourses = this.userWorker.getCurrentUser().getMyCourses();
		if ((allowedCourses != null) && (course != null) && (course.getCourseId() != null)
				&& allowedCourses.contains(course.getCourseId())) {
			this.courseId = course.getCourseId();
			this.course = course;

			return true;
		} else {
			return Explorer.class;
		}
	}

	public Object onActivate() {
		this.logger.debug("--- Bin im zweiten onActivate");
		return true;
	}

	public Course onPassivate() {
		return this.course;
	}

	void cleanupRender() {
		this.customizeForm.clearErrors();
		// Clear the flash-persisted fields to prevent anomalies in onActivate
		// when we hit refresh on page or browser
		// button
		this.courseId = null;
		this.course = null;
		this.selectedUsers = null;
		this.selectedQuizzes = null;
		this.selectedActivities = null;
	}

	// void pageReset() {
	// selectedUsers = null;
	// userIds = getUsers();
	// }

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
		this.courseModel = new CourseIdSelectModel(courses);
		this.userIds = this.getUsers();
		this.quizIds = new ArrayList<Long>();

		final List<Long> courseList = new ArrayList<Long>();
		courseList.add(this.courseId);
		final ResultListStringObject quizList = this.init.getRatedObjects(courseList);

		final Map<Long, String> quizzesMap = CollectionFactory.newMap();
		final List<String> quizzesTitles = new ArrayList<String>();

		if ((quizList != null) && (quizList.getElements() != null))
		{
			this.logger.debug(quizList.getElements().toString());
			final List<String> quizStringList = quizList.getElements();
			for (Integer x = 0; x < quizStringList.size(); x = x + 3) {
				final Long combinedQuizId = Long.parseLong((quizStringList.get(x) + quizStringList.get(x + 1)));
				quizzesMap.put(combinedQuizId, quizStringList.get(x + 2));
				quizzesTitles.add(quizStringList.get(x + 2));
				this.quizIds.add(combinedQuizId);
			}

		} else {
			this.logger.debug("No rated Objetcs found");
		}
	}

	public final ValueEncoder<Course> getCourseValueEncoder() {
		// List<Course> courses =
		// courseDAO.findAllByOwner(userWorker.getCurrentUser());
		return this.courseValueEncoder.create(Course.class);
	}

	// returns datepicker params
	public JSONLiteral getDatePickerParams() {
		return this.dateWorker.getDatePickerParams(this.currentlocale);
	}

	public String getQuestionResult() {
		if (this.courseId != null) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			if (this.endDate != null) {
				endStamp = new Long(this.endDate.getTime() / 1000);
			} // else endtime= 1334447632L;

			if (this.beginDate != null) {
				beginStamp = new Long(this.beginDate.getTime() / 1000);
			} // else starttime = 1308968800L;

			if ((this.resolution == null) || (this.resolution < 10)) {
				this.resolution = 30;
			}
			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courseList = new ArrayList<Long>();
			courseList.add(this.courseId);

			// calling dm-server
			for (int i = 0; i < courseList.size(); i++) {
				this.logger.debug("Courses: " + courseList.get(i));
			}

			// List<String> types = null;
			// if(selectedActivities != null && !selectedActivities.isEmpty()) {
			// types = new ArrayList<String>();
			// for(EResourceType resourceType : selectedActivities) {
			// types.add(resourceType.name().toLowerCase());
			// }
			// }
			//

			List<Long> quizzesList = new ArrayList<Long>();
			// quizzesList.add(11114282L);
			// quizzesList.add(11114861L);

			final ResultListStringObject quizList = this.init.getRatedObjects(courseList);

			final Map<Long, String> quizzesMap = CollectionFactory.newMap();
			final List<String> quizzesTitles = new ArrayList<String>();

			if ((quizList != null) && (quizList.getElements() != null))
			{
				this.logger.debug(quizList.getElements().toString());
				final List<String> quizStringList = quizList.getElements();
				for (Integer x = 0; x < quizStringList.size(); x = x + 3) {
					final Long combinedQuizId = Long.parseLong((quizStringList.get(x) + quizStringList.get(x + 1)));
					quizzesMap.put(combinedQuizId, quizStringList.get(x + 2));
					quizzesTitles.add(quizStringList.get(x + 2));
				}

			} else {
				this.logger.debug("No rated Objetcs found");
			}

			if (this.selectedQuizzes != null) {
				quizzesList = this.selectedQuizzes;
			} else if ((quizzesMap != null) && (quizzesMap.keySet() != null)) {
				quizzesList = new ArrayList<Long>();
				quizzesList.addAll(quizzesMap.keySet());
			}

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution
					+ " QuizzesAmount:" + quizzesList.size());

			final String result = this.analysis.computePerformanceBoxplot(courseList, this.selectedUsers, quizzesList,
					beginStamp, endStamp);

			final JSONArray graphParentArray = new JSONArray();

			final ObjectMapper mapper = new ObjectMapper();
			List<BoxPlot> resultList;
			BoxPlot singleResult;
			String resultListString = "";
			JsonNode jsonObj;
			try {
				jsonObj = mapper.readTree(result);
				final JsonNode elementArray = jsonObj.get("elements");
				if (elementArray != null) {
					if (elementArray.isArray()) {

						resultList = mapper.readValue(elementArray.toString(), new TypeReference<List<BoxPlot>>() {
						});

						this.logger.debug("Entries Size:" + jsonObj.get("elements").size() + " Values:"
								+ jsonObj.get("elements").toString());

						this.logger.debug("Entries parsed Size: " + resultList.size() + " Values:"
								+ resultList.toString());

						for (Integer w = 0; w < resultList.size(); w++) {
							final Long quizID = Long.parseLong(resultList.get(w).getName());
							resultList.get(w).setName(quizzesMap.get(quizID));
							// resultList.get(w).setName(quizzesTitles.get(w));
						}

						resultListString = mapper.writeValueAsString(resultList);

						this.logger.debug("Entries JSON Output: " + resultListString);
					} else {
						singleResult = mapper.readValue(elementArray.toString(), new TypeReference<BoxPlot>() {
						});

						this.logger.debug("Entries: " + jsonObj.get("elements").toString());

						this.logger.debug("Entries parsed: " + singleResult.toString());

						final Long quizID = Long.parseLong(singleResult.getName());
						singleResult.setName(quizzesMap.get(quizID));
						// resultList.get(w).setName(quizzesTitles.get(w));

						final List<BoxPlot> tmpResultList = new ArrayList<BoxPlot>();
						tmpResultList.add(singleResult);

						resultListString = mapper.writeValueAsString(tmpResultList);

					}
				}
			} catch (final JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.logger.debug("Cumulative result: " + result);
			return resultListString;

		}
		return "";
	}

	void setupRender() {
		this.logger.debug(" ----- Bin in Setup Render");

		final ArrayList<Long> courseList = new ArrayList<Long>();
		courseList.add(this.course.getCourseId());

		if (this.endDate == null) {
			this.endDate = this.course.getLastRequestDate();
		} else {
			this.selectedUsers = null;
			this.userIds = this.getUsers();
		}
		if (this.beginDate == null) {
			this.beginDate = this.course.getFirstRequestDate();
		} else {
			this.selectedUsers = null;
			this.userIds = this.getUsers();
		}
		final Calendar beginCal = Calendar.getInstance();
		final Calendar endCal = Calendar.getInstance();
		beginCal.setTime(this.beginDate);
		endCal.setTime(this.endDate);
		this.resolution = this.dateWorker.daysBetween(this.beginDate, this.endDate);

		//
		// Calendar beginCal = Calendar.getInstance();
		// Calendar endCal = Calendar.getInstance();
		// beginCal.setTime(beginDate);
		// endCal.setTime(endDate);
		// this.resolution = dateWorker.daysBetween(beginDate, endDate);
		// logger.debug("SetupRender End --- BeginDate:" + beginDate + " EndDate: " + endDate + " Res: " + resolution);
	}

	@AfterRender
	public void afterRender() {
		this.javaScriptSupport.addScript("");
	}

	void onPrepareFromCustomizeForm() {
		this.course = this.courseDAO.getCourseByDMSId(this.courseId);
	}

	void onSuccessFromCustomizeForm() {
		this.logger.debug("   ---  onSuccessFromCustomizeForm ");
		this.logger.debug("Selected activities: " + this.selectedActivities);
		this.logger.debug("Selected users: " + this.selectedUsers);
	}

	public String getLocalizedDate(final Date inputDate) {
		final SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy", this.currentlocale);
		return df_date.format(inputDate);
	}

	public String getFirstRequestDate() {
		return this.getLocalizedDate(this.beginDate);// .course.getFirstRequestDate());
	}

	public String getLastRequestDate() {
		return this.getLocalizedDate(this.endDate);// .course.getLastRequestDate());
	}
}
