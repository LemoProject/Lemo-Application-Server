package de.lemo.apps.components;

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
import de.lemo.apps.services.internal.d3.D3GraphDataItem;

/*
 * Basic implementation for an graph object
 */
@SupportsInformalParameters
@Import(library = { "../js/d3/d3_custom.js",
					"../js/d3/d3.v2.js" })
public class D3BasicGraph implements ClientElement {

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
	private D3GraphDataItem dataItemsList;

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
		JSONArray dataArrayNodes = null;
		JSONArray dataArrayLinks = null;

		//
		// do it only if user give us some values
		//
		if (this.dataItemsList != null) {
			dataArrayNodes = new JSONArray();
			dataArrayLinks = new JSONArray();

			dataArrayNodes = this.dataItemsList.getNodes();
			dataArrayLinks = this.dataItemsList.getLinks();
		}

		//
		// if the user doesn't give us some chart values we add an empty value array.
		//
		if (dataArrayNodes != null) {
			spec.put("nodes", dataArrayNodes);
		} else {
			spec.put("nodes", new JSONArray(new JSONArray()));
		}

		if (dataArrayLinks != null) {
			spec.put("links", dataArrayLinks);
		} else {
			spec.put("links", new JSONArray(new JSONArray()));
		}

		spec.put("id", this.getClientId());

		this.javascriptSupport.addInitializerCall("d3GraphChart", spec);
	}

	@Override
	public String getClientId() {
		return this.assignedClientId;
	}

}
