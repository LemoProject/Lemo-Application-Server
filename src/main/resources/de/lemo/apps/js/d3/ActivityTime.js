(function(d3custom, $) {

  d3custom.run = function() {

    var data = d3custom.data;
    var locale = data.pop();
    
    nv.addGraph(function() {
      var chart = nv.models.lineWithFocusChart().x(function(d) {
        return d[0];
      }).y(function(d) {
        return d[1];
      }).color(d3.scale.category10().range());

      chart.xTickFormat(function(d) {
    	  console.log("locale:"+locale.locale);
        return d3.time.format(locale.locale)(new Date(d))
      });

      d3.select('#viz svg').datum(data).transition().duration(500).call(chart);

      dataExport.lineWithFocusChartButton('.export-button', d3.select('#viz svg').data(), chart, locale);
      
      nv.utils.windowResize(chart.update);
      
      return chart;
    });

  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
