/**
 * File ./de/lemo/apps/restws/entities/ResultListStringObject.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultListStringObject {

	private List<String> elements;

	public ResultListStringObject()
	{

	}

	public ResultListStringObject(final List<String> elements)
	{
		this.elements = elements;
	}

	@XmlElement
	public List<String> getElements()
	{
		return this.elements;
	}

	public void setElements(final List<String> elements)
	{
		this.elements = elements;
	}

}
