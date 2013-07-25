/**
 * File ./src/main/java/de/lemo/apps/restws/entities/CourseObject.java
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an course object
 *
 */
@XmlRootElement
public class CourseObject {

	
	private Long id;
	
	private String title;
	
	private String description;
	
	private Long participants;
	
	private Long lastRequest;
	
	private Long firstRequest;
	
	private Long hash;
	
	private boolean genderSupport;

	public CourseObject() {
	}

	public CourseObject(final Long id, final String title, final String description, final Long participants, final Long lastRequest,
			final Long firstRequest, final Long hash, boolean genderSupport) {
		this.id = id;
		this.title = title;
		this.participants = participants;
		this.description = description;
		this.lastRequest = lastRequest;
		this.firstRequest = lastRequest;
		this.hash = hash;
		this.genderSupport = genderSupport;
	}
	
	
	@XmlElement
	public Long getId() {
		return this.id;
	}
	
	@XmlElement
	public String getTitle() {
		return this.title;
	}
	
	@XmlElement
	public String getDescription() {
		return this.description;
	}
	
	@XmlElement
	public Long getParticipants() {
		return this.participants;
	}

	@XmlElement
	public Long getLastRequest() {
		return this.lastRequest;
	}
	
	@XmlElement
	public Long getFirstRequest() {
		return this.firstRequest;
	}
	
	@XmlElement
	public Long getHash() {
		return hash;
	}

	@XmlElement
	public boolean isGenderSupport() {
		return genderSupport;
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
	
	

	public void setHash(Long hash) {
		this.hash = hash;
	}

	

	public void setGenderSupport(boolean genderSupport) {
		this.genderSupport = genderSupport;
	}
}
