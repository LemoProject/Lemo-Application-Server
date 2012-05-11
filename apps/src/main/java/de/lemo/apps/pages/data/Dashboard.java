package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey="dashboardTitle")
@BreadCrumbReset
public class Dashboard {
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems3"})
    private JqPlotLine chart1;
	
	@Component(parameters = {"dataItems=testPieData"})
    private JqPlotPie chart2 ;
	
	@Inject
    private Logger logger;
	
	@Inject
	private Locale currentLocale;
	
	@Inject 
	private DateWorker dateWorker;
	
	@Inject
	private StatisticWorker statisticWorker;
	
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
	private Long widgetCourse1Id;
	
	@Property
	private Long widgetCourse2Id;
	
	@Property
	private Long widgetCourse3Id;
	

	private Course widgetCourse1;

	private Course widgetCourse2;
	
	private Course widgetCourse3;

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
		
		widgetCourse1 = courseDAO.getCourse(userWorker.getCurrentUser().getWidget1());
		widgetCourse2 = courseDAO.getCourse(userWorker.getCurrentUser().getWidget2());
		widgetCourse3 = courseDAO.getCourse(userWorker.getCurrentUser().getWidget3());
		widgetCourse1Id = widgetCourse1.getId();
		widgetCourse2Id = widgetCourse2.getId();
		widgetCourse3Id = widgetCourse3.getId();
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
	
	public List<Course> getMyCourses(){
		return courseDAO.findAllByOwner(userWorker.getCurrentUser());
	}
	
	void onSuccessFromCourseForm1(){
		logger.debug("Course ID:"+ widgetCourse1 );
		User user = userWorker.getCurrentUser();
		user.setWidget1(widgetCourse1Id);
		userDAO.update(user);
	}
	
	void onSuccessFromCourseForm2(){
		logger.debug("Course ID:"+ widgetCourse2 );
		User user = userWorker.getCurrentUser();
		user.setWidget2(widgetCourse2Id);
		userDAO.update(user);
	}
	
	
	void onSuccessFromCourseForm3(){
		logger.debug("Course ID:"+ widgetCourse2 );
		User user = userWorker.getCurrentUser();
		user.setWidget3(widgetCourse3Id);
		userDAO.update(user);
	}
	
	
	@Cached
	public List getFirstQuestionDataItems3(){
		
		List<List<XYDateDataItem>> dataList = CollectionFactory.newList();
        List<XYDateDataItem> list1 = CollectionFactory.newList();
        
        Long id3 = userWorker.getCurrentUser().getWidget3();
        Course course = courseDAO.getCourse(id3);
        Date endDate = course.getLastRequestDate();
        Date beginDate = endDate;
        if(endDate!=null){
        	Calendar cal = Calendar.getInstance();
    		cal.setTime(beginDate);
    		cal.add(Calendar.MONTH,-1);
    		beginDate = cal.getTime();
        } 
        
    	Integer resolution = dateWorker.daysBetween(beginDate, endDate);
        
    	if(id3!=null){
        	Long endStamp=0L;
        	Long beginStamp=0L;
        	
        	if(endDate!=null){
        		
        		endStamp = new Long(endDate.getTime()/1000);
        	} 
	        
        	if(beginDate!=null){
        		
        		beginStamp = new Long(beginDate.getTime()/1000);
        	} 
        	
			
			if (resolution == null || resolution < 10 )
				resolution = 30;
			List<Long> roles = new ArrayList<Long>();
			List<Long> courses = new ArrayList<Long>();
			courses.add(course.getCourseId());
			
			//calling dm-server
			for (int i=0;i<courses.size();i++){
				logger.debug("Courses: "+courses.get(i));
			}
			logger.debug("Starttime: "+beginStamp+ " Endtime: "+endStamp+ " Resolution: "+resolution);
			ResultListLongObject results = init.computeQ1(courses, roles, beginStamp, endStamp, resolution);
			
			
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
	
	
	public String getWidgetName(int widget){
		switch(widget){
		case 1:
			Long id1 = userWorker.getCurrentUser().getWidget1(); 
			if(id1!=null) return getWidgetCourse1().getCourseDescription(); //courseDAO.getCourse(id1).getCourseDescription();
		case 2:
			Long id2 = userWorker.getCurrentUser().getWidget2(); 
			if(id2!=null) return getWidgetCourse2().getCourseDescription(); //courseDAO.getCourse(id2).getCourseDescription();
		case 3:
			Long id3 = userWorker.getCurrentUser().getWidget3(); 
			if(id3!=null) return getWidgetCourse3().getCourseDescription(); //courseDAO.getCourse(id3).getCourseDescription();
		default: return "Widget unused";	
		}
	}
	
	@Cached
	public Course getWidgetCourse1(){
		Long id1 = userWorker.getCurrentUser().getWidget1(); 
		return courseDAO.getCourse(id1);
	}
	
	@Cached
	public Course getWidgetCourse2(){
		Long id2 = userWorker.getCurrentUser().getWidget2(); 
		return courseDAO.getCourse(id2);
	}
	
	@Cached
	public Course getWidgetCourse3(){
		Long id3 = userWorker.getCurrentUser().getWidget3(); 
		return courseDAO.getCourse(id3);
	}
	
	public String getLocalizedDate(Date date){
		return dateWorker.getLocalizedDate(date,currentLocale);
	}
	
	
	public String getLocalizedDateTime(Date date){
		return dateWorker.getLocalizedDateTime(date,currentLocale);
	}
	
//	public Course getWidgetCourse(int widget){
//		switch(widget){
//		case 1:
//			Long id1 = userWorker.getCurrentUser().getWidget1(); 
//			if(id1!=null) return courseDAO.getCourse(id1);
//		case 2:
//			Long id2 = userWorker.getCurrentUser().getWidget2(); 
//			if(id2!=null) return courseDAO.getCourse(id2);
//		case 3:
//			Long id3 = userWorker.getCurrentUser().getWidget3(); 
//			if(id3!=null) return courseDAO.getCourse(id3);
//		default: return new Course();	
//		}
//	}
	
	
	
	
//	public String getFirstquestion(){
//		
//		Long starttime = 1108968800L;
//		Long endtime= 1334447632L;
//		int resolution = 30;
//		List<Long> roles = new ArrayList<Long>();
//		List<Long> courses = new ArrayList<Long>();
//		courses.add(2100L);
//		courses.add(2200L);
//		ResultListLongObject results = init.computeQ1(courses, roles, starttime, endtime, resolution);
//		if (results != null && results.getElements()!= null) {
//			for (int i = 0;i< results.getElements().size();i++)
//				logger.info("List element "+i+"; "+results.getElements().get(i));
//			return "Guck mal";
//		} else return "Guck lieber nicht";
//	}

}
