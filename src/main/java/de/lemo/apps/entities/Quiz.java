/**
 * File ./src/main/java/de/lemo/apps/entities/Quiz.java
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
