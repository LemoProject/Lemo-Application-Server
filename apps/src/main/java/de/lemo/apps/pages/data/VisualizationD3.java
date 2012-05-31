package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;

import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlot;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;


import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="visualizationTitle")
@Import(library={"../../js/d3/d3_custom.js"})
public class VisualizationD3 {
	
	@Environmental
    private JavaScriptSupport javaScriptSupport;
	
	@Inject
    private Logger logger;
	
	@Inject 
	private DateWorker dateWorker;
	
	@Inject 
	private UserWorker userWorker;
	
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
	
	@Component(id = "customizeform")
	private Form form;
	
	@Property
	@Persist
    private Course course;
	
	@Property
	@Persist
	private Long courseId;
	
	@Property
	private Date endDate;
	
	@Property
	private Date beginDate;
	
	@Property
	@Persist
	Integer resolution;
	
	@Property
	@Persist
	private List<EResourceType> activities;
	
	private List<List<XYDataItem>> testData;
	
	// Value Encoder for activity multi-select component
    @Property(write=false)
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer, EResourceType.class);
    
    // Select Model for activity multi-select component
    @Property(write=false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);
    
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
    
    
 // returns datepicker params
	public JSONLiteral getDatePickerParams(){
		return dateWorker.getDatePickerParams();
	}

    @Cached
    public List getTestData()
    {
        List<List<XYDataItem>> dataList = CollectionFactory.newList();
        List<XYDataItem> list1 = CollectionFactory.newList();
        List<XYDataItem> list2 = CollectionFactory.newList();

        list1.add(new XYDataItem(0, 0));
        list1.add(new XYDataItem(6, 1));
        list1.add(new XYDataItem(12, 3));
        list1.add(new XYDataItem(18, 5));
        list1.add(new XYDataItem(24, 2));

        list2.add(new XYDataItem(0, 1));
        list2.add(new XYDataItem(6, 2));
        list2.add(new XYDataItem(12, 7));
        list2.add(new XYDataItem(18, 13.5));
        list2.add(new XYDataItem(24, 10));

        dataList.add(list1);
        dataList.add(list2);

        return dataList;
    }
    
    @AfterRender
    public void afterRender() {
            javaScriptSupport.addScript("" 
            );
    }
    
    
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
}
