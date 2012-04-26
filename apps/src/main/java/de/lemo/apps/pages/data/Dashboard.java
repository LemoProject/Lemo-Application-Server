package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;


import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.tynamo.security.services.SecurityService;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultList;
import de.lemo.apps.restws.entities.ResultListLong;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey="dashboardTitle")
public class Dashboard {
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems3"})
    private JqPlotLine chart1;
	
	@Component(parameters = {"dataItems=testPieData"})
    private JqPlotPie chart2 ;
	
	@Inject
    @Path("../../images/icons/glyphicons_019_cogwheel.png")
    @Property
    private Asset wheel;
	
	@Inject
	private CourseDAO courseDAO;
	
	@Inject 
	private UserDAO userDAO;
	
	@Inject
	private UserWorker userWorker;
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject
	private SecurityService securityService;
	
	@Inject
	private ApplicationStateManager applicationStateManager;
	
	@Inject
	private Initialisation init;
	
	@Component(id = "courseForm1")
	private Form courseForm1;
	
	@Component(id = "courseForm2")
	private Form courseForm2;
	
	@Component(id = "courseForm3")
	private Form courseForm3;
	
	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel1;
	
	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel2;
	
	@Property
	@SuppressWarnings("unused")
	private SelectModel courseModel3;
	
	@Property
	private Long widgetCourse1;
	
	@Property
	private Long widgetCourse2;
	
	@Property
	private Long widgetCourse3;

//	@SuppressWarnings("unused")
//	@SessionState(create = false)
//	@Property
//	private CurrentUser currentUser;
//	
//
//	void onActivate() {
//		if (securityService.getSubject().isAuthenticated() && !applicationStateManager.exists(CurrentUser.class)) {
//			CurrentUser currentUser = applicationStateManager.get(CurrentUser.class);
//			currentUser.merge(securityService.getSubject().getPrincipal());
//		}
//
//	}
	
	
	void onPrepareForRender() {
		List<Course> courses = courseDAO.findAllByOwner(userWorker.getCurrentUser());
		courseModel1 = new CourseIdSelectModel(courses);
		courseModel2 = new CourseIdSelectModel(courses);
		courseModel3 = new CourseIdSelectModel(courses);
		
		widgetCourse1 = userWorker.getCurrentUser().getWidget1();
		widgetCourse2 = userWorker.getCurrentUser().getWidget2();
		widgetCourse3 = userWorker.getCurrentUser().getWidget3();
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
	
	@Cached
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
	@Property
	@Persist
	private Integer count;
	
	public Date getStartTime(){
		return 	init.getStartTime();
	}
	
	public List<Course> getMyCourses(){
		return courseDAO.findAllByOwner(userWorker.getCurrentUser());
	}
	
	void onSuccessFromCourseForm1(){
		System.out.println("Course ID:"+ widgetCourse1 );
		User user = userWorker.getCurrentUser();
		user.setWidget1(widgetCourse1);
		userDAO.update(user);
	}
	
	void onSuccessFromCourseForm2(){
		System.out.println("Course ID:"+ widgetCourse2 );
		User user = userWorker.getCurrentUser();
		user.setWidget2(widgetCourse2);
		userDAO.update(user);
	}
	
	
	void onSuccessFromCourseForm3(){
		System.out.println("Course ID:"+ widgetCourse2 );
		User user = userWorker.getCurrentUser();
		user.setWidget3(widgetCourse3);
		userDAO.update(user);
	}
	
	
	public List getFirstQuestionDataItems(){
		List<List<XYDataItem>> dataList = CollectionFactory.newList();
        List<XYDataItem> list1 = CollectionFactory.newList();

        Long starttime = 1308968800L;
		Long endtime= 1334447632L;
		int resolution = 30;
		List<Long> roles = new ArrayList<Long>();
		List<Long> courses = new ArrayList<Long>();
		courses.add(2100L);
		courses.add(2200L);
		//calling dm-server
		ResultListLong results = init.computeQ1(courses, roles, starttime, endtime, resolution);
		//checking if result size matches resolution 
        if(results!= null && results.getElements()!=null && results.getElements().size() == resolution)
        	for(int i=0 ;i<resolution;i++){
        		list1.add(new XYDataItem(i, results.getElements().get(i)));
        	}
        dataList.add(list1);
        return dataList;
	}
	
	
	public List getFirstQuestionDataItems3(){
		List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
        List<XYDateDataItem> list1 = CollectionFactory.newList();
        Long id3 = userWorker.getCurrentUser().getWidget3();
        Course course = courseDAO.getCourse(id3);
        Date endDate = course.getLastRequestDate();
    	Date beginDate = course.getFirstRequestDate();
    	Integer resolution = 0;
        if(id3!=null){
        	Long endStamp=0L;
        	Long beginStamp=0L;
        	
        	if(endDate!=null){
        		endStamp = new Long(endDate.getTime()/1000);
        	} //else endtime= 1334447632L;
	        
        	if(beginDate!=null){
        		beginStamp = new Long(beginDate.getTime()/1000);
        	} //else starttime = 1308968800L;
        	
			
			//int resolution = 30;
			if (resolution == null || resolution < 10 )
				resolution = 170;
			List<Long> roles = new ArrayList<Long>();
			List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());
			
			//calling dm-server
			for (int i=0;i<courses.size();i++){
				System.out.println("Courses: "+courses.get(i));
			}
			System.out.println("Starttime: "+beginStamp+ " Endtime: "+endStamp+ " Resolution: "+resolution);
			ResultListLong results = init.computeQ1(courses, roles, beginStamp, endStamp, resolution);
			
			
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			System.out.println("BeginDate: "+beginDate);
			//checking if result size matches resolution 
			if(results!= null && results.getElements()!=null && results.getElements().size() == resolution)
	        for(int i=0 ;i<resolution;i++){
	        	
	        	beginCal.add(Calendar.DAY_OF_MONTH, 1);
	        	//System.out.println(" Run: "+i+" Date: "+beginCal.getTime()+" Value: "+results.getElements().get(i));
	        	list1.add(new XYDateDataItem(beginCal.getTime() , results.getElements().get(i)));
	        }
    	}
        dataList.add(list1);
        return dataList;
	}
	
	
	public String getWidgetName(int widget){
		switch(widget){
		case 1:
			Long id1 = userWorker.getCurrentUser().getWidget1(); 
			if(id1!=null) return courseDAO.getCourse(id1).getCourseDescription();
		case 2:
			Long id2 = userWorker.getCurrentUser().getWidget2(); 
			if(id2!=null) return courseDAO.getCourse(id2).getCourseDescription();
		case 3:
			Long id3 = userWorker.getCurrentUser().getWidget3(); 
			if(id3!=null) return courseDAO.getCourse(id3).getCourseDescription();
		default: return "Widget unused";	
		}
	}
	
	
	public String getFirstquestion(){
		
		Long starttime = 1108968800L;
		Long endtime= 1334447632L;
		int resolution = 30;
		List<Long> roles = new ArrayList<Long>();
		List<Long> courses = new ArrayList<Long>();
		courses.add(2100L);
		courses.add(2200L);
		ResultListLong results = init.computeQ1(courses, roles, starttime, endtime, resolution);
		if (results != null && results.getElements()!= null) {
			for (int i = 0;i< results.getElements().size();i++)
				System.out.println("List element "+i+"; "+results.getElements().get(i));
//		if (results != null) {
//			for (int i = 0;i< results.size();i++)
//				System.out.println("List element "+i+"; "+results.get(i));
			return "Guck mal";
		} else return "Guck lieber nicht";
	}

}
