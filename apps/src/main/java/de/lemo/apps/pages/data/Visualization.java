package de.lemo.apps.pages.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
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
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;

import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="visualizationTitle")
public class Visualization {

	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
	private Request request;
	
	@Inject
    private Logger logger;
	
	@Inject 
	private DateWorker dateWorker;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Messages messages;
	
	@Inject
	private TypeCoercer coercer;
	
	@Inject
	private Locale currentlocale;
	
	@Inject
	private JavaScriptSupport jsSupport;
	
	@Inject 
	private UserWorker userWorker;
	
	@Inject
	private Initialisation init;
	
	@Inject
	private AnalysisWorker analysisWorker;
	
	@Inject
	private Analysis analysis;
	
	@InjectPage
	Visualization visPage;
	
	@Inject 
	private CourseDAO courseDAO;
	
	@InjectComponent
	private Zone formZone;
	
	@Component(id = "optionForm")
	private Form optionForm;
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
    private JqPlotLine chart1;
	
	@Property
	@Persist
	private List<EResourceType> activities;
	
	@Property
	//@Persist
    private Course course;
	
	@Property
	//@Persist
	private Long courseId;
	
	@Property
	private JSONObject paramsZone;
	
	@Component(id = "beginDate")
	private DateField beginDateField;

	@Component(id = "endDate")
	private DateField endDateField;
	
	@Persist
	@Property
	private Date endDate;
	
	@Persist
	@Property
	private Date beginDate;
	
	//@Property
	//@Persist
	Integer val;
	
	@Property
	//@Persist
	Integer max;
	
	@Property
	//@Persist
	Integer min;
	
	@Property
	//@Persist
	Integer slideZone;
	
	@Property
	//@Persist
	Integer resolution;
	
	@Property
	//@Persist
	Integer activity;
	
	@Property
	@Persist
	private Boolean twentyFourhMode;
	
//	@Property
//	private JSONObject params;	
//	
//		
//	@Component
//	private Zone myZone;
//	
//	@OnEvent(org.apache.tapestry5.EventConstants.ACTIVATE)
//	public void initSliderZone(){
//		max=30;
//		min=0;
//		slideZone=this.resolution;
//		paramsZone=new JSONObject();
//		paramsZone.put("value", slideZone);
//	}
//
//	@OnEvent(value=org.apache.tapestry5.EventConstants.ACTION, component="sliderZone")
//	public Object returnZone(){
//		String input = request.getParameter("slider");
//		slideZone=Integer.parseInt(input);
//		return myZone.getBody();
//	}
//	
	



	
	void setupRender() {
		logger.debug(" ----- Bin in Setup Render");
		if (this.twentyFourhMode == null) this.twentyFourhMode = false;
		logger.debug("SetupRender Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		this.course = courseDAO.getCourseByDMSId(courseId);
		if(this.endDate==null) 
			this.endDate = course.getLastRequestDate();
		if(this.beginDate==null) 
			this.beginDate= course.getFirstRequestDate();
		Calendar beginCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		beginCal.setTime(beginDate);
		endCal.setTime(endDate);
		if(this.twentyFourhMode){
			endCal.setTime(beginDate);
			beginCal.add(Calendar.DAY_OF_MONTH, -1);
			endCal.add(Calendar.DAY_OF_MONTH, +1);
			beginDate=beginCal.getTime();
			endDate=endCal.getTime();
			this.resolution=(dateWorker.daysBetween(beginDate, endDate)+1)*24;
			logger.debug("SetupRender End ---Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		} else this.resolution=dateWorker.daysBetween(beginDate, endDate);
		logger.debug("SetupRender End --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
	}
	
	Object onActivate(Long courseId){
		
		logger.debug("--- Bin im ersten onActivate");
		logger.debug("OnActivate Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		List<Long> allowedCourses = userWorker.getCurrentUser().getMyCourses();
		if(allowedCourses!=null && courseId!= null && allowedCourses.contains(courseId)){
			this.courseId = courseId;
			
			return true;
		} else return Explorer.class;
	}
	
	Object onActivate(){
		logger.debug("--- Bin im zweiten onActivate");
		return Explorer.class;
	}
	
	Long onPassivate(){
		Boolean is24hModeTemp = this.twentyFourhMode;
		logger.debug("On Passivate Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		//componentResources.discardPersistentFieldChanges();
		logger.debug("On Passivate End --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		this.twentyFourhMode = is24hModeTemp;
        return courseId;
	}
	
	void onPrepareFromOptionForm(){
		
		logger.debug(" ----- Bin in On Prepare");
		logger.debug("On Prepare Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		
		this.course = courseDAO.getCourseByDMSId(courseId);
	}
	
	void onSuccessFromOptionForm() {
		logger.debug(" ----- Bin in On Success");
		logger.debug("On Success Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		//setupRender();
		//return this;//request.isXHR() ? formZone.getBody() : null;
	}
	
//    Object onSuccess(){
//    	logger.debug(" ----- Begin: "+beginDate+ " EndDate: "+endDate);
//    	visPage.course = course;
//    	visPage.courseId = courseId;
//    	visPage.slideZone = slideZone;
//    	visPage.resolution = slideZone;
//    	visPage.beginDate = beginDate;
//    	visPage.endDate	 = endDate;
//    	return this;
//    	//return request.isXHR() ? formZone.getBody() : null;
//    }
	
	Object onActionFromUpdateTimeMode() {
		
		this.course = courseDAO.getCourseByDMSId(courseId);
		if(this.twentyFourhMode==null){
			this.twentyFourhMode = true;
			}
			else if (!this.twentyFourhMode){
				this.twentyFourhMode=!this.twentyFourhMode;
			}else {
				this.beginDate= course.getFirstRequestDate();
				this.endDate = course.getLastRequestDate();
				this.twentyFourhMode=!this.twentyFourhMode;
			}
	  	return formZone;															 
	 }
	
	
	// returns datepicker params
	public JSONLiteral getDatePickerParams(){
		return dateWorker.getDatePickerParams();
	}
	
	
    
    void cleanupRender() {
		optionForm.clearErrors();
		// Clear the flash-persisted fields to prevent anomalies in onActivate when we hit refresh on page or browser button
		this.courseId = null;
		this.course = null;
		
	}
    
    // Value Encoder for activity multi-select component
    @Property(write=false)
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer, EResourceType.class);
    
    // Select Model for activity multi-select component
    @Property(write=false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);
	
	@Property(write=false)
	@Retain
	private BeanModel analysisGridModel;
    {
    	analysisGridModel = beanModelSource.createDisplayModel(ResourceRequestInfo.class, componentResources.getMessages());
    	analysisGridModel.include("resourcetype","title","requests");
    	analysisGridModel.add("show",null);
    	    	
    }
    
    @Property
    private ResourceRequestInfo analysisItem;
    
    /**
	 * Gibt das Datum in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
	 * @param inputDate
	 * @return Ein Objekt vom Typ String
	 */
	public String getLocalizedDate(Date inputDate) {
		SimpleDateFormat df_date = new SimpleDateFormat( "MMM dd, yyyy", currentlocale );
		return df_date.format(inputDate);
	}
	
	public String getFirstRequestDate() {
		return getLocalizedDate(this.course.getFirstRequestDate());
	}
	
	public String getLastRequestDate() {
		return getLocalizedDate(this.course.getLastRequestDate());
	}

    //public List<Course> getCourses() { return courseDAO.findAll(); }
    
    
//    public Object onActionFromUpdate(){
//    	System.out.println(" ----- Begin: "+beginDate+ " EndDate: "+endDate);
//		this.resolution = slideZone;
//		visPage.course = course;
//		visPage.courseId = courseId;
//		visPage.slideZone = slideZone;
//		visPage.resolution = slideZone;
//		visPage.beginDate = beginDate;
//		visPage.endDate	 = endDate;
//		return visPage;
//    }
    

    
    public String getResourceTypeName(){
    	if(this.analysisItem!=null && this.analysisItem.getResourcetype() != "")
    		return messages.get("EResourceType."+this.analysisItem.getResourcetype());
    	else return messages.get("EResourceType.UNKNOWN");
    }
    
    @Cached
    public List getAnalysisList(){
    	
    	List<String> resList = new ArrayList<String>();
		resList.add(EResourceType.COURSE.toString());
		resList.add(EResourceType.FORUM.toString());
		resList.add(EResourceType.RESOURCE.toString());
		//resList.add(EResourceType.ASSIGNMENT.toString());
		resList.add(EResourceType.QUESTION.toString());
		resList.add(EResourceType.WIKI.toString());
		resList.add(EResourceType.UNKNOWN.toString());
	
		logger.debug("Starting Extended Analysis");
		
		List resultList;
		
		if(activities!=null && activities.size()>=1)
			resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, activities);
		else resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, null);
		logger.debug("ExtendedAnalysisWorker: "+resultList);
		
    	return resultList;
    }
    
    
    public List getFirstQuestionDataItems(){
		List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
        List<XYDateDataItem> list1 = CollectionFactory.newList();
        List<XYDateDataItem> list2 = CollectionFactory.newList();
        if(courseId!=null){
        	Long endStamp=0L;
        	Long beginStamp=0L;
        	if(endDate!=null){
        		endStamp = new Long(endDate.getTime()/1000);
        	} //else endtime= 1334447632L;
	        
        	if(beginDate!=null){
        		beginStamp = new Long(beginDate.getTime()/1000);
        	} //else starttime = 1308968800L;
        	
			if (this.resolution == null || this.resolution < 10 )
				this.resolution = 30;
			List<Long> roles = new ArrayList<Long>();
			List<Long> courses = new ArrayList<Long>();
			courses.add(courseId);
			
			//calling dm-server
			for (int i=0;i<courses.size();i++){
				logger.debug("Courses: "+courses.get(i));
			}
			logger.info("Starttime: "+beginStamp+ " Endtime: "+endStamp+ " Resolution: "+resolution);
			ResultListLongObject results = analysis.computeQ1(courses, roles, beginStamp, endStamp, resolution);
			
			ResultListRRITypes rri;
			 if(activities!=null && activities.size()>=1)
				rri =  analysisWorker.usageAnalysisExtendedDetails(this.course, beginDate, endDate, resolution, activities);
			else rri =  analysisWorker.usageAnalysisExtendedDetails(this.course, beginDate, endDate, resolution, null);
			
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			logger.debug("BeginDate: "+beginDate);
			//checking if result size matches resolution 
			if(results!= null && results.getElements()!=null && results.getElements().size() == resolution)
	        for(int i=0 ;i<resolution;i++){
	        	
	        	if(this.twentyFourhMode)
	        		beginCal.add(Calendar.HOUR_OF_DAY, 1);
	        		else beginCal.add(Calendar.DAY_OF_MONTH, 1);
	        	list1.add(new XYDateDataItem(beginCal.getTime() , results.getElements().get(i)));
	        	//list2.add(new XYDateDataItem(beginCal.getTime() , results.getElements().get(i)+25L));
	        }
    	}
        dataList.add(list1);
        dataList.add(list2);
        return dataList;
	}

}
