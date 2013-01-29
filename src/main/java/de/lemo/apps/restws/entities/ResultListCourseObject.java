/**
 * File ./de/lemo/apps/restws/entities/ResultListCourseObject.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

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

	public ResultListCourseObject(final List<CourseObject> elements)
	{
		this.elements = elements;
	}

	@XmlElement
	public List<CourseObject> getElements()
	{
		return this.elements;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public void setElements(final List<CourseObject> elements) {
		this.elements = elements;
	}

}
