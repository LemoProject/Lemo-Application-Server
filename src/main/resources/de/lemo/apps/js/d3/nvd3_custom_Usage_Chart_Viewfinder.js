(function(d3custom, $, undefined) {

  function csvExport(data, chart) {
    dataExport.lineWithFocusChartButton('.export-button', data, chart, true);
  }
  
  
  d3custom.run = function() {
	
	  var data = []; 
	  data = d3custom.data;

	  
	  nv.addGraph(function() {
	    var  chart = nv.models.lineWithFocusChart()
		                     .x(function(d) { return d[0] })
		                     .y(function(d) { return d[1] }) 
		                     .color(d3.scale.category10().range());

		  chart.xAxis
		           .tickFormat(function(d) {
		              return d3.time.format('%x')(new Date(d))
		            });
		  
		  chart.x2Axis
          .tickFormat(function(d) {
             return d3.time.format('%x')(new Date(d))
           });

		  chart.yAxis
		  		.tickFormat(d3.format(''));
		  
		  chart.y2Axis
		        .tickFormat(d3.format(',.2f'));

		 
		  d3.select('#viz svg')
		      .datum(data)
		    .transition().duration(500)
		      .call(chart);

		  nv.utils.windowResize(chart.update);
	
		  csvExport(d3.select('#viz svg').data(), chart);
		  
		  return chart;
		});
	  
	 
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
