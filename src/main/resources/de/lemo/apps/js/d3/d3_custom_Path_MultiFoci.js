
(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  

    var w = 960,
        h = 700,
        selectedNodes,
  	    vis,
  	    root;
  
    var startDistance = 200;

    // function for unified colors in all visualizations
	  function customColor(name) {
		  if (name=="Resource")
		  return "#ff8f1e ";
		  else if (name=="Course")
		  return "#1f77b4";
		  else if (name=="Forum")
		  return "#2ca02c";
		  else if (name=="Question")
		  return "#9467bd";
		  else if (name=="Quiz")
		  return "#d62728";
		  else if (name=="Assignment")
		  return "#8c564b";
		  else if (name=="Scorm")
		  return "#7f7f7f";
		  else if (name=="Wiki")
		  return "#17becf";
		  return "#e377c2";
		}

	  
   var outer = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h)
      .attr("pointer-events", "all");;
    
   //adding zoom / drag layer   
   var vis = outer.append('svg:g')
    	.call(d3.behavior.zoom()
    			.scaleExtent([0.1,2])
    			.on("zoom", rescale))
      	.on("dblclick.zoom", null)
    .append('svg:g')
      .on("mousedown", mousedown)
      .on("touchstart", mousedown);
    
   vis.append('svg:rect')
		.attr("class", "parent")
		.attr('width', w)
		.attr('height', h)
		 .on("click", function(d){drawGraph();});
    
    vis.append("svg:defs")
      .append("svg:marker")
      .attr("id", "Triangle")
      .attr("viewBox", "0 0 14 14")
      .attr("refX", 80)
      .attr("refY", 7)
      .attr("markerUnits", "userSpaceOnUse")
      .attr("markerWidth", 14)
      .attr("markerHeight", 14)
      .attr("orient", "auto")
      .append("svg:path")
      .attr("d", "M 0 0 L 14 7 L 0 14 z");
    
    var _nodes = d3custom.nodes,
        _links = d3custom.links;
    	
    // check if we have values to work with
    if(!_nodes || !_links) {
      $("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
    	return;
    }
    
    
    // calculate initial multiplier for charge and gravity
    var k = Math.sqrt(_nodes.length / (w * h));
    
    // setting up limiting variables for the presentation of the values
    var visits_min=1,
   		visits_max=500, 
		  minDistance=1,
		  maxDistance=300,
		  currentDistance=-1;
		  minCharge=0,
		  maxCharge=15000,
		  currentCharge=-1;
    
    // calculation of the optimal value for d3 Charge between nodes
   	var optCharge = maxCharge;
   
   	// calculation of the optimal value for d3 LinkDistance between nodes
   	var optDistance=(maxDistance/(1/Math.sqrt(_links.length)))/(_nodes.length/3);
	
    // check if optDistance is in rane
   	if (optDistance > maxDistance) optDistance = maxDistance;
   	if (optDistance < minDistance) optDistance = minDistance;
   	
   	// setting up foci for displaying a focused node (foci[0]), its neighbours (foci[1]) and all other nodes (foci[2])
  	var foci = [{x:w/2, y:h/2},{x:w/2,y:h/2},{x:900,y:300}];

    var force = d3.layout.force()
      .on("tick",function (e) {tick(e)})
      .distance(optDistance)
      .charge(-optCharge)
      .friction(.9)
      .gravity(1)
      .size([w, h]);

    this.addLink = function (source, target) {
      links.push({"source":findNode(source),"target":findNode(target)});
      update();
    }

    var findNode = function(id,mynodes) {
      for (var i in mynodes) {if (mynodes[i]["id"] == id) return mynodes[i];};
    }
    
    var findNeighbours = function(id,mylinks) {
    	var neighbours = {};
    	neighbours["0"] = []; // neighbor nodes
    	neighbours["1"] = []; // Non neighbor nodes
    	neighbours["2"] = []; // Links which have no connection to Node(id)
    	
    	// Adding neigbor Nodes and non neighbor links to array
    	$.each(mylinks, function(i,v) {
    		if (v.source.id == id ) neighbours["0"].push(v.target)
    			else if (v.target.id == id ) neighbours["0"].push(v.source)
    				else {
    					neighbours["2"].push(v);
    				}
        });
    	
    	// Adding non neigbor nodes to array
    	$.each(nodes, function(i,nodeValue) {
    		if( $.inArray(nodeValue, neighbours["0"]) == -1 && nodeValue.id != id)
    			 //return true;	
    			 neighbours["1"].push(nodeValue);
    	
    	});
      return neighbours;
    }

    var existNode = function(id,mynodes) {
        for (var i in mynodes) {if (mynodes[i]["id"] == id) return true; }
        return false;
    }
    
    // initialize by transfering elements from _nodes to nodes and _links to links
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

    	 currentCharge = -optCharge;
    	 currentDistance = optDistance;
    	 
    	 drawGraph();
    }
    
    
    var linkScale = d3.scale.linear()
    	.domain([1, 50])
    	.range([1, 7]);
    
    var	nodes = force.nodes(),
      	links = force.links();
    
    //init visualization
    init();
    
    force
    .nodes(nodes)
    .links(links)
    .start();
    
    // filter nodes by minimum and maximum value and then check which links are still existent
    function filterNodes(min, max){
		nodes = [];
		links = [];
		var tempNodes = [],
			oldNodes = [],
			oldNodesCounter = 0,
			nodesCounter = 0,
			linkCounter = 0;
		
    	$.each(_nodes, function(i,v) {
    		if(v.value >= min && v.value <= max){
    			nodes.push({"id":i, "name":v.name, "value": v.value, "type": v.type});
   		 		nodesCounter++;
    		} else {
				  oldNodes.push({"id":i, "name":v.name, "value": v.value, "type": v.type});
				  oldNodesCounter++;
				}
	   	});
    	
    	$.each(_links, function(i,v) {
			if(existNode(v.target,nodes) && existNode(v.source,nodes)){
				 links.push({"source":findNode(v.source,nodes),"target":findNode(v.target,nodes),"value":v.value});
				 linkCounter++;
			}
  		});
    }

    function drawGraph() {
    	
      var link = vis.selectAll("line.link")
		    .data(links);

      link.enter().insert("svg:line" , ".node")
     	  .attr("class", function(d) { return "link linkId-"+d.source.id+"-"+d.target.id+" from-" + d.source.id + " to-" + d.target.id; })
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
	      .style("stroke-width", 0)
/*	    		function(d) {
 	 		    if(d.value <= 10) return 1
	 		    else if(d.value > 10 && d.value <= 50) return 2
	 			  else if(d.value > 50 ) return 3
	 				else return 1;
	      });*/
 
      link.exit().remove();

      var node = vis.selectAll("g.node")
        .data(nodes, function(d) { return d.id;})

      var nodeEnter = node.enter().append("g")
        .attr("class", function(d,i) { return "node gnodeId-" + i; })
        //.call(force.drag);
 
 
	    nodeEnter.append("circle")
        .attr("class",  function(d,i) { return "node nodeId-" + i; })
	      .attr("r", function(d) {
		 	    var c = 0;
		 	    if(d.value<=5 ) c = 5
		 	    if(d.value>5 && d.value<=20) c = d.value;
		 	    if(d.value>20) c = 20 + Math.sqrt(d.value);
		 	    return (c > 100 ? 100 : c);
		    })
	      .style("fill", function(d) { return customColor(d.type); })
        .on("click", function(d){
        
          // on click of a circle highlight the links to and from this circle
          // display text of the node and its neighbours and assign foci by calling focus(d)
        		
        	  force.stop();
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
		      //clickedEdges.style("stroke", "red");
		
		      var neighbours = findNeighbours(d.id,links);
		      var neighbourSelectString = "";
		      $.each(neighbours["0"], function(i,v) {
			      if(neighbourSelectString=="")
				    neighbourSelectString = neighbourSelectString+"g.gnodeId-"+v.id;
			      else neighbourSelectString = neighbourSelectString+", g.gnodeId-"+v.id;
		      });
		      var neighbourNodes = vis.selectAll(neighbourSelectString);
		      
		      //now we select all nodes which are no neighbor
		      var nonNeighbourSelectString = "";
		      $.each(neighbours["1"], function(i,v) {
			      if(nonNeighbourSelectString=="")
			    	  nonNeighbourSelectString = nonNeighbourSelectString+"circle.nodeId-"+v.id;
			      else nonNeighbourSelectString = nonNeighbourSelectString+", circle.nodeId-"+v.id;
		      });
		      var nonNeighbourNodes = vis.selectAll(nonNeighbourSelectString);

		      
		      
		    //now we select all links which are not directly connected
		      var nonNeighbourLinksSelectString = "";
		      $.each(neighbours["2"], function(i,v) {
			      if(nonNeighbourLinksSelectString=="")
			    	  nonNeighbourLinksSelectString = nonNeighbourLinksSelectString+"line.linkId-"+v.source.id+"-"+v.target.id;
			      else nonNeighbourLinksSelectString = nonNeighbourLinksSelectString+", line.linkId-"+v.source.id+"-"+v.target.id;
		      });
		      var nonNeighbourLinks = vis.selectAll(nonNeighbourLinksSelectString);

		      
	       	vis.selectAll("text.nametext").remove();
		
	       	console.log("NonNeighborNodes: "+nonNeighbourNodes);
	       	
	       	nonNeighbourLinks.transition()
       			.duration(1500)
       			.style("stroke-width", function(d) {return 0;})
       			.remove();
	       	
	       	nonNeighbourNodes.transition()
	       		.duration(1500)
	       		.attr("r", function(d) {return 0;})
	       		.each("end", function(d) {force.start()})
	       		.remove();
	       	
	       	
		      neighbourNodes.append("svg:text")
	          .attr("class", "nametext")
	          .attr("dx", function(d) {
              var c = 0;
						  if(d.value<=5 ) c = 5
						  else if(d.value>5 && d.value<=20) c = parseInt(d.value);
						  else if(d.value>20) c = 20 + Math.sqrt(d.value);
              return (c > 100 ? 100+6 : c+6 );
							})
            .attr("dy", "1em")
	          .text(function(d) { return d.name; });

		      focus(d);
		      
		     
		      
		    })
		    
	      .on("mouseout", function(d) {
	      
	      // undo hightlighting of connected links and the node
	      
		      var edges = vis.selectAll( "line.highlightable");
		      edges.attr("marker-end", null);
		      edges.style("stroke", function(d) {
 	 		      if(d.value <= 10) return "lightgrey"
	 		      else if(d.value > 10 && d.value <= 50) return "green"
	 			    else if(d.value > 50 ) return "darkgreen"
	 				  else return "lightgrey";
		      })
		      .style("stroke-width", 0);
		      edges.classed("highlightable",false)
		      selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	     	    .duration(250)
	     	    .attr("r", function(d) {
	     		    var c = 0;
			 	      if(d.value<=5 ) c = 5
			 	      if(d.value>5 && d.value<=20) c = d.value;
			 	      if(d.value>20) c = 20 + Math.sqrt(d.value);
			 	      return (c > 100 ? 100 : c);
            })
		        .style("stroke", "#333");
        })
	      .on("mouseover", function(d) {
	      
	      // hightlight node and connected links on mouseover
	      
		      var edges = selectArcs(d);
		      edges
		      	.attr("marker-end", "url(#Triangle)")
		      	.classed("highlightable",true)
		      	.style("stroke-width", function(l) {return linkScale(l.value)})
		      	.each(function(l) {return console.log("link weight: "+ l.value+ " SourceId: "+l.source.id+" TargetId: "+l.target.id)})
		
		      selectedCircle = vis.selectAll("circle.nodeId-"+d.index).transition()
	    	    .duration(250)
	    	    .attr("r", function(d) {
	    		    var c = 0;
			 	      if(d.value<=5 ) c = 5
			 	      else if(d.value>5 && d.value<=20) c = parseInt(d.value);
			 		    else if(d.value>20) c = 20 + Math.sqrt(d.value);
			 	      return (c > 100 ? 100+6 : c+6 );
            })
            
        });
	
	    nodeEnter.append("nodetitle")
	      .text(function(d) { return "<b>Learning object:</b> "+ d.name+"<br /><br /><b>Lerning object type:</b> "+ d.type+"<br /><br /> <b>Activities</b>: "+d.value;});

      // Exit any old nodes.
      node.exit().remove();
   
     
    }
    
    // change the focus of the node clicked and neighbour nodes depending on their current focus state
    function focus(d) {
	    if (d.focus==1) {
	      nodes.forEach(function (o) {
	        o.focus=-1;
        });
	      force.distance(optDistance)
	    	  .charge(-optCharge);
		  } else {
	      nodes.forEach(function (o) {
	        o.focus=0;
        });
	      force.distance(currentDistance)
   	 		  .charge(currentCharge);
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
	    //force.start();
	  }
	  
	  function printLegend() {
      vis.append("svg:rect")
    	  .attr("x", (w/4)*2.5 - 20)
    		.attr("y", 50)
    		.attr("stroke", "lightgrey")
    		.attr("height", 2)
    		.attr("width", 30);

    	vis.append("svg:text")
    	  .attr("x", 30 + (w/4)*2.5)
    	  .attr("y", 55)
    	  .text("1 to 10 interactions");

    	vis.append("svg:rect")
    	  .attr("x", (w/4)*2.5 - 20)
    	  .attr("y", 80)
    	  .attr("stroke", "green")
    	  .attr("fill", "green")
    	  .attr("height", 2)
    	  .attr("width", 30);

    	vis.append("svg:text")
    	  .attr("x", 30 + (w/4)*2.5)
    	  .attr("y", 85)
    	  .text("11 to 50 interactions")
    		   
    	vis.append("svg:rect")
    	  .attr("x", (w/4)*2.5 - 20)
    	  .attr("y", 110)
    	  .attr("stroke", "darkgreen")
    	  .attr("fill", "darkgreen")
    	  .attr("height", 2)
    	  .attr("width", 30);
    		
    	vis.append("svg:text")
	 		  .attr("x", 30 + (w/4)*2.5)
	 		  .attr("y", 115)
	 		  .text("More than 50 interactions")
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
			
      // calculate how many neighbours the focused node has
			nodes.forEach(function(o,i) {
				if(o.focus == 2) nAmount++;	
			}) 	  

      // display nodes according to focus
			nodes.forEach(function(o,i) {

        if (o.focus==1) {
				  o.y += (foci[0].y - o.y) ;
				  o.x += (foci[0].x - o.x) ;
				}

//				else if (o.focus==2) {
//				  o.y += (foci[1].y+nPos - o.y) * k;
//				  o.x += (foci[1].x-nPos - o.x) * k;
//				  
//				  var delta = 260/nAmount; 
//				  nPos+=(delta);
//				}
//
//        else if (o.focus==0) {
//					  o.y += (foci[2].y - o.y) * k;
//					  o.x += (foci[2].x - o.x) * k;
//			  }
			});

			vis.selectAll("g.node")
			  .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

		};

    // return links from and to the node ds
    function selectArcs(d) {
      return vis.selectAll( "line.to-" + d.index + ",line.from-" + d.index)
    }
    
    
    function rescale() {
    	  trans=d3.event.translate;
    	  scale=d3.event.scale;
    	  current_scale= scale;
    	  scroll= vis.attr("transform",
    	      	"translate(" + trans + ")"
    	      	+ " scale(" + scale + ")");
    	  console.log("SCALE: "+ scale + " TRANS: "+ trans);

    }
    
    function mousedown() {
    	
    	    // allow panning if nothing is selected
    	    vis.call(d3.behavior.zoom().on("zoom"), rescale);
    	    return;
    	  
    	}

        
      
    $('nodetitle').parent().tipsy({
	    gravity: 'sw',
	    html: true,
	    title: function() { return $(this).find('nodetitle').text(); }
	  });
      
      
      
    $( "#distancesupportSlider" ).bind( "slide", function(event, ui) {
      currentDistance=ui.value;
    	force.linkDistance(currentDistance);
      drawGraph();
    });
      
    $( "#chargesupportSlider" ).bind( "slide", function(event, ui) {
      currentCharge=-1*(optCharge/ui.value);
      force.charge(currentCharge);
      drawGraph();
    });
     
    $('#supportSlider').mouseup(function(){
      filterNodes(visits_min,visits_max);
      
      drawGraph();
      force.start();
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
			}
    });
      
     
      
    $('#distancesupportSlider').slider({
    	range: false,
			min : minDistance,
			max :  maxDistance,
			value : optDistance,
			slide : function( event, ui ) {
				$( "#distanceslider-label" ).html( "Distance ("+minDistance+"-"+maxDistance+"): " + ui.value );
   		}
    });
     
    $('#chargesupportSlider').slider({
   		range: false,
			min : minCharge,
			max :  maxCharge,
			value: maxCharge,
			slide : function( event, ui ) {
			  $( "#chargeslider-label" ).html( "Charge ("+minCharge+"-"+maxCharge+"): " + ui.value );
    	}
    });

  };
  
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
