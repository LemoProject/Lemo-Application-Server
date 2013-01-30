package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * service container for time formats as long
 * 
 * @author Boris Wenzlaff
 */
@XmlRootElement
public class SCTime {

	private long time;

	public long getTime() {
		return this.time;
	}

	public void setTime(final long time) {
		this.time = time;
	}

}
