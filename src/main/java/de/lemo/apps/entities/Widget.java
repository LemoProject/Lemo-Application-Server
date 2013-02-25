/**
 * File Widget.java
 * Date Feb 14, 2013
 * Copyright TODO (INSERT COPYRIGHT)
 */
package de.lemo.apps.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "widget")
public class Widget extends AbstractEntity {

	private static final long serialVersionUID = -8473743292482994742L;

	private String name;
	private Long courseId;
	private Long position;
	private User user;

	public Widget(final String name, final User user, final Long courseId, final Long position) {
		this.name = name;
		this.user = user;
		this.courseId = courseId;
		this.position = position;
	}

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

	@ManyToOne
	@JoinColumn(updatable = true, insertable = true, name = "user_id")
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
