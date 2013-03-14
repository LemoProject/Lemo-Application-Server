/**
 * File RoleEnum.java
 * Date Feb 14, 2013
 */
package de.lemo.apps.entities;

import de.lemo.apps.exceptions.EnumValueNotFoundException;


/**
 * Roles for users of the application server.
 */
public enum Roles {

	/**
	 * Administrator role with access to user management but no access to LMS content.
	 */
	ADMIN("DashboardAdmin")
	
	,
 
	/**
	 * Global user with unrestricted access to all courses.
	 */
	GLOBAL("Dashboard"),

	/**
	 * Default role with access to specified courses.
	 */
	TEACHER("Dashboard");
	
	private final String startpage;

	Roles(String v) {
		startpage = v;
	}

	public String startpage() {
		return startpage;
	}
	
	public static Roles fromValue(String v) throws EnumValueNotFoundException {
		if (v != null) {
			v = v.trim();
			if (v.equals(""))
				return null;

			for (Roles c : Roles.values()) {
				if (c.startpage.equals(v.toUpperCase())) {
					return c;
				}
			}
		}
		System.out.println("Unable to Parse Role item: " + v);
		throw new EnumValueNotFoundException();
	}
	

}
