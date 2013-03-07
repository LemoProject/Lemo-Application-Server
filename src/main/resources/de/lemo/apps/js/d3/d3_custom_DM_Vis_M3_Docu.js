(function(d3custom, $, undefined) {

  d3custom.run = function() {


  // setting variables
  // amt is the amount of paths per page (calculated later, but max 24)
  // maxPos is the maximum length of our paths
  // page is the current page
  // pages is the amount of pages
  // page/pages is calculated later on, but minimum 1
	var w = 920,
    h = 500,
    amt = 24,
    maxPos = 11,
    fill = d3.scale.category20(),
    nodes = [],
    links = [],
    foci = [];
	 var page = 1,
	    pages = 1;
	
	// assigning nodes and links to variables
	var _nodes = d3custom.nodes,
		  _links = d3custom.links;
		
  // function for unified colors in all visualizations
	function color(name) {
    if (name == "resource")
      return "#ff8f1e ";
    else if (name == "course")
      return "#1f77b4";
    else if (name == "forum")
      return "#2ca02c";
    else if (name == "question")
      return "#9467bd";
    else if (name == "quiz")
      return "#d62728";
    else if (name == "assignment")
      return "#8c564b";
    else if (name == "scorm")
      return "#7f7f7f";
    else if (name == "wiki")
      return "#17becf";
    return "#e377c2";
  }
	  
	  
	//Bind slide event to update minSup Value in front end
	$( "#minSup-slider" ).bind( "slide", function(event, ui) {
		$( "#minSupSlider-label" ).html( "Minimum Support (0.1 - 1): " + ui.value/10 );
	});

	//Bind slide event to update pathlength Value in front end
	$( "#pathLength-slider" ).bind( "slide", function(event, ui) {
		$( "#pathLengthSlider-label" ).html( "Path Length (1 - 200): " + ui.values[0] +" - "+ui.values[1]  );
	});

	//check if we have values to work with
	if(!_nodes || !_links) {
	 	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	 	return;
	}

  // calculate the amount of paths per page and the amount of pages by repeatedly
  // dividing the total amount of paths by the amount of pages until it is less than 24
	function calculate() {
    var amtPaths = _nodes[_nodes.length-1].pathId;
    var i = +amtPaths;
    while (i>24) {
      i = amtPaths/++pages;
    }
    amt=Math.round(i+0.5);
  }
    
  calculate();

  $("#pages").html(''+page+"/"+pages);

  // hide buttons if on first/last page
  if (page==1) $("#prev").hide();
  if (page==pages) $("#next").hide();

  // parsing our _nodes and _links arrays and looking if they are being displayed in the specific page
  // if they do they get pushed to nodes and links
  function init() {
   	var posCounter = 1;
   	var skippedCounter = 0;
		$.each(_nodes, function(i,v) {
      if (v.pathId < amt*(page-1)+1) {
        skippedCounter++;
      }
		if(v.pathId < amt*page && v.pathId >= amt*(page-1)) {
  			if (i >=1 && _nodes[i-1].pathId==_nodes[i].pathId) {
  				posCounter++;
  			} else {
  			  posCounter = 1;
  		  }
  	    nodes.push({"pid":v.pathId , "pos":posCounter, "name":v.title, "value": v.value, "type": v.type, "totalRequests": v.totalRequests, "totalUsers":v.totalUsers, "selected":false});
  		}
    });
		
	  if(_links.length){
	    $.each(_links, function(i,v) {
  			if(v.pathId < amt*page && v.pathId >= amt*(page-1))  {
	        links.push({"source" : nodes[v.source - skippedCounter],
	                    "target" : nodes[v.target - skippedCounter],
	                    "value" : v.value});
        }
  		});
	  } else {
   		links.push({"source":nodes[_links.source],"target":nodes[_links.target],"value":_links.value});
   	}
	}

  // setting the horizontal foci depending on amt
  for (var i=0; i<amt; i++) {
  	foci.push({x:20+(w-40)/(amt+1)*(i), y:20});
  }


  var vis = d3.select("#viz").append("svg:svg")
    .attr("width", w)
    .attr("height", h);

  var force = d3.layout.force()
    .friction(.85)
    .gravity(0)
    .linkStrength(0)
    .charge(0)
    .size([w, h])
  	.on("tick", function(e) {tick(e)});

  // called permanently by force
	function tick(e) {
	  // Push nodes toward their designated focus.
	  var k = .1 * e.alpha;
	  nodes.forEach(function(o, i) {
      // calculation of vertical position using foci + with the help of relative position in path (o.pos)
	    o.y += (foci[o.pid-(page-1)*amt-1].y - o.y + (o.pos-1)*350/maxPos + 20) * k;
	    // calculation of horizontal position using foci
	    o.x += (foci[o.pid-(page-1)*amt-1].x - o.x) * k;
	  });


   vis.selectAll("g.node")
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"});

  vis.selectAll("line.link")
      .attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });
	};

 	nodes = force.nodes(),
	links = force.links();

 	init();

 	var pathAmount = [];

  function restart() {
    
    var link = vis.selectAll("line.link")
      .data(links);
          
    link.enter().append("svg:line")
      .attr("class", "link")
      .style("stroke-width","1")
      .style("stroke","#000");
    
    link.exit().remove();
    
    var node = vis.selectAll("g.node")
       .data(nodes, function(d) { return d.id;});
    
    node.exit().remove();
    
    node.selectAll("circle")
      .attr("r",8)
      .style("fill", function(d) { return fill(d.name); })
      .style("stroke", function(d) { return d3.rgb(fill(1)).darker(2); })
      .style("stroke-width", 1.5)
    
    node.selectAll("text")
      .text(function(d) {return d.name});
    
    var nodeEnter = node.enter().append("svg:g")
      .attr("class", "node");
    
    nodeEnter.append("circle")
      .attr("class", "node")
      .attr("r",8)
      .attr("title",function(d){return d.name;})
      .attr("cx", function(o) {return 0;})
      .attr("cy", function(o) { return 0;})
      .on("click", function(d) {
      
        // function used for focusing/unfocusing a path
        
        // if the node clicked is already selected, it resets the foci,
        // deletes the names, and sets selected false
        
        if (d.selected == true) {
          for (var i=0; i<foci.length; i++)
            foci[i]=({x:20+(w-40)/(amt-1)*(i), y:20});
       	d3.selectAll("g.node text").text(function(o, i) {
          return "";
       	});
      	force.start();
      	d.selected=false;
    		}

        // if the node is not selected, it sets the name visible on
        // nodes with the same path id  and sets them selected aswell
        // then it resets the foci and calculates the foci new
        // depending on whether the path is on the right half or on the left
        
        else {
          d3.selectAll("g.node text").text(function(o, i) {
      	    return o.pid != d.pid ? "" : o.name;
          });
          nodes.forEach(function(o, i) {
      		  if (o.pid == d.pid) {
        		  o.selected=true;
            } else {
        		  o.selected=false;
      		  }
    		});
        		
    		for (var i=0; i<amt; i++) {
    		  foci[i]={x:20+(w-40)/(amt-1)*(i), y:20};
    		}

        var p = d.pid-(amt*(page-1))-1;
    		if (p >= amt/2) {
      	  for (var i=0; i<p; i++){
      		  foci[i].x=20+660/(amt-1)*i;
      	  }
      		for (var i=p; i<amt; i++) {
      		  foci[i].x=280+660/(amt-1)*i;
      		}
    	  } else {
      	  for (var i=0; i<p+1; i++) {
      	    foci[i].x=20+660/(amt-1)*i;
      	  }
      	  for (var i=p+1; i<amt; i++) {
      	    foci[i].x=280+660/(amt-1)*i;
      	  }
    	  }
    		force.start();
		    }
      })
      .style("fill", function(d) { return fill(d.name); })
      .style("stroke", function(d) { return d3.rgb(fill(1)).darker(2); })
      .style("stroke-width", 1.5);
    
    nodeEnter.append("svg:text")
      .attr("class", "nodetext")
      // calculating the x distance depending on the length of the name if it is displayed left of the node
      .attr("dx", function(d) {return d.pid-(amt*(page-1))-1 >= amt/2 ? -d.name.length*4.5 - 22 : "1.5em";})
      .attr("dy", ".3em")
      .text(function(d) { return ""});
          
    nodeEnter.append("nodetitle")
      .text(function(d) { return "<b>Support</b>: "+d.value});

    force.start();
  }

  $('nodetitle').parent().tipsy({
    gravity: 'sw',
    html: true,
    title: function() { return $(this).find('nodetitle').text(); }
  });

  // function used for changing pages, true -> next page, false -> previous page
  // resets node and links array, initializes them, resets foci and restarts the graph
  change_page = function(bool) {
    if (bool) {page++;}
    else {page--;}
    if (page==1) $("#prev").hide();
    else $("#prev").show();
    if (page==pages) $("#next").hide();
    else $("#next").show();
    $("#pages").html(''+page+"/"+pages);
    nodes = [];
    links = [];
    init();
    force.nodes(nodes);
    force.links(links);
    for (var i=0; i<amt; i++) {
      foci[i]={x:20+(w-40)/(amt-1)*(i), y:20};
    }
    restart();
    d3.selectAll("g.node text").text(function(o, i) {
      return "";
    });
  }
  
  restart();

};



})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
