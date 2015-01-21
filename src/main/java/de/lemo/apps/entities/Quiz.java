/**
 * File ./src/main/java/de/lemo/apps/entities/Quiz.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((combinedId == null) ? 0 : combinedId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quiz other = (Quiz) obj;
		if (combinedId == null) {
			if (other.combinedId != null)
				return false;
		} else if (!combinedId.equals(other.combinedId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	

	
	

}
