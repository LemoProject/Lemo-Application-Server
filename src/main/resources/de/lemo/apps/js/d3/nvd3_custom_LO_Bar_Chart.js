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
		  	//.rotateLabels(-35);

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

//		  var 	container = d3.select(this),
//		  		wrap = container.selectAll('g.nv-wrap.nv-multiBarWithLegend').data([data]),
//		  		g = wrap.select('g');
		  
//		  var xTicks = g.select('.nv-x.nv-axis > g').selectAll('g');
//		  xTicks.selectAll('text')
//		  	.attr('transform', function(d,i,j) { 
//            	console.log("blub-"+d+"----"+i+" --- "+j);
//            	return   j> 0 ? 'translate(-50,'+data[0].values[j].x.length+'),rotate(-35 0 0)' : 'translate(-50,60),rotate(-35 0 0)' })
//           // .attr('transform', function(d,i,j) { return 'translate('+(-10*4.5 - 22)+',-80)' })
//            .attr('text-transform', rotateLabels > 0 ? 'start' : 'end');
		  
//		  d3.selectAll('text')
//		  	.attr("transform", function(d) {
//		  		return "translate(-10,-20),rotate(-35)";
//		  	});
//		  
		  nv.utils.windowResize(chart.update);
		  
		  d3.selectAll(".nv-x text")
	      .attr("transform","rotate(45)")
	      .attr("dy","0")
	      .attr("dx",function(d) {
	      if (d!=null) {
	        return 2.8*d.length+20;
	        }
	      });

		  return chart;
		});
	  
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
