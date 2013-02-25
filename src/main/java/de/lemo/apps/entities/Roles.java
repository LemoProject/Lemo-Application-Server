/**
 * File RoleEnum.java
 * Date Feb 14, 2013
 * Copyright TODO (INSERT COPYRIGHT)
 */
package de.lemo.apps.entities;

/**
 * Roles for users of the application server.
 */
public enum Roles {

	/**
	 * Administrator role with access to user management but no access to LMS content.
	 */
	ADMIN,

	/**
	 * Global user with unrestricted access to all courses.
	 */
	GLOBAL,

	/**
	 * Default role with access to specified courses.
	 */
	TEACHER,

}
