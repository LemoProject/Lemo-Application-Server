package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an course object
 *
 */
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

	public CourseObject() {
	}

	public CourseObject(final Long id, final String title, final String description, final Long participants, final Long lastRequest,
			final Long firstRequest) {
		this.id = id;
		this.title = title;
		this.participants = participants;
		this.description = description;
		this.lastRequest = lastRequest;
		this.firstRequest = lastRequest;
	}

	public Long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public Long getParticipants() {
		return this.participants;
	}

	public Long getLastRequest() {
		return this.lastRequest;
	}

	public Long getFirstRequest() {
		return this.firstRequest;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param participants
	 *            the participants to set
	 */
	public void setParticipants(final Long participants) {
		this.participants = participants;
	}

	/**
	 * @param lastRequest
	 *            the lastRequest to set
	 */
	public void setLastRequest(final Long lastRequest) {
		this.lastRequest = lastRequest;
	}

	/**
	 * @param lastRequest
	 *            the lastRequest to set
	 */
	public void setFirstRequest(final Long firstRequest) {
		this.firstRequest = firstRequest;
	}
}
