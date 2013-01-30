package de.lemo.apps.pages;

import org.apache.tapestry5.services.ExceptionReporter;

/**
 * Controller class for the system wide Exception Template
 * 
 * @version 1.0
 * @author Andreas Pursian
 */
public class ExceptionReport implements ExceptionReporter {

	private String error;

	public void reportException(final Throwable exception) {
		this.error = exception.getLocalizedMessage();
	}

	public String getError() {
		return this.error;
	}
}