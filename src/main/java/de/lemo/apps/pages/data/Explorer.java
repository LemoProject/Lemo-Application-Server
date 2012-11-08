package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.beaneditor.BeanModelImpl;
import org.apache.tapestry5.internal.services.MapMessages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="myCourses")
public class Explorer {
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private JavaScriptSupport jsSupport;
	
	@Inject
	private DateWorker dateWorker;
	
	@Inject 
	private StatisticWorker statisticWorker;
	
	@Inject
	private AnalysisWorker analysisWorker;
	
	@Inject
	private Locale currentLocale;
	
	@Component(parameters = {"dataItems=usageAnalysis"})
    private JqPlotLine chart1;
	
	@Inject
    @Path("../../images/icons/UsageAnalysis2_Icon.png")
    @Property
    private Asset usageAnalysisIcon;
	
	@Inject
    @Path("../../images/icons/UsageAnalysisLO_Icon.png")
    @Property
    private Asset usageAnalysisLOIcon;
	
	@Inject
    @Path("../../images/icons/PathAnalysis_Icon.png")
    @Property
    private Asset navAnalysisIcon;
	
	@Inject
    @Path("../../images/icons/PathAnalysis2_Icon.png")
    @Property
    private Asset pathAnalysisIcon;
	
	@Inject 
	private CourseDAO courseDAO;
	
	@Inject 
	private UserWorker userWorker;
	
	@Property
    private Course course;
	
	@Property
	@Persist
    private Course initCourse;
	
	@InjectComponent
	private Zone courseZone;
	
	@InjectComponent
	private Zone courseVisZone;
	
	@InjectComponent
	private Zone analysisSelectZone;
	
	@InjectComponent
	private Zone courseLastMonthZone;
	
	@InjectComponent
	private Zone courseOverallZone;
	
	@InjectComponent
	private Zone courseVisLastMonthZone;
	
	@InjectComponent
	private Zone courseVisOverallZone;
	
	@Inject
	private Initialisation init;
	
	@Inject
	private Analysis analysis;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	/**
	 * Preparing the Grid Model for the 
	 */
	@Property(write=false)
	@Retain
	private BeanModel coursesGridModel;
    {
    	coursesGridModel = beanModelSource.createDisplayModel(Course.class, componentResources.getMessages());
    	coursesGridModel.include("coursename","lastRequestDate");
    	coursesGridModel.add("compare",null);
    	    	
    }
    
    public Object onActivate(Course course){
    	if (course !=null)
    		this.initCourse = course;
    	return true;
    }

    /**
	 * Inserting a some js to deal with tab changes (e.g. Summary, Last Month, etc.) after the page rendering is finished
	 */
    @AfterRender
    void afterRender() {
        jsSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function() { "
                + " $('.tabs a:first').tab('show');"
                + "});", courseZone.getClientId());
    } 

    @Cached
    public List<Course> getCourses() { 
    	return courseDAO.findAllByOwner(userWorker.getCurrentUser()); 
    }
    
    public Course getCurrentCourse(){
    	if(this.initCourse !=null)
    			return this.initCourse;
    		else return getCourses().get(0);
    }
    
    //TODO Fix Problem with jqplot and zone updates
    Object onActionFromShow(Long id)
	  {
    	this.initCourse = courseDAO.getCourse(id);
    	//TODO Till problem with chart painting and zone updates is not solved, incremental page rendering is off
    	//return new MultiZoneUpdate("courseZone", courseZone.getBody()).add("courseLastMonthZone", courseLastMonthZone.getBody())
    	//															  .add("analysisSelectZone", analysisSelectZone.getBody());
    																  //.add("courseVisZone", courseVisZone.getBody());
    	return this;
	  }
    
    Object onActionFromFavorite(Long id){
    	courseDAO.toggleFavorite(id);
    	this.initCourse = courseDAO.getCourse(id);
    	return this;
    }
    
	
	public String getFirstRequestDate() {
		return dateWorker.getLocalizedDateTime(getCurrentCourse().getFirstRequestDate(),currentLocale);
	}
	
	public String getLastRequestDate() {
		return dateWorker.getLocalizedDateTime(getCurrentCourse().getLastRequestDate(),currentLocale);
	}
    
    @Cached
	public List getUsageAnalysisLastMonth(){
    	
			Date endDate = getCurrentCourse().getLastRequestDate();
			return analysisWorker.usageAnalysis(getCurrentCourse(), endDate, Calendar.MONTH, -1,null);	
	}
    
    //TODO Scheinbar greift ein Template Property noch auf diese Funktion zu ... auch wen dem nicht so sein sollte -> die korrekte Methode lautet ...LastMonth
    @Cached
	public List getUsageAnalysis(){
			Date endDate = getCurrentCourse().getLastRequestDate();
			return analysisWorker.usageAnalysis(getCurrentCourse(), endDate, Calendar.MONTH, -1,null);	
	}
    
    @Cached
	public List getUsageAnalysisOverall(){
		
			Date endDate = getCurrentCourse().getLastRequestDate();
			Date beginDate = getCurrentCourse().getFirstRequestDate();
			return analysisWorker.usageAnalysis(course, beginDate, endDate,null);
	}
    
    public Long getAverageRequest(List<List<XYDateDataItem>> dataItemList){
		return statisticWorker.getAverageRequest(dataItemList);
	}
	
	public Long getOverallRequest(List<List<XYDateDataItem>> dataItemList){
		return statisticWorker.getOverallRequest(dataItemList);
	}
	
	public Long getMaxRequest(List<List<XYDateDataItem>> dataItemList){
		return statisticWorker.getMaxRequest(dataItemList);
	}
	
	public Date getMaxRequestDate(List<List<XYDateDataItem>> dataItemList){
		return statisticWorker.getMaxRequestDate(dataItemList);
	}
    
    
    
    public String getLocalizedDate(Date date){
		return dateWorker.getLocalizedDate(date,currentLocale);
	}
	
	
	public String getLocalizedDateTime(Date date){
		return dateWorker.getLocalizedDateTime(date,currentLocale);
	}

}
