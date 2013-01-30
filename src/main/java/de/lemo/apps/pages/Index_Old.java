package de.lemo.apps.pages;

import java.util.Date;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;

/**
 * Start page of application apps.
 */
public class Index_Old {

	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@InjectComponent
	private Zone zone;

	@Persist
	@Property
	private int clickCount;

	@Inject
	private AlertManager alertManager;

	public Date getCurrentTime() {
		return new Date();
	}

	void onActionFromIncrement() {
		this.alertManager.info("Increment clicked");

		this.clickCount++;
	}

	Object onActionFromIncrementAjax() {
		this.clickCount++;

		this.alertManager.info("Increment (via Ajax) clicked");

		return this.zone;
	}
}
