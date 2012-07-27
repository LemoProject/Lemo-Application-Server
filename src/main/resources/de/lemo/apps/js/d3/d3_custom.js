
(function(d3custom, $, undefined) {

   
  var w = 960, 
      h = 700; 
   
  d3custom.run = function() {

    var color = d3.scale.category20();

    var vis = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h);
    
    vis.append("svg:defs")
    .append("svg:marker")
      .attr("id", "Triangle")
      .attr("viewBox", "0 0 10 10")
      .attr("refX", 30)
      .attr("refY", 5)
      .attr("markerUnits", "strokeWidth")
      .attr("markerWidth", 10)
      .attr("markerHeight", 10)
      .attr("orient", "auto")
      .append("svg:path")
    	 .attr("d", "M 0 0 L 10 5 L 0 10 z");
    
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
          .gravity(.2)
          .distance(400)
          .charge(-10)
          .size([w, h])
          .start();
    
    	function selectArcs(d) {
    		return vis.selectAll("line.to-" + d.index +
                             ",line.from-" + d.index);
    	}

      var link = vis.selectAll("line.link")
          .data(links)
          .enter().append("svg:line")
          .attr("class", function(d) { return "link from-" + d.source.index +
                                   " to-" + d.target.index; })
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
             .call(force.drag)
        	 .on("mouseout", function(d) {
        		 selectArcs(d).attr("marker-end", null);
        		 selectArcs(d).style("stroke", "grey");
        	 })
            .on("mouseover", function(d) {
            	selectArcs(d).attr("marker-end", "url(#Triangle)");
            	selectArcs(d).style("stroke", "red");
            	
            });
      
        node.append("svg:title")
	    .text(function(d) { return "<b>Ressource:</b> "+ d.name+"<br /><br /> <b>Besuche</b>: "+d.value;});
        
        
      force.on("tick", function() {
        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });
        
        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
        /**node.attr("transform", function(d) { return "translate(" + (1.3*d.x) + "," + (20*d.value) + ")"; });*/
        
       
      });
      
      $('svg title').parent().tipsy({ 
	        gravity: 'sw', 
	        html: true, 
	        title: function() { return $(this).find('title').text(); }
	      });
    
  };
  
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
