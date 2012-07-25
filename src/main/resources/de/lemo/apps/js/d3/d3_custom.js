
(function(d3custom, $, undefined) {

   
  var w = 960, 
      h = 500; 
   
  d3custom.run = function() {

    var color = d3.scale.category20();

    var vis = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h);
    
    var nodes = d3custom.nodes;
    var links = d3custom.links;
    
    if(!nodes || !links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }

    $.each(links, function(i,e) {
  		e.target = nodes[e.target];
  		e.source = nodes[e.source];
    });
    
    var force = d3.layout.force()
          .nodes(nodes)
          .links(links)
          .gravity(.04)
          .distance(500)
          .charge(-10)
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
          .style("stroke-width", function(d) { return 1; });

      var node = vis.selectAll("g.node")
          .data(nodes)
          .enter().append("svg:g")
          .attr("class", "node")
          .call(force.drag);

        node.append("circle")
             .attr("class", "node")
             .attr("r", function(d) { 
            	 	var c = d.value/10;
            	 	return (c > 100 ? 100 : c);
            	 })
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
    
  };
  
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
