/**
 * File ./de/lemo/apps/restws/entities/ResultListBoxPlot.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

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