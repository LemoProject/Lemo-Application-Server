package de.lemo.apps.restws.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ResultListCourseObject {

	
	private List<CourseObject> elements;
	
	

	public ResultListCourseObject()
	{
		
	}
	
	public ResultListCourseObject(List<CourseObject> elements)
	{
		this.elements = elements;
	}
	
	@XmlElement
	public List<CourseObject> getElements()
	{
		return this.elements;
	}
	
	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<CourseObject> elements) {
		this.elements = elements;
	}
	
}
