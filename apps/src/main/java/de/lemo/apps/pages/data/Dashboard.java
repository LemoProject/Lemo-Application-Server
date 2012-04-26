package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;


import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.tynamo.security.services.SecurityService;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.components.JqPlotPie;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.ResultList;
import de.lemo.apps.restws.entities.ResultListLong;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey="dashboardTitle")
public class Dashboard {
	
//	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
//    private JqPlotLine chart1;
	
	@Component(parameters = {"dataItems=testPieData"})
    private JqPlotPie chart2 ;
	
	@Inject
    @Path("../../images/icons/glyphicons_019_cogwheel.png")
    @Property
    private Asset wheel;
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject
	private SecurityService securityService;
	
	@Inject
	private ApplicationStateManager applicationStateManager;
	
	@Inject
	private Initialisation init;

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
	
	public String getStartTime(){
		return 	init.getStartTime();
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
