package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
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
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer,
            EResourceType.class);

    // Select Model for activity multi-select component
    @Property(write = false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);

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
        List<Long> courses = new ArrayList<Long>();
        courses.add(course.getCourseId());
         List<Long> elements = analysis.computeCourseUsers(courses, beginDate.getTime() / 1000, endDate.getTime() / 1000).getElements();
        logger.info("          ----        "+elements);
         return elements;
    }

    public Object onActivate(Course course) {
        logger.debug("--- Bin im ersten onActivate");
        List<Long> allowedCourses = userWorker.getCurrentUser().getMyCourses();
        if(allowedCourses != null && course != null && course.getCourseId() != null
                && allowedCourses.contains(course.getCourseId())) {
            this.courseId = course.getCourseId();
            this.course = course;
            if(this.selectedCourses == null){
            	this.selectedCourses= new ArrayList<Long>();
            	this.selectedCourses.add(courseId);
            }
            return true;
        } else
            return Explorer.class;
    }

    public Object onActivate() {
        logger.debug("--- Bin im zweiten onActivate");
        return true;
    }

    public Course onPassivate() {
        return course;
    }

    void cleanupRender() {
        customizeForm.clearErrors();
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

//    void pageReset() {
//        selectedUsers = null;
//        userIds = getUsers();
//    }

    void onPrepareForRender() {
        List<Course> courses = courseDAO.findAllByOwner(userWorker.getCurrentUser());
        courseModel = new CourseIdSelectModel(courses);
        userIds = getUsers();
        
        quizIds = new ArrayList<Long>();
        
        List<Long> courseList = new ArrayList<Long>();
		courseList.add(courseId);
        ResultListStringObject quizList = init.getRatedObjects(courseList);
		
		Map<Long,String> quizzesMap = CollectionFactory.newMap();
		List<String> quizzesTitles = new ArrayList<String>();
		
		if(quizList!=null && quizList.getElements()!=null)
		{
			logger.debug(quizList.getElements().toString());
			List<String> quizStringList = quizList.getElements();
			 for(Integer x = 0; x < quizStringList.size(); x=x+3) {
				 Long combinedQuizId = Long.parseLong((quizStringList.get(x)+quizStringList.get(x+1)));
				 quizzesMap.put(combinedQuizId, quizStringList.get(x+2));
				 quizzesTitles.add(quizStringList.get(x+2));
				 quizIds.add(combinedQuizId);
			 }
		
		} else logger.debug("No rated Objetcs found");
    }

    public final ValueEncoder<Course> getCourseValueEncoder() {
        // List<Course> courses =
        // courseDAO.findAllByOwner(userWorker.getCurrentUser());
        return courseValueEncoder.create(Course.class);
    }

    // returns datepicker params
    public JSONLiteral getDatePickerParams() {
        return dateWorker.getDatePickerParams(currentlocale);
    }

    public String getQuestionResult() {
    	List<List<TextValueDataItem>> dataList = CollectionFactory.newList();
 
        if(courseId != null) {
            Long endStamp = 0L;
            Long beginStamp = 0L;
            if(endDate != null) {
                endStamp = new Long(endDate.getTime() / 1000);
            }

            if(beginDate != null) {
                beginStamp = new Long(beginDate.getTime() / 1000);
            }

            //if(this.resolution == null || this.resolution < 10)
            this.resolution = 100;
            List<Long> roles = new ArrayList<Long>();
            List<Long> courses = new ArrayList<Long>();
            courses.add(courseId);

            // calling dm-server
            for(int i = 0; i < courses.size(); i++) {
                logger.debug("Courses: " + courses.get(i));
            }

            logger.debug("Starttime: " + beginStamp + " Endtime: " + endStamp + " Resolution: " + resolution);

//            @SuppressWarnings("unchecked")
//            List<ResourceRequestInfo> results = analysisWorker.learningObjectUsage(this.course, beginDate, endDate,
//                selectedUsers, selectedActivities);
            
            List<Long> courseList = new ArrayList<Long>();
	    	if(selectedCourses!= null && !selectedCourses.isEmpty()){
	    		if (!selectedCourses.contains(courseId))
	    			selectedCourses.add(courseId);
	    		courseList = selectedCourses;
	    	}	
	    		else courseList.add(courseId);
            
	    	List<Long> quizzesList = new ArrayList<Long>();
	    	
	    	ResultListStringObject quizList = init.getRatedObjects(courseList);
			
			Map<Long,String> quizzesMap = CollectionFactory.newMap();
			List<String> quizzesTitles = new ArrayList<String>();
			
			if(quizList!=null && quizList.getElements()!=null)
			{
				logger.debug(quizList.getElements().toString());
				List<String> quizStringList = quizList.getElements();
				 for(Integer x = 0; x < quizStringList.size(); x=x+3) {
					 Long combinedQuizId = Long.parseLong((quizStringList.get(x)+quizStringList.get(x+1)));
					 quizzesMap.put(combinedQuizId, quizStringList.get(x+2));
					 quizzesTitles.add(quizStringList.get(x+2));
				 }
			
			} else logger.debug("No rated Objetcs found");
			
			if(selectedQuizzes != null){
				quizzesList = selectedQuizzes;
			} else if(quizzesMap!=null && quizzesMap.keySet()!=null){
				quizzesList = new ArrayList<Long>();
				quizzesList.addAll(quizzesMap.keySet());
			}    
			
			
			logger.debug("Starttime: "+beginStamp+ " Endtime: "+endStamp+ " Resolution: "+resolution);
		
			List<Long> results = analysis.computePerformanceHistogram(courseList, selectedUsers, quizzesList, resolution, beginStamp, endStamp);
			logger.debug("results for performance histogram:"+ results);	
			
			List<Long> preparedResults = CollectionFactory.newList();
			
			if (results!= null)
			{	
				Integer splitCounter = 0;
				Integer quizCounter = 0;
				Long avgCounter = 0L;
				Long avgAmount = 0L;
				//preparedResults.add(new ArrayList<Long>());
				List<Long> currentList = new ArrayList<Long>();
				 for(Integer i = 0; i < results.size(); i++) 
				 {
					 currentList.add(results.get(i));
					 avgAmount = avgAmount+results.get(i)*splitCounter;
					 //logger.debug("Percent: "+splitCounter+" persons:"+results.get(i)+" Result:"+results.get(i)*splitCounter);
					 if(results.get(i) != null && results.get(i) > 0) avgCounter = avgCounter+results.get(i);
						
					 splitCounter++;
					 if(splitCounter==resolution) {
						 
						 
						 
						 List<Long> avgResult = new ArrayList<Long>();
						 if (avgCounter != 0){
							 preparedResults.add(avgAmount/ avgCounter);
							 logger.debug("Result for "+ quizzesMap.get(quizzesList.get(quizCounter))+" : "+avgAmount/ avgCounter);
						 }else preparedResults.add(0L);
						 //preparedResults.add(avgResult);
						 quizCounter++; 
						 splitCounter=0;
						 avgAmount=0L;
						 avgCounter = 0L;
						 currentList=new ArrayList<Long>();
					 }
					 
				 }
			}

            JSONArray graphParentArray = new JSONArray();
            
            //for(Integer z = 0; z < preparedResults.size(); z++) {
	            JSONObject graphDataObject = new JSONObject();
	            JSONArray graphDataValues = new JSONArray();
	            List<Long> tmpResults = preparedResults; 
	            
	            if(tmpResults != null && tmpResults.size() > 0)
	                for(Integer j = 0; j < tmpResults.size(); j++) {
	                    JSONObject graphValue = new JSONObject();
	                    
	                    
	                    graphValue.put("x", quizzesMap.get(quizzesList.get(j)));
	                    graphValue.put("y", tmpResults.get(j));
	
	                    graphDataValues.put(graphValue);
	                }
	
	            graphDataObject.put("values", graphDataValues);
	            graphDataObject.put("key", "Performance");
	            
	            graphParentArray.put(graphDataObject);
             //}
//            JSONObject graphDataObject2 = new JSONObject();
//            JSONArray graphDataValues2 = new JSONArray();
//
//            if(results != null && results.size() > 0) {
//                for(Integer i = 0; i < results.size(); i++) {
//                    JSONObject graphValue2 = new JSONObject();
//
//                    graphValue2.put("x", results.get(i).getTitle());
//                    graphValue2.put("y", results.get(i).getUsers());
//
//                    graphDataValues2.put(graphValue2);
//                }
//            }
//            graphDataObject2.put("values", graphDataValues2);
//            graphDataObject2.put("key", "User");

           
           // graphParentArray.put(graphDataObject2);

            logger.debug(graphParentArray.toString());

            return graphParentArray.toString();
        }
        return "";
    }

    
    void setupRender() {
        logger.debug(" ----- Bin in Setup Render");

        ArrayList<Long> courseList = new ArrayList<Long>();
        courseList.add(course.getCourseId());
        
        
        if(this.endDate == null){
            this.endDate = course.getLastRequestDate();
        } else {
       	 	this.selectedUsers = null;
       	 	userIds = getUsers();
       }
        if(this.beginDate == null){
            this.beginDate = course.getFirstRequestDate();
        } else {
       	 	this.selectedUsers = null;
       	 	userIds = getUsers();
        }
        Calendar beginCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        beginCal.setTime(beginDate);
        endCal.setTime(endDate);
        this.resolution = dateWorker.daysBetween(beginDate, endDate);

        
        
//
//        Calendar beginCal = Calendar.getInstance();
//        Calendar endCal = Calendar.getInstance();
//        beginCal.setTime(beginDate);
//        endCal.setTime(endDate);
//        this.resolution = dateWorker.daysBetween(beginDate, endDate);
//        logger.debug("SetupRender End --- BeginDate:" + beginDate + " EndDate: " + endDate + " Res: " + resolution);
    }

    @AfterRender
    public void afterRender() {
        javaScriptSupport.addScript("");
    }

    void onPrepareFromCustomizeForm() {
        this.course = courseDAO.getCourseByDMSId(courseId);
    }

    void onSuccessFromCustomizeForm() {
        logger.debug("   ---  onSuccessFromCustomizeForm ");
        logger.debug("Selected activities: " + selectedActivities);
        logger.debug("Selected users: " + selectedUsers);
    }

    public String getLocalizedDate(Date inputDate) {
        SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy", currentlocale);
        return df_date.format(inputDate);
    }

    public String getFirstRequestDate() {
        return getLocalizedDate(this.beginDate);//.course.getFirstRequestDate());
    }

    public String getLastRequestDate() {
        return getLocalizedDate(this.endDate);//.course.getLastRequestDate());
    }
}
