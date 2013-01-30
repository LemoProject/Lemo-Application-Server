package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "results")
public class ResultList {

	// @XmlElement
	private List<?> elements;

	public ResultList() {

	}

	public ResultList(final List<?> elements) {
		this.setElements(elements);
	}

	public List<?> getElements() {
		return this.elements;
	}

	public void setElements(final List<?> elements) {
		this.elements = elements;
	}

}
