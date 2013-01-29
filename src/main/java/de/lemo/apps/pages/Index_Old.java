/**
 * File ./de/lemo/apps/pages/Index_Old.java
 * Date 2013-01-29
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages;

import java.util.Date;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.alerts.AlertManager;

/**
 * Start page of application apps.
 */
public class Index_Old
{

	@InjectComponent
	private Zone zone;

	@Inject
	private AlertManager alertManager;

	public Date getCurrentTime()
	{
		return new Date();
	}

	void onActionFromIncrement()
	{
		this.alertManager.info("Increment clicked");
	}

	Object onActionFromIncrementAjax()
	{
		this.alertManager.info("Increment (via Ajax) clicked");

		return this.zone;
	}
}
