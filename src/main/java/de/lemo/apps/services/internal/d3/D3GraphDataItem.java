package de.lemo.apps.services.internal.d3;

import java.io.Serializable;
import org.apache.tapestry5.json.JSONArray;

public class D3GraphDataItem implements Comparable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6743372426916686477L;

	private JSONArray nodes;

	private JSONArray links;

	public D3GraphDataItem() {

	}

	/**
	 * @return the nodes
	 */
	public JSONArray getNodes() {
		return this.nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(final JSONArray nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the links
	 */
	public JSONArray getLinks() {
		return this.links;
	}

	/**
	 * @param links
	 *            the links to set
	 */
	public void setLinks(final JSONArray links) {
		this.links = links;
	}

	@Override
	public int compareTo(final Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
