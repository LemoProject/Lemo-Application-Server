package de.lemo.apps.restws.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceRequestInfo {

	private Long id;
	private String resourcetype;
	private Long requests;
	private Long users;
	private String title;
	private Long resolutionSlot;

	public Long getUsers() {
		return this.users;
	}

	public void setUsers(final Long users) {
		this.users = users;
	}

	public Long getResolutionSlot() {
		return this.resolutionSlot;
	}

	public void setResolutionSlot(final Long resolutionSlot) {
		this.resolutionSlot = resolutionSlot;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getResourcetype() {
		return this.resourcetype;
	}

	public void setResourcetype(final String resourcetype) {
		this.resourcetype = resourcetype;
	}

	public Long getRequests() {
		return this.requests;
	}

	public void setRequests(final Long requests) {
		this.requests = requests;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public ResourceRequestInfo() {
	}

	public void incRequests() {
		this.requests++;
	}

	public ResourceRequestInfo(final Long id, final String resourceType, final Long requests, final String title, final Long resolutionSlot) {
		this.id = id;
		this.resourcetype = resourceType;
		this.requests = requests;
		this.title = title;
		this.resolutionSlot = resolutionSlot;
	}

}