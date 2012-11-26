(function(d3custom, $, undefined) {


  d3custom.run = function() {
	
	  var data = [];
	  
	  data = d3custom.data;
	  
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
	  
	  var  dataXAxis = data[0].values;
	  
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
 
		  nv.utils.windowResize(chart.update); 
		 
		  d3.selectAll('.nv-x text')
		    .attr('transform', 'translate(0,5)rotate(45)')
		    .style('text-anchor', 'start'); // in later nvd3 versions using attr instead of style should work too

		  return chart;
		});
	  
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
