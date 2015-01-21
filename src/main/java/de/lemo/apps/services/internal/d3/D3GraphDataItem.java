/**
 * File ./src/main/java/de/lemo/apps/services/internal/d3/D3GraphDataItem.java
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
