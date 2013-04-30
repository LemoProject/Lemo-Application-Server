package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SCConnector {

	private String name;
	private Long platformId;
	private String platformName;

	@Override
	public String toString() {
		return platformId + " / " + platformName + " - " + name;
	}

	public String getName() {
		return name;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public String getPlatformName() {
		return platformName;
	}

}
