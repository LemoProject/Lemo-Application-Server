package de.lemo.apps.application;

import java.util.ResourceBundle;

public class ApplicationInfo {

    private static final String BUNDLE_NAME = "de.lemo.apps.application.config"; //$NON-NLS-1$
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    private ApplicationInfo() {
        // no instance needed
    }

    public static String getSystemName() {
        return resourceBundle.getString("lemo.system-name");
    }

    public static String getDisplayName() {
        return resourceBundle.getString("lemo.display-name");
    }
}
