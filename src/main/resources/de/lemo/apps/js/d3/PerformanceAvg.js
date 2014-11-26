(function(d3custom, $, undefined) {


  d3custom.run = function() {
	
	  var data = [];
	  
	  data = d3custom.data;
	  
	  if(!data || !data[0]) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }  
	  
	  var  	dataRequests = data[0].values;

	  
	  data[0].values = dataRequests;
	  
	  function sortfunction(a, b){
		  return (a.y - b.y) //causes the array to be sorted numerically and ascending
	  }
	  
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
		  
		  chart.color(function(d, i) { 
			  return hashColor(d.values[i].x); 
			  });
		  
		  d3.select('#viz svg')
		      .datum(data)
		      .transition().duration(500)
		      .call(chart);
 
		  nv.utils.windowResize(chart.update); 
		 
		  d3.selectAll('.nv-x text')
		    .attr('transform', 'translate(0,5)rotate(45)')
		    .style('text-anchor', 'start');
		  d3.selectAll(".nv-bar.positive").style("fill", function(d) { return hashColor(d.x) });
		  
		  return chart;
		});	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
