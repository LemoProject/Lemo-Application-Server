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
	private List<ResourceRequestInfo> courses;
	private List<ResourceRequestInfo> forums;
	private List<ResourceRequestInfo> questions;
	private List<ResourceRequestInfo> quiz;
	private List<ResourceRequestInfo> resources;
	private List<ResourceRequestInfo> scorms;
	private List<ResourceRequestInfo> wikis;

	public void setAssignmentRRI(final List<ResourceRequestInfo> assignments) {
		this.assignments = assignments;
	}

	public void setCourseRRI(final List<ResourceRequestInfo> courses) {
		this.courses = courses;
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
		this.courses = new ArrayList<ResourceRequestInfo>();
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
	public List<ResourceRequestInfo> getCoursesRRI() {
		return this.courses;
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

//		if ((resourceType != null) && resourceType.equals(EResourceType.COURSE)) {
//			return this.courses;
//		}

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
