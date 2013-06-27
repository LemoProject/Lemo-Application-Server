package de.lemo.apps.restws.entities;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an course object
 *
 */
@Deprecated
@XmlRootElement
public class Course {

	private Long id;

	private String title;

	private String description;

	private Integer participantAmount;

	private Date lastAction;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the participantAmount
	 */
	public Integer getParticipantAmount() {
		return this.participantAmount;
	}

	/**
	 * @param participantAmount
	 *            the participantAmount to set
	 */
	public void setParticipantAmount(final Integer participantAmount) {
		this.participantAmount = participantAmount;
	}

	/**
	 * @return the lastAction
	 */
	public Date getLastAction() {
		return this.lastAction;
	}

	/**
	 * @param lastAction
	 *            the lastAction to set
	 */
	public void setLastAction(final Date lastAction) {
		this.lastAction = lastAction;
	}

}
