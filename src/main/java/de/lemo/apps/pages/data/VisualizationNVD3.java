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
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer,
            EResourceType.class);
    
    @Property(write=false)
	@Retain
	private BeanModel resourceGridModel;
    {
    	resourceGridModel = beanModelSource.createDisplayModel(ResourceRequestInfo.class, componentResources.getMessages());
    	resourceGridModel.include("resourcetype","title","requests");
    	//resourceGridModel.add("show",null);
    	    	
    }

    // Select Model for activity multi-select component
    @Property(write = false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);

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
        List<Long> courses = new ArrayList<Long>();
        courses.add(course.getCourseId());
         List<Long> elements = analysis.computeCourseUsers(courses, beginDate.getTime() / 1000, endDate.getTime() / 1000).getElements();
        logger.info(" User Ids:         ----        "+elements);
         return elements;
    }
    
    
    @Cached
    public List<ResourceRequestInfo> getResourceList(){
    	this.course = courseDAO.getCourseByDMSId(courseId);
		
		List<ResourceRequestInfo> resultList;
		
		if(selectedActivities!=null && selectedActivities.size()>=1){
			logger.debug("Starting Extended Analysis - Including LearnbObject Selection ...  ");
			resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, selectedActivities);
		}else {
			logger.debug("Starting Extended Analysis - Including ALL LearnObjects ....");
			resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, null);
		}
		logger.debug("ExtendedAnalysisWorker Results: "+resultList);
		
    	return resultList;
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
        courseIds = userWorker.getCurrentUser().getMyCourses();
    }

    public final ValueEncoder<Course> getCourseValueEncoder() {
        // List<Course> courses =
        // courseDAO.findAllByOwner(userWorker.getCurrentUser());
        return courseValueEncoder.create(Course.class);
    }

    // returns datepicker params
    public JSONLiteral getDatePickerParams() {
        return dateWorker.getDatePickerParams();
    }

    public String getQuestionResult() {
        
    	List<Long> courseList = new ArrayList<Long>();
    	if(selectedCourses!= null && !selectedCourses.isEmpty()){
    		if (!selectedCourses.contains(courseId))
    			selectedCourses.add(courseId);
    		courseList = selectedCourses;
    	}	
    		else courseList.add(courseId);
   

        boolean considerLogouts = true;

        ArrayList<String> types = null;
        if(selectedActivities != null && !selectedActivities.isEmpty()) {
            types = new ArrayList<String>();
            for(EResourceType resourceType : selectedActivities) {
                types.add(resourceType.name().toUpperCase());
            }
        }

        Long endStamp = 0L;
        Long beginStamp = 0L;
        if(beginDate != null) {
            beginStamp = new Long(beginDate.getTime() / 1000);
        }
        if(endDate != null) {
            endStamp = new Long(endDate.getTime() / 1000);
        }
        
        
        
        
        this.resolution=(dateWorker.daysBetween(beginDate, endDate)+1);

  
        HashMap<Long, ResultListLongObject> results = analysis.computeCourseActivity(courseList, null, selectedUsers, beginStamp, endStamp, resolution, types);
    
        JSONArray graphParentArray = new JSONArray();
        JSONObject graphDataObject = new JSONObject();
        JSONArray graphDataValues = new JSONArray();
        
        if(results!=null)
		{			
			Set<Long> courseSet = results.keySet();
			Iterator<Long> it = courseSet.iterator();
			while(it.hasNext()){
				
				Long courseId = it.next();
				ResultListLongObject resultObject = results.get(courseId);
        
			    graphDataObject = new JSONObject();
			    graphDataValues = new JSONArray();
			    Long currentDateStamp = 0L;
			    
                for(Integer i = 0; i < resultObject.getElements().size(); i++) {
                    JSONArray graphValue = new JSONArray();
                    Long dateMultiplier = 60 * 60 * 24 * i.longValue() * 1000;
                    currentDateStamp = beginStamp * 1000 + dateMultiplier;
                    graphValue.put(0, currentDateStamp);
                    graphValue.put(1, resultObject.getElements().get(i));
                    graphDataValues.put(graphValue);
                }
			     
			    Course course = courseDAO.getCourseByDMSId(courseId);
			    graphDataObject.put("values", graphDataValues);
			    graphDataObject.put("key",course.getCourseName());
			    graphParentArray.put(graphDataObject);
			        
			}       
			        
		}
        

       

        logger.debug(graphParentArray.toString());
        
        return graphParentArray.toString();       
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
        return getLocalizedDate(this.endDate);//course.getLastRequestDate());
    }
    
    public String getResourceTypeName(){
    	if(this.resourceItem!=null && this.resourceItem.getResourcetype() != "")
    		return messages.get("EResourceType."+this.resourceItem.getResourcetype());
    	else return messages.get("EResourceType.UNKNOWN");
    }
}
