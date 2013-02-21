/**
 * File RoleEnum.java
 * Date Feb 14, 2013
 * Copyright TODO (INSERT COPYRIGHT)
 */
package de.lemo.apps.entities;

import de.lemo.apps.exceptions.EnumValueNotFoundException;

public enum Roles {

	ADMIN("100"),
	GLOBAL("10"),
	TEACHER("1");

	private final String value;

	Roles(final String weight) {
		value = weight;
	}

	public String value() {
		return value;
	}

	public static Roles fromValue(String v) throws EnumValueNotFoundException {
		if (v != null) {
			v = v.trim().toUpperCase();
			if (v.equals("") || v.equals("-1")) {
				return null;
			}

			for (Roles c : Roles.values()) {
				if (c.value.equals(v)) {
					return c;
				}
			}
		}
		System.out.println("Unable to Parse Roles: " + v);
		throw new EnumValueNotFoundException();
	}
}
