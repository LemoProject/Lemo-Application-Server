(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	  var data = [];
	  
	  data = d3custom.data;
	  
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }

      var w = 980 - 80,
      h = 800 - 180,
      x = d3.scale.linear().range([0, w]),
      y = d3.scale.linear().range([0, h]),
      textLengthMulti = 4.5,
      root,
      node;
      
      if ($("#viz").width() < 920)
      	w=$("#viz").width()
      
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
      
//      var svg = d3.select("#viz").append("div")
//      	.style("width", w + "px")
//      	.style("height", h + "px")
//      	.append("svg:svg")
//      	.attr("width", w)
//      	.attr("height", h)
//      	.append("svg:g")
//      	.attr("transform", "translate(.5,.5)");
      
      //d3.json("TreeMap_Example_big.json", function(data) {
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
			.text(function(d) { return "<b>"+d.name+"</b>: <br /> Activities: "+d.requests+"<br /> User: "+d.user+" </br>"+Math.round(d.value/data.value*100*10)/10+" %";});

      
    	  var textEnter = cell.append("svg:text")
    	  	.attr("y", function(d) { return d.dy / 2; })
    	  	.attr("dy", ".35em");
      
    	  textEnter.append("tspan")
    	  	.attr("class","tspan1")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	.text(function(d) { if (!d.requests==0){ d.w = d.name.length*textLengthMulti; return d.dx > d.w ? name(d) : d.name.slice(0,name(d).length/2); }})
    	  	.style("opacity", function(d) {return getOpacity(d);});
    	  
    	  textEnter.append("tspan")
    	  	.attr("class","tspan2")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	.attr("dy", "1em")
    	    .text(function(d) { if (!d.requests==0){ if(!(node==root)) {d.w = d.name.length*textLengthMulti; return d.dx > d.w ? "" : d.name.slice(d.name.length/2,d.name.length)};} })
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


      //});
      
      function name(d) {
    	  var alreadyIn = false;
    	  if ((node == root)) {
    	      console.log(data);
    		  var biggest = true;
    		  for (var i=0; i<d.parent.children.length; i++) {
    		  	if (d.area < d.parent.children[i].area) {
    		  	  biggest=false;
    		  	}
    		  }
    		  if (biggest)
    		  	return Math.round(d.parent.value/data.value*100*10)/10+" %";
    		  else
    		  	return "";
    	  }
    	  else
    		  return d.name;
      	}
      
      	function user(d) {
      		return d.user;
      	}
      	
      	function requests(d) {
      		return d.requests;
      	}
      	
      	function zoom(d) {
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
      			.text(function(d) {  if(!(node==root)) {d.w = d.name.length*textLengthMulti; return d.dx > d.w ? name(d) : d.name.slice(0,d.name.length/2)} else return name(d);})
      			.style("opacity", function(d) { return getOpacity(d);});
             
      		textEnter.select(".tspan2")
      			.attr("x", function(d) { return kx * d.dx / 2; })
      			.attr("dy", "1em")
      			.text(function(d) { if(!(node==root)) {d.w = d.name.length*textLengthMulti; return d.dx > d.w ? "" : d.name.slice(d.name.length/2,d.name.length)} else return ""; })
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
