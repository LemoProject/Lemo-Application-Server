package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Result{

	private Long value;
	
	public Result(Long value){
		this.value = value;
	}
	
	@XmlElement(name="value")
	public Long getValue(){
		return this.value;
	}
	
	public void setValue(Long value){
		this.value = value;
	}
	
	
}
