package de.lemo.apps.restws.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="results")
public class ResultList {

	@XmlElement
	private List<?> elements;
	
	public ResultList()
	{
		
	}
	
	public ResultList(List<?> elements)
	{
		this.setElements(elements);
	}
	
	
	public List<?> getElements()
	{
		return this.elements;
	}
	
	public void setElements(List<?> elements)
	{
		this.elements = elements;
	}
	
}
