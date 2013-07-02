/**
 * File ./src/main/java/de/lemo/apps/restws/entities/BoxPlot.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a boxplot for use in a boxplot diagram
 *
 */
@XmlRootElement
public class BoxPlot {

	private String name = "";
	private Double median = 0.0, upperWhisker = 0.0, lowerWhisker = 0.0;
	// absolute zugriffe und einzigartige zugriffe
	private Double upperQuartil = 0.0, lowerQuartil = 0.0;

	@XmlElement
	public Double getMedian() {
		return this.median;
	}

	public void setMedian(final Double median) {
		this.median = median;
	}

	@XmlElement
	public Double getUpperQuartil() {
		return this.upperQuartil;
	}

	public void setUpperQuartil(final Double upperQuartil) {
		this.upperQuartil = upperQuartil;
	}

	@XmlElement
	public Double getLowerQuartil() {
		return this.lowerQuartil;
	}

	public void setLowerQuartil(final Double lowerQuartil) {
		this.lowerQuartil = lowerQuartil;
	}

	@XmlElement
	public Double getUpperWhisker() {
		return this.upperWhisker;
	}

	public void setUpperWhisker(final Double upperWhisker) {
		this.upperWhisker = upperWhisker;
	}

	@XmlElement
	public Double getLowerWhisker() {
		return this.lowerWhisker;
	}

	public void setLowerWhisker(final Double lowerWhisker) {
		this.lowerWhisker = lowerWhisker;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
