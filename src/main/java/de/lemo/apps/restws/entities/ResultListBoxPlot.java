package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result list with boxpot objects
 */
@XmlRootElement
public class ResultListBoxPlot {

	private List<BoxPlot> elements;

	public ResultListBoxPlot() {

	}

	public ResultListBoxPlot(final List<BoxPlot> elements) {
		this.elements = elements;
	}

	// @XmlElement
	public List<BoxPlot> getElements() {
		return this.elements;
	}

}