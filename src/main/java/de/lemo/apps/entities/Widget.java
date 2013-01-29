package de.lemo.apps.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Widget extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6978813940322609187L;
	
	public Widget(String name,User user,Long courseId, Long position){
		this.name= name;
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
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the position
	 */
	public Long getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Long position) {
		this.position = position;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
