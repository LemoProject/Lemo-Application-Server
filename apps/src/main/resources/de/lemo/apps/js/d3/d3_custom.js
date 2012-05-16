$(document).ready(function() {
	
	
	var nodes = [{"name":"Bio1","value":15,"group":1},{"name":"Bio2","value":10,"group":1},{"name":"Bio3","value":5,"group":1}],
	             
	    links= [{"source":1,"target":0,"value":18},{"source":2,"target":1,"value":2}];
	
	var w = 960,
    h = 500
    
    var color = d3.scale.category20();

var vis = d3.select("#viz").append("svg:svg")
    .attr("width", w)
    .attr("height", h);


	var force = d3.layout.force()
        .nodes(nodes)
        .links(links)
        .gravity(.05)
        .distance(100)
        .charge(-100)
        .size([w, h])
        .start();

    var link = vis.selectAll("line.link")
        .data(links)
        .enter().append("svg:line")
        .attr("class", "link")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; })
        .style("stroke", "#999")
        .style("stroke-width", function(d) { return Math.sqrt(d.value); });

    var node = vis.selectAll("g.node")
    		.data(nodes)
    	.enter().append("svg:g")
    	.attr("class", "node")
    	.call(force.drag);

    	node.append("circle")
           .attr("class", "node")
           .attr("r", function(d) { return d.value})
           .style("fill", function(d) { return color(d.group); })
           .call(force.drag);
		
    	node.append("text")
        .attr("class", "nodetext")
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function(d) { return d.name+" ("+d.group+")" });

    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; });

      node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
    });

	
});
