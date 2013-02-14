/**
	 * File RoleEnum.java
	 *
	 * Date Feb 14, 2013 
	 *
	 * Copyright TODO (INSERT COPYRIGHT)
	 */
package de.lemo.apps.entities;

import java.io.Serializable;
import de.lemo.apps.exceptions.EnumValueNotFoundException;



public enum RoleEnum implements Serializable{
	
	ADMIN("100"), 
	TEACHER("1"); 
	
	private final String value; 
	
	RoleEnum(final String weight) { value = weight; }

	public String value() { return value; }

	public static RoleEnum fromValue(String v) throws EnumValueNotFoundException { 
		if(v!=null) {
			v = v.trim();
			if(v.equals("") || v.equals("-1"))
				return null;
			
			for (RoleEnum c: RoleEnum.values()) { 
				if (c.value.equals(v.toUpperCase())) 
					{ 
						return c; 
					}
			} 
		}
		System.out.println("Unable to Parse Roles: "+v); 
		throw new EnumValueNotFoundException(); 
	}
}
