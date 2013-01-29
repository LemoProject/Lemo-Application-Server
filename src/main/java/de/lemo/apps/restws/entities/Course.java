/**
 * File ./de/lemo/apps/restws/entities/Course.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.restws.entities;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

	private Long Id;

	private String title;

	private String description;

	private Integer participantAmount;

	private Date lastAction;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.Id = id;
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
