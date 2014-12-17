/**
 * File ./src/main/java/de/lemo/apps/pages/viz/FrequentPathBide.java
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

package de.lemo.apps.pages.viz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.application.VisualisationHelperWorker;
import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.entities.LearningType;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LearningTypeValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

@RequiresAuthentication
@BreadCrumb(titleKey = "visFrequentPathBide")
@Import(library = { "../../js/d3/FrequentPath.js",
		"../../js/d3/libs/d3.v2.js"
})
public class FrequentPathBide {

	private static final int THOU = 1000;
	
	@Environmental
	private JavaScriptSupport javaScriptSupport;
	
	@Inject @Path("../../js/d3/Lemo.js")
	private Asset lemoJs;
	
	@Inject
	SelectModelFactory selectModelFactory;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

	@Inject
	private CourseIdValueEncoder courseValueEncoder;

	@Inject
	private Analysis analysis;

	@Inject
	private UserWorker userWorker;
	
	@Inject
	private VisualisationHelperWorker visWorker;


	@Inject
	private CourseDAO courseDAO;

	@Inject
	private Locale currentlocale;

	@Inject
	private Messages messages;

	@Inject
	private TypeCoercer coercer;

	@Inject
	private Request request;

	@Property
	private BreadCrumbInfo breadCrumb;

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

	// Value Encoder for gender multi-select component
	@Property(write = false)
	private final ValueEncoder<GenderEnum> genderEncoder = new EnumValueEncoder<GenderEnum>(this.coercer,
					GenderEnum.class);
		
	// Select Model for gender multi-select component
	@Property(write = false)
	private final SelectModel genderModel = new EnumSelectModel(GenderEnum.class, this.messages);

	@Property
	@Persist
	private List<GenderEnum> selectedGender;

	@Inject
	@Property
	private LongValueEncoder userIdEncoder;
	
	@Inject
	@Property
	private LearningTypeValueEncoder learningTypeEncoder;
	
	@Property
	@Persist
	private List<LearningType> selectedLearningTypes;
	
	@Property
	private SelectModel learningTypeSelectModel;

	@Property
	@Persist
	private List<Long> userIds;

	@Property
	@Persist
	private List<Long> selectedUsers;
	
	@Persist
	@Property
	private Boolean userOptionEnabled;

	public List<Long> getUsers() {
		final List<Long> courses = new ArrayList<Long>();
		courses.add(this.course.getCourseId());
		final List<Long> elements = this.analysis
				.computeCourseUsers(courses, this.beginDate.getTime() / THOU, this.endDate.getTime() / THOU, this.visWorker.getGenderIds(this.selectedGender)).getElements();
		return elements;
	}

	public Object onActivate(final Course course) {
		this.logger.debug("--- Bin im ersten onActivate");
		final List<Long> allowedCourses = this.userWorker.getCurrentUser().getMyCourseIds();
		if ((allowedCourses != null) && (course != null) && (course.getCourseId() != null)
				&& allowedCourses.contains(course.getCourseId())) {
			this.courseId = course.getCourseId();
			this.course = course;
			
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
			this.logger.debug("MinSup:" + this.minSup);

			return true;
		} else {
			return Explorer.class;
		}
	}

	public Object onActivate() {
		this.logger.debug(" No Course Id provided ...");
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
		this.selectedLearningTypes = null;
		this.selectedGender = null;
		this.minSup = 9;
		this.pathLengthMin = null;
		this.pathLengthMax = null;
		this.beginDate = null;
		this.endDate = null;
	}

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.courseModel = new CourseIdSelectModel(courses);

		this.userIds = this.getUsers();
		
		List<Long> cids = new ArrayList<Long>();
		for(Course c : courses)
		{
			cids.add(c.getCourseId());
		}
		
		ResultListStringObject learningTypeList = null;
		try {
			learningTypeList = this.init.getLearningTypes(cids);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		final List<LearningType> learningTypes = new ArrayList<LearningType>();
		


		
		if ((learningTypeList != null) && (learningTypeList.getElements() != null)) {
			this.logger.debug(learningTypeList.toString());
			final List<String> learningStringList = learningTypeList.getElements();
			for (Integer x = 0; x < learningStringList.size(); x = x + 2) {
				final Long learningTypeId = Long.parseLong(learningStringList.get(x) );
				learningTypes.add(new LearningType(learningStringList.get(x + 1),learningTypeId));
				//this.learningTypeIds.add(learningTypeId);
			}

			this.learningTypeEncoder.setUp(learningTypes);

			this.learningTypeSelectModel = selectModelFactory.create(learningTypes, "name");

		} else {
			this.logger.debug("No Learning Types found");
		}
	}

	public final ValueEncoder<Course> getCourseValueEncoder() {
		return this.courseValueEncoder.create(Course.class);
	}

	@Inject
	private Initialisation init;
	
    Object onActionFromRefreshZone(String taskId) {
    	return init.taskResult(taskId);
    }
	
	@Property
	@Persist
	Integer val;

	@Property
	Long max, min;

	@Property
	@Persist
	Integer minSup;

	@Property
	@Persist
	Long pathLengthMin;

	@Property
	@Persist
	Long pathLengthMax;

	@Property
	private JSONObject minSupParams,
					pathLengthParams,
					minValue,
					maxValue;

	// Seting up paramaters for jquery sliders
	@OnEvent(org.apache.tapestry5.EventConstants.ACTIVATE)
	public void initSlider() {

		if (this.minSup == null) {
			this.minSup = 9;
		}

		this.minSupParams = new JSONObject();

		this.minSupParams.put("min", 1);
		this.minSupParams.put("max", 10);
		this.minSupParams.put("value", this.minSup);

		this.pathLengthParams = new JSONObject();
		this.max = 200L;
		this.min = 1L;

		if (this.pathLengthMax != null) {
			this.max = this.pathLengthMax;
		}
		if (this.pathLengthMin != null) {
			this.min = this.pathLengthMin;
		}
		this.pathLengthParams.put("min", 1);
		this.pathLengthParams.put("max", 200);
		this.pathLengthParams.put("range", true);
		this.pathLengthParams.put("values", new JSONArray(this.min, this.max));
	}

	// returns datepicker params
	public JSONLiteral getDatePickerParams() {
		return this.dateWorker.getDatePickerParams(this.currentlocale);
	}

	@Property
	private double minSupDouble;

	private Double minSupValue;

	public String getQuestionResult() {
		final ArrayList<Long> courseIds = new ArrayList<Long>();
		courseIds.add(this.courseId);

		final boolean considerLogouts = false;

		List<String> learningTypeList = new ArrayList<String>();
		final Set<String> learningTypeMap = CollectionFactory.newSet();
		ResultListStringObject availableTypes = null;
		try {
			availableTypes = this.init.getLearningTypes(courseIds);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if ((availableTypes != null) && (availableTypes.getElements() != null)) {
			this.logger.debug(availableTypes.getElements().toString());
			final List<String> learningStringList = availableTypes.getElements();
			for (Integer x = 0; x < learningStringList.size(); x = x + 2) {
				learningTypeMap.add(learningStringList.get(x +1));
			}

		} else {
			this.logger.debug("No Learning Types found");
		}
		
		if (this.selectedLearningTypes != null && !this.selectedLearningTypes.isEmpty()) {
			for(LearningType q : this.selectedLearningTypes)
			{
				learningTypeList.add(q.getName());
			}
		} else if (learningTypeMap != null ) {
			learningTypeList = new ArrayList<String>();
			learningTypeList.addAll(learningTypeMap);
		}
		
		List<Long> gender = this.visWorker.getGenderIds(this.selectedGender);


		Long endStamp = 0L;
		Long beginStamp = 0L;
		if (this.beginDate != null) {
			beginStamp = new Long(this.beginDate.getTime() / THOU);
		}
		if (this.endDate != null) {
			endStamp = new Long(this.endDate.getTime() / THOU);
		}

		// Check value for minumim support .. if no value is set it will default to 8 -> 0.8
		if ((this.minSup == null) || this.minSup.equals(0)) {
			this.minSup = 9;
		}
		this.minSupValue = new Double(this.minSup);
		this.minSupValue = this.minSupValue / 10;
		this.logger.debug("MinSupValue:" + this.minSupValue + "  --  " + this.minSupValue.doubleValue());
		this.minSupDouble = this.minSupValue.doubleValue();

		this.logger.debug("PathLength: " + this.pathLengthMin + "  --  " + this.pathLengthMax);

		return this.analysis.computeQFrequentPathBIDE(userWorker.getCurrentUser().getId(), courseIds, this.selectedUsers, learningTypeList, this.pathLengthMin, this.pathLengthMax,
				this.minSupDouble, considerLogouts, beginStamp, endStamp, gender);
	}

	public String getSupportValue() {
		Double minSupTemp = new Double(this.minSup);
		minSupTemp = minSupTemp / 10;
		return minSupTemp.toString();
	}

	public String getPathLengthValue() {
		if ((this.pathLengthMin == null) && (this.pathLengthMax == null)) {
			return messages.get("freqAllPaths");
		}
		if ((this.pathLengthMin == null) && (this.pathLengthMax != null)) {
			return "1 - " + this.pathLengthMax;
		}
		if ((this.pathLengthMin != null) && (this.pathLengthMax == null)) {
			return this.pathLengthMin + " - 200";
		}
		return this.pathLengthMin + " - " + this.pathLengthMax;
	}

	void setupRender() {
		this.logger.debug(" ----- Bin in Setup Render");
		
		javaScriptSupport.importJavaScriptLibrary(lemoJs);
		
		userOptionEnabled = ServerConfiguration.getInstance().getUserOptionEnabled();

		final ArrayList<Long> courseList = new ArrayList<Long>();
		courseList.add(this.course.getCourseId());

		final Calendar beginCal = Calendar.getInstance();
		final Calendar endCal = Calendar.getInstance();
		
		
		beginCal.setTime(this.beginDate);
		endCal.setTime(this.endDate);
		this.resolution = this.dateWorker.daysBetween(this.beginDate, this.endDate);
		this.logger.debug("SetupRender End --- BeginDate:" + this.beginDate + " EndDate: " + this.endDate + " Res: " + this.resolution);

	}

	@AfterRender
	public void afterRender() {
		this.javaScriptSupport.addScript("");
		javaScriptSupport.addScript("var options = document.getElementsByTagName('option');	for(var i = 0; i<options.length;i++){options[i].setAttribute('title', options[i].innerHTML);}");
	}

	void onPrepareFromCustomizeForm() {
		this.course = this.courseDAO.getCourseByDMSId(this.courseId);
	}

	void onSuccessFromCustomizeForm() {
		this.logger.debug("   ---  onSuccessFromCustomizeForm ");
		final String input = this.request.getParameter("minSup-slider");
		if (input != null) {
			this.minSup = Integer.parseInt(input);
		}
		this.logger.debug("MinSup Value: " + this.minSup);
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
