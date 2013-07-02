/**
 * File ./src/main/java/de/lemo/apps/services/Ajax/ZoneUpdater.java
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

package de.lemo.apps.services.Ajax;

/**
 * A simple mixin for attaching javascript that updates a zone on any client-side event.
 * Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
 */

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// This annotation tells Tapestry to declare the js file in the page so that the browser will pull it in.
@Import(library = "../js/ZoneUpdater.js")
public class ZoneUpdater {

	// Parameters

	/**
	 * The event to listen for on the client. If not specified, zone update can only be triggered manually through
	 * calling updateZone on the JS object.
	 */
	@Parameter(name = "clientEvent", defaultPrefix = BindingConstants.LITERAL)
	private String clientEvent;

	/**
	 * The event to listen for in your component class
	 */
	@Parameter(name = "event", defaultPrefix = BindingConstants.LITERAL, required = true)
	private String event;

	@Parameter(name = "prefix", defaultPrefix = BindingConstants.LITERAL, value = "default")
	private String prefix;

	@Parameter(name = "context")
	private Object[] context;

	/**
	 * The zone to be updated by us.
	 */
	@Parameter(name = "zone", defaultPrefix = BindingConstants.LITERAL, required = true)
	private String zone;

	/**
	 * Set secure to true if https is being used, else set to false.
	 */
	@Parameter(name = "secure", defaultPrefix = BindingConstants.LITERAL, value = "false")
	private boolean secure;

	// Useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	/**
	 * The element we attach ourselves to
	 */
	@InjectContainer
	private ClientElement clientElement;

	// The code

	void afterRender() {
		final String listenerURI = this.componentResources.createEventLink(this.event, this.context).toAbsoluteURI(this.secure);

		// Add some JavaScript to the page to instantiate a ZoneUpdater. It will run when the DOM has been fully loaded.

		final JSONObject spec = new JSONObject();
		spec.put("elementId", this.clientElement.getClientId());
		spec.put("clientEvent", this.clientEvent);
		spec.put("listenerURI", listenerURI);
		spec.put("zoneId", this.zone);
		this.javaScriptSupport.addScript("%sZoneUpdater = new ZoneUpdater(%s)", this.prefix, spec.toString());
	}
}