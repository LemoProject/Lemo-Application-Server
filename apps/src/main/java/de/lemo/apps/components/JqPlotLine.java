package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;

@Import( library={ 	"../js/jqplot/plugins/jqplot.barRenderer.js",
					"../js/jqplot/plugins/jqplot.highlighter.min.js",
					"../js/jqplot/plugins/jqplot.cursor.min.js",
					"../js/jqplot/plugins/jqplot.dateAxisRenderer.js"
		})
public class JqPlotLine extends JqPlot {

/**
* Invoked to allow subclasses to further configure the parameters passed to this component's javascript
* options. Subclasses may override this method to configure additional features of the jqPlot library.
*
* @param config parameters object
*/
    protected void configure(JSONObject config)
    {
    
      JSONObject options = new JSONObject();
      JSONObject seriesDefaults = new JSONObject();
      
      JSONObject axisDefaults = new JSONObject();
      JSONObject renderer= new JSONObject();
      JSONObject dateAxisRenderer= new JSONObject();
      JSONObject rendererOptions= new JSONObject();
      renderer.put("renderer", new JSONLiteral("jQuery.jqplot.BarRenderer"));
      renderer.put("rendererOptions",new JSONLiteral("{barDirection: 'vertical', highlightMouseDown: false, barWidth: 10}"));
      
      renderer.put("pointLabels", new JSONLiteral("{show: true}"));
      renderer.put("shadowAngle", new JSONLiteral("135"));
      renderer.put("fill", new JSONLiteral("true"));
      renderer.put("fillColor", new JSONLiteral("'#222'"));
      renderer.put("fillAlpha", new JSONLiteral("0.2"));
      renderer.put("fillAndStroke", new JSONLiteral("true"));
      
      seriesDefaults.put("seriesDefaults", renderer);
      seriesDefaults.put("legend", new JSONObject("{ show:true, location: 'e' }"));
      //seriesDefaults.put("shadowAngle", new JSONLiteral("5"));
      //seriesDefaults.put("pointLabels", new JSONObject("{ show:true }"));
      //seriesDefaults.put("title",new JSONLiteral("'Überschrift'"));
      seriesDefaults.put("highlighter",new JSONObject("{show: true, showLabel: true, tooltipAxes: 'y',sizeAdjust: 10.5 , tooltipLocation : 'ne'}"));
      seriesDefaults.put("cursor", new JSONObject("{ show:true, zoom: true, constrainZoomTo: x, showTooltip:true }"));
      seriesDefaults.put("grid", new JSONObject("{background: '#FFF', drawGridlines: false, gridLineColor: '#222', borderColor: '#222', shadow: false, drawBorder: false}"));
      axisDefaults.put("yaxis", new JSONObject("{padMin: 0}"));
      axisDefaults.put("xaxis", new JSONLiteral("{padMin: 0, renderer:jQuery.jqplot.DateAxisRenderer}"));
      //dateAxisRenderer.put("renderer", new JSONLiteral("jQuery.jqplot.DateAxisRenderer"));
      
      
      seriesDefaults.put("axes",axisDefaults);
      
      config.put("options",seriesDefaults);
      
      
    }

}