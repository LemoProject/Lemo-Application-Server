package de.lemo.apps.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;

@Import( library={ 	"../js/jqplot/plugins/jqplot.barRenderer.js",
					"../js/jqplot/plugins/jqplot.highlighter.min.js",
					"../js/jqplot/plugins/jqplot.cursor.min.js",
					"../js/jqplot/plugins/jqplot.dateAxisRenderer.min.js"
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
      JSONObject renderer= new JSONObject();
      JSONObject rendererOptions= new JSONObject();
      //renderer.put("renderer", new JSONLiteral("jQuery.jqplot.BarRenderer"));
      //renderer.put("rendererOptions",new JSONLiteral("{barDirection: 'vertical', highlightMouseDown: false}"));
      renderer.put("pointLabels", new JSONLiteral("{show: true}"));
      renderer.put("shadowAngle", new JSONLiteral("135"));
      seriesDefaults.put("seriesDefaults", renderer);
      seriesDefaults.put("legend", new JSONObject("{ show:true, location: 'e' }"));
      //seriesDefaults.put("shadowAngle", new JSONLiteral("5"));
      //seriesDefaults.put("pointLabels", new JSONObject("{ show:true }"));
      seriesDefaults.put("title",new JSONLiteral("'Überschrift'"));
      seriesDefaults.put("highlighter",new JSONObject("{show: true, showLabel: true, tooltipAxes: 'y',sizeAdjust: 7.5 , tooltipLocation : 'ne'}"));
      seriesDefaults.put("cursor", new JSONObject("{ show:true, zoom: true, showTooltip:true }"));
      seriesDefaults.put("grid", new JSONObject("{background: '#FFF', drawGridlines: false, gridLineColor: '#222', borderColor: '#222', shadow: false, drawBorder: false}"));
      config.put("options",seriesDefaults);
      
      
    }

}