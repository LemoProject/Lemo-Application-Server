package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
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
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.services.internal.CourseIdSelectModel;
import de.lemo.apps.services.internal.CourseIdValueEncoder;
import de.lemo.apps.services.internal.jqplot.XYDataItem;

@RequiresAuthentication
@BreadCrumb(titleKey = "visualizationTitle")
@Import(library = { "../../js/d3/d3_custom.js" })
public class VisualizationD3 {

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private Logger logger;

    @Inject
    private DateWorker dateWorker;

    @Inject
    private CourseIdValueEncoder courseValueEncoder;

    @Inject
    private Analysis analysis;

    @Inject
    private UserWorker userWorker;

    @Inject
    private CourseDAO courseDAO;

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

    @Component(id = "customizeForm")
    private Form customizeForm;

    @Property
    @SuppressWarnings("unused")
    private SelectModel courseModel;

    @Property
    @Persist
    private Course course;

    @Property
    @Persist
    private Long courseId;

    @Persist
    @Property
    private Date endDate;

    @Persist
    @Property
    private Date beginDate;

    @Property
    @Persist
    Integer resolution;

    @Property
    @Persist
    private List<EResourceType> activities;

    @Property
    @Persist
    private List<Course> courses;

    private List<List<XYDataItem>> testData;

    // Value Encoder for activity multi-select component
    @Property(write = false)
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer,
            EResourceType.class);

    // Select Model for activity multi-select component
    @Property(write = false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);

    public Object onActivate(Course course) {
        logger.debug("--- Bin im ersten onActivate");
        List<Long> allowedCourses = userWorker.getCurrentUser().getMyCourses();
        if(allowedCourses != null && course != null && course.getCourseId() != null
                && allowedCourses.contains(course.getCourseId())) {
            this.courseId = course.getCourseId();
            this.course = course;
            if(this.endDate == null)
                this.endDate = course.getLastRequestDate();
            if(this.beginDate == null)
                this.beginDate = course.getFirstRequestDate();
            Calendar beginCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            beginCal.setTime(beginDate);
            endCal.setTime(endDate);
            this.resolution = dateWorker.daysBetween(beginDate, endDate);
            return true;
        } else
            return Explorer.class;
    }

    public Object onActivate() {
        logger.debug("--- Bin im zweiten onActivate");
        return true;
    }

    public Course onPassivate() {
        return course;
    }

    void cleanupRender() {
        customizeForm.clearErrors();
        // Clear the flash-persisted fields to prevent anomalies in onActivate when we hit refresh on page or browser
        // button
        this.courseId = null;
        this.course = null;

    }

    void onPrepareForRender() {
        List<Course> courses = courseDAO.findAllByOwner(userWorker.getCurrentUser());
        courseModel = new CourseIdSelectModel(courses);
    }

    public final ValueEncoder<Course> getCourseValueEncoder() {
        // List<Course> courses = courseDAO.findAllByOwner(userWorker.getCurrentUser());
        return courseValueEncoder.create(Course.class);
    }

    // returns datepicker params
    public JSONLiteral getDatePickerParams() {
        return dateWorker.getDatePickerParams();
    }

    public String getQuestionResult() {
        ArrayList<Long> courseIds = new ArrayList<Long>();
        courseIds.add(courseId);
        Long endStamp = 0L;
        Long beginStamp = 0L;
        if(endDate != null) {
            endStamp = new Long(endDate.getTime() / 1000);
        }
        if(beginDate != null) {
            beginStamp = new Long(beginDate.getTime() / 1000);
        }

        String computeQ2 = analysis.computeCourseUserPaths(courseIds, beginStamp, endStamp);

        return computeQ2;
    }

    @AfterRender
    public void afterRender() {
        javaScriptSupport.addScript(""
                );
    }

    void setupRender() {
        logger.debug(" ----- Bin in Setup Render");

        if(this.endDate == null)
            this.endDate = course.getLastRequestDate();
        if(this.beginDate == null)
            this.beginDate = course.getFirstRequestDate();

        Calendar beginCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        beginCal.setTime(beginDate);
        endCal.setTime(endDate);
        this.resolution = dateWorker.daysBetween(beginDate, endDate);
        logger.debug("SetupRender End --- BeginDate:" + beginDate + " EndDate: " + endDate + " Res: " + resolution);
    }

    void onPrepareFromCustomizeForm() {
        this.course = courseDAO.getCourseByDMSId(courseId);
    }

    void onSuccessFromCustomizeForm() {
        System.out.println("###### onSuccessCustomizeFormForm");
    }

    public String getLocalizedDate(Date inputDate) {
        SimpleDateFormat df_date = new SimpleDateFormat("MMM dd, yyyy", currentlocale);
        return df_date.format(inputDate);
    }

    public String getFirstRequestDate() {
        return getLocalizedDate(this.course.getFirstRequestDate());
    }

    public String getLastRequestDate() {
        return getLocalizedDate(this.course.getLastRequestDate());
    }
}
