package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
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

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	@XmlElement(name = "platformId")
	public Long getPlatformId() {
		return platformId;
	}

	@XmlElement(name = "platformName")
	public String getPlatformName() {
		return platformName;
	}

	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	
	/**
	 * @param platformName the platformName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

}
