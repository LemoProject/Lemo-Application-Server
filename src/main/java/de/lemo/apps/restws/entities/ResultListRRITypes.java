/**
 * File ./src/main/java/de/lemo/apps/restws/entities/ResultListRRITypes.java
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result list with resource information for the types in the lemo db
 */
@XmlRootElement
public class ResultListRRITypes {

	private List<ResourceRequestInfo> assignments;
	private List<ResourceRequestInfo> chats;
	private List<ResourceRequestInfo> forums;
	private List<ResourceRequestInfo> questions;
	private List<ResourceRequestInfo> quiz;
	private List<ResourceRequestInfo> resources;
	private List<ResourceRequestInfo> scorms;
	private List<ResourceRequestInfo> wikis;

	public void setAssignmentRRI(final List<ResourceRequestInfo> assignments) {
		this.assignments = assignments;
	}

	public void setChatRRI(final List<ResourceRequestInfo> chat) {
		this.chats = chat;
	}

	public void setForumRRI(final List<ResourceRequestInfo> forums) {
		this.forums = forums;
	}

	public void setQuestionRRI(final List<ResourceRequestInfo> questions) {
		this.questions = questions;
	}

	public void setQuizRRI(final List<ResourceRequestInfo> quiz) {
		this.quiz = quiz;
	}

	public void setResourceRRI(final List<ResourceRequestInfo> resources) {
		this.resources = resources;
	}

	public void setScormRRI(final List<ResourceRequestInfo> scorms) {
		this.scorms = scorms;
	}

	public void setWikiRRI(final List<ResourceRequestInfo> wikis) {
		this.wikis = wikis;
	}

	public ResultListRRITypes() {
		this.assignments = new ArrayList<ResourceRequestInfo>();
		this.chats = new ArrayList<ResourceRequestInfo>();
		this.forums = new ArrayList<ResourceRequestInfo>();
		this.questions = new ArrayList<ResourceRequestInfo>();
		this.quiz = new ArrayList<ResourceRequestInfo>();
		this.resources = new ArrayList<ResourceRequestInfo>();
		this.scorms = new ArrayList<ResourceRequestInfo>();
		this.wikis = new ArrayList<ResourceRequestInfo>();
	}

	@XmlElement
	public List<ResourceRequestInfo> getAssignmentRRI() {
		return this.assignments;
	}

	@XmlElement
	public List<ResourceRequestInfo> getForumRRI() {
		return this.forums;
	}

	@XmlElement
	public List<ResourceRequestInfo> getChatRRI() {
		return this.chats;
	}

	@XmlElement
	public List<ResourceRequestInfo> getQuestionsRRI() {
		return this.questions;
	}

	@XmlElement
	public List<ResourceRequestInfo> getQuizRRI() {
		return this.quiz;
	}

	@XmlElement
	public List<ResourceRequestInfo> getResourcesRRI() {
		return this.resources;
	}

	@XmlElement
	public List<ResourceRequestInfo> getScormRRI() {
		return this.scorms;
	}

	@XmlElement
	public List<ResourceRequestInfo> getWikiRRI() {
		return this.wikis;
	}

	public List<ResourceRequestInfo> getResultListByResourceType(final EResourceType resourceType) {

		if ((resourceType != null) && resourceType.equals(EResourceType.ASSIGNMENT)) {
			return this.assignments;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.FORUM)) {
			return this.forums;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.CHAT)) {
			return this.chats;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.QUESTION)) {
			return this.questions;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.QUIZ)) {
			return this.quiz;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.RESOURCE)) {
			return this.resources;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.SCORM)) {
			return this.scorms;
		}

		if ((resourceType != null) && resourceType.equals(EResourceType.WIKI)) {
			return this.wikis;
		}

		return new ArrayList<ResourceRequestInfo>();
	}

}
