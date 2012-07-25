$(document).ready(function() {
	
//	var nodes = [{"name":"Bio1","value":15,"group":1},{"name":"Bio2","value":10,"group":1},{"name":"Bio3","value":5,"group":1},
//	             {"name":"Bio4","value":5,"group":1},
//	             {"name":"Chem1","value":15,"group":2},{"name":"Chem2","value":12,"group":2},{"name":"Chem3","value":10,"group":2},{"name":"Chem4","value":10,"group":2},
//	             {"name":"Chem5","value":10,"group":2},{"name":"Chem6","value":10,"group":2},{"name":"Chem7","value":10,"group":2},{"name":"Chem8","value":5,"group":2}],
//	             
//	    links= [{"source":"Bio1","target":"Bio2","type"="click","value":18},{"source":"Bio2","target":"Bio1",type="click","value":2},{"source":"Bio3","target":"Bio1",type="click","value":2},
//	            
//	            {"source":"Bio4","target":"Bio2",type="click","value":18},{"source":"Chem1","target":"Chem2",type="click","value":2},{"source":"Chem1","target":7,type="click","value":2},{"source":7,"target":8,type="click","value":2},
//	            {"source":8,"target":9,type="click","value":2},{"source":9,"target":10,type="click","value":2},{"source":6,"target":4,type="click","value":2}];
//	
//	
	
	
	
	var nodes = [{"name":"Bio1","value":15,"group":1},{"name":"Bio2","value":10,"group":1},{"name":"Bio3","value":5,"group":1},
	             {"name":"Bio4","value":5,"group":1},
	             {"name":"Chem1","value":15,"group":2},{"name":"Chem2","value":12,"group":2},{"name":"Chem3","value":10,"group":2},{"name":"Chem4","value":10,"group":2},
	             {"name":"Chem5","value":10,"group":2},{"name":"Chem6","value":10,"group":2},{"name":"Chem7","value":10,"group":2},{"name":"Chem8","value":5,"group":2}],
	             
	    links= [{"source":1,"target":0,"value":18},{"source":2,"target":1,"value":2},{"source":3,"target":1,"value":2},	            
	            {"source":4,"target":5,"value":18},{"source":5,"target":6,"value":2},{"source":6,"target":7,"value":2},{"source":7,"target":8,"value":2},
	            {"source":8,"target":9,"value":2},{"source":9,"target":10,"value":2},{"source":6,"target":4,"value":2}];
	
	var w = 960,
    h = 500
    
    var color = d3.scale.category20();

var vis = d3.select("#viz").append("svg:svg")
    .attr("width", w)
    .attr("height", h);


	var force = d3.layout.force()
        .nodes(nodes)
        .links(links)
        .gravity(.03)
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
        .text(function(d) { return d.name+" ("+d.value+")" });

    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; });
      
      node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
      /**node.attr("transform", function(d) { return "translate(" + (1.3*d.x) + "," + (20*d.value) + ")"; });*/
      
     
    });
    
    

	
});
