package de.lemo.apps.restws.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultListLong {

	
	private List<Long> elements;
	
	public ResultListLong()
	{
		
	}
	
	public ResultListLong(List<Long> elements)
	{
		this.elements = elements;
	}
	
	@XmlElement
	public List<Long> getElements()
	{
		return this.elements;
	}
	
	public void setElements(List<Long> elements)
	{
		this.elements = elements;
	}
	
}