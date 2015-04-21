package de.lemo.apps.components;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;

import de.lemo.apps.application.DateWorker;
import de.lemo.apps.entities.Courses;
import de.lemo.apps.entities.Features;

public class ScatterPlotForm {
	@Property
	@Persist
	@Validate("required")
	private Features x_Axis;
	
	@Property
	@Persist
	@Validate("required")
	private Features y_Axis;
	
	@Property
	@Persist
	@Validate("required")
	private Features radius;
	
	@Property
	@Persist
	@Validate("required")
	private Courses reference_course;

	@Property
	@Persist
	@Validate("required")
	private Courses current_course;
	
	@Inject
	private DateWorker dateWorker;
	
	@Component(id = "beginDate")
	private DateField beginDateField;

	@Component(id = "endDate")
	private DateField endDateField;
	
	@Property
	@Persist
	private Date beginDate;
	
	@Property
	@Persist
	private Date endDate;
	
	
	public void setupRender(){
		endDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(1381795200000L);
		beginDate = calendar.getTime();	
		radius = Features.PROGRESS_PERCENTAGE;
		x_Axis  = Features.WORDCOUNT;
		y_Axis = Features.UPVOTES;
		
	}
	// returns datepicker params
	public JSONLiteral getDatePickerParams() {
		JSONLiteral result = this.dateWorker.getDatePickerParams(new Locale("en"));
		return result;
	}
}