/**
 * File ./de/lemo/apps/restws/entities/Result.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {

	private Long value;

	public Result(final Long value) {
		this.value = value;
	}

	@XmlElement(name = "value")
	public Long getValue() {
		return this.value;
	}

	public void setValue(final Long value) {
		this.value = value;
	}

}
