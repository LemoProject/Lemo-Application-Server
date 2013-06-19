package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result list with string objects
 */
@XmlRootElement
public class ResultListStringObject {

	private List<String> elements;

	public ResultListStringObject() {

	}

	public ResultListStringObject(final List<String> elements) {
		this.elements = elements;
	}

	@XmlElement
	public List<String> getElements() {
		return this.elements;
	}

	public void setElements(final List<String> elements) {
		this.elements = elements;
	}

}
