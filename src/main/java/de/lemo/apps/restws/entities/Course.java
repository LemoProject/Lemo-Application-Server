/**
 * File ./src/main/java/de/lemo/apps/restws/entities/Course.java
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
