/**
 * File ./src/main/java/de/lemo/apps/entities/GenderEnum.java
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
 * File GenderEnum.java
 * Date Apr 25, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.entities;

import java.io.Serializable;
import de.lemo.apps.exceptions.EnumValueNotFoundException;


/**
 * @author Boris Wenzlaff
 *
 */
public enum GenderEnum implements Serializable{
	
	FEMALE(1L), 
	MALE(2L),
	UNKNOWN(0L);
	
	
	private final Long value; 
	
	GenderEnum(Long v) { value = v; }

	public Long value() { return value; }

	public static GenderEnum fromValue(String v) throws EnumValueNotFoundException{ 
		if(v!=null) {
			v = v.trim();
			if(v.equals(""))
				return null;
			
			for (GenderEnum c: GenderEnum.values()) {
				if (c.value.equals(v)) 
				{ 
					return c; 
				} 
			} 
		}
		System.out.println("Unable to Parse GenderEnum: "+v);
		throw new EnumValueNotFoundException(); 
	} 
}