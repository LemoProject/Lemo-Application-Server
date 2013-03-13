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