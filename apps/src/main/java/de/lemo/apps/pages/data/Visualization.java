package de.lemo.apps.pages.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLongObject;
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
	private Locale currentlocale;
	
	@Inject
	private JavaScriptSupport jsSupport;
	
	@Inject 
	private UserWorker userWorker;
	
	@Inject
	private Initialisation init;
	
	@Inject
	private Analysis analysis;
	
	@InjectPage
	Visualization visPage;
	
	@Property
	private JSONObject paramsZone;
	
	//@Persist
	@Property
	private Date endDate;
	
	//@Persist
	@Property
	private Date beginDate;
	
	//@Property
	@Persist
	Integer val;
	
	@Property
	@Persist
	Integer max;
	
	@Property
	@Persist
	Integer min;
	
	@Property
	@Persist
	Integer slideZone;
	
	@Property
	@Persist
	Integer resolution;
	
	@Property
	@Persist
	Integer activity;
	
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
	
	@Component(id = "customizeform")
	private Form form;
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
    private JqPlotLine chart1;
	
	@Inject 
	private CourseDAO courseDAO;
	
	@InjectComponent
	private Zone formZone;
	
	@Property
	@Persist
    private Course course;
	
	@Property
	@Persist
	private Long courseId;
	
	void setupRender() {
		
	}
	
	public Object onActivate(Course course){
		logger.debug("--- Bin im ersten onActivate");
		List<Long> allowedCourses = userWorker.getCurrentUser().getMyCourses();
		if(allowedCourses!=null && course !=null && course.getCourseId()!= null && allowedCourses.contains(course.getCourseId())){
			this.courseId = course.getCourseId();
			this.course = course;
			if(this.endDate==null) 
				this.endDate = course.getLastRequestDate();
			if(this.beginDate==null) 
				this.beginDate= course.getFirstRequestDate();
			Calendar beginCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			endCal.setTime(endDate);
			this.resolution=dateWorker.daysBetween(beginDate, endDate);
			return true;
		} else return Explorer.class;
	}
	
	public Object onActivate(){
		logger.debug("--- Bin im zweiten onActivate");
		return true;
	}
	
	
	
	public Course onPassivate(){
         return course;
	}
    
    void cleanupRender() {
		form.clearErrors();
		// Clear the flash-persisted fields to prevent anomalies in onActivate when we hit refresh on page or browser button
		this.courseId = null;
		this.course = null;
		
	}
	
	
	@Property(write=false)
	@Retain
	private BeanModel analysisGridModel;
    {
    	analysisGridModel = beanModelSource.createDisplayModel(Course.class, componentResources.getMessages());
    	analysisGridModel.include("coursename","lastRequestDate");
    	analysisGridModel.add("favorite",null);
    	    	
    }
    
    
    
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
    
    Object onSuccess(){
    	logger.debug(" ----- Begin: "+beginDate+ " EndDate: "+endDate);
    	this.resolution = slideZone;
    	visPage.course = course;
    	visPage.courseId = courseId;
    	visPage.slideZone = slideZone;
    	visPage.resolution = slideZone;
    	visPage.beginDate = beginDate;
    	visPage.endDate	 = endDate;
    	return this;
    	//return request.isXHR() ? formZone.getBody() : null;
    }
    
    public List getFirstQuestionDataItems(){
		List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
        List<XYDateDataItem> list1 = CollectionFactory.newList();
        if(courseId!=null){
        	Long endStamp=0L;
        	Long beginStamp=0L;
        	if(endDate!=null){
        		endStamp = new Long(endDate.getTime()/1000);
        	} //else endtime= 1334447632L;
	        
        	if(beginDate!=null){
        		beginStamp = new Long(beginDate.getTime()/1000);
        	} //else starttime = 1308968800L;
        	
			
			//int resolution = 30;
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
			
			
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			logger.debug("BeginDate: "+beginDate);
			//checking if result size matches resolution 
			if(results!= null && results.getElements()!=null && results.getElements().size() == resolution)
	        for(int i=0 ;i<resolution;i++){
	        	
	        	beginCal.add(Calendar.DAY_OF_MONTH, 1);
	        	list1.add(new XYDateDataItem(beginCal.getTime() , results.getElements().get(i)));
	        }
    	}
        dataList.add(list1);
        return dataList;
	}

}
