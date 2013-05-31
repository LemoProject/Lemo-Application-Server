package de.lemo.apps.pages.viz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
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
import de.lemo.apps.application.VisualisationHelperWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey = "visActivityLearningObject")
@Import(library = { "../../js/d3/ActivityLearningObject.js" })
public class ActivityLearningObject {

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private AnalysisWorker analysisWorker;
	
	@Inject
	private VisualisationHelperWorker visWorker;

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
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

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
	
	
	@Property
	private ResourceRequestInfo resourceItem;

	@Persist
	private List<ResourceRequestInfo> showDetailsList;


	// Value Encoder for activity multi-select component
	@Property(write = false)
	private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(this.coercer,
			EResourceType.class);

	// Select Model for activity multi-select component
	@Property(write = false)
	private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, this.messages);
	
	
	@Property(write = false)
	@Retain
	private BeanModel resourceGridModel;
	{
		this.resourceGridModel = this.beanModelSource.createDisplayModel(ResourceRequestInfo.class, this.componentResources
				.getMessages());
		this.resourceGridModel.include("resourcetype", "title", "requests");
	}


	// Value Encoder for gender multi-select component
	@Property(write = false)
	private final ValueEncoder<GenderEnum> genderEncoder = new EnumValueEncoder<GenderEnum>(this.coercer,
						GenderEnum.class);
			
	// Select Model for gender multi-select component
	@Property(write = false)
	private final SelectModel genderModel = new EnumSelectModel(GenderEnum.class, this.messages);

	@Property
	@Persist
	private List<EResourceType> selectedActivities;
		
	@Property
	@Persist
	private List<GenderEnum> selectedGender;


	@Inject
	@Property
	private LongValueEncoder userIdEncoder;

	@Property
	@Persist
	private List<Long> userIds;

	@Property
	@Persist
	private List<Long> selectedUsers;

	public List<Long> getUsers() {
		final List<Long> courses = new ArrayList<Long>();
		courses.add(this.course.getCourseId());
		if(beginDate == null)
			this.beginDate = course.getFirstRequestDate();
		if(endDate == null)
			this.endDate = course.getLastRequestDate();
		
		final List<Long> elements = this.analysis
				.computeCourseUsers(courses, this.beginDate.getTime() / 1000, this.endDate.getTime() / 1000, this.visWorker.getGenderIds(this.selectedGender)).getElements();
		this.logger.info("          ----        " + elements);
		return elements;
	}
	
	
	
	public List<ResourceRequestInfo> getResourceList() {
		this.course = this.courseDAO.getCourseByDMSId(this.courseId);

		List<ResourceRequestInfo> resultList;

		if ((this.selectedActivities != null) && (this.selectedActivities.size() >= 1)) {
			this.logger.debug("Starting Extended Analysis - Including LearnbObject Selection ...  ");
			resultList = this.analysisWorker.usageAnalysisExtended(this.course, this.beginDate, this.endDate, this.selectedActivities, this.selectedGender);
		} else {
			this.logger.debug("Starting Extended Analysis - Including ALL LearnObjects ....");
			resultList = this.analysisWorker.usageAnalysisExtended(this.course, this.beginDate, this.endDate, null, this.selectedGender);
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
		this.selectedGender = null;
		this.selectedActivities = null;
	}

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.courseModel = new CourseIdSelectModel(courses);
		this.userIds = this.getUsers();
	}

	public final ValueEncoder<Course> getCourseValueEncoder() {
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

			if ((this.resolution == null) || (this.resolution < 10)) {
				this.resolution = 30;
			}
			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(this.courseId);

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

			@SuppressWarnings("unchecked")
			final
			List<ResourceRequestInfo> results = this.analysisWorker.learningObjectUsage(this.course, this.beginDate, this.endDate,
					this.selectedUsers, this.selectedActivities, this.selectedGender);

			final JSONArray graphParentArray = new JSONArray();
			final JSONObject graphDataObject = new JSONObject();
			final JSONArray graphDataValues = new JSONArray();

			if ((results != null) && (results.size() > 0)) {
				for (Integer j = 0; j < results.size(); j++) {
					final JSONObject graphValue = new JSONObject();

					graphValue.put("x", results.get(j).getTitle()+"_"+j);
					graphValue.put("y", results.get(j).getRequests());

					graphDataValues.put(graphValue);
				}
			}

			graphDataObject.put("values", graphDataValues);
			graphDataObject.put("key", "Activities");

			final JSONObject graphDataObject2 = new JSONObject();
			final JSONArray graphDataValues2 = new JSONArray();

			if ((results != null) && (results.size() > 0)) {
				for (Integer i = 0; i < results.size(); i++) {
					final JSONObject graphValue2 = new JSONObject();

					graphValue2.put("x", results.get(i).getTitle()+"_"+i);
					graphValue2.put("y", results.get(i).getUsers());

					graphDataValues2.put(graphValue2);
				}
			}
			graphDataObject2.put("values", graphDataValues2);
			graphDataObject2.put("key", "User");

			graphParentArray.put(graphDataObject);
			graphParentArray.put(graphDataObject2);

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
	}

	@AfterRender
	public void afterRender() {
		this.javaScriptSupport.addScript("$('#beginDate').val('%s');",getFirstRequestDate());
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
		final SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd, yy", this.currentlocale);
		return dfDate.format(inputDate);
	}

	public String getFirstRequestDate() {
		return this.getLocalizedDate(this.beginDate);
	}

	public String getLastRequestDate() {
		return this.getLocalizedDate(this.endDate);
	}
	
	public String getResourceTypeName() {
		if ((this.resourceItem != null) && (!this.resourceItem.getResourcetype().equals(""))) {
			return this.messages.get("EResourceType." + this.resourceItem.getResourcetype());
		} else {
			return this.messages.get("EResourceType.UNKNOWN");
		}
	}
	
	
}
