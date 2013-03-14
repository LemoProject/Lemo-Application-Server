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
  	  
  
 
  
 var  w = 960,
  h = 800,
  rx = w / 2,
  ry = h / 2,
  m0,
  rotate = 0;
 
 function color(name) {
     name=name.toLowerCase();
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


var splines = [];

var cluster = d3.layout.cluster()
  .size([360, ry - 120])
  .sort(function(a, b) { return d3.ascending(a.key, b.key); });

var bundle = d3.layout.bundle();

var line = d3.svg.line.radial()
  .interpolate("bundle")
  .tension(.85)
  .radius(function(d) { return d.y; })
  .angle(function(d) { return d.x / 180 * Math.PI; });

//Chrome 15 bug: &lt;http://code.google.com/p/chromium/issues/detail?id=98951&gt;
var div = d3.select("#viz");

var svg = div.append("svg:svg")
.attr("width", w)
.attr("height", h)
.append("svg:g")
.attr("transform", "translate(" + rx + "," + ry + ")");

svg.append("svg:path")
.attr("class", "arc")
.attr("d", d3.svg.arc().outerRadius(ry - 20).innerRadius(ry - 110).startAngle(0).endAngle(2 * Math.PI))
.attr("fill",function(d,i) { return color("wiki");})
.on("mousedown", mousedown);


  var test =  packages.root(data).children;
  var types = [];
  var typeAmt = [];
//console.log(test);
  test.forEach(function(d) {
  if (d.imports!=undefined)
  d.amt=calculateAllLinks(d);
  });


var nodes = cluster.nodes(packages.root(data)),
  links = packages.imports(nodes),
  splines = bundle(links);


var both = [];
for (var i=0; i<links.length; i++) {
for (var j=0; j<links.length; j++) {
if (i!=j) {
if (links[i].source.name == links[j].target.name && links[i].target.name == links[j].source.name) {
//console.log(links[i].source.name+"/"+links[j].target.name);
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
  
  sortBy(4);

d3.select("input[type=range]").on("change", function() {
line.tension(this.value / 100);
path.attr("d", function(d, i) { return line(splines[i]); });
});


d3.select(window)
.on("mousemove", mousemove)
.on("mouseup", mouseup);

function mouse(e) {
return [e.pageX - rx, e.pageY - ry];
}

function mousedown() {
m0 = mouse(d3.event);
d3.event.preventDefault();
}

function mousemove() {
if (m0) {
var m1 = mouse(d3.event),
    dm = Math.atan2(cross(m0, m1), dot(m0, m1)) * 180 / Math.PI;
div.style("-webkit-transform", "translate3d(0," + (ry - rx) + "px,0)rotate3d(0,0,0," + dm + "deg)translate3d(0," + (rx - ry) + "px,0)");
}
}

function mouseup() {
if (m0) {
var m1 = mouse(d3.event),
    dm = Math.atan2(cross(m0, m1), dot(m0, m1)) * 180 / Math.PI;

rotate += dm;
if (rotate > 360) rotate -= 360;
else if (rotate < 0) rotate += 360;
m0 = null;

div.style("-webkit-transform", "rotate3d(0,0,0,0deg)");

svg
    .attr("transform", "translate(" + rx + "," + ry + ")rotate(" + rotate + ")")
  .selectAll("g.node text")
    .attr("dx", function(d) { return (d.x + rotate) % 360 < 180 ? 8 : -8; })
    .attr("text-anchor", function(d) { return (d.x + rotate) % 360 < 180 ? "start" : "end"; })
    .attr("transform", function(d) { return (d.x + rotate) % 360 < 180 ? null : "rotate(180)"; });
}
}

function mouseover(d) {

svg.selectAll("path.link")
.classed("invisible",true);

svg.selectAll("path.link.target-" + d.key)
  .classed("target", true)
  .classed("invisible",false)
  .each(updateNodes("source", true));

svg.selectAll("path.link.source-" + d.key)
  .classed("source", true)
  .classed("invisible",false)
  .each(updateNodes("target", true));
  
svg.selectAll("path.link.srctar-" + d.key)
  .classed("srctar", true)
  .classed("invisible",false)
  .each(updateNodes("srctar", true));
  
svg.selectAll("path.typeArc")
   .classed("invisible",true);
}

function mouseout(d) {

svg.selectAll("path.link")
.classed("invisible",false);

svg.selectAll("path.link.source-" + d.key)
  .classed("source", false)
  .each(updateNodes("target", false));

svg.selectAll("path.link.target-" + d.key)
  .classed("target", false)
  .each(updateNodes("source", false));

svg.selectAll("path.link.srctar-" + d.key)
  .classed("srctar", false)
  .each(updateNodes("srctar", false));

svg.selectAll("path.typeArc")
   .classed("invisible",false);
}

function updateNodes(name, value) {
	return function(d) {
	if (value) this.parentNode.appendChild(this);
	if(name=="srctar")
	svg.select("#node-" + d["target"].key).classed(name, value);
	else
	svg.select("#node-" + d[name].key).classed(name, value);
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
 //Compare "a" and "b" in some fashion, and return -1, 0, or 1
}

function sortfunction2(a, b){
nameA = a.name.toLowerCase();
nameB = b.name.toLowerCase();
if (nameA<nameB) return -1;
if (nameA>nameB) return 1;
return 0;
//Compare "a" and "b" in some fashion, and return -1, 0, or 1
}

function sortfunction3(a, b){
return (b.size-a.size);
//Compare "a" and "b" in some fashion, and return -1, 0, or 1
}

function sortfunction4(a, b){
return (b.type.localeCompare(a.type));
//Compare "a" and "b" in some fashion, and return -1, 0, or 1
}

function sortBy(d) {
	cluster = d3.layout.cluster()
	.size([360, ry - 120]);
	
	if (d==1) {
	cluster.sort(sortfunction);
	}
	else if (d==2) {
	cluster.sort(sortfunction2);
	}
	else if (d==3) {
	cluster.sort(sortfunction3);
	}
	else if (d==4) {
	cluster.sort(sortfunction4);
	}

	 nodes = cluster.nodes(packages.root(data)),
	 links = packages.imports(nodes),
	 splines = bundle(links);
	 
	if (d==1) {
	nodes.sort(sortfunction);
	}
	else if (d==2) {
	nodes.sort(sortfunction2);
	}
	else if (d==3) {
	nodes.sort(sortfunction3);
	}
	else if (d==4) {
	nodes.sort(sortfunction4);
	}
 
 $('#viz').remove();
 
 div = d3.select("body").insert("div")
 .attr("id","viz");
 
 svg = div.append("svg:svg")
.attr("width", w)
.attr("height", h)
.append("svg:g")
.attr("transform", "translate(" + rx + "," + ry + ")");



svg.append("svg:path")
.attr("class", "arc")
.attr("d", d3.svg.arc().outerRadius(ry - 120).innerRadius(0).startAngle(0).endAngle(2 * Math.PI))
.on("mousedown", mousedown);

var arc = d3.svg.arc()
    .innerRadius(ry - 135)
    .outerRadius(ry - 125)
    .startAngle(function(d,i) { return (2*Math.PI)/(nodes.length-1)*(i-1)})
    .endAngle(function(d,i) {return (2*Math.PI)/(nodes.length-1)*(i)});

  var arcs = svg.selectAll("path")
    .data(nodes)
    .enter().append("path")
    .attr("fill",function(d,i) { return color(d.type);})
    .attr("d",arc)
    .attr("class","typeArc");;
    
path = svg.selectAll("path.link")
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
  return "link srctar-" + d.source.key + " srctar-" + d.target.key;
  }
  else
  return "link source-" + d.source.key + " target-" + d.target.key;})
  .attr("d", function(d, i) { return line(splines[i]); });
 

svg.selectAll("g.node")
  .data(nodes.filter(function(n) { return !n.children; }))
.enter().append("svg:g")
  .attr("class", "node")
  .attr("id", function(d) { return "node-" + d.key; })
  .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
.append("svg:text")
  .attr("dx", function(d) { return d.x < 180 ? 8 : -8; })
  .attr("dy", ".31em")
  .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
  .attr("transform", function(d) { return d.x < 180 ? null : "rotate(180)"; })
  .attr("font-size",function(d) {return 10+d.amt/10;})
  .text(function(d) { return d.name; })
  .on("mouseover", function(d) {mouseover(d);})
  .on("mouseout", function(d) {mouseout(d);});
 
}


function validString(d) {
	var output="";
	for (var i=0; i<d.length; i++) {
	if (i>20) {
	  break;
	  }
	  else {
	  if(d.charAt(i)==" ") {
	  output += "_";
	  }
	  else if(d.charAt(i)==".") {
	  output += "_";
	  }
	  else if(d.charAt(i)=="?") {
	  output += "_";
	  }
	  else {
	  output += d.charAt(i);
	  }
	  }
	  }
	  return output;
	}
	  

  	  
	  
  }  
  })(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);