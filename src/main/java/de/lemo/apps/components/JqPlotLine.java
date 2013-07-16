package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;

/**
 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
 * options. Subclasses may override this method to configure additional features of the jqPlot library.
 */
@Import(library = { "../js/jqplot/plugins/jqplot.barRenderer.js",
					"../js/jqplot/plugins/jqplot.highlighter.min.js",
					"../js/jqplot/plugins/jqplot.cursor.min.js",
					"../js/jqplot/plugins/jqplot.dateAxisRenderer.js",
					"../js/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js",
					"../js/jqplot/plugins/jqplot.trendline.min.js",
					"../js/jqplot/plugins/jqplot.canvasTextRenderer.min.js"

})

public class JqPlotLine extends JqPlot {

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this component's javascript
	 * options. Subclasses may override this method to configure additional features of the jqPlot library.
	 * 
	 * @param config
	 *            parameters object
	 */

	@Inject
	private Messages messages;
	
	@Override
	protected void configure(final JSONObject config) {

		final JSONObject seriesDefaults = new JSONObject();
		final JSONObject axisDefaults = new JSONObject();
		final JSONObject renderer = new JSONObject();

		// change to "jQuery.jqplot.BarRenderer" and create an corresponding class if Bar Chart is required
		renderer.put("renderer", new JSONLiteral("jQuery.jqplot.LineRenderer"));

		seriesDefaults.put("seriesDefaults", renderer);
		seriesDefaults.put("legend", new JSONObject("{ show:false, location: 'e' }"));

		seriesDefaults.put("highlighter", new JSONObject(
				"{show: true, showLabel: true, tooltipAxes: 'y',sizeAdjust: 10.5 , tooltipLocation : 'ne'}"));
		seriesDefaults.put("cursor", new JSONObject("{ show:true, zoom: true, constrainZoomTo: x, showTooltip:true }"));
		seriesDefaults
				.put(
						"grid",
						new JSONObject(
								"{background: '#FFF', drawGridlines: false, gridLineColor: '#222', borderColor: '#222', shadow: false, drawBorder: false}"));
		axisDefaults
				.put(
						"yaxis",
						new JSONLiteral(
								"{padMin: 0, label: '"+messages.get("activities")+"', labelRenderer: jQuery.jqplot.CanvasAxisLabelRenderer,labelOptions: {fontFamily: 'Arial',fontSize: '9pt'}}"));
		axisDefaults.put("xaxis", new JSONLiteral("{padMin: 0, renderer:jQuery.jqplot.DateAxisRenderer}"));

		seriesDefaults.put("axes", axisDefaults);

		config.put("options", seriesDefaults);

	}

}