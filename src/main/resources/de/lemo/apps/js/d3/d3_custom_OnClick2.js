
(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var w = 960, 
      h = 700,
      selectedNodes,
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
    
    var _nodes = d3custom.nodes,
    	_links = d3custom.links;
    	

    
    
    //calculate initial multiplier for charge and gravity
    var k = Math.sqrt(_nodes.length / (w * h));
    
   var 	visits_min=1,
   		visits_max=500, 
		  minDistance=1,
		  maxDistance=400,
		  minCharge=0,
		  maxCharge=10/k;
    
    
    var force = d3.layout.force()
    .on("tick",tick)
    .distance(maxDistance)
    .charge(-maxCharge)
    .gravity(100 * k)
    .size([w, h]);
    
    
    
    this.addLink = function (source, target) {
        links.push({"source":findNode(source),"target":findNode(target)});
        update();
    }

    var findNode = function(id,mynodes) {
        for (var i in mynodes) {if (mynodes[i]["id"] == id) return mynodes[i];};
    }
    
    var findNeighbours = function(id,mylinks) {
    	var neighbours = [];
    	console.log("NeighbourID:"+ id );
    	 $.each(mylinks, function(i,v) {if (v.source.id == id ) neighbours.push(v.target)
        						if (v.target.id == id ) neighbours.push(v.source)});
        return neighbours;
    }
    
    var existNode = function(id,mynodes) {
        for (var i in mynodes) {if (mynodes[i]["id"] == id) return true; }
        return false;
    }
    
    function init(){
    	 $.each(_nodes, function(i,v) {
    		 nodes.push({"id":i, "name":v.name, "value": v.value});
    	 });
    	 $.each(_links, function(i,v) {
    		 links.push({"source":nodes[v.source],"target":nodes[v.target]});
    	 });
    	 
    	 //$.each(links, function(i,v) {
        	// console.log("Links Array ["+i+"]:"+v.target.name +"-"+v.source.name);
        //});
    	 
    	 update2();
    	 
    }

    
   var 	nodes = force.nodes(),
		links = force.links();
   

   //check if we have values to work with
    if(!_nodes || !_links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }

    
    //init visualisation
    init();
    
    function filterNodes(min, max){
		nodes = [];
		links = [];
		var tempNodes = [],
			oldNodes = [],
			oldNodesCounter = 0,
			nodesCounter = 0,
			linkCounter = 0;
		
    	$.each(_nodes, function(i,v) {
    		console.log("Schreibe Nodes"+i);
    		if(v.value >= min && v.value <= max){
   		 		nodes.push({"id":i, "name":v.name, "value": v.value});
   		 		console.log("Nodes"+nodesCounter+" --- "+nodes[nodesCounter].name +" ---ID:"+nodes[nodesCounter].id +" ---- "+v.name);
   		 		nodesCounter++;
    		} else {
				oldNodes.push({"id":i, "name":v.name, "value": v.value});
				console.log("OldNodes"+oldNodesCounter+" --- "+oldNodes[oldNodesCounter].name +" ---- "+v.name);
				oldNodesCounter++;
				}
	   	 });
    	
    	$.each(_links, function(i,v) {
    		console.log("Bin in Links Schleife -- Target: "+ v.target +" Source: "+ v.source+" --- " +existNode(v.target,nodes)+"----"+existNode(v.source,nodes));
			if(existNode(v.target,nodes) && existNode(v.source,nodes)){
				
				 links.push({"source":findNode(v.source,nodes),"target":findNode(v.target,nodes)});
				 console.log("Links"+linkCounter+" --- "+links[linkCounter].source.name +" ---- "+v.source.name);
				 linkCounter++;
			}
  		});

	}    
    
function update2() {
	
	
	
	var link = vis.selectAll("line.link")
		.data(links, function(d) { return d.target.id; });
     
	 link.enter().insert("svg:line" , ".node")
     	 .attr("class", function(d) { return "link from-" + d.source.id +
                              " to-" + d.target.id; })
	     .attr("x1", function(d) { return d.source.x; })
	     .attr("y1", function(d) { return d.source.y; })
	     .attr("x2", function(d) { return d.target.x; })
	     .attr("y2", function(d) { return d.target.y; })
	     .style("stroke", "#999")
	     .style("stroke-width", function(d) { return 1; });
 
 link.exit().remove();

 var node = vis.selectAll("g.node")
     .data(nodes, function(d) { return d.id;})

 var nodeEnter = node.enter().append("g")
            .attr("class", function(d,i) { return "node gnodeId-" + i; })
            .call(force.drag);
 
 
	 nodeEnter.append("circle")
	 .attr("class",  function(d,i) { return "node nodeId-" + i; })
	 .attr("r", function(d) {
		 	var c = 0;
		 	if(d.value<=10 ) c = 8
		 		else c = d.value/4;
		 	return (c > visits_max ? visits_max/4 : c);
		 })
	 .style("fill", function(d) { return color(1); })
	 //.call(force.drag)
	 
	.on("click", function(d){
		var edges = vis.selectAll("line.link"),
			clickedEdges = selectArcs(d);
		
		edges.attr("marker-end", null);
		edges.style("stroke", "grey");
		
		clickedEdges.classed("highlightable",false)
		
		clickedEdges.attr("marker-end", "url(#Triangle)");
		clickedEdges.style("stroke", "red");
		
		var neighbours = findNeighbours(d.id,links);
		var neighbourSelectString = "";
		$.each(neighbours, function(i,v) {
			if(neighbourSelectString=="")
				neighbourSelectString = neighbourSelectString+"g.gnodeId-"+v.id;
			else neighbourSelectString = neighbourSelectString+", g.gnodeId-"+v.id;
		});
		console.log("NeighbourhoddString: "+neighbourSelectString);
		var neighbourNodes = vis.selectAll(neighbourSelectString);
		console.log("NeighbourSelection"+neighbourNodes);
		
//		$.each(neighbourNodes, function(i,v) {
//			v.parentNode.append("svg:text")
//			.attr("class", "nodetext")
//			.attr("dx", 12)
//			.attr("dy", ".35em")
//			.text(function(d) { return v.name; });
//		});
		//neighbourNodes.append().style("fill", function(d) { return color(5); })
		
		vis.selectAll("text.nametext").remove();
		
		neighbourNodes.append("svg:text")
	   .attr("class", "nametext")
	   .attr("dx", 12)
	   .attr("dy", ".35em")
	   .text(function(d) { return d.name; });
		
		console.log("Number of neighbours: "+neighbours.length);
	})
	.on("mouseout", function(d) {
		 var edges = vis.selectAll( "line.highlightable");
		 edges.attr("marker-end", null);
		 edges.style("stroke", "grey");
		 edges.classed("highlightable",false)
		 selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	     	.duration(250)
	     	.attr("r", function(d) { 
	     		var c = 0;
			 	if(d.value<=10 ) c = 8
			 		else c = d.value/4;
			 	return (c > visits_max ? visits_max/4 : c);} )
		 .style("fill", function(d) { return color(1); });
		 
	 })
	.on("mouseover", function(d) {
		var edges = selectArcs(d);
		edges.attr("marker-end", "url(#Triangle)");
		edges.style("stroke", "blue");
		edges.classed("highlightable",true)
		
		selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	    	.duration(250)
	    	.attr("r", function(d) { 
	    		var c = 0;
			 	if(d.value<=10 ) c = 8
			 		else c = d.value/4;
			 	return (c > visits_max ? visits_max/4+6 : c+6);} )
	    	.style("fill", function(d) { return "red"; });
		 console.log("Selected Circle: "+selectedCircle);
	});
	//.exit().remove()
	// .call(force.drag);
	//     .enter().append("svg:g")
	//     .attr("class", "node")
	//     .call(force.drag);
	 
	nodeEnter.append("nodetitle")
	   .text(function(d) { return "<b>Ressource:</b> "+ d.name+"<br /><br /> <b>Besuche</b>: "+d.value;});



   nodeEnter.append("svg:text")
   .attr("class", "valuetext")
   .attr("dx", -24)
   .attr("dy", ".35em")
   .text(function(d) { return d.value; });

   

   // Exit any old nodes.
   node.exit().remove();
   
   force
   .nodes(nodes)
   .links(links)
   .start();
	
}
    

		function tick() {
			vis.selectAll("line.link")
			 	.attr("x1", function(d) { return d.source.x; })
		        .attr("y1", function(d) { return d.source.y; })
		        .attr("x2", function(d) { return d.target.x; })
		        .attr("y2", function(d) { return d.target.y; });
		    
		    //node.attr("cx", function(d) { return d.x; })
		    //  	.attr("cy", function(d) { return d.y; });
		
			 vis.selectAll("g.node")
			 	.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
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
      
      
      
      $( "#distancesupportSlider" ).bind( "slide", function(event, ui) {
          force.linkDistance(ui.value);
          console.log("dist Value: "+ui.value);
          update2();
      });
      
      $( "#chargesupportSlider" ).bind( "slide", function(event, ui) {
          force.charge(-1*ui.value);
          update2();
      });
//      
//      $( "#supportSlider" ).bind( "slide", function(event, ui) {
//          filterNodes(ui.values[ 0 ],ui.values[ 1 ]);
//          update2();
//      });
      
      $('#supportSlider').mouseup(function(){
    	  filterNodes(visits_min,visits_max);
          update2();
      });
      
      
      $('#supportSlider').slider({

			//range: true,
			min : visits_min,
			max : visits_max,
			values :  [visits_min, visits_max],
			slide : function( event, ui ) {
							$( "#slider-label" ).html( "Visits: " + ui.values[ 0 ] + " - " + ui.values[ 1 ] );

							min = visits_min = ui.values[ 0 ];
							max = visits_max = ui.values[ 1 ];
						
							console.log("Min Value:" + min + "Max Value: "+max );
							console.log("Nodes: "+nodes);
						}
      
      	

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
