package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CourseObject {

	@XmlElement
	private Long id;	
	@XmlElement
	private String title;
	@XmlElement
	private String description;
	@XmlElement
	private Long participants;
	@XmlElement
	private Long lastRequest;
	@XmlElement
	private Long firstRequest;
	
	public CourseObject()
	{}
	
	public CourseObject(Long id, String title, String description, Long participants, Long lastRequest, Long firstRequest)
	{
		this.id = id;
		this.title = title;
		this.participants = participants;
		this.description = description;
		this.lastRequest = lastRequest;
		this.firstRequest = lastRequest;
	}
	
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public Long getParticipants() {
		return participants;
	}
	public Long getLastRequest() {
		return lastRequest;
	}
	
	public Long getFirstRequest() {
		return firstRequest;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(Long participants) {
		this.participants = participants;
	}

	/**
	 * @param lastRequest the lastRequest to set
	 */
	public void setLastRequest(Long lastRequest) {
		this.lastRequest = lastRequest;
	}
	
	/**
	 * @param lastRequest the lastRequest to set
	 */
	public void setFirstRequest(Long firstRequest) {
		this.firstRequest = firstRequest;
	}
}
