/**
 * File ./src/main/java/de/lemo/apps/pages/viz/Performance.java
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

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
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
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.SelectModelFactory;
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
import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.GenderEnum;
import de.lemo.apps.entities.LearningType;
import de.lemo.apps.entities.Quiz;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListStringObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LearningTypeValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;
import de.lemo.apps.services.internal.QuizValueEncoder;
import de.lemo.apps.services.internal.QuizValueEncoderWorker;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;

/**
 * Visualisation for the performance histogram
 */
@RequiresAuthentication
@BreadCrumb(titleKey = "visPerformance")
@Import(library = { "../../js/d3/Performance.js" })
public class Performance {
	private static final int THOU = 1000;
	
	@Inject 
	@Path("../../js/d3/Lemo.js")
	private Asset lemoJs;

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
	SelectModelFactory selectModelFactory;

	@Property
	private SelectModel quizSelectModel;

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

	@Inject
	@Property
	private LearningTypeValueEncoder learningTypeEncoder;
	
	@Property
	@Persist
	private List<LearningType> selectedLearningTypes;
	
	@Property
	private SelectModel learningTypeSelectModel;
	
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
	private QuizValueEncoder quizEncoder;

	@Property
	@Persist
	private List<Long> userIds, quizIds;

	@Property
	@Persist
	private List<Long> selectedUsers, selectedCourses;
	
	@Property
	@Persist
	private List<Quiz> selectedQuizzes;
	
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
			this.logger.debug(" No Course Id provided ...");
			return Explorer.class;
		}

	public Course onPassivate() {
		return this.course;
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
		this.selectedLearningTypes = null;
		this.selectedGender = null;
		this.beginDate = null;
		this.endDate = null;
	}

	void onPrepareForRender() {
		final List<Course> courses = this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
		this.courseModel = new CourseIdSelectModel(courses);
		this.userIds = this.getUsers();

		this.quizIds = new ArrayList<Long>();

		final List<Long> courseList = new ArrayList<Long>();
		courseList.add(this.courseId);
		ResultListStringObject quizList = null;
		try {
			quizList = this.init.getRatedObjects(courseList);
		} catch (RestServiceCommunicationException e) {
			logger.error(e.getMessage());
		}

		final Map<Long, String> quizzesMap = CollectionFactory.newMap();
		final List<String> quizzesTitles = new ArrayList<String>();
		final List<Quiz> quizzesList = new ArrayList<Quiz>();
		
		if ((quizList != null) && (quizList.getElements() != null)) {
			this.logger.debug(" QuizList Elements "+quizList.getElements().toString());
			final List<String> quizStringList = quizList.getElements();
			for (Integer x = 0; x < quizStringList.size(); x = x + 2) {
				final Long quizId = Long.parseLong((quizStringList.get(x)));
				quizzesList.add(new Quiz(quizStringList.get(x + 1),quizId));
				quizzesMap.put(quizId, quizStringList.get(x + 1));
				quizzesTitles.add(quizStringList.get(x + 1));
				this.quizIds.add(quizId);
				this.logger.debug("Quiz item:"+quizId+ " -- " + quizStringList.get(x + 1));
			}
			this.quizEncoder.setUp(quizzesList);
			
			quizSelectModel = selectModelFactory.create(quizzesList, "name");
			
		} else {
			this.logger.info("No rated Objetcs found");
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
			
			this.learningTypeEncoder.setUp(learningTypes);

			learningTypeSelectModel = selectModelFactory.create(learningTypes, "name");

		} else {
			this.logger.debug("No Learning Types found");
		}
		
	}

	public final ValueEncoder<Course> getCourseValueEncoder() {
		return this.courseValueEncoder.create(Course.class);
	}
	
	public final ValueEncoder<Quiz> getQuizEncoder() {
		return this.quizEncoder.create(Quiz.class);
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
				endStamp = new Long(this.endDate.getTime() / THOU);
			}

			if (this.beginDate != null) {
				beginStamp = new Long(this.beginDate.getTime() / THOU);
			}
			this.resolution = 20;
			final List<Long> roles = new ArrayList<Long>();
			final List<Long> courses = new ArrayList<Long>();
			courses.add(this.courseId);

			// calling dm-server
			for (int i = 0; i < courses.size(); i++) {
				this.logger.debug("Courses: " + courses.get(i));
			}

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

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
				logger.error(e.getMessage());
			}

			final Map<Long, String> quizzesMap = CollectionFactory.newMap();
			final List<String> quizzesTitles = new ArrayList<String>();

			if ((quizList != null) && (quizList.getElements() != null)) {
				this.logger.debug(quizList.getElements().toString());
				final List<String> quizStringList = quizList.getElements();
				for (Integer x = 0; x < quizStringList.size(); x = x + 2) {
					Long quizId = Long.parseLong(quizStringList.get(x) );
					quizzesMap.put(quizId, quizStringList.get(x + 1));
					quizzesTitles.add(quizStringList.get(x + 1));
				}

			} else {
				this.logger.debug("No rated Objetcs found");
			}
			
			List<Long> gender = this.visWorker.getGenderIds(this.selectedGender);


			if (this.selectedQuizzes != null && !this.selectedQuizzes.isEmpty()) {
				for(Quiz q : this.selectedQuizzes)
					quizzesList.add(q.getCombinedId());
			} else if ((quizzesMap != null) && (quizzesMap.keySet() != null)) {
				logger.debug("Adding QuizzesMap");
				quizzesList = new ArrayList<Long>();
				quizzesList.addAll(quizzesMap.keySet());
			} 

			this.logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + this.resolution);

			final List<Long> results = this.analysis.computePerformanceHistogram(courseList, this.selectedUsers, quizzesList,
					(long) this.resolution, beginStamp, endStamp, gender);
			this.logger.debug("results for performance histogram:" + results);

			final List<List<Long>> preparedResults = CollectionFactory.newList();

			if (results != null) {
				Integer splitCounter = 0;
				Integer quizCounter = 0;
				List<Long> currentList = new ArrayList<Long>();
				for (Integer i = 0; i < results.size(); i++) {
					currentList.add(results.get(i));
					splitCounter++;
					if (splitCounter == this.resolution) {
						quizCounter++;
						splitCounter = 0;
						preparedResults.add(currentList);
						currentList = new ArrayList<Long>();
					}

				}
			}
			
			final JSONArray graphParentArray = new JSONArray();

			for (Integer z = 0; z < preparedResults.size(); z++) {
				final JSONObject graphDataObject = new JSONObject();
				final JSONArray graphDataValues = new JSONArray();
				final List<Long> tmpResults = preparedResults.get(z);

				if ((tmpResults != null) && (tmpResults.size() > 0)) {
					for (Integer j = 0; j < tmpResults.size(); j++) {
						final JSONObject graphValue = new JSONObject();
						graphValue.put("x", (100 / this.resolution) * j + "-" + (100 / this.resolution) * (j + 1) +  "%");
						graphValue.put("y", tmpResults.get(j));

						graphDataValues.put(graphValue);
					}
				}

				graphDataObject.put("values", graphDataValues);
				graphDataObject.put("key", quizzesMap.get(quizzesList.get(z)));
				graphParentArray.put(graphDataObject);
			}

			this.logger.debug(graphParentArray.toString());

			return graphParentArray.toString();
		}
		return "";
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
		this.logger.debug("Selected activities: " + this.selectedLearningTypes);
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
