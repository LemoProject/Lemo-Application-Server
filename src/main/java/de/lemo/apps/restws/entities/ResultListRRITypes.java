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

	private List<ResourceRequestInfo> tasks;
	private List<ResourceRequestInfo> learningObjects;
	private List<ResourceRequestInfo> collaborativeObjects;

	public void setAssignmentRRI(final List<ResourceRequestInfo> assignments) {
		this.tasks = assignments;
	}

	public void setChatRRI(final List<ResourceRequestInfo> chat) {
		this.learningObjects = chat;
	}

	public void setForumRRI(final List<ResourceRequestInfo> forums) {
		this.collaborativeObjects = forums;
	}


	public ResultListRRITypes() {
		this.tasks = new ArrayList<ResourceRequestInfo>();
		this.learningObjects = new ArrayList<ResourceRequestInfo>();
		this.collaborativeObjects = new ArrayList<ResourceRequestInfo>();
	}

	@XmlElement
	public List<ResourceRequestInfo> getTaskRRI() {
		return this.tasks;
	}

	@XmlElement
	public List<ResourceRequestInfo> getCollaborativeObjectRRI() {
		return this.collaborativeObjects;
	}

	@XmlElement
	public List<ResourceRequestInfo> getLearningObjectRRI() {
		return this.learningObjects;
	}
}
