
(function(d3custom, $, undefined) {

   
  
  
   
  d3custom.run = function() {
	  
	  
	  var w = 960, 
      h = 700,
      visits_min=1,
      visits_max=30, 
	  minDistance=1,
	  maxDistance=20,
	  minCharge=0,
	  maxCharge=10;
  
  var selectedNodes,
  	  node,
  	  link,
  	  vis,
  	  root;
  
  

    var color = d3.scale.category20();

    vis = d3.select("#viz").append("svg:svg")
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
    
    
    
    
    //selectedNodes = filterNodes(nodes, );
    var k = Math.sqrt(nodes.length / (w * h));
    
    var force = d3.layout.force()
    .nodes(nodes)
    .links(links)
    .on("tick",tick)
    //.gravity(.2)
    .distance(400)
    //.charge(-10)
    .charge(-10 / k)
    .gravity(100 * k)
    .size([w, h]);
    
   
    
    if(!nodes || !links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }

    
    
    
    console.log("Link Array: "+ links);
    
    
    
    update(nodes,links);   
    
    
    function filterNodes(nodes,  min, max){
		
  		_nodes = [];
  		_links = [];
   		$.each(nodes, function(i,v) {
			if(v.value >= min && v.value <= max){
				
				_nodes.splice(i,0,v);
				
			}
  		});
   		
//   		$.each(links, function(i,v) {
//			if(v.value >= min && v.value <= max){
//				
//				_nodes.push(v);
//				
//			}
//  		});
	 console.log("Selected Nodes: "+_nodes);
	 //selectedNodes.transition().duration(0).attr("visibility", "none");
	 return _nodes;
	  
	}    
    
    
 
function update(_nodes,___links) {
	__links = [].concat(links);
	
	_links = [];
	$.each(__links, function(i,v) {
    	console.log("Link Target: "+v.target+ "Link Source: "+v.source);
  		if(v.target in _nodes && v.source in _nodes) {
  			console.log("Link Schleife: "+ _nodes[v.target].name);
  			
  			v.target = _nodes[v.target];
  			v.source = _nodes[v.source];
	  		_links.push(v);
	  		
  		} else {console.log("Eintrag nicht uebernommen...");}
    });
		console.log("___Links: "+___links);
		console.log("_Links: "+_links);
		console.log("_Nodes: "+_nodes);
		force
			.nodes(_nodes)
			.links(_links)
			.start();
   

      link = vis.selectAll("line.link")
          .data(links, function(d) { return d.target.id; })
          .enter().insert("svg:line" , ".node")
          .attr("class", function(d) { return "link from-" + d.source.index +
                                   " to-" + d.target.index; })
          .attr("x1", function(d) { return d.source.x; })
          .attr("y1", function(d) { return d.source.y; })
          .attr("x2", function(d) { return d.target.x; })
          .attr("y2", function(d) { return d.target.y; })
          .style("stroke", "#999")
          .style("stroke-width", function(d) { return 1; });
      
      //link.exit().remove();

      node = vis.selectAll("g.node")
          .data(_nodes)
        .enter().append("svg:g")
          .attr("class", "node");
      
    node.append("nodetitle")
	    .text(function(d) { return "<b>Ressource:</b> "+ d.name+"<br /><br /> <b>Besuche</b>: "+d.value;});
    
    node.append("circle")
             .attr("class",  function(d,i) { return "node nodeId-" + i; })
             .attr("r", function(d) { 
            	 	var c = d.value/2;
            	 	return (c > visits_max ? visits_max : c);
            	 })
             .style("fill", function(d) { return color(1); })
             //.call(force.drag)
             
            .on("click", function(d){
            	vis.selectAll("line.link").attr("marker-end", null);
            	vis.selectAll("line.link").style("stroke", "grey");
            	
            	selectArcs(d).classed("highlightable",false)
            	
            	selectArcs(d).attr("marker-end", "url(#Triangle)");
            	selectArcs(d).style("stroke", "red");
            })
            .on("mouseout", function(d) {
            	 vis.selectAll( "line.highlightable").attr("marker-end", null);
            	 vis.selectAll( "line.highlightable").style("stroke", "grey");
            	 vis.selectAll( "line.highlightable").classed("highlightable",false)
            	 selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	             	.duration(250)
	             	.attr("r", function(d) { 
	             		var c = d.value/2;
	             		return (c > visits_max ? visits_max : c);} )
            	 .style("fill", function(d) { return color(1); });
            	 
        	 })
            .on("mouseover", function(d) {
            	selectArcs(d).attr("marker-end", "url(#Triangle)");
            	selectArcs(d).style("stroke", "blue");
            	selectArcs(d).classed("highlightable",true)
            	
            	selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
                	.duration(250)
                	.attr("r", function(d) { 
                	 	var c = d.value/2;
                	 	return (c > visits_max ? visits_max+6 : c+6);} )
                	.style("fill", function(d) { return "red"; });
            	 console.log("Selected Circle: "+selectedCircle);
            })
            //.exit().remove()
            .call(force.drag);
    
	    node.append("svg:text")
	    .attr("class", "nodetext")
	    .attr("dx", 12)
	    .attr("dy", ".35em")
	    .text(function(d) { return d.value; });

      
        // Exit any old nodes.
        //node.exit().remove();
        
       
        
         
} // Update End

		function tick() {
		    link.attr("x1", function(d) { return d.source.x; })
		        .attr("y1", function(d) { return d.source.y; })
		        .attr("x2", function(d) { return d.target.x; })
		        .attr("y2", function(d) { return d.target.y; });
		    
		    //node.attr("cx", function(d) { return d.x; })
		    //  	.attr("cy", function(d) { return d.y; });
		
		    node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
		    /**node.attr("transform", function(d) { return "translate(" + (1.3*d.x) + "," + (20*d.value) + ")"; });*/
		    
		   
		  };


        function selectArcs(d) {
     	   console.log(d + "Hello, world!");
     	   visSelect = vis.selectAll( "line.to-" + d.index +
                    ",line.from-" + d.index);
     	   console.log(visSelect);
     	   return vis.selectAll( "line.to-" + d.index +
                                  ",line.from-" + d.index)
        }        
        
        function selectNodes(d) {
      	   return vis.selectAll( "line.to-" + d.index +
                                   ",line.from-" + d.index)
         }       
        
      
      $('nodetitle').parent().tipsy({ 
	        gravity: 'sw', 
	        html: true, 
	        title: function() { return $(this).find('nodetitle').text(); }
	      });
     
      
      $('#supportSlider').slider({

			//range: true,
			min : 0,
			max :  100,
			values :  [visits_min, visits_max],
			slide : function( event, ui ) {
							$( "#slider-label" ).html( "Visits: " + ui.values[ 0 ] + " - " + ui.values[ 1 ] );

							min = ui.values[ 0 ];
							max = ui.values[ 1 ];
							console.log("Min Value:" + min + "Max Value: "+max );
							console.log("Nodes: "+nodes);
						}
      
      	

		});
      
      $('#supportSlider').mouseup(function(){
    	  update(filterNodes(nodes, min, max), links);
      });
      
      $('#distancesupportSlider').slider({

  	  	range: false,
			min : minDistance,
			max :  maxDistance,
			value : maxDistance,
			slide : function( event, ui ) {
				$( "#distanceslider-label" ).html( "Distance ("+minDistance+"-"+maxDistance+"): " + ui.value );
				console.log("Distance Value: "+ui.value);
				
    		}
	     
    });
    
    
    
    

    $('#chargesupportSlider').slider({
  	  
  	  
			range: false,
			min : minCharge,
			max :  maxCharge,
			value: maxCharge,
			slide : function( event, ui ) {
				$( "#chargeslider-label" ).html( "Charge ("+minCharge+"-"+maxCharge+"): " + ui.value );
    		
				console.log("Charge Value: "+ui.value);
    		}
    });
      
      
      
    
  };
  
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
