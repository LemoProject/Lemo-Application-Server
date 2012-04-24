package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.beaneditor.BeanModelImpl;
import org.apache.tapestry5.internal.services.MapMessages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLong;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="explorerTitle")
public class Visualization {
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private JavaScriptSupport jsSupport;
	
	@Inject
	private Initialisation init;
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
    private JqPlotLine chart1;
	
	@Inject 
	private CourseDAO courseDAO;
	
	@Property
    private Course course;
	
	public Boolean onActivate(Course course){
		this.courseId = course.getCourseId();
		return true;
	}
	
	private Long courseId;
	
	@Property(write=false)
	@Retain
	private BeanModel coursesGridModel;
    {
    	coursesGridModel = beanModelSource.createDisplayModel(Course.class, componentResources.getMessages());
    	coursesGridModel.include("coursename","lastRequestDate");
    	coursesGridModel.add("favorite",null);
    	    	
    }
    

    public List<Course> getCourses() { return courseDAO.findAll(); }
    
    
    public List getFirstQuestionDataItems(){
		List<List<XYDataItem>> dataList = CollectionFactory.newList();
        List<XYDataItem> list1 = CollectionFactory.newList();

        Long starttime = 1108968800L;
		Long endtime= 1334447632L;
		int resolution = 30;
		List<Long> roles = new ArrayList<Long>();
		List<Long> courses = new ArrayList<Long>();
		courses.add(courseId);
		ResultListLong results = init.computeQ1(courses, roles, starttime, endtime, resolution);
        for(int i=0 ;i<resolution;i++){
        	list1.add(new XYDataItem(i, results.getElements().get(i)));
        }
        dataList.add(list1);
        return dataList;
	}
    


}
