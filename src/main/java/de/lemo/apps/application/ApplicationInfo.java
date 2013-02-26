/**
	 * File ApplicationInfo.java
	 *
	 * Date Feb 14, 2013 
	 *
	 */
package de.lemo.apps.application;

import java.util.ResourceBundle;

public final class ApplicationInfo {

	private static final String BUNDLE_NAME = "de.lemo.apps.application.config";
	private static final ResourceBundle RESOURCEBUNDLE = ResourceBundle.getBundle(ApplicationInfo.BUNDLE_NAME);

	private ApplicationInfo() {
		// no instance needed
	}

	public static String getSystemName() {
		return ApplicationInfo.RESOURCEBUNDLE.getString("lemo.system-name");
	}

	public static String getDisplayName() {
		return ApplicationInfo.RESOURCEBUNDLE.getString("lemo.display-name");
	}
}
