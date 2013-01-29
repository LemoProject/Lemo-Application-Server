/**
 * File ./de/lemo/apps/restws/entities/ResultList.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "results")
public class ResultList {

	// @XmlElement
	private List<?> elements;

	public ResultList()
	{

	}

	public ResultList(final List<?> elements)
	{
		this.setElements(elements);
	}

	public List<?> getElements()
	{
		return this.elements;
	}

	public void setElements(final List<?> elements)
	{
		this.elements = elements;
	}

}
