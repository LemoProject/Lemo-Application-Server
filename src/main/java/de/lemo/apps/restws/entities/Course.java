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
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the participantAmount
	 */
	public Integer getParticipantAmount() {
		return participantAmount;
	}

	/**
	 * @param participantAmount the participantAmount to set
	 */
	public void setParticipantAmount(Integer participantAmount) {
		this.participantAmount = participantAmount;
	}

	/**
	 * @return the lastAction
	 */
	public Date getLastAction() {
		return lastAction;
	}

	/**
	 * @param lastAction the lastAction to set
	 */
	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}	
	
	
	
}
