package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
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
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.pages.data.Explorer;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.LongValueEncoder;

@RequiresAuthentication
@BreadCrumb(titleKey = "visualizationTitle")
@Import(library = { "../../js/d3/d3_custom_DM_Vis_M2.js" })
public class VisualizationFP {

    @Environmental
    private JavaScriptSupport javaScriptSupport;

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

//    @InjectComponent
//    private Zone formZone;

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
//    @Property(write = false)
//    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer,
//            EResourceType.class);

    // Select Model for activity multi-select component
//    @Property(write = false)
//    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);

//    @Property
//    @Persist
//    private List<EResourceType> selectedActivities;

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
            if(this.endDate == null) {
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
            logger.debug("MinSup:"+minSup);
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
        this.minSup=8;
    }

//    void pageReset() {
//        selectedUsers = null;
//        userIds = getUsers();
//    }

    void onPrepareForRender() {
        List<Course> courses = courseDAO.findAllByOwner(userWorker.getCurrentUser());
        courseModel = new CourseIdSelectModel(courses);
        
        userIds = getUsers();
    }

    public final ValueEncoder<Course> getCourseValueEncoder() {
        // List<Course> courses =
        // courseDAO.findAllByOwner(userWorker.getCurrentUser());
        return courseValueEncoder.create(Course.class);
    }
    
    
	@Property
	@Persist
	Integer val;
	
	@Property
	//@Persist
	Integer max;
	
	@Property
	//@Persist
	Integer min;
	
	@Property
	@Persist
	Integer minSup;

	@Property
	private JSONObject minSupParams,
					minValue,
					maxValue;
		
//	@Component
//	private Zone myZone;
	
	@OnEvent(org.apache.tapestry5.EventConstants.ACTIVATE)
	public void initSliderZone(){
		max=10;
		min=1;
		if(minSup==null)
				minSup=8;
		//minSupParams=new JSONArray();
		minSupParams=new JSONObject();
		
		minSupParams.put("min", 1);
		minSupParams.put("max", 10);
		minSupParams.put("value", minSup);
		//JSONLiteral slideFunction = new JSONLiteral("function( event, ui ) {$( '#minSupSlider-label' ).html( 'Minimum Support (0.1 - 1): ' + ui.value ); console.log('MinSup Value: '+ui.value);}");
		//minSupParams.put("slide", slideFunction);
		
	}

//	@OnEvent(value=org.apache.tapestry5.EventConstants.SUBMIT, component="minSup")
//	public Object returnZone(){
//		String input = request.getParameter("jquery.slider");
//		minSup=Integer.parseInt(input);
//		System.out.println("MinSup Value: "+minSup);
//		return this;
//	}
//
//    
    
    
    

    // returns datepicker params
    public JSONLiteral getDatePickerParams() {
        return dateWorker.getDatePickerParams();
    }

    @Property
    //@Persist
    private double minSupDouble;
    
    //@Property
    //@Persist
    private Double minSupValue;
    
    public String getQuestionResult() {
        ArrayList<Long> courseIds = new ArrayList<Long>();
        courseIds.add(courseId);

        boolean considerLogouts = false;

//        ArrayList<String> types = null;
//        if(selectedActivities != null && !selectedActivities.isEmpty()) {
//            types = new ArrayList<String>();
//            for(EResourceType resourceType : selectedActivities) {
//                types.add(resourceType.name().toLowerCase());
//            }
//        }

        Long endStamp = 0L;
        Long beginStamp = 0L;
        if(beginDate != null) {
            beginStamp = new Long(beginDate.getTime() / 1000);
        }
        if(endDate != null) {
            endStamp = new Long(endDate.getTime() / 1000);
        }
        if(minSup==null || minSup.equals(0)) minSup = 8;
        minSupValue = new Double(minSup);
        minSupValue = minSupValue / 10;
        logger.debug("MinSupValue:"+ minSupValue + "  --  "+ minSupValue.doubleValue());
        minSupDouble = minSupValue.doubleValue(); 
        return analysis.computeQFrequentPathBIDE(courseIds, selectedUsers, minSupDouble , considerLogouts, beginStamp, endStamp);
    }
    
    public String getSupportValue(){
    	Double minSupTemp = new Double(minSup);
        minSupTemp = minSupTemp / 10;
    	return minSupTemp.toString();
    }

    void setupRender() {
        logger.debug(" ----- Bin in Setup Render");

        ArrayList<Long> courseList = new ArrayList<Long>();
        courseList.add(course.getCourseId());

        Calendar beginCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        beginCal.setTime(beginDate);
        endCal.setTime(endDate);
        this.resolution = dateWorker.daysBetween(beginDate, endDate);
        logger.debug("SetupRender End --- BeginDate:" + beginDate + " EndDate: " + endDate + " Res: " + resolution);
        
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
        String input = request.getParameter("minSup-slider");
		if(input!=null)
			minSup=Integer.parseInt(input);
		logger.debug("MinSup Value: "+minSup);
//        logger.debug("Selected activities: " + selectedActivities);
        logger.debug("Selected users: " + selectedUsers);
    }

    public String getLocalizedDate(Date inputDate) {
        SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy", currentlocale);
        return df_date.format(inputDate);
    }

    public String getFirstRequestDate() {
        return getLocalizedDate(this.course.getFirstRequestDate());
    }

    public String getLastRequestDate() {
        return getLocalizedDate(this.course.getLastRequestDate());
    }
}
