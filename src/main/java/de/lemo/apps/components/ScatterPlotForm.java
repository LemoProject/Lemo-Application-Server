package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

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
	private Features training_course;
}
