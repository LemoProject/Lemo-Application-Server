package de.lemo.apps.application;

import java.util.ResourceBundle;

public class ApplicationInfo {

    private static final String BUNDLE_NAME = "lemo"; //$NON-NLS-1$
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    private ApplicationInfo() {
        // no instance needed
    }

    public static String getDMSBaseUrl() {
        return resourceBundle.getString("apps.dms-base-url");
    }
}
