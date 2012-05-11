package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
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

import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;

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
	private Locale currentLocale;
	
//	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
//    private JqPlotLine chart1;
	
	@Component(parameters = {"dataItems=testPieData"})
    private JqPlotPie chart2 ;
	
	@Inject 
	private CourseDAO courseDAO;
	
	@Inject 
	private UserWorker userWorker;
	
	@Property
    private Course course;
	
	@InjectComponent
	private Zone courseZone;
	
	@InjectComponent
	private Zone courseVisZone;
	
	@Inject
	private Initialisation init;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	
	@Property(write=false)
	@Retain
	private BeanModel coursesGridModel;
    {
    	coursesGridModel = beanModelSource.createDisplayModel(Course.class, componentResources.getMessages());
    	coursesGridModel.include("coursename","lastRequestDate");
    	coursesGridModel.add("compare",null);
    	    	
    }
    
    @AfterRender
    void afterRender() {
        jsSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function() { "
                + " $('.tabs a:first').tab('show');"
              //  + " alert('Hello'); "
                + "});", courseZone.getClientId());
    } 

    public List<Course> getCourses() { 
    	return courseDAO.findAllByOwner(userWorker.getCurrentUser()); 
    }
    
    void onActionFromShow(Long id)
	  {
    	this.course = courseDAO.getCourse(id);
    	ajaxResponseRenderer.addRender("courseZone", courseZone.getBody());
	    //return courseZone.getBody();
	  }
    
    void onActionFromUpdate(Long id){
    	courseDAO.toggleFavorite(id);
    }
    
	
	public String getFirstRequestDate() {
		return dateWorker.getLocalizedDateTime(this.course.getFirstRequestDate(),currentLocale);
	}
	
	public String getLastRequestDate() {
		return dateWorker.getLocalizedDateTime(this.course.getLastRequestDate(),currentLocale);
	}
	
   
    public List getTestPieData()
    {
        List<List<TextValueDataItem>> dataList = CollectionFactory.newList();
        List<TextValueDataItem> list1 = CollectionFactory.newList();
      
        list1.add(new TextValueDataItem("Mozilla Firefox",12));
        list1.add(new TextValueDataItem("Google Chrome", 9));
        list1.add(new TextValueDataItem("Safari (Webkit)",14));
        list1.add(new TextValueDataItem("Internet Explorer", 16));
        list1.add(new TextValueDataItem("Opera", 2));

      
        dataList.add(list1);
      
        return dataList;
    }
    
    
    //@OnEvent(EventConstants.PROGRESSIVE_DISPLAY) 
    public List getFirstQuestionDataItems(){
    	List<List<XYDataItem>> dataList = CollectionFactory.newList();
		if (course!=null && course.getCourseId()!=null){
	        List<XYDataItem> list1 = CollectionFactory.newList();
	    
	        Long starttime = 1108968800L;
			Long endtime= 1334447632L;
			int resolution = 30;
			List<Long> roles = new ArrayList<Long>();
			List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());
			ResultListLongObject results = init.computeQ1(courses, roles, starttime, endtime, resolution);
	        for(int i=0 ;i<resolution;i++){
	        	list1.add(new XYDataItem(i, results.getElements().get(i)));
	        }
	        dataList.add(list1);
	        return dataList;
		}
		return dataList;
	}
    
//    @Cached
//    public Object getChart2(){
//    	return chart2;
//    }

}
