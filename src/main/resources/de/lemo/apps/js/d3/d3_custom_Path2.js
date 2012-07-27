(function(d3custom, $, undefined) {

   
  var w = 960, 
      h = 700; 
   
  d3custom.run = function() {

    var color = d3.scale.category20();

    var svg = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h);
    
    
    
    var nodes = d3custom.nodes;
    var links = d3custom.links;
    
    if(!nodes || !links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }

    function selectArcs(d) {
		return svg.selectAll("line.to-" + d.index +
                         ",line.from-" + d.index);
	}
    
    links.forEach(function(link) {
  	  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
  	  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
  	});
    
 // Per-type markers, as they don't inherit styles.
	svg.append("svg:defs")
    .append("svg:marker")
    .attr("id", "Triangle")
     .attr("viewBox", "0 -5 10 10")
	    .attr("refX", 13)
	    .attr("refY", 0)
	    .attr("markerUnits",2)
	    .attr("markerWidth", 6)
	    .attr("markerHeight", 6)
	    .attr("orient", "auto")
	  .append("svg:path")
	    .attr("d", "M0,-5L10,0L0,5");

	var force = d3.layout.force()
	    .nodes(d3.values(nodes))
	    .links(links)
	    .size([w, h])
	    .gravity(.1)
	    .distance(200)
	    .charge(-400)
	    .on("tick", tick)
	    .start();


	    

	

	var path = svg.append("svg:g").selectAll("path")
	    .data(force.links())
	  .enter().append("svg:path")
	    .attr("class", function(d) { return "link from-" + d.source.index +
                                   " to-" + d.target.index; })
        .attr("marker-end", function(d) { return "url(#Triangle)"; })
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
	    .call(force.drag)
		.on("mouseout", function(d) {
			selectArcs(d).attr("marker-end", null);
			selectArcs(d).style("stroke", "grey");
		})
		.on("mouseover", function(d) {
			selectArcs(d).attr("marker-end", "url(#Triangle)");
			selectArcs(d).style("stroke", "red");
   	
		});
	
	 circle.append("svg:title")
	    .text(function(d) { return "Ressource: " + d.name;});
	 
	    

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

  };   
   
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
