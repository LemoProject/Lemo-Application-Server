/**
 * File Quiz.java
 * Date Apr 15, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.entities;


/**
 * @author Boris Wenzlaff
 *
 */
public class Quiz {
	
	private String name;
	private Long combinedId;
	
	
	public Quiz(String name, Long combinedId){
		this.name = name;
		this.combinedId = combinedId;
	}
	
	public Quiz(Long combinedId){
		this.combinedId = combinedId;
	}
	
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
	 * @return the combinedId
	 */
	public Long getCombinedId() {
		return combinedId;
	}
	
	/**
	 * @param combinedId the combinedId to set
	 */
	public void setCombinedId(Long combinedId) {
		this.combinedId = combinedId;
	}
	
	

}
