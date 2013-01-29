/**
 * File ./de/lemo/apps/entities/Widget.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Widget extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6978813940322609187L;

	public Widget(final String name, final User user, final Long courseId, final Long position) {
		this.name = name;
		this.user = user;
		this.courseId = courseId;
		this.position = position;
	}

	private String name;
	private Long courseId;
	private Long position;
	private User user;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return this.courseId;
	}

	/**
	 * @param courseId
	 *            the courseId to set
	 */
	public void setCourseId(final Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the position
	 */
	public Long getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(final Long position) {
		this.position = position;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

}
