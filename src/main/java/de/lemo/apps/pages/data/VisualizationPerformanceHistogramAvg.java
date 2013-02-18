package de.lemo.apps.pages.data;

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
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey = "visualizationTitle")
@Import(library = { "../../js/d3/nvd3_custom_Histogram.js" })
public class VisualizationPerformanceHistogramAvg {

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
	private List<Long> selectedUsers, selectedCourses, selectedQuizzes;

	public List<Long> getUsers() {
		final List<Long> courses = new ArrayList<Long>();
		courses.add(this.course.getCourseId());
		final List<Long> elements = this.analysis
				.computeCourseUsers(courses, this.beginDate.getTime() / 1000, this.endDate.getTime() / 1000).getElements();
		this.logger.info("          ----        " + elements);
		return elements;
	}

	public Object onActivate(final Course course) {
		this.logger.debug("--- Bin im ersten onActivate");
		final List<Long> allowedCourses = this.userWorker.getCurrentUser().getMyCourseIds();
		if ((allowedCourses != null) && (course != null) && (course.getCourseId() != null)
				&& allowedCourses.contains(course.getCourseId())) {
			this.courseId = course.getCourseId();
			this.course = course;
			if (this.selectedCourses == null) {
				this.selectedCourses = new ArrayList<Long>();
				this.selectedCourses.add(this.courseId);
			}
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
		this.selectedCourses = null;
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
		ResultListStringObject quizList = null;
		try {
			quizList = this.init.getRatedObjects(courseList);
		} catch (RestServiceCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Map<Long, String> quizzesMap = CollectionFactory.newMap();
		final List<String> quizzesTitles = new ArrayList<String>();

		if ((quizList != null) && (quizList.getElements() != null)) {
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
		final List<List<TextValueDataItem>> dataList = CollectionFactory.newList();

		if (this.courseId != null) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			if (this.endDate != null) {
				endStamp = new Long(this.endDate.getTime() / 1000);
			}

			if (this.beginDate != null) {
				beginStamp = new Long(this.beginDate.getTime() / 1000);
			}

			// if(this.resolution == null || this.resolution < 10)
			this.resolution = 100;
			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(this.courseId);

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

			// @SuppressWarnings("unchecked")
			// List<ResourceRequestInfo> results = analysisWorker.learningObjectUsage(this.course, beginDate, endDate,
			// selectedUsers, selectedActivities);

			List<Long> courseList = new ArrayList<Long>();
			if ((this.selectedCourses != null) && !this.selectedCourses.isEmpty()) {
				if (!this.selectedCourses.contains(this.courseId)) {
					this.selectedCourses.add(this.courseId);
				}
				courseList = this.selectedCourses;
			} else {
				courseList.add(this.courseId);
			}

			List<Long> quizzesList = new ArrayList<Long>();

			ResultListStringObject quizList = null;
			try {
				quizList = this.init.getRatedObjects(courseList);
			} catch (RestServiceCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final Map<Long, String> quizzesMap = CollectionFactory.newMap();
			final List<String> quizzesTitles = new ArrayList<String>();

			if ((quizList != null) && (quizList.getElements() != null)) {
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

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

			final List<Long> results = this.analysis.computePerformanceHistogram(courseList, this.selectedUsers, quizzesList,
					this.resolution, beginStamp, endStamp);
			this.logger.debug("results for performance histogram:" + results);

			final List<Long> preparedResults = CollectionFactory.newList();

			if (results != null) {
				Integer splitCounter = 0;
				Integer quizCounter = 0;
				Long avgCounter = 0L;
				Long avgAmount = 0L;
				// preparedResults.add(new ArrayList<Long>());
				List<Long> currentList = new ArrayList<Long>();
				for (Integer i = 0; i < results.size(); i++) {
					currentList.add(results.get(i));
					avgAmount = avgAmount + results.get(i) * splitCounter;
					// logger.debug("Percent: "+splitCounter+" persons:"+results.get(i)+" Result:"+results.get(i)*splitCounter);
					if ((results.get(i) != null) && (results.get(i) > 0)) {
						avgCounter = avgCounter + results.get(i);
					}

					splitCounter++;
					if (splitCounter == this.resolution) {

						final List<Long> avgResult = new ArrayList<Long>();
						if (avgCounter != 0) {
							preparedResults.add(avgAmount / avgCounter);
							this.logger.debug("Result for " + quizzesMap.get(quizzesList.get(quizCounter)) + " : "
									+ avgAmount / avgCounter);
						} else {
							preparedResults.add(0L);
						}
						// preparedResults.add(avgResult);
						quizCounter++;
						splitCounter = 0;
						avgAmount = 0L;
						avgCounter = 0L;
						currentList = new ArrayList<Long>();
					}

				}
			}

			final JSONArray graphParentArray = new JSONArray();

			// for(Integer z = 0; z < preparedResults.size(); z++) {
			final JSONObject graphDataObject = new JSONObject();
			final JSONArray graphDataValues = new JSONArray();
			final List<Long> tmpResults = preparedResults;

			if ((tmpResults != null) && (tmpResults.size() > 0)) {
				for (Integer j = 0; j < tmpResults.size(); j++) {
					final JSONObject graphValue = new JSONObject();

					graphValue.put("x", quizzesMap.get(quizzesList.get(j)));
					graphValue.put("y", tmpResults.get(j));

					graphDataValues.put(graphValue);
				}
			}

			graphDataObject.put("values", graphDataValues);
			graphDataObject.put("key", "Performance");

			graphParentArray.put(graphDataObject);
			// }
			// JSONObject graphDataObject2 = new JSONObject();
			// JSONArray graphDataValues2 = new JSONArray();
			//
			// if(results != null && results.size() > 0) {
			// for(Integer i = 0; i < results.size(); i++) {
			// JSONObject graphValue2 = new JSONObject();
			//
			// graphValue2.put("x", results.get(i).getTitle());
			// graphValue2.put("y", results.get(i).getUsers());
			//
			// graphDataValues2.put(graphValue2);
			// }
			// }
			// graphDataObject2.put("values", graphDataValues2);
			// graphDataObject2.put("key", "User");

			// graphParentArray.put(graphDataObject2);

			this.logger.debug(graphParentArray.toString());

			return graphParentArray.toString();
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
		final SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd, yyyy", this.currentlocale);
		return dfDate.format(inputDate);
	}

	public String getFirstRequestDate() {
		return this.getLocalizedDate(this.beginDate);
	}

	public String getLastRequestDate() {
		return this.getLocalizedDate(this.endDate);
	}
}
