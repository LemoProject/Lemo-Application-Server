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