package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result list with course objects
 */
@XmlRootElement
public class ResultListCourseObject {

	private List<CourseObject> elements;

	public ResultListCourseObject() {

	}

	public ResultListCourseObject(final List<CourseObject> elements) {
		this.elements = elements;
	}

	@XmlElement
	public List<CourseObject> getElements() {
		return this.elements;
	}
	
	public void setElements(final List<CourseObject> elements) {
		this.elements = elements;
	}

}
