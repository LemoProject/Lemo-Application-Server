/**
 * File ./src/main/java/de/lemo/apps/pages/viz/ActivityTimeCumulative.java
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
	 * File VisualizationCumulative.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.pages.viz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
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
import de.lemo.apps.application.VisualisationHelperWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.BoxPlot;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

/**
 * Visualisation for cumulative user access by week and days
 */
@RequiresAuthentication
@BreadCrumb(titleKey = "visActivityTimeCumulutive")
@Import(library = { "../../js/d3/ActivityTimeCumulative.js" })
public class ActivityTimeCumulative {

	private static final int THOU = 1000;
	
	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private AnalysisWorker analysisWorker;

	@Inject
	private CourseIdValueEncoder courseValueEncoder;
	
	@Inject
	private VisualisationHelperWorker visWorker;

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
	
	@Inject
	private ComponentResources coRes;

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
	
	@Persist
	private Map<Long, Date> beginMem;

	@Persist
	private Map<Long, Date> endMem;

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
		final List<Long> elements = this.analysis
				.computeCourseUsers(courses, this.beginDate.getTime() / THOU, this.endDate.getTime() / THOU, this.visWorker.getGenderIds(this.selectedGender)).getElements();
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
		this.selectedActivities = null;
		this.selectedGender = null;
		this.beginDate = null;
		this.endDate = null;
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
		if (this.courseId != null) {
			Long endStamp = 0L;
			Long beginStamp = 0L;
			if (this.endDate != null) {
				endStamp = new Long(this.endDate.getTime() / THOU);
			}

			if (this.beginDate != null) {
				beginStamp = new Long(this.beginDate.getTime() / THOU);
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

			List<String> types = null;
			if ((this.selectedActivities != null) && !this.selectedActivities.isEmpty()) {
				types = new ArrayList<String>();
				for (final EResourceType resourceType : this.selectedActivities) {
					types.add(resourceType.name().toLowerCase());
				}
			}

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

			final String result = this.analysis.computeCumulativeUserAccess(courses, types, null, null, beginStamp, endStamp);

			this.logger.debug("ResultString RAW: "+result);	
			

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

						this.logger.debug("Entries parsed Size: " + resultList.size() + " Values:" + resultList.toString());


						resultListString = mapper.writeValueAsString(resultList);

						this.logger.debug("Entries JSON Output: " + resultListString);
					} else {
						singleResult = mapper.readValue(elementArray.toString(), new TypeReference<BoxPlot>() {
						});

						this.logger.debug("Entries: " + jsonObj.get("elements").toString());

						this.logger.debug("Entries parsed: " + singleResult.toString());

						final List<BoxPlot> tmpResultList = new ArrayList<BoxPlot>();
						tmpResultList.add(singleResult);

						resultListString = mapper.writeValueAsString(tmpResultList);

					}
				}
			} catch (final JsonProcessingException e) {
				logger.error(e.getMessage());
			} catch (final IOException e) {
				logger.error(e.getMessage());
			}
			
			
			
			this.logger.debug("Cumulative result: " + resultListString);
			return resultListString;

		}
		return "";
	}

	void setupRender() {
		this.logger.debug(" ----- Bin in Setup Render");

		final ArrayList<Long> courseList = new ArrayList<Long>();
		courseList.add(this.course.getCourseId());
		
		if(beginMem == null)
		{
			this.beginMem = new HashMap<Long, Date>();
		}
		
		if(endMem == null)
		{
			this.endMem = new HashMap<Long, Date>();
		}
		
		if (this.endDate == null) {
			if(this.endMem.get(this.courseId) == null){
				this.endDate = this.course.getLastRequestDate();
			}else{
				this.endDate = this.endMem.get(courseId);
			}
		} else {
			this.selectedUsers = null;
			this.userIds = this.getUsers();
		}
		if (this.beginDate == null) {
			if(this.beginMem.get(this.courseId) == null){
				this.beginDate = this.course.getFirstRequestDate();
			}
			else
			{
				this.beginDate = this.beginMem.get(this.courseId);
			}
		} else {
			this.selectedUsers = null;
			this.userIds = this.getUsers();
		}
		
		if(this.beginDate != null){
			this.beginMem.put(this.courseId, this.beginDate);
		}
		if(this.endDate != null){
			this.endMem.put(this.courseId, this.endDate);
		}
		final Calendar beginCal = Calendar.getInstance();
		final Calendar endCal = Calendar.getInstance();
		beginCal.setTime(this.beginDate);
		endCal.setTime(this.endDate);
		this.resolution = this.dateWorker.daysBetween(this.beginDate, this.endDate);
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
		final SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd, yy", this.currentlocale);
		return dfDate.format(inputDate);
	}

	public String getFirstRequestDate() {
		return this.getLocalizedDate(this.beginDate);
	}

	public String getLastRequestDate() {
		return this.getLocalizedDate(this.endDate);
	}
}
