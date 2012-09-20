(function(d3custom, $, undefined) {


  d3custom.run = function() {

	var w = 960,
    h = 400,
    amt = 20,
    maxPos = 11,
    fill = d3.scale.category20(),
    nodes = [],
    nodes2 = [],
    links = [],
    foci = [/*{x: 30+900/amt, y: 20}, {x: 30, y: 20}, {x: 600, y: 20}, {x: 800, y: 20}*/];
	
	
//	var _nodes = d3custom.nodes,
//		_links = d3custom.links;
//	
//	
	
//	function init(){
//		console.log("Bin im Init");
//	   	var posCounter = 1; 
//		$.each(_nodes, function(i,v) {
//			
//			if (i >=1 && _nodes[i-1].pathId==_nodes[i].pathId) {
//				posCounter++;
//			} else {posCounter = 1; console.log("Neuer Pfad");}
//			console.log("Schreibe Node "+i+" Pfad: "+v.pathId+" Position: "+posCounter+" Name:"+v.title);
//	   		nodes.push({"pid":v.pathId , "pos":posCounter, "name":v.title, "value": v.value, "type": v.type, "totalRequests": v.totalRequests, "totalUsers":v.totalUsers});
//	   	 });
//	   	 $.each(_links, function(i,v) {
//	   		 links.push({"source":nodes[v.source],"target":nodes[v.target],"value":v.value});
//	   	 });
//	}

for (var i=0; i<amt; i++) {
	foci.push({x:50+860/(amt-1)*(i), y:20});
}

nodes2.push({pfad:1, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:1, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
          
        {pfad:2, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:2, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
          
        {pfad:3, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:3, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
            
  		
        {pfad:4, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:4, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:5, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:5, pos: 2, name: "Folien Einfuehrung"},
        
        
        {pfad:6, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:6, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:7, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:7, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:8, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:8, pos: 2, name: "Folien Einfuehrung"},
        {pfad:8, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:9, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:9, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:9, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:10, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:10, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:10, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:11, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:11, pos: 2, name: "Folien Einfuehrung"},
        {pfad:11, pos: 3, name: "Folien Einfuehrung"},
         
        {pfad:12, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:12, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:12, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
         
        {pfad:13, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:13, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:13, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
       
         
        {pfad:14, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:14, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:14, pos: 3, name: "Folien Einfuehrung"},
        
        {pfad:15, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:15, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:15, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:16, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:16, pos: 2, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:16, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:16, pos: 4, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
       
        
        {pfad:17, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:17, pos: 2, name: "Folien Einfuehrung"},
        {pfad:17, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:17, pos: 4, name: "Folien Einfuehrung"},
        {pfad:17, pos: 5, name: "Folien 01"},
        {pfad:17, pos: 6, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:18, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:18, pos: 2, name: "Folien Einfuehrung"},
        {pfad:18, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:18, pos: 4, name: "Folien 01"},
        {pfad:18, pos: 5, name: "Folien 02"},
        {pfad:18, pos: 6, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:18, pos: 7, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},          
        
        {pfad:19, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:19, pos: 2, name: "Folien Einfuehrung"},
        {pfad:19, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:19, pos: 4, name: "Folien Einfuehrung"},
        {pfad:19, pos: 5, name: "Folien 01"},
        {pfad:19, pos: 6, name: "Folien 02"},
        {pfad:19, pos: 7, name: "Folien 03"},
        {pfad:19, pos: 8, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        
        {pfad:20, pos: 1, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:20, pos: 2, name: "Folien Einfuehrung"},
        {pfad:20, pos: 3, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:20, pos: 4, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"},
        {pfad:20, pos: 5, name: "Folien Einfuehrung"},
        {pfad:20, pos: 6, name: "Folien 01"},
        {pfad:20, pos: 7, name: "Folien 02"},
        {pfad:20, pos: 8, name: "Folien 03"},
        {pfad:20, pos: 9, name: "Folien 04"},
        {pfad:20, pos: 10, name: "Folien 05"},
        {pfad:20, pos: 11, name: "BPK Bioprozesstechnik II - Bioprozesskontrolle (Gotz)"}
        
       
       
        );
          


var vis = d3.select("#viz").append("svg:svg")
.attr("width", w)
.attr("height", h);

var force = d3.layout.force()
    .nodes(nodes2)
    .links(links)
    .friction(.9)
    .gravity(0)
    .linkStrength(0)
    .charge(0)
    .size([w, h])
	.on("tick", function(e) {tick(e)});

	
	function tick(e) {	
	  // Push nodes toward their designated focus.
	  var k = .1 * e.alpha;
	  nodes.forEach(function(o, i) {
	    o.y += (foci[o.pid-1].y - o.y + (o.pos-1)*380/maxPos + 20) * k;
	    o.x += (foci[o.pid-1].x - o.x) * k;
	  });
	
	
	  vis.selectAll("circle.node")
	      .attr("cx", function(d) { return d.x; })
	      .attr("cy", function(d) { return d.y; });
	      
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

//for (var i=0; i<nodes2.length;i++) {
//	
// 	console.log("Before sort "+nodes2[i].pfad+"/"+nodes2[i].pos);
// }



/*nodes2.sort(function(a, b){
	if(a.path < b.path)
		return -1;
	if (a.path > b.path && a.pos < b.pos)
		return 1;
	if (a.path = b.path && a.pos > b.pos)
		return 1;
	if (a.path = b.path && a.pos < b.pos)
		return -1;
	return 0; 	
})
*/
//for (var i=0; i<nodes2.length;i++) {
// 	console.log("After sort"+nodes2[i].pfad+"/"+nodes2[i].pos);
// }

//for(var j=0; j<nodes2.length; j++) {
//  nodes.push(nodes2[j]);
//}
//
//for (var j=1; j<nodes.length; j++) {
//       if (nodes[j-1].pfad==nodes[j].pfad) {
//       console.log("in");
//       links.push({source: nodes[j-1], target: nodes[j]});
//       }
//    }
 console.log(links.length);
 
/*for (var i=0; i<links.length;i++) {
 	console.log(""+links[i].source.pfad+"/"+links[i].target.pfad);
 }
*/
vis.selectAll("line.link")
      .data(links)
      .enter().append("svg:line")
      .attr("class", "link")
      .style("stroke-width","1")
      .style("stroke","#000");

 var nodeEnter = vis.selectAll("circle.node")
      .data(nodes)
    .enter().append("svg:circle")
      .attr("class", "node")
      .attr("cx", function(d) { return 0; })
      .attr("cy", function(d) { return 0; })
      .attr("r", 8)
      .style("fill", function(d) { return fill(d.name); })
      .style("stroke", function(d) { return d3.rgb(fill(1)).darker(2); })
      .style("stroke-width", 1.5);
      //.call(force.drag);
 
nodeEnter.append("nodetitle")
.text(function(d) { return "<b>Ressource:</b> "+ d.name+"<br /><b>Requests:</b> "+ d.totalRequests+" ("+d.totalUsers+" User)<br /><br /> <b>Support</b>: "+d.value});


force.start();
	

	
$('nodetitle').parent().tipsy({ 
    gravity: 'sw', 
    html: true, 
    title: function() { return $(this).find('nodetitle').text(); }
  });
	
	
  };	
})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
