
(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var w = 960, 
      h = 700,
      fill = d3.scale.category20(),
      selectedNodes,
  	  vis,
  	  root;
  
	  var startDistance = 200;
	  
	  
    var color = d3.scale.category20();

    vis = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h);
    
    vis.append("svg:defs")
    .append("svg:marker")
      .attr("id", "Triangle")
      .attr("viewBox", "0 0 10 10")
      .attr("refX", 50)
      .attr("refY", 5)
      .attr("markerUnits", "strokeWidth")
      .attr("markerWidth", 10)
      .attr("markerHeight", 10)
      .attr("orient", "auto")
      .append("svg:path")
      .attr("d", "M 0 0 L 10 5 L 0 10 z");
    
    var _nodes = d3custom.nodes,
    	_links = d3custom.links;
    	
  //check if we have values to work with
    if(!_nodes || !_links) {
    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
    	return;
    }
    
    
    //calculate initial multiplier for charge and gravity
   var k = Math.sqrt(_nodes.length / (w * h));
    
   var 	visits_min=1,
   		visits_max=500, 
		  minDistance=1,
		  maxDistance=400,
		  minCharge=0,
		  maxCharge=10;
    
   //Calculation of the optimal value for d3 Charge between nodes 
   	var optCharge = maxCharge/k;
   
   	//Calculation of the optimal value for d3 LinkDistance between nodes 
   	var optDistance=(maxDistance/(1/Math.sqrt(_links.length)))/_nodes.length;
	
   	var foci = [{x:10, y:10},{x:10+300,y:10},{x:800,y:600}];  

   	
   	console.log("OptLinkDistance with MaxDist: "+maxDistance+" and Nodes: "+_nodes.length+" and Links: "+_links.length+" = "+optDistance+" with k:"+k);
   	
    var force = d3.layout.force()
    .on("tick",function (e) {tick(e)})
    .distance(optDistance)
    .charge(-optCharge)
    .friction(.9)
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
    		 nodes.push({"id":i, "name":v.name, "value": v.value, "type": v.type, "focus": -1});
    	 });
    	 $.each(_links, function(i,v) {
    		 links.push({"source":nodes[v.source],"target":nodes[v.target],"value":v.value});
    	 });
    	 
    	 $( "#chargeslider-label" ).html( "Charge ("+minCharge+"-"+maxCharge+"): " + optCharge*k );
    	 
    	 $( "#distanceslider-label" ).html( "Distance ("+minDistance+"-"+maxDistance+"): " + optDistance );
    	 
    	 $( "#slider-label" ).html( "Visits: " + visits_min + " - " + visits_max );
    	 
    	 //$.each(links, function(i,v) {
        	// console.log("Links Array ["+i+"]:"+v.target.name +"-"+v.source.name);
        //});
    	 
    	 
    	 printLegend();
    	 update2();
    	 
    }

    
   var 	nodes = force.nodes(),
		links = force.links();
   

   
    
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
    		//console.log("Schreibe Nodes"+i);
    		if(v.value >= min && v.value <= max){
    			 nodes.push({"id":i, "name":v.name, "value": v.value, "type": v.type});
   		 		//console.log("Nodes"+nodesCounter+" --- "+nodes[nodesCounter].name +" ---ID:"+nodes[nodesCounter].id +" ---- "+v.name);
   		 		nodesCounter++;
    		} else {
				oldNodes.push({"id":i, "name":v.name, "value": v.value, "type": v.type});
				//console.log("OldNodes"+oldNodesCounter+" --- "+oldNodes[oldNodesCounter].name +" ---- "+v.name);
				oldNodesCounter++;
				}
	   	 });
    	
    	$.each(_links, function(i,v) {
    		//console.log("Bin in Links Schleife -- Target: "+ v.target +" Source: "+ v.source+" --- " +existNode(v.target,nodes)+"----"+existNode(v.source,nodes));
			if(existNode(v.target,nodes) && existNode(v.source,nodes)){
					
				 links.push({"source":findNode(v.source,nodes),"target":findNode(v.target,nodes),"value":v.value});
				// console.log("Links"+linkCounter+" --- "+links[linkCounter].source.name +" ---- "+v.source.name);
				 linkCounter++;
			}
  		});

	}    
    
function update2() {
	
	
	
	var link = vis.selectAll("line.link")
		.data(links);//, function(d) { return d.target.id; });
     
	 link.enter().insert("svg:line" , ".node")
     	 .attr("class", function(d) { return "link from-" + d.source.id +
                              " to-" + d.target.id; })
	     .attr("x1", function(d) { return d.source.x; })
	     .attr("y1", function(d) { return d.source.y; })
	     .attr("x2", function(d) { return d.target.x; })
	     .attr("y2", function(d) { return d.target.y; })
	     .style("stroke", function(d) { 
	    	 		if(d.value <= 10) return "lightgrey"
	    	 		else if(d.value > 10 && d.value <= 50) return "green"
	    	 			else if(d.value > 50 ) return "darkgreen"
	    	 				else return "lightgrey";
	    	 })
	     .style("stroke-width", function(d) { 
 	 		if(d.value <= 10) return 1
	 		else if(d.value > 10 && d.value <= 50) return 2
	 			else if(d.value > 50 ) return 3
	 				else return 1;
	     });
 
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
		 	if(d.value<=5 ) c = 5
		 	if(d.value>5 && d.value<=20) c = d.value;
		 	if(d.value>20) c = 20 + Math.sqrt(d.value);
		 	return (c > 100 ? 100 : c);
		 })
	 //.style("fill", function(d) { return color(1); })
	 .style("fill", function(d) { return fill(d.type); })
		 //.call(force.drag)
	 
	.on("click", function(d){
		var edges = vis.selectAll("line.link"),
			clickedEdges = selectArcs(d);
		
		edges.attr("marker-end", null);
		edges.style("stroke", function(d) { 
	 		if(d.value <= 10) return "lightgrey"
	 		else if(d.value > 10 && d.value <= 50) return "green"
	 			else if(d.value > 50 ) return "darkgreen"
	 				else return "lightgrey";
		});
		
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
		//console.log("NeighbourhoddString: "+neighbourSelectString);
		var neighbourNodes = vis.selectAll(neighbourSelectString);
		//console.log("NeighbourSelection"+neighbourNodes);
		
		vis.selectAll("text.nametext").remove();
		
		neighbourNodes.append("svg:text")
	   .attr("class", "nametext")
	   .attr("dx", function(d) { var c = 0;
							 if(d.value<=5 ) c = 5
							 	else if(d.value>5 && d.value<=20) c = parseInt(d.value);
							 		else if(d.value>20) c = 20 + Math.sqrt(d.value);
							 return (c > 100 ? 100+6 : c+6 ); 
							 }) //-24
	   .attr("dy", "1em")
	   .text(function(d) { return d.name; });
		
		focus(d);
		
		
		//console.log("Number of neighbours: "+neighbours.length);
	})
	.on("mouseout", function(d) {
		 var edges = vis.selectAll( "line.highlightable");
		 edges.attr("marker-end", null);
		 edges.style("stroke", function(d) { 
 	 		if(d.value <= 10) return "lightgrey"
	 		else if(d.value > 10 && d.value <= 50) return "green"
	 			else if(d.value > 50 ) return "darkgreen"
	 				else return "lightgrey";
		 });
		 edges.classed("highlightable",false)
		 selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	     	.duration(250)
	     	.attr("r", function(d) { 
	     		var c = 0;
			 	if(d.value<=5 ) c = 5
			 	if(d.value>5 && d.value<=20) c = d.value;
			 	if(d.value>20) c = 20 + Math.sqrt(d.value);
			 	return (c > 100 ? 100 : c);} )
		 .style("fill", function(d) { return fill(d.type); })
		 .style("stroke", "#333");
		 
	 })
	.on("mouseover", function(d) {
		var edges = selectArcs(d);
		edges.attr("marker-end", "url(#Triangle)");
		//edges.style("stroke", "red");
		edges.classed("highlightable",true)
		
		selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	    	.duration(250)
	    	.attr("r", function(d) { 
	    		var c = 0;
			 	if(d.value<=5 ) c = 5
			 	else if(d.value>5 && d.value<=20) c = parseInt(d.value);
			 		else if(d.value>20) c = 20 + Math.sqrt(d.value);
			 	return (c > 100 ? 100+6 : c+6 );} )
	    	//.style("fill", function(d) { return "red"; })
	    	.style("stroke", "red");
		 console.log("Selected Circle: "+selectedCircle);
	});
	
	nodeEnter.append("nodetitle")
	   .text(function(d) { return "<b>Ressource:</b> "+ d.name+"<br /><br /><b>Lernobjekttyp:</b> "+ d.type+"<br /><br /> <b>Besuche</b>: "+d.value;});



//   nodeEnter.append("svg:text")
//   .attr("class", "valuetext")
//   .attr("dx", function(d) { var c = 0;
//							 if(d.value<=5 ) c = 5
//							 	else if(d.value>5 && d.value<=20) c = parseInt(d.value);
//							 		else if(d.value>20) c = 20 + Math.sqrt(d.value);
//							 return (c > 100 ? 100+6 : c+6 ); 
//							 }) 
//   .attr("dy", "-.35em")
//   .text(function(d) { return d.value; });

   

   // Exit any old nodes.
   node.exit().remove();
   
   force
   .nodes(nodes)
   .links(links)
   .start();
   

}
    

function focus(d) {
	  if (d.focus==1) {
	      nodes.forEach(function (o) {
	      o.focus=-1;});
	      force.distance(optDistance)
	    	 .charge(-optCharge);
		
	  }
	  else {
	    nodes.forEach(function (o) {
	      o.focus=0;});
	    force.distance(300)
   	 		 .charge(-200);
	    d.focus=1;
	    for (var i=0; i<links.length; i++) {
	      if (links[i].target.id==d.id) {
	        links[i].source.focus=2;
	      }
	      else if (links[i].source.id==d.id) {
	        links[i].target.focus=2;
	      }
	    }
	  }
	  force.start();
	}




	function printLegend() {
    		   vis.append("svg:rect")
    		   .attr("x", (w/4)*3 - 20)
    		   .attr("y", 50)
    		   .attr("stroke", "lightgrey")
    		   .attr("height", 1)
    		   .attr("width", 40);

    		vis.append("svg:text")
    		   .attr("x", 30 + (w/4)*3)
    		   .attr("y", 55)
    		   .text("1 to 10 requests");

    		vis.append("svg:rect")
    		   .attr("x", (w/4)*3 - 20)
    		   .attr("y", 80)
    		   .attr("stroke", "green")
    		    .attr("fill", "green")
    		   .attr("height", 1)
    		   .attr("width", 40);

    		vis.append("svg:text")
    		   .attr("x", 30 + (w/4)*3)
    		   .attr("y", 85)
    		   .text("11 to 50 requests")
    		   
    		   vis.append("svg:rect")
    		   .attr("x", (w/4)*3 - 20)
    		   .attr("y", 110)
    		   .attr("stroke", "darkgreen")
    		   .attr("fill", "darkgreen")
    		   .attr("height", 2)
    		   .attr("width", 40);
    		
    		vis.append("svg:text")
	 		   .attr("x", 30 + (w/4)*3)
	 		   .attr("y", 115)
	 		   .text("More than 50 requests")
    		
    	}

		function tick(e) {
			
			var k = .5 * e.alpha;
			
			vis.selectAll("line.link")
			 	.attr("x1", function(d) { return d.source.x; })
		        .attr("y1", function(d) { return d.source.y; })
		        .attr("x2", function(d) { return d.target.x; })
		        .attr("y2", function(d) { return d.target.y; });
		    
			var nPos = 0,
				nAmount = 0;
			
			
			nodes.forEach(function(o,i) {
				if(o.focus == 2) nAmount++;	
			}) 	  
			  
			
			nodes.forEach(function(o,i) {
				  if (o.focus==1) {
				  o.y += (foci[0].y - o.y) * k;
				  o.x += (foci[0].x - o.x) * k;
				  }
				  else if (o.focus==2) {
				  o.y += (foci[1].y+nPos - o.y) * k;
				  o.x += (foci[1].x-nPos - o.x) * k;
				  
				  var delta = 260/nAmount; 
				  nPos+=(delta);
				  console.log("Delta:"+delta)
				  }
				  else if (o.focus==0) {
					  o.y += (foci[2].y - o.y) * k;
					  o.x += (foci[2].x - o.x) * k;
					  }
				  });
			
		    //node.attr("cx", function(d) { return d.x; })
		    //  	.attr("cy", function(d) { return d.y; });
		
			 vis.selectAll("g.node")
			 	.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
		    /**node.attr("transform", function(d) { return "translate(" + (1.3*d.x) + "," + (20*d.value) + ")"; });*/
		    
		   
		  };


        function selectArcs(d) {
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
     
      $('#supportSlider').mouseup(function(){
    	  filterNodes(visits_min,visits_max);
          update2();
      });
      
      
      $('#supportSlider').slider({

			range: true,
			min : visits_min,
			max : visits_max,
			values :  [visits_min, visits_max],
			slide : function( event, ui ) {
							$( "#slider-label" ).html( "Visits: " + ui.values[ 0 ] + " - " + ui.values[ 1 ] );

							min = visits_min = ui.values[ 0 ];
							max = visits_max = ui.values[ 1 ];
						
//							console.log("Min Value:" + min + "Max Value: "+max );
//							console.log("Nodes: "+nodes);
						}
      
      	

		});
      
     
      
      $('#distancesupportSlider').slider({

  	  		range: false,
			min : minDistance,
			max :  maxDistance,
			value : optDistance,
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
