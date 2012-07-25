$(document).ready(function() {
	
	

	var links = [
//		             {"source": "Bio1", "target": "Bio2", "type": "click", value:2},
//		             {"source": "Bio2", "target": "Bio1", "type": "click", value:2},
//		             {"source": "Bio1", "target": "Bio3", "type": "click", value:2},
//		             {"source": "Bio1", "target": "Bio4", "type": "click", value:2},
//		             {"source": "Bio2", "target": "Bio4", "type": "click", value:10},
//		             {"source": "Bio3", "target": "Bio4", "type": "click", value:2},
//		             {"source": "Bio4", "target": "Bio3", "type": "click", value:2},
		             {"source": "Chem1", "target": "Chem2", "type": "click", value:2},
		             {"source": "Chem1", "target": "Chem3", "type": "click", value:2},
		             {"source": "Chem1", "target": "Chem4", "type": "click", value:2},
		             {"source": "Chem1", "target": "Chem5", "type": "click", value:2},
		             
		             {"source": "Chem1", "target": "Chem7", "type": "click", value:2},
		             {"source": "Chem2", "target": "Chem1", "type": "click", value:12},
		             {"source": "Chem2", "target": "Chem4", "type": "click", value:2},
		             {"source": "Chem2", "target": "Chem6", "type": "click", value:2},
		             {"source": "Chem3", "target": "Chem1", "type": "click", value:2},
		             
		             {"source": "Chem3", "target": "Chem7", "type": "click", value:2},
		             {"source": "Chem4", "target": "Chem1", "type": "click", value:2},
		             {"source": "Chem4", "target": "Chem5", "type": "click", value:2},
		             {"source": "Chem4", "target": "Chem6", "type": "click", value:2},
		             {"source": "Chem5", "target": "Chem1", "type": "click", value:2},
		             {"source": "Chem5", "target": "Chem3", "type": "click", value:2},
		             {"source": "Chem6", "target": "Chem2", "type": "click", value:2},
		             
		             {"source": "Chem6", "target": "Chem4", "type": "click", value:50},
		             {"source": "Chem7", "target": "Chem2", "type": "click", value:2},
		             {"source": "Chem7", "target": "Chem1", "type": "click", value:2}
		             
		           ];

	 //nodes = [	{"name":"Bio1","value":15,"group":1},
//				{"name":"Bio2","value":10,"group":1},
//					{"name":"Bio3","value":5,"group":1},
//		            {"name":"Bio4","value":5,"group":1},
//		            {"name":"Chem1","value":15,"group":2},
//		            {"name":"Chem2","value":12,"group":2},
//		            {"name":"Chem3","value":10,"group":2},
//		            {"name":"Chem4","value":10,"group":2},
//		            {"name":"Chem5","value":10,"group":2},
//		            {"name":"Chem6","value":10,"group":2},
//		            {"name":"Chem7","value":10,"group":2}];

	var nodes = {};

	// Compute the distinct nodes from the links.
	links.forEach(function(link) {
	  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
	  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
	});



	var w = 960,
	    h = 500;

	var svg = d3.select("#viz").append("svg:svg")
	    .attr("width", w)
	    .attr("height", h);


	var force = d3.layout.force()
	    .nodes(d3.values(nodes))
	    .links(links)
	    .size([w, h])
	    .gravity(.1)
	    .distance(200)
	    .charge(-400)
	    .on("tick", tick)
	    .start();


	    

	// Per-type markers, as they don't inherit styles.
	svg.append("svg:defs").selectAll("marker")
	    .data(["click"])
	  .enter().append("svg:marker")
	    .attr("id", String)
	    .attr("viewBox", "0 -5 10 10")
	    .attr("refX", 13)
	    .attr("refY", 0)
	    .attr("markerUnits",2)
	    .attr("markerWidth", 8)
	    .attr("markerHeight", 8)
	    .attr("orient", "auto")
	  .append("svg:path")
	    .attr("d", "M0,-5L10,0L0,5");

	var path = svg.append("svg:g").selectAll("path")
	    .data(force.links())
	  .enter().append("svg:path")
	    .attr("class", function(d) { return "link " + d.type; })
	    .attr("marker-end", function(d) { return "url(#" + d.type + ")"; })
	    .style("stroke", function(d) { 	if (d.value < 10) return "grey";
	    								if (d.value >= 10 && d.value < 50) return "lightgreen"; 
	    								if (d.value >= 50) return "green"; })
	    .style("stroke-width", function(d) { 	var strokeWidth = Math.sqrt(d.value)
	    										if(strokeWidth > 3) { return 3}
	    											else {return strokeWidth}; });


	var circle = svg.append("svg:g").selectAll("circle")
	    .data(force.nodes())
	  .enter().append("svg:circle")
	    .attr("r", 8)
	    .call(force.drag);
	    
	 circle.append("svg:title")
	    .text(function(d) { return "Value is: " + d.name;});
	 
	    

	var text = svg.append("svg:g").selectAll("g")
	    .data(force.nodes())
	  .enter().append("svg:g");

	// A copy of the text with a thick white stroke for legibility.
	text.append("svg:text")
	    .attr("x", 8)
	    .attr("y", ".31em")
	    .attr("class", "shadow")
	    .text(function(d) { return d.name; });

	text.append("svg:text")
	    .attr("x", 8)
	    .attr("y", ".31em")
	    .text(function(d) { return d.name; });
	    
	 

	// Use elliptical arc path segments to doubly-encode directionality.
	function tick() {
	  path.attr("d", function(d) {
	    var dx = d.target.x - d.source.x,
	        dy = d.target.y - d.source.y,
	        dr = Math.sqrt(dx * dx + dy * dy);
	    return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
	  });

	  circle.attr("transform", function(d) {
	    return "translate(" + d.x + "," + d.y + ")";
	  });

	  text.attr("transform", function(d) {
	    return "translate(" + d.x + "," + d.y + ")";
	  });
	}


	 $('svg title').parent().tipsy({ 
	        gravity: 'sw', 
	        html: true, 
	        title: function() { return $(this).find('title').text(); }
	      });

    
   
	
});
