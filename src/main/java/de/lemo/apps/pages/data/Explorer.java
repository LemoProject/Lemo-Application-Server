/**
 * File Explorer.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.pages.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
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
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotLine;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;
import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

/**
 * Course explorer, represents an overview for the user
 */
@SuppressWarnings({ "unused", "unused", "unused", "unused" })
@RequiresAuthentication
@BreadCrumb(titleKey = "myCourses")
public class Explorer {

	@SuppressWarnings("unused")
	@Inject
	private Logger logger;

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	@Inject
	private Locale currentLocale;

	@Component(parameters = { "dataItems=usageAnalysis" })
	private JqPlotLine chart1;

	
	@Inject
	@Path("../../images/icons/Circle_Graph_Icon.png")
	@Property
	private Asset circleGraphIcon;
	
	@Inject
	@Path("../../images/icons/UsageAnalysis2_Icon.png")
	@Property
	private Asset usageAnalysisIcon;

	@Inject
	@Path("../../images/icons/UsageAnalysisLO_Icon.png")
	@Property
	private Asset usageAnalysisLOIcon;

	@Inject
	@Path("../../images/icons/UsageAnalysisLO_TreeMap_Icon.png")
	@Property
	private Asset usageAnalysisLOTreeMapIcon;

	@Inject
	@Path("../../images/icons/PathAnalysis_Icon.png")
	@Property
	private Asset navAnalysisIcon;

	@Inject
	@Path("../../images/icons/PathAnalysis2_Icon.png")
	@Property
	private Asset pathAnalysisIcon;

	@Inject
	@Path("../../images/icons/CumulativeUserAnalysis_Icon.png")
	@Property
	private Asset cumulativeUserAnalysisIcon;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserWorker userWorker;

	@Property
	private Course course;

	@Property
	private boolean favorite;

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
	
	@Property
    private String courseSearch;

	/**
	 * Preparing the Grid Model for the
	 */
	@Property(write = false)
	@Retain
	private BeanModel coursesGridModel;
	{
		this.coursesGridModel = this.beanModelSource.createDisplayModel(Course.class,
				this.componentResources.getMessages());
		this.coursesGridModel.include("coursename", "lastRequestDate");
	}

	public void onPrepare() {
		favorite = userWorker.getCurrentUser().getFavoriteCourses().contains(getCurrentCourse());
	}

	public Object onActivate(final Course course) {
		if (course != null) {
			this.initCourse = course;
		}
		return true;
	}
	

	/**
	 * Inserting a some js to deal with tab changes (e.g. Summary, Last Month, etc.) after the page rendering is
	 * finished
	 */
	@AfterRender
	void afterRender() {
		this.jsSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function() { "
				+ " $('.tabs a:first').tab('show');"
				+ "});", this.courseZone.getClientId());
	}

	@Cached
	public List<Course> getCourses() {
		return this.courseDAO.findAllByOwner(this.userWorker.getCurrentUser(), false);
	}

	public Course getCurrentCourse() {
		if (this.initCourse != null) {
			return this.initCourse;
		} else if (this.getCourses() != null && this.getCourses().size() > 0) {
			return this.getCourses().get(0);
		} else {
			return null;
		}
	}

	// TODO Fix Problem with jqplot and zone updates
	Object onActionFromShow(final Long id) {
		this.initCourse = this.courseDAO.getCourse(id);
		// TODO Till problem with chart painting and zone updates is not solved, incremental page rendering is off
		return this;
	}

	Object onActionFromFavorite(final Long id) {
		favorite = this.userDAO.toggleFavoriteCourse(id, this.userWorker.getCurrentUser().getId());
		this.initCourse = this.courseDAO.getCourse(id);
		return this;
	}

	public String getFirstRequestDate() {
		if (this.getCurrentCourse() != null) {
			return this.dateWorker.getLocalizedDateTime(this.getCurrentCourse().
					getFirstRequestDate(), this.currentLocale);
		}
		else {
			return null;
		}
	}

	public String getLastRequestDate() {
		if (this.getCurrentCourse() != null) {
			return this.dateWorker.getLocalizedDateTime(this.getCurrentCourse().
					getLastRequestDate(), this.currentLocale);
		}
		else {
			return null;
		}
	}

	@Cached
	public List getUsageAnalysisLastMonth() {
		if (this.getCurrentCourse() != null) {
			final Date endDate = this.getCurrentCourse().getLastRequestDate();
			return this.analysisWorker.usageAnalysis(this.getCurrentCourse(), endDate, Calendar.MONTH, -1, null);
		} else {
			return null;
		}
	}

	// TODO Scheinbar greift ein Template Property noch auf diese Funktion zu ... auch wen dem nicht so sein sollte ->
	// die korrekte Methode lautet ...LastMonth
	@Cached
	public List getUsageAnalysis() {
		if (this.getCurrentCourse() != null) {
			final Date endDate = this.getCurrentCourse().getLastRequestDate();
			return this.analysisWorker.usageAnalysis(this.getCurrentCourse(), endDate, Calendar.MONTH, -1, null);
		} else {
			return null;
		}
	}

	@Cached
	public List getUsageAnalysisOverall() {
		if (this.getCurrentCourse() != null) {
			final Date endDate = this.getCurrentCourse().getLastRequestDate();
			final Date beginDate = this.getCurrentCourse().getFirstRequestDate();
			return this.analysisWorker.usageAnalysis(this.course, beginDate, endDate, null);
		} else {
			return null;
		}
	}
	
	
	@OnEvent(value = "provideCompletions")
    public List<String> autoComplete(String start)
    {
        List<String> strings = new ArrayList<String>();
        
        if (start != null)
        {	List<Course> courseList = getCourses();
        	
            for(Course value : courseList){
                if(value.getCourseDescription().toUpperCase().startsWith(start.toUpperCase())) 
                    strings.add(value.getCourseDescription());
            }
        }
        
        return strings;
    }

	public Long getAverageRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getAverageRequest(dataItemList);
	}

	public Long getOverallRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getOverallRequest(dataItemList);
	}

	public Long getMaxRequest(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getMaxRequest(dataItemList);
	}

	public Date getMaxRequestDate(final List<List<XYDateDataItem>> dataItemList) {
		return this.statisticWorker.getMaxRequestDate(dataItemList);
	}

	public String getLocalizedDate(final Date date) {
		return this.dateWorker.getLocalizedDate(date, this.currentLocale);
	}

	public String getLocalizedDateTime(final Date date) {
		return this.dateWorker.getLocalizedDateTime(date, this.currentLocale);
	}

}
