/**
 * File ./src/main/java/de/lemo/apps/pages/viz/ActivityGraph.java
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
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.apache.tapestry5.EventConstants;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.application.VisualisationHelperWorker;
import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.entities.LearningObject;
import de.lemo.apps.entities.LearningType;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LearningObjectValueEncoder;
import de.lemo.apps.services.internal.LearningTypeValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

@RequiresAuthentication
@BreadCrumb(titleKey = "visPrediction")
@Import(library = { "../../js/d3/libs/d3.v3.js",
					"../../js/d3/Classifier.js",
					"../../js/d3/Sankey.js",
					"../../js/d3/Prediction.js"},
		stylesheet = {"../../css/prediction.css"})
		


public class Prediction {

	private static final int THOU = 1000;

	@Inject @Path("../../js/d3/Lemo.js")
	private Asset lemoJs;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Logger logger;

	@Inject
	private DateWorker dateWorker;

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
	private Messages messages;

	@Inject
	private TypeCoercer coercer;

	@Property
	private BreadCrumbInfo breadCrumb;

	@InjectComponent
	private Zone formZone;

	@Inject
	private Initialisation init;
	
	@Inject
	private Request request;


	@Property
	@Persist
	private List<LearningObject> selectedLearningObjects;

	@Property
	@Persist
	private List<LearningType> selectedLearningTypes;

	@Property
	@Persist
	private Course course;

	@Property
	@Persist
	private Long courseId;

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
	private Integer resolution;

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

	@Property
	@Persist
	private List<Long> userIds, learningObjectIds;

	@Property
	@Persist
	private List<Long> selectedUsers;

	@Persist
	@Property
	private Boolean userOptionEnabled;

	@Property
	@Persist
	private int support;

	@Property
	private JSONObject paramsZone;
	@OnEvent(EventConstants.ACTIVATE)
	public void initSliderZone(){
	paramsZone=new JSONObject();
	paramsZone.put("value", support);
	}
/*	@Component
	private Zone myZone;
	@OnEvent(value=EventConstants.ACTION, component="sliderZone")
	public Object returnZone(){
	String input = request.getParameter("slider");
	support=Integer.parseInt(input);
	return myZone.getBody();
	}*/
	
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

			return true;
		} else {
			return Explorer.class;
		}
	}

	//TODO this is just for debugging.
	public Object onActivate() {
		this.courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.course=courses.get(0);
		final List<Long> allowedCourses = this.userWorker.getCurrentUser().getMyCourseIds();
		if ((allowedCourses != null) && (course != null) && (course.getCourseId() != null)
				&& allowedCourses.contains(course.getCourseId())) {
			this.courseId = course.getCourseId();
			return true;
		} else {
			return Explorer.class;
		}
	}

	public Course onPassivate() {
		return this.course;
	}

	void cleanupRender() {
		// Clear the flash-persisted fields to prevent anomalies in onActivate
		// when we hit refresh on page or browser
		// button
		this.courseId = null;
		this.course = null;
		this.selectedUsers = null;
		this.selectedLearningTypes = null;
		this.selectedGender = null;
		this.beginDate = null;
		this.endDate = null;
	}

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.userIds = this.getUsers();

		this.learningObjectIds = new ArrayList<Long>();

		final List<Long> courseList = new ArrayList<Long>();
		courseList.add(this.courseId);
		ResultListStringObject learningObjectList = null;
		try {
			learningObjectList = this.init.getLearningObjects(courseList);
		} catch (RestServiceCommunicationException e) {
			logger.error(e.getMessage());
		}

		final List<LearningObject> learningList = new ArrayList<LearningObject>();

		if ((learningObjectList != null) && (learningObjectList.getElements() != null)) {
			this.logger.debug(learningObjectList.getElements().toString());
			final List<String> learningStringList = learningObjectList.getElements();
			for (Integer x = 0; x < learningStringList.size(); x = x + 2) {
				final Long learningId = Long.parseLong(learningStringList.get(x) );
				learningList.add(new LearningObject(learningStringList.get(x + 1),learningId));
				this.learningObjectIds.add(learningId);
			}


		} else {
			this.logger.debug("No Learning Objetcs found");
		}

		ResultListStringObject learningTypeList = null;
		try {
			learningTypeList = this.init.getLearningTypes(courseList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		final List<LearningType> learningTypes = new ArrayList<LearningType>();

		if ((learningTypeList != null) && (learningTypeList.getElements() != null)) {
			this.logger.debug(learningTypeList.getElements().toString());
			final List<String> learningStringList = learningTypeList.getElements();
			for (Integer x = 0; x < learningStringList.size(); x = x + 2) {
				final Long learningTypeId = Long.parseLong(learningStringList.get(x) );
				learningTypes.add(new LearningType(learningStringList.get(x + 1),learningTypeId));
			}


		} else {
			this.logger.debug("No Learning Types found");
		}
	}

	public final ValueEncoder<Course> getCourseValueEncoder() {
		return this.courseValueEncoder.create(Course.class);
	}

	// returns datepicker params
	public JSONLiteral getDatePickerParams() {
		return this.dateWorker.getDatePickerParams(this.currentlocale);
	}

	public String getLocale() {
		JSONObject localeObject = new JSONObject();
		localeObject.put("charge", messages.get("charge"));
		localeObject.put("distance", messages.get("distance"));
		localeObject.put("visits", messages.get("visits"));
		localeObject.put("activities", messages.get("activities"));
		localeObject.put("learningObject", messages.get("learning-object"));
		localeObject.put("resourcetypeLabel", messages.get("resourcetype-label"));
		return localeObject.toString();
	}

	public String getQuestionResult() {
		List<Long> courseList = new ArrayList<Long>();
		courseList.add(this.courseId);

		List<Long> learningList = new ArrayList<Long>();

		ResultListStringObject learningObjectList = null;
		try {
			learningObjectList = this.init.getLearningObjects(courseList);
		} catch (RestServiceCommunicationException e) {
			logger.error(e.getMessage());
		}

		final Map<Long, String> learningMap = CollectionFactory.newMap();
		final List<String> learningTitles = new ArrayList<String>();

		if ((learningObjectList != null) && (learningObjectList.getElements() != null)) {
			this.logger.debug(learningObjectList.getElements().toString());
			final List<String> learningObjectStringList = learningObjectList.getElements();
			for (Integer x = 0; x < learningObjectStringList.size(); x = x + 2) {
				final Long quizId = Long.parseLong(learningObjectStringList.get(x) );
				learningMap.put(quizId, learningObjectStringList.get(x + 1));
				learningTitles.add(learningObjectStringList.get(x + 1));
			}

		} else {
			this.logger.debug("No Learning Objetcs found");
		}

		if (this.selectedLearningObjects != null && !this.selectedLearningObjects.isEmpty()) {
			for(LearningObject q : this.selectedLearningObjects)
			{
				learningList.add(q.getId());
			}
		} else if ((learningMap != null) && (learningMap.keySet() != null)) {
			learningList = new ArrayList<Long>();
			learningList.addAll(learningMap.keySet());
		}

		List<String> learningTypeList = new ArrayList<String>();
		final Set<String> learningTypeMap = CollectionFactory.newSet();
		ResultListStringObject availableTypes = null;
		try {
			availableTypes = this.init.getLearningTypes(courseList);
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

		final ArrayList<Long> courseIds = new ArrayList<Long>();
		courseIds.add(this.courseId);

		final boolean considerLogouts = true;

		List<Long> gender = this.visWorker.getGenderIds(this.selectedGender);

		Long endStamp = 0L;
		Long beginStamp = 0L;
		if (this.beginDate != null) {
			beginStamp = new Long(this.beginDate.getTime() / THOU);
		}
		if (this.endDate != null) {
			endStamp = new Long(this.endDate.getTime() / THOU);
		}

		String result = this.analysis.computePrediction(courseIds, this.selectedUsers, learningTypeList, considerLogouts, beginStamp, endStamp, gender, learningList); 
		//String result = this.analysis.computeCourseUserPaths(courseIds, beginStamp, endStamp, gender, learningList); 
		this.logger.info("ResultString: "+result);

		return result;
	}

	void setupRender() {
		this.logger.debug(" ----- Bin in Setup Render");

		javaScriptSupport.importJavaScriptLibrary(lemoJs);

		userOptionEnabled = ServerConfiguration.getInstance().getUserOptionEnabled();

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
		javaScriptSupport.addScript("var options = document.getElementsByTagName('option');	for(var i = 0; i<options.length;i++){options[i].setAttribute('title', options[i].innerHTML);}");
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