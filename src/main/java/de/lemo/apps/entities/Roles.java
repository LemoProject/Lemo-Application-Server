/**
 * File ./src/main/java/de/lemo/apps/entities/Roles.java
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
	TEACHER("Dashboard"),
	
	/**
	 * Default role with access to specified courses.
	 */
	STUDENT("Dashboard");
	
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
		throw new EnumValueNotFoundException();
	}
	

}
