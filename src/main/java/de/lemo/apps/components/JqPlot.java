/**
           * File ./src/main/java/de/lemo/apps/components/JqPlot.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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

package de.lemo.apps.components;

/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 * 
 * Copyright 2008-2010 by chenillekit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import de.lemo.apps.services.internal.jqplot.js.*;
import de.lemo.apps.services.internal.jqplot.DataJqPlotSerializer;
import java.util.List;

/**
 * Created By Got5 - https://github.com/got5/tapestry5-jqPlot
 * chart component based on <a href="http://www.jqplot.com/index.php"> javascript library</a>.
 * Inspired form tapestry implementation that came from Chenillekit project kudos to homburg
 */
@SupportsInformalParameters
@Import(stack = JqPlotJavaScriptStack.STACK_ID, library = { "T5jqPlot.js" })
public class JqPlot implements ClientElement {

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * the list of data item arrays.
	 */
	@Parameter(name = "dataItems", required = false, defaultPrefix = BindingConstants.PROP)
	private List<List<DataJqPlotSerializer>> dataItemsList;

	/**
	 * PageRenderSupport to get unique client side id.
	 */
	@Environmental
	private JavaScriptSupport javascriptSupport;

	/**
	 * For blocks, messages, create actionlink, trigger event.
	 */
	@Inject
	private ComponentResources resources;

	private String assignedClientId;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	@SetupRender
	void setupRender() {
		this.assignedClientId = this.javascriptSupport.allocateClientId(this.clientId);
	}

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 */
	@BeginRender
	void beginRender(final MarkupWriter writer) {
		writer.element("div", "id", this.getClientId());
		this.resources.renderInformalParameters(writer);
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 */
	@AfterRender
	void afterRender() {
		final JSONObject spec = new JSONObject();
		final JSONObject config = new JSONObject();
		JSONArray dataArray = null;

		//
		// Let subclasses do more.
		//
		this.configure(config);

		//
		// do it only if user give us some values
		//
		if ((this.dataItemsList != null) && (this.dataItemsList.size() > 0)) {
			dataArray = new JSONArray();

			for (final List<DataJqPlotSerializer> dataItems : this.dataItemsList) {
				final JSONArray data = JqPlot.buildDataValues(dataItems);
				dataArray.put(data);
			}
		}

		//
		// if the user doesn't give us some chart values we add an empty value array.
		//
		if (dataArray != null) {
			spec.put("data", dataArray);
		} else if (config.has("data")) {
			spec.put("data", config.get("data"));
		} else {
			spec.put("data", new JSONArray(new JSONArray()));
		}

		if (config.has("options")) {
			spec.put("options", config.get("options"));
		}

		spec.put("id", this.getClientId());

		this.javascriptSupport.addInitializerCall("jqPlotChart", spec);
	}

	/**
	 * let us build the data value string for Flotr.
	 * 
	 * @param dataItems
	 *            a list of data items
	 * @return a JSON array containing the data items
	 */
	private static JSONArray buildDataValues(final List<DataJqPlotSerializer> dataItems) {
		final JSONArray data = new JSONArray();

		for (final DataJqPlotSerializer dataItem : dataItems) {
			data.put(dataItem.toJSONArray());
		}

		return data;
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the Flotr.
	 * <p/>
	 * This implementation does nothing.
	 * 
	 * @param config
	 *            parameters object
	 */
	protected void configure(final JSONObject config) {
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId() {
		return this.assignedClientId;
	}
}