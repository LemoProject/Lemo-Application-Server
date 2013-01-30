package de.lemo.apps.restws.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultListLongObject {

	private List<Long> elements;

	public ResultListLongObject() {
		this.elements = new ArrayList<Long>();
	}

	public ResultListLongObject(final List<Long> elements) {
		this.elements = elements;
	}

	@XmlElement
	public List<Long> getElements() {
		return this.elements;
	}

	public void setElements(final List<Long> elements) {
		this.elements = elements;
	}

}