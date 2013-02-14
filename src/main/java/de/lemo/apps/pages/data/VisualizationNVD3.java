package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
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
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

@RequiresAuthentication
@BreadCrumb(titleKey = "visualizationTitle")
@Import(library = { "../../js/d3/nvd3_custom_Usage_Chart_Viewfinder.js" })
public class VisualizationNVD3 {
	
	final int RESOLUTION_MAX = 500;
	final int RESOLUTION_BASIC_MULTIPLIER = 4;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private CourseIdValueEncoder courseValueEncoder;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Analysis analysis;

	@Inject
	private UserWorker userWorker;

	@Inject
	private AnalysisWorker analysisWorker;

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
	Integer resolution, resolutionComputed;

	@Property
	@Persist
	private List<Course> courses;

	@Property
	private ResourceRequestInfo resourceItem;

	@Persist
	private List<ResourceRequestInfo> showDetailsList;

	// Value Encoder for activity multi-select component
	@Property(write = false)
	private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(this.coercer,
			EResourceType.class);

	@Property(write = false)
	@Retain
	private BeanModel resourceGridModel;
	{
		this.resourceGridModel = this.beanModelSource.createDisplayModel(ResourceRequestInfo.class, this.componentResources
				.getMessages());
		this.resourceGridModel.include("resourcetype", "title", "requests");
		// resourceGridModel.add("show",null);

	}

	// Select Model for activity multi-select component
	@Property(write = false)
	private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, this.messages);

	@Property
	@Persist
	private List<EResourceType> selectedActivities;

	@Inject
	@Property
	private LongValueEncoder userIdEncoder, courseIdEncoder;

	@Property
	@Persist
	private List<Long> userIds, courseIds;

	@Property
	@Persist
	private List<Long> selectedUsers, selectedCourses;

	public List<Long> getUsers() {
		final List<Long> courses = new ArrayList<Long>();
		courses.add(this.course.getCourseId());
		final List<Long> elements = this.analysis
				.computeCourseUsers(courses, this.beginDate.getTime() / 1000, this.endDate.getTime() / 1000).getElements();
		this.logger.info(" User Ids:         ----        " + elements);
		return elements;
	}

	@Cached
	public List<ResourceRequestInfo> getResourceList() {
		this.course = this.courseDAO.getCourseByDMSId(this.courseId);

		List<ResourceRequestInfo> resultList;

		if ((this.selectedActivities != null) && (this.selectedActivities.size() >= 1)) {
			this.logger.debug("Starting Extended Analysis - Including LearnbObject Selection ...  ");
			resultList = this.analysisWorker.usageAnalysisExtended(this.course, this.beginDate, this.endDate, this.selectedActivities);
		} else {
			this.logger.debug("Starting Extended Analysis - Including ALL LearnObjects ....");
			resultList = this.analysisWorker.usageAnalysisExtended(this.course, this.beginDate, this.endDate, null);
		}
		this.logger.debug("ExtendedAnalysisWorker Results: " + resultList);

		return resultList;
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
		return Explorer.class;
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
		this.selectedCourses = null;
		this.selectedActivities = null;
		this.beginDate = null;
		this.endDate = null;
	}

	// void pageReset() {
	// selectedUsers = null;
	// userIds = getUsers();
	// }

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser());
		this.courseModel = new CourseIdSelectModel(courses);
		this.userIds = this.getUsers();
		this.courseIds = this.userWorker.getCurrentUser().getMyCourseIds();
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

		List<Long> courseList = new ArrayList<Long>();
		if ((this.selectedCourses != null) && !this.selectedCourses.isEmpty()) {
			if (!this.selectedCourses.contains(this.courseId)) {
				this.selectedCourses.add(this.courseId);
			}
			courseList = this.selectedCourses;
		} else {
			courseList.add(this.courseId);
		}

		final boolean considerLogouts = true;

		ArrayList<String> types = null;
		if ((this.selectedActivities != null) && !this.selectedActivities.isEmpty()) {
			types = new ArrayList<String>();
			for (final EResourceType resourceType : this.selectedActivities) {
				types.add(resourceType.name().toUpperCase());
			}
		}

		Long endStamp = 0L;
		Long beginStamp = 0L;
		if (this.beginDate != null) {
			beginStamp = new Long(this.beginDate.getTime() / 1000);
		}
		if (this.endDate != null) {
			endStamp = new Long(this.endDate.getTime() / 1000);
		}

		

		this.resolution = (this.dateWorker.daysBetween(this.beginDate, this.endDate) + 1);
		this.logger.debug("Resolution: " + this.resolution + " ResolutionMultiplier: " + this.resolutionComputed);
		
		this.resolutionComputed = RESOLUTION_MAX;
		
		final HashMap<Long, ResultListLongObject> results = this.analysis.computeCourseActivity(courseList, null, this.selectedUsers,
				beginStamp, endStamp, this.resolutionComputed, types);

		final JSONArray graphParentArray = new JSONArray();
		JSONObject graphDataObject = new JSONObject();
		JSONObject graphUserObject = new JSONObject();
		JSONArray graphDataValues = new JSONArray();
		JSONArray graphUserValues = new JSONArray();

		if (results != null) {
			final Set<Long> courseSet = results.keySet();
			final Iterator<Long> it = courseSet.iterator();
			while (it.hasNext()) {

				final Long courseId = it.next();
				final ResultListLongObject resultAllObjects = results.get(courseId);
				this.logger.debug("ResultList Length: " + resultAllObjects.getElements().size() + " Resolution: "
						+ this.resolution);
				final ResultListLongObject resultDataObjects = new ResultListLongObject(resultAllObjects.getElements()
						.subList(0, this.resolutionComputed - 1));
				final ResultListLongObject resultUserObjects = new ResultListLongObject(resultAllObjects.getElements()
						.subList(this.resolutionComputed, resultAllObjects.getElements().size() - 1));

				graphDataObject = new JSONObject();
				graphUserObject = new JSONObject();
				graphDataValues = new JSONArray();
				graphUserValues = new JSONArray();

				Long currentDateStamp = 0L;
				Double dateResolution = this.resolution.doubleValue() / this.resolutionComputed.doubleValue();
				
				for (Integer i = 0; i < resultDataObjects.getElements().size(); i++) {
					final JSONArray graphDataValue = new JSONArray();
					final JSONArray graphUserValue = new JSONArray();
					Double dateMultiplier = dateResolution * 60 * 60 * 24  * i.longValue() * 1000;
					currentDateStamp = beginStamp * 1000 + dateMultiplier.longValue();
					graphDataValue.put(0, currentDateStamp);
					graphDataValue.put(1, resultDataObjects.getElements().get(i));

					graphUserValue.put(0, currentDateStamp);
					graphUserValue.put(1, resultUserObjects.getElements().get(i));

					graphDataValues.put(graphDataValue);
					graphUserValues.put(graphUserValue);
				}

				final Course course = this.courseDAO.getCourseByDMSId(courseId);
				graphDataObject.put("values", graphDataValues);
				graphDataObject.put("key", course.getCourseName());

				graphUserObject.put("values", graphUserValues);
				graphUserObject.put("key", course.getCourseName() + "-User");

				graphParentArray.put(graphDataObject);
				graphParentArray.put(graphUserObject);

			}

		}

		// logger.debug(graphParentArray.toString());

		return graphParentArray.toString();
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
		return this.getLocalizedDate(this.endDate);// course.getLastRequestDate());
	}

	public String getResourceTypeName() {
		if ((this.resourceItem != null) && (this.resourceItem.getResourcetype() != "")) {
			return this.messages.get("EResourceType." + this.resourceItem.getResourcetype());
		} else {
			return this.messages.get("EResourceType.UNKNOWN");
		}
	}
}
