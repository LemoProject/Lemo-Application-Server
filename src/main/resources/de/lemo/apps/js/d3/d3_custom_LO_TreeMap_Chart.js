(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	  var data = [];
	  
	  data = d3custom.data;
	  
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data.</div>'));
	    	return;
	  }

      var w = 1280 - 80,
      h = 800 - 180,
      x = d3.scale.linear().range([0, w]),
      y = d3.scale.linear().range([0, h]),
      root,
      node;
	  
	  function color(name) {
		  if (name=="resource")
		  return "#ff8f1e ";
		  else if (name=="course")
		  return "#1f77b4";
		  else if (name=="forum")
		  return "#2ca02c";
		  else if (name=="question")
		  return "#9467bd";
		  else if (name=="quiz")
		  return "#d62728";
		  else if (name=="assignment")
		  return "#8c564b";
		  else if (name=="scorm")
		  return "#7f7f7f";
		  else if (name=="wiki")
		  return "#17becf";
		  return "#e377c2";
		}
	  
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
			.text(function(d) { return "<b>"+d.name+"</b>: <br /> Requests: "+d.requests+"<br /> User: "+d.user;});

      
    	  var textEnter = cell.append("svg:text")
    	  	.attr("y", function(d) { return d.dy / 2; })
    	  	.attr("dy", ".35em");
      
    	  textEnter.append("tspan")
    	  	.attr("class","tspan1")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	//.text(function(d) { d.tex = name(d); d.w = d.tex.length*2; return d.dx > d.w ? d.tex : d.tex.slice(0,d.tex.length/2); });
    	  	.text(function(d) { d.w = d.name.length*2; return d.dx > d.w ? name(d) : d.name.slice(0,name(d).length/2); });
     
    	  textEnter.append("tspan")
    	  	.attr("class","tspan2")
    	  	.attr("text-anchor", "middle")
    	  	.attr("x", function(d) { return d.dx / 2; })
    	  	.attr("dy", "1em")
    	  	//.text(function(d) { d.w = d.tex.length*2; return d.dx > d.w ? "" : d.tex.slice(d.tex.length/2,d.tex.length); });
    	  	.text(function(d) { if(!(node==root)) {d.w = d.name.length*2; return d.dx > d.w ? "" : d.name.slice(d.name.length/2,d.name.length)}; });
          
    	  objTypes=[];
      
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
    		  for (var i=0; i<objTypes.length;i++) {
    			  if (d.parent.name==objTypes[i]){
    				  alreadyIn=true;
    				  return "";
    			  }
    		  }
    		  if (!alreadyIn) {
    			  objTypes.push(d.parent.name);
    			  console.log(objTypes);
    			  return d.parent.name;
    		  }
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
      			//.text(function(d) { d.tex = name(d); d.w = d.tex.length*2; return d.dx > d.w ? d.tex : d.tex.slice(0,d.tex.length/2); });
      			.text(function(d) {  if(!(node==root)) {d.w = d.name.length*3; return d.dx > d.w ? name(d) : d.name.slice(0,d.name.length/2)} else return name(d);});
      
      		textEnter.select(".tspan2")
      			.attr("x", function(d) { return kx * d.dx / 2; })
      			.attr("dy", "1em")
      			//.text(function(d) { d.w = d.tex.length*2; return d.dx > d.w ? "" : d.tex.slice(d.tex.length/2,d.tex.length); });
      			.text(function(d) { if(!(node==root)) {d.w = d.name.length*3; return d.dx > d.w ? "" : d.name.slice(d.name.length/2,d.name.length)} else return ""; });
      
      		objTypes=[];
      		d3.event.stopPropagation();
      } 
      	
      	$('squaretitle').parent().tipsy({
      	    gravity: 'sw',
      	    html: true,
      	    title: function() { return $(this).find('squaretitle').text(); }
      	  });
	  
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
