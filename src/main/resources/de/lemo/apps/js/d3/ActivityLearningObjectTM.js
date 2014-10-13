(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	  var data = [];
	  
	  data = d3custom.data;
	  var locale = d3custom.locale;
	  var color = function(d) { 
		  return hashColor(d); 
		  };
	  
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }

      var w = 980 - 80;
      if ($("#viz").width() < 920)
      	w=$("#viz").width();
      var h = 800 - 180,
      x = d3.scale.linear().range([0, w]),
      y = d3.scale.linear().range([0, h]),
      textLengthMulti = 4.5,
      namesList = [],
      parentNamesList=[],
      root,
      node;     
      
      var treemap = d3.layout.treemap()
      	.round(false)
      	.size([w, h])
      	.sticky(true)
      	.value(function(d) { return d.requests; });
      
      var svg = d3.select("#viz").append("svg:svg")
      .attr("width", w)
      .attr("height", h)
      .append("svg:g")
      .attr("transform", "translate(.5,.5)");
      
    	  node = root = data;
    	  var nodes = treemap.nodes(root)
    	  		.filter(function(d) { return !d.children; });
    	  
    	  var cell = svg.selectAll("g")
    	  		.data(nodes)
    	  		.enter().append("svg:g")
    	  		.attr("class", "cell")
    	  		.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; })
    	  		.on("click", function(d) { return zoom(node == d.parent ? root : d.parent); });
    	  
    	  cell.append("svg:rect")
    	  	.attr("width", function(d) { return d.dx - 1; })
    	  	.attr("height", function(d) { return d.dy - 1; })
    	  	.style("fill", function(d) { return color(d.parent.name); });
    	  
    	  cell.append("squaretitle")
			.text(function(d) { return "<b>"+d.name+"</b>: <br /> "+locale.activities+": "+d.requests+"<br /> "+locale.user+": "+d.user+" </br>"+Math.round(d.value/data.value*100*10)/10+" %";});

      
    	  var textEnter = cell.append("svg:text")
    	  	.attr("y", function(d) { return d.dy / 2; })
    	  	.attr("dy", ".35em");
      
    	  textEnter.append("tspan")
    	  	.attr("class","tspan1")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	.text(function(d) { return name(d,true); })
    	  	.style("opacity", function(d) {return getOpacity(d);});
    	  
    	  textEnter.append("tspan")
    	  	.attr("class","tspan2")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	.attr("dy", "1em")
    	    .text(function(d) { return name(d,false); })
    	    .style("opacity", function(d) {return getOpacity(d);});
                
    	 // objTypes=[];
      
    	  d3.select(window).on("click", function() { zoom(root); });

       
    	  d3.select("#Requests").on("click", function() {
      
    		  treemap.value(requests).nodes(root);
    		  zoom(node);

    		  d3.select("#Requests").classed("active", true);
    		  d3.select("#User").classed("active", false);
    	  });

    	  d3.select("#User").on("click", function() {
    		  treemap.value(user).nodes(root);
    		  zoom(node);

    		  d3.select("#Requests").classed("active", false);
    		  d3.select("#User").classed("active", true);
    	  });

      
      function name(d, tspan1) {
    	   if ((node == root)) {
    	      console.log(d);
    		  var biggest = true;
    		  for (var i=0; i<d.parent.children.length; i++) {
	    		if (d.area < d.parent.children[i].area) {
	    		  biggest=false;
	    		}
    		  }
    		  if (biggest) {
    		    if (tspan1) {
    		    	if (parentNamesList.indexOf(d.parent.name)==-1) {
	    		    	namesList.push(d.name);
	    		    	parentNamesList.push(d.parent.name);
	    		  		return d.parent.name;
	    		  	}
    		  	}
    		  	else {
    		  	  if (namesList.indexOf(d.name)!=-1) {
    		  		return locale.activities+": "+d.parent.value+" / "+Math.round(d.parent.value/data.value*100*10)/10+" %";
    		  	  }
    		  	}
    		  }
    	  }
    	  else {
    	  namesList=[];
    	  parentNamesList=[];
    	  	if ( d.dx > d.name.length*textLengthMulti) {
    			if (tspan1)
    		  		return d.name.slice(0,d.name.length/2);
    		  	else
    		  		return d.name.slice(d.name.length/2,d.name.length);
    		}
    		else
    			if (tspan1)
    		   		return d.name;
    		  	
    	  }
    	  return "";
      	}
      
      	function user(d) {
      		return d.user;
      	}
      	
      	function requests(d) {
      		return d.requests;
      	}
      	
      	function zoom(d) {
    	  	nameslist=[];
    	  	parentNamesList=[];
      		node = d;
      		var kx = w / d.dx, ky = h / d.dy;
      		x.domain([d.x, d.x + d.dx]);
      		y.domain([d.y, d.y + d.dy]);
      		
      		var t = svg.selectAll("g.cell").transition()
      			.duration(d3.event.altKey ? 7500 : 750)
      			.attr("transform", function(d) { return "translate(" + x(d.x) + "," + y(d.y) + ")"; });
      		
      		t.select("rect")
      			.attr("width", function(d) { return kx * d.dx - 1; })
      			.attr("height", function(d) { return ky * d.dy - 1; });
      		
//      		 var cell = svg.selectAll("g.rect")
//      		 	.append("nodetitle")
//      			.text(function(d) { return "<b>Course</b>: "+name(d);});
	
     
      		var textEnter = t.select("text")
      			.attr("x", function(d) { return kx * d.dx / 2; })
      			.attr("y", function(d) { return ky * d.dy / 2; });
      
      		textEnter.select(".tspan1")
      			.attr("x", function(d) { return kx * d.dx / 2; })
      			.text(function(d) {  return name(d,true); })
      			.style("opacity", function(d) { return getOpacity(d);});
             
      		textEnter.select(".tspan2")
      			.attr("x", function(d) { return kx * d.dx / 2; })
      			.attr("dy", "1em")
      			.text(function(d) { return name(d,false); })
      			.style("opacity", function(d) { return getOpacity(d); });
               
      		objTypes=[];
      		d3.event.stopPropagation();
      } 
      	
      	$('squaretitle').parent().tipsy({
      	    gravity: 'sw',
      	    html: true,
      	    title: function() { return $(this).find('squaretitle').text(); }
      	  });
      	
      	function getOpacity(d) {
            var opac = 0;
            if (d.dx > d.w) {
            	opac = 1;
            }	else {
            		if(d.dy < 20 ) return 0; 
            		var o = d.name.slice(0,d.name.length/2).length*textLengthMulti;
            		opac = d.dx > o ? 1 : 0;
            		o = d.name.slice(d.name.length/2,d.name.length).length*textLengthMulti;
            		opac = d.dx > o ? 1 : 0;
            	}
            return opac;
      	}
      	     	
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
