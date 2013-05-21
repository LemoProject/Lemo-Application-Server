(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var w = 960, 
      h = 500, 
	  data = d3custom.data;
	  console.log("Data: "+data);
		//check if we have values to work with
		  if(!data) {
		    	//$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
		    	//return;
		  }
	  
	 // Generate random variables.
	  var values = d3.range(20).map(d3.random.logNormal(Math.log(40), .4));
	  
	  console.log("Value"+values);

	  // A formatter for counts.
	  var formatCount = d3.format(",.0f");

	  var margin = {top: 10, right: 30, bottom: 30, left: 30},
	      width = w - margin.left - margin.right,
	      height = h - margin.top - margin.bottom;

	  var x = d3.scale.linear()
	      .domain([1, 100])
	      .range([0, width]);

	  // Generate a histogram using twenty uniformly-spaced bins.
	  var data2 = d3.layout.histogram()
	      .bins(x.ticks(20))
	      (values);
	  console.log("calcultedData:"+data2)
	  
	  var y = d3.scale.linear()
	      .domain([0, d3.max(data2, function(d) { return d.y; })])
	      .range([height, 0]);

	  var xAxis = d3.svg.axis()
	      .scale(x)
	      .orient("bottom");

	  var svg = d3.select("#viz").append("svg:svg")
	      .attr("width", width + margin.left + margin.right)
	      .attr("height", height + margin.top + margin.bottom)
	    .append("g")
	      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	  console.log("calcultedData 2:"+data2)
	  
	  var bar = svg.selectAll(".bar")
	      .data(data2)
	    .enter().append("g")
	      .attr("class", "bar")
	      .attr("transform", function(d) { return "translate(" + x(d.x) + "," + y(d.y) + ")"; });

	  bar.append("rect")
	      .attr("x", 1)
	      .attr("width", x(data2[0].dx) - 1)
	      .attr("height", function(d) { return height - y(d.y); });

	  bar.append("text")
	      .attr("dy", ".75em")
	      .attr("y", 6)
	      .attr("x", x(data2[0].dx) / 2)
	      .attr("text-anchor", "middle")
	      .text(function(d) { return formatCount(d.y); });

	  svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis);
	
  }
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
