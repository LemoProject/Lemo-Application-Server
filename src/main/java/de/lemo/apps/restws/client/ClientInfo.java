package de.lemo.apps.restws.client;

import java.util.ResourceBundle;

import de.lemo.apps.application.ApplicationInfo;

public class ClientInfo {

    private static final String BUNDLE_NAME = "lemo"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        } catch (Exception e) {
            resourceBundle = ResourceBundle.getBundle(ApplicationInfo.getSystemName());
        }
    }

    private ClientInfo() {
        // no instance needed
    }

    public static String getDMSBaseUrl() {
        return resourceBundle.getString("apps.dms-base-url");
    }
}
