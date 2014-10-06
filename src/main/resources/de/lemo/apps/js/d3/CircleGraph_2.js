(function(d3custom, $, undefined) {


  d3custom.run = function() {	  
	  
  var nodes = d3custom.nodes,
      links = d3custom.links;

    	
  //check if we have values to work with
    if(!nodes || !links) {
    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
    	return;
    }
    
    var data = [];

    //transforming data into the right format (name,size,imports[],type)
    for (var i=0; i<nodes.length; i++) {
        var imports=[];
        for (var j=0; j<links.length; j++) {
          if (links[j].source==i && nodes[links[j].target].name != nodes[i].name) {
            var redundant = false;
            for (var p=0; p<imports.length; p++) {
              if (imports[p] == validString(nodes[links[j].target].name))
                redundant = true;
            }
            if (!redundant)
              imports.push(validString(nodes[links[j].target].name));
          }
        }
        data.push({"name":validString(nodes[i].name),"size":nodes[i].value,"imports":imports,"type":nodes[i].type});
      }
    
   var  w = $("#viz").width(),
    h = 700,
    rx = w / 2 - 50,
    ry = h / 2,
    m0,
    rotate = 0;

  var splines = [];

  var cluster = d3.layout.cluster()
    .size([360, ry - 120]);

  var bundle = d3.layout.bundle();

  var line = d3.svg.line.radial()
    .interpolate("bundle")
    .tension(.85)
    .radius(function(d) { return d.y-20; })
    .angle(function(d) { return d.x / 180 * Math.PI; });

  //Chrome 15 bug: &lt;http://code.google.com/p/chromium/issues/detail?id=98951&gt;
  var div = d3.select('#viz');

  var svg = div.append('svg:svg')
  .attr("id","vizsvg")
  .attr("width", w)
  .attr("height", h)
  .append("svg:g")
  .attr("transform", "translate(" + rx + "," + ry + ")");

  svg.append("svg:path")
    .attr("class", "arc")
    .attr("d", d3.svg.arc().outerRadius(ry - 120).innerRadius(0).startAngle(0).endAngle(2 * Math.PI));


    var test =  packages.root(data).children;
    
    test.forEach(function(d) {
  	  if (d.imports!=undefined)
  	  	d.amt=calculateAllLinks(d);
    });

      cluster.sort(sortfunction4);

  var nodes = cluster.nodes(packages.root(data)),
    links = packages.imports(nodes),
    splines = bundle(links);


  var both = [];
  for (var i=0; i<links.length; i++) {
    for (var j=0; j<links.length; j++) {
      if (i!=j) {
        if (links[i].source.name == links[j].target.name && links[i].target.name == links[j].source.name) {
          both.push(links[j]);
        }
      }
    }
  }

  function calculateAllLinks(d) {
    amt = d.imports.length;
     for (var i=1; i<test.length; i++) {
      for (var j=0; j<test[i].imports.length; j++) {
       if (test[i].imports[j]==d.name)
       amt++;
       }
      }
      return amt;
    }
    
    
    
    
    var arc = d3.svg.arc()
        .innerRadius(ry - 135)
        .outerRadius(ry - 125)
        .startAngle(function(d,i) { return (2*Math.PI)/(nodes.length-1)*(i-1)})
        .endAngle(function(d,i) {return (2*Math.PI)/(nodes.length-1)*(i)});


      var arcs = svg.selectAll("path")
        .data(nodes)
        .enter().append("path")
        .attr("d",arc)
        .attr("class","typeArc");

      var arcs2 = svg.selectAll("path.typeArc")
      //.transition()
      //.duration(750)
      .attr("fill",function(d,i) {return hashColor(nodes[0].children[i].name)});

  var path = svg.selectAll("path.linkCG")
      .data(links)
    .enter().append("svg:path")
      .attr("class", function(d) {
      var bool=false;
      for(var i=0; i<both.length; i++) {
      if (both[i].source.name==d.source.name && both[i].target.name==d.target.name) {
      bool=true;
      }
      }
      if (bool) {
      return "linkCG srctar-" + d.source.key + " srctar-" + d.target.key;
      }
      else
      return "linkCG source-" + d.source.key + " target-" + d.target.key;})
      .attr("d", function(d, i) { return line(splines[i]); });

  var gNode = svg.selectAll("g.node")
  .data(nodes.filter(function(n) { return !n.children; }))
.enter().append("g")
  .attr("class", "node")
  .attr("id", function(d) { return "node-" + d.key; })
  .attr("transform", function(d) { return "rotate(" + (d.x - 90) + "), translate(" + d.y + ")"; })
 
  gNode.append("svg:text")
  .attr("class","circletext")
  .attr("dy", ".31em")
  .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
  .attr("transform", function(d) { return d.x < 180 ? null : "rotate(180)"; })
  .attr("font-size",function(d) {return 10+d.amt/10;})
  .on("mouseover", function(d) {mouseover(d);})
  .on("mouseout", function(d) {mouseout(d);})
  .text(function(d) { return d.key; });
 

//  d3.select("input[type=range]").on("change", function() {
//    line.tension(this.value / 100);
//    path.attr("d", function(d, i) { return line(splines[i]); });
//  });


  function mouse(e) {
    return [e.pageX - rx, e.pageY - ry];
  }


  function mouseover(d) {
    svg.selectAll("path.linkCG")
    .classed("invisible",true);
    
    svg.selectAll("text")
    .classed("invisible",true);
    
    svg.select("#node-"+d.name).select("text")
    .classed("invisible",false);

    svg.selectAll("path.linkCG.target-" + d.key)
      .classed("target", true)
      .classed("invisible",false)
      .each(updateNodes("source", true));

    svg.selectAll("path.linkCG.source-" + d.key)
      .classed("source", true)
      .classed("invisible",false)
      .each(updateNodes("target", true));
      
    svg.selectAll("path.linkCG.srctar-" + d.key)
      .classed("srctar", true)
      .classed("invisible",false)
      .each(updateNodes("srctar", true));
      
  }

  function mouseout(d) {
    svg.selectAll("path.linkCG")
    .classed("invisible",false);
    
    svg.selectAll("text")
    .classed("invisible",false);

    svg.selectAll("path.linkCG.source-" + d.key)
      .classed("source", false)
      .each(updateNodes("target", false));

    svg.selectAll("path.linkCG.target-" + d.key)
      .classed("target", false)
      .each(updateNodes("source", false));

    svg.selectAll("path.linkCG.srctar-" + d.key)
      .classed("srctar", false)
      .each(updateNodes("srctar", false));

  }

  function updateNodes(name, value) {
  	return function(d) {
    	if(name=="srctar") {
    	var node = svg.select("#node-" + d["target"].key).classed(name, value);
    	node.select("text").classed("invisible", false);
    	}
    	else {
    	var node = svg.select("#node-" + d[name].key).classed(name, value);
    	node.select("text").classed("invisible", false);
    	}
    	};
  	}

  function cross(a, b) {
  	return a[0] * b[1] - a[1] * b[0];
  }

  function dot(a, b) {
  	return a[0] * b[0] + a[1] * b[1];
  }


  function sortfunction(a, b){
  	return (b.amt-a.amt);
  }

  function sortfunction2(a, b){
  	nameA = a.name.toLowerCase();
  	nameB = b.name.toLowerCase();
  	if (nameA<nameB) return -1;
  	if (nameA>nameB) return 1;
  	return 0;
  }

  function sortfunction3(a, b){
  	return (b.size-a.size);
  }

  function sortfunction4(a, b){
  	return (a.type.localeCompare(b.type));
  }

  sortBy = function(d) {
    cluster = d3.layout.cluster()
    .size([360, ry - 120]);
    
    if (d==1) {
    cluster.sort(sortfunction);
    }
    if (d==2) {
    cluster.sort(sortfunction2);
    }
    if (d==3) {
    cluster.sort(sortfunction3);
    }    
    if (d==4) {
    cluster.sort(sortfunction4);
    }

    nodes = cluster.nodes(packages.root(data)),
    links = packages.imports(nodes),
    splines = bundle(links);  

    var arcs2 = svg.selectAll("path.typeArc")
      .attr("fill",function(d,i) {return hashColor(nodes[0].children[i].name)});

    path = svg.selectAll("path.linkCG")
      .data(links)
      .transition()
      .duration(750)
      .attr("class", function(d) {
        var bool=false;
        for(var i=0; i<both.length; i++) {
          if (both[i].source.name==d.source.name && both[i].target.name==d.target.name) {
            bool=true;
          }
        }
        if (bool) {
          return "linkCG srctar-" + d.source.key + " srctar-" + d.target.key;
        }
        else
          return "linkCG source-" + d.source.key + " target-" + d.target.key;})
      .attr("d", function(d, i) { return line(splines[i]); });
     

    svg.selectAll("g.node")
      .data(nodes.filter(function(n) { return !n.children; }))
      .transition()
      .duration(750)
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
    .select("text")
      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
      .attr("transform", function(d) { return d.x < 180 ? null : "rotate(180)"; });
     
}


  function validString(d) {
  	var str = d.slice(0,20);
  	var output = d.replace(/[^\A-Z0-9����+$]/gi,'_');
  	return output;
  	}
    }  
  })(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);