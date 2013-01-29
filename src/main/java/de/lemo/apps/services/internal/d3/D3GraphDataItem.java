package de.lemo.apps.services.internal.d3;

import java.io.Serializable;

import org.apache.tapestry5.json.JSONArray;

public class D3GraphDataItem implements Comparable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6743372426916686477L;

	
	private JSONArray nodes;
	
	private JSONArray links;
	
	public D3GraphDataItem(){
		
	}

	/**
	 * @return the nodes
	 */
	public JSONArray getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(JSONArray nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the links
	 */
	public JSONArray getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(JSONArray links) {
		this.links = links;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
