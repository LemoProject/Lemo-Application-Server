(function(d3custom, $, undefined) {


  d3custom.run = function() {
	
	  var data = [];
	  
	  data = d3custom.data;
	  
	  nv.addGraph(function() {
		  var chart = nv.models.multiBarChart()
		  	.showControls(false)
		  	.reduceXTicks(false);

		  chart.xAxis
		           .tickFormat(function(d) {
		              return d;
		            });

		  chart.yAxis
		  		.tickFormat(d3.format(''));
		  
		 
		  d3.select('#viz svg')
		      .datum(data)
		    .transition().duration(500)
		      .call(chart);

		  d3.selectAll('nv-x text')
		  	.attr("transform", function(d) {
		  		"rotate(135)"
		  	});
		  
		  nv.utils.windowResize(chart.update);

		  return chart;
		});
	  
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
