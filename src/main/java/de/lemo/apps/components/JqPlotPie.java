/**
 * File ./src/main/java/de/lemo/apps/components/JqPlotPie.java
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

import de.lemo.apps.components.JqPlot;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;

/**
 * Add an pie diagram to jqplot
 */
@Import(library = { "../js/jqplot/plugins/jqplot.pieRenderer.js" })
public class JqPlotPie extends JqPlot {

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the jqPlot library.
	 * 
	 * @param config
	 *            parameters object
	 */
	@Override
	protected void configure(final JSONObject config) {

		final JSONObject options = new JSONObject();
		final JSONObject seriesDefaults = new JSONObject();
		final JSONObject renderer = new JSONObject();
		final JSONObject rendererOptions = new JSONObject();
		renderer.put("renderer", new JSONLiteral("jQuery.jqplot.PieRenderer"));
		renderer.put("rendererOptions", new JSONLiteral("{showDataLabels: true}"));
		seriesDefaults.put("seriesDefaults", renderer);
		seriesDefaults.put("legend", new JSONObject("{ show:false, location: 'e' }"));
		seriesDefaults.put("grid", new JSONObject(
				"{background: '#FFF', gridLineColor: '#222', borderColor: '#222', shadow: false, drawBorder: false}"));
		config.put("options", seriesDefaults);
	}

}