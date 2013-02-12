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

/*svg.append("svg:path")
  .attr("class", "arc")
  .attr("d", d3.svg.arc().outerRadius(ry - 120).innerRadius(ry).startAngle(0).endAngle(2 * Math.PI))
  .on("mousedown", mousedown);
*/  
 // var classes = [{"name":"Formale_Grundlagen_de","size":574,"type":"Course","imports":["Uebung03","Uebung2","Klausur_Teil_2_SoSe_1","Uebung_09","Teil_2_Klausur_WS09-1","Teil_2_Klausur_WS10-1","Uebung_08","Teil_2_Probeklausur_1","Teil_2_Probeklausur2","Uebung_11","Klausur_Teil_1_SoSe_1","Klausur_Teil1","_bung06","_bung05","Teil_2_Probeklausur1_","Foliensatz_11","Foliensatz_10","Foliensatz_09","_bung04A","Uebung_4","Design_Patterns_for_D","_","JFLAP_Homepage","Uebung_12","quiz1","Nachklausur","Teil_2_KlausurWS10-11","Klausur_Teil2","Teile_2_Klausur_WS11-","Foliensatz_12","Foliensatz_06","Foliensatz_07","Uebung1","Folien01_pdf","Multiple_java","Uebung07","Klausur_Teil_1_WS09-1","Foliensatz_08","Teil_1_-_Probeklausur","Homepage_des_Buches","Foliensatz_05","Uebung_13","Probeklausur_1_L_sung"]},{"name":"Multiple_java","size":2,"type":"Resource","imports":["Folien01_pdf"]},{"name":"Folien01_pdf","size":20,"type":"Resource","imports":["Folien02_pdf","Formale_Grundlagen_de","Teil_2_Klausur_WS09-1","Homepage_des_Buches","Uebung_08","Klausur_Teil2","Uebung_4","Foliensatz_07"]},{"name":"Uebung1","size":32,"type":"Quiz","imports":["Folien01_pdf","Formale_Grundlagen_de","Uebung2","quiz1","Klausur_Teil2","DFA-1Zustand"]},{"name":"Teil_1_-_Probeklausur","size":7,"type":"Resource","imports":["Formale_Grundlagen_de","Klausur_Teil_1_WS09-1","Teil_1_-_Probeklausur"]},{"name":"Teil_1_-_Probeklausur","size":9,"type":"Resource","imports":["Klausur_Teil_1_WS09-1","Teil_2_Probeklausur_1"]},{"name":"Klausur_Teil_1_WS09-1","size":11,"type":"Resource","imports":["Klausur_Teil_1_WS_10-","Teil_2_Klausur_WS09-1","Klausur_Teil_1_WS09-1"]},{"name":"Klausur_Teil_1_WS_10-","size":9,"type":"Resource","imports":["Formale_Grundlagen_de","Probeklausur_1_L_sung","Klausur_Teil_1_SoSe_1","Klausur_Teil_1_WS09-1","Klausur_Teil_1_WS10-1"]},{"name":"Klausur_Teil_1_SoSe_1","size":6,"type":"Resource","imports":["Teil_2_Klausur_WS09-1","Teil_2_Probeklausur_1","Probeklausur_1_L_sung","Klausur_Teil_1_SoSe_1"]},{"name":"Probeklausur_1_L_sung","size":6,"type":"Resource","imports":["Teil_2_Probeklausur1_","Probeklausur_2_L_sung","Formale_Grundlagen_de"]},{"name":"Probeklausur_2_L_sung","size":4,"type":"Resource","imports":["Klausur_Teil_1_WS09-1","Teil_1_-_Probeklausur"]},{"name":"Klausur_Teil_1_WS09-1","size":5,"type":"Resource","imports":["Klausur_Teil_1_WS_10-","Klausur_Teil_1_WS09-1","Klausur_Teil_1_WS10-1"]},{"name":"Klausur_Teil_1_WS10-1","size":7,"type":"Resource","imports":["Teil_2_KlausurWS10-11","Teil_2_Klausur_WS10-1","Klausur_Teil_1_SoSe_1"]},{"name":"Klausur_Teil_1_SoSe_1","size":12,"type":"Resource","imports":["Klausur_Teil_1_WS09-1","Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Klausur_Teil_2_SoSe_1","Foliensatz_12","Klausur_Teil1","Foliensatz_06","Klausur_Teil_1_WS10-1","Foliensatz_07"]},{"name":"Klausur_Teil1","size":35,"type":"Assignment","imports":["Uebung1","Formale_Grundlagen_de","Teil_1_-_Probeklausur","Teil_2_Probeklausur_1","Klausur_Teil2","_","Foliensatz_07"]},{"name":"Teil_2_Probeklausur_1","size":57,"type":"Resource","imports":["Klausur_Teil_2_SoSe_1","Teil_2_Klausur_WS09-1","Teil_2_Klausur_WS10-1","Teil_2_Probeklausur2","Klausur_Teil2","Klausur_Teil1","Teil_2_Probeklausur2_","Teil_2_Probeklausur1_","Formale_Grundlagen_de","Uebung07","Foliensatz_09","NFARaetsel"]},{"name":"Teil_2_Probeklausur2","size":39,"type":"Resource","imports":["Design_Patterns_for_D","Teil_2_Probeklausur2_","Teil_2_Probeklausur1_","Teil_2_Klausur_WS09-1","Teil_2_Probeklausur_1","Klausur_Teil2"]},{"name":"Teil_2_Klausur_WS09-1","size":62,"type":"Resource","imports":["Klausur_Teil_2_SoSe_1","Teil_2_Klausur_WS09-1","Teil_2_Klausur_WS10-1","Teil_2_KlausurWS10-11","Teil_2_Probeklausur2","Klausur_Teil2","Foliensatz_12","Teil_2_Probeklausur2_","Teil_2_Probeklausur1_","Formale_Grundlagen_de","Uebung07","Foliensatz_09"]},{"name":"Teil_2_Klausur_WS10-1","size":60,"type":"Resource","imports":["Formale_Grundlagen_de","Klausur_Teil_2_SoSe_1","Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Teil_2_Probeklausur2","Klausur_Teil2"]},{"name":"Klausur_Teil_2_SoSe_1","size":59,"type":"Resource","imports":["Teil_2_Klausur_WS09-1","Teil_2_Klausur_WS10-1","Klausur_Teil_2_SoSe_1","Klausur_Teil2","Klausur_Teil_1_SoSe_1","Teile_2_Klausur_WS11-","Foliensatz_12","Klausur_Teil1","Teil_2_Probeklausur1_","Formale_Grundlagen_de","Foliensatz_10","Foliensatz_09"]},{"name":"Teil_2_Probeklausur1_","size":51,"type":"Resource","imports":["Klausur_Teil_2_SoSe_1","Nachklausur","Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Teil_2_Probeklausur2","Klausur_Teil2","Klausur_Teil1","Teil_2_Probeklausur2_","Probeklausur_2_L_sung","Formale_Grundlagen_de"]},{"name":"Teil_2_Probeklausur2_","size":40,"type":"Resource","imports":["Teil_2_Probeklausur1_","Formale_Grundlagen_de","Teil_2_Klausur_WS09-1","Teil_2_Probeklausur2","Klausur_Teil2","_bung05"]},{"name":"Teil_2_Klausur_WS09-1","size":78,"type":"Resource","imports":["Klausur_Teil_2_SoSe_1","Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Teil_2_Klausur_WS10-1","Uebung_08","Teil_2_Probeklausur_1","Teil_2_Probeklausur2","Klausur_Teil2","Uebung_11","Foliensatz_12","Foliensatz_07","Uebung1","Teil_2_Probeklausur2_","Teil_2_Probeklausur1_","Formale_Grundlagen_de","Foliensatz_10"]},{"name":"Teil_2_KlausurWS10-11","size":66,"type":"Resource","imports":["Teil_2_Probeklausur1_","Uebung03","Formale_Grundlagen_de","Klausur_Teil_2_SoSe_1","Foliensatz_09","Teil_2_Klausur_WS09-1","Teil_2_Probeklausur_1","Klausur_Teil2"]},{"name":"Klausur_Teil_2_SoSe_1","size":63,"type":"Resource","imports":["_","Klausur_Teil_2_SoSe_1","Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Teil_2_Klausur_WS10-1","Teil_2_Probeklausur_1","Teil_2_Probeklausur2","Klausur_Teil2","Teile_2_Klausur_WS11-","Klausur_Teil1","Foliensatz_12","Teil_2_Probeklausur1_","Folien01_pdf","Formale_Grundlagen_de","Foliensatz_11"]},{"name":"Klausur_Teil2","size":146,"type":"Assignment","imports":["_","Uebung03","Klausur_Teil_2_SoSe_1","Nachklausur","Uebung_09","Teil_2_KlausurWS10-11","Teil_2_Probeklausur_1","Teil_2_Probeklausur2","Teile_2_Klausur_WS11-","Foliensatz_12","Klausur_Teil1","Foliensatz_07","Uebung1","Formale_Grundlagen_de","Uebung07","Foliensatz_10","Homepage_des_Buches"]},{"name":"Teile_2_Klausur_WS11-","size":6,"type":"Resource","imports":["Formale_Grundlagen_de","Nachklausur","Teil_2_Probeklausur_1"]},{"name":"Foliensatz_12","size":52,"type":"Resource","imports":["Klausur_Teil_2_SoSe_1","Teil_2_KlausurWS10-11","Teil_2_Klausur_WS10-1","Teil_2_Probeklausur_1","Klausur_Teil2","Uebung_11","Klausur_Teil1","Formale_Grundlagen_de","Foliensatz_11","Foliensatz_10","Teil_1_-_Probeklausur","Foliensatz_09"]},{"name":"Foliensatz_06","size":14,"type":"Resource","imports":["Formale_Grundlagen_de","Foliensatz_10","Foliensatz_08","Uebung_09","Foliensatz_07"]},{"name":"Foliensatz_07","size":59,"type":"Resource","imports":["Uebung2","Teil_2_Probeklausur_1","Uebung_08","Klausur_Teil2","Klausur_Teil_1_SoSe_1","Foliensatz_06","Klausur_Teil_1_WS10-1","Formale_Grundlagen_de","Uebung07","Foliensatz_08","Foliensatz_10","Foliensatz_09","Foliensatz_05"]},{"name":"Uebung07","size":39,"type":"Quiz","imports":["Formale_Grundlagen_de","Foliensatz_08","Gram01","Uebung_09","Uebung_08","Klausur_Teil2","Foliensatz_07"]},{"name":"Gram01","size":14,"type":"Question","imports":["Uebung07"]},{"name":"Foliensatz_08","size":54,"type":"Resource","imports":["Uebung03","Klausur_Teil_2_SoSe_1","Uebung_08","Teil_2_Probeklausur_1","Klausur_Teil2","Klausur_Teil1","Foliensatz_12","Foliensatz_06","Foliensatz_07","Teil_2_Probeklausur2_","Formale_Grundlagen_de","Foliensatz_11","Foliensatz_10","Foliensatz_09"]},{"name":"Uebung_08","size":46,"type":"Quiz","imports":["Formale_Grundlagen_de","quiz1","Foliensatz_08","Uebung_09","Mehrdeutigkeit","_bung06"]},{"name":"Mehrdeutigkeit","size":21,"type":"Question","imports":["Uebung_08"]},{"name":"Uebung_09","size":41,"type":"Quiz","imports":["Formale_Grundlagen_de","Uebung_12","Foliensatz_08","deterKAutom","Uebung_08","Klausur_Teil2","Uebung_11","Klausur_Teil1"]},{"name":"deterKAutom","size":15,"type":"Question","imports":["Uebung_09"]},{"name":"_bung06","size":8,"type":"Quiz","imports":["DFAMin","Formale_Grundlagen_de","Uebung07","_bung05"]},{"name":"_bung05","size":11,"type":"Quiz","imports":["Formale_Grundlagen_de","DFAToRE","Uebung_08","_bung06"]},{"name":"Uebung_11","size":36,"type":"Quiz","imports":["Uebung1","Formale_Grundlagen_de","TyP3","Foliensatz_11","Uebung_12","Foliensatz_08","Uebung_09"]},{"name":"Foliensatz_09","size":64,"type":"Resource","imports":["Teil_2_Klausur_WS09-1","Teil_2_KlausurWS10-11","Teil_2_Klausur_WS10-1","Teil_2_Probeklausur_1","Klausur_Teil2","Teil_2_Probeklausur2","Klausur_Teil1","Foliensatz_12","Foliensatz_07","Formale_Grundlagen_de","Foliensatz_10","Foliensatz_08"]},{"name":"Foliensatz_10","size":58,"type":"Resource","imports":["Teil_2_Probeklausur1_","Formale_Grundlagen_de","Foliensatz_11","Foliensatz_08","Foliensatz_09","Klausur_Teil_2_SoSe_1","Foliensatz_12","Foliensatz_07"]},{"name":"Foliensatz_11","size":59,"type":"Resource","imports":["Uebung1","Folien01_pdf","Formale_Grundlagen_de","Klausur_Teil_1_WS09-1","Klausur_Teil_2_SoSe_1","Foliensatz_10","Foliensatz_09","Teil_2_Klausur_WS09-1","Teil_2_Klausur_WS10-1","Foliensatz_12"]},{"name":"TyP3","size":11,"type":"Question","imports":["Uebung_11"]},{"name":"Uebung_12","size":32,"type":"Quiz","imports":["Formale_Grundlagen_de","TM","quiz1"]},{"name":"TM","size":10,"type":"Question","imports":["Uebung_12"]},{"name":"Nachklausur","size":35,"type":"Assignment","imports":["Design_Patterns_for_D","Formale_Grundlagen_de","Klausur_Teil_2_SoSe_1","Klausur_Teil2","Klausur_Teil_1_SoSe_1","Klausur_Teil_1_WS09-1"]},{"name":"quiz1","size":11,"type":"Quiz","imports":["Uebung1","Formale_Grundlagen_de","Uebung2","Foliensatz_09","Teil_2_Klausur_WS09-1"]},{"name":"_","size":26,"type":"Forum","imports":["Folien01_pdf","Formale_Grundlagen_de","Homepage_des_Buches","Klausur_Teil2"]},{"name":"Folien02_pdf","size":4,"type":"Resource","imports":["Folien01_pdf","Folien03_pdf"]},{"name":"_","size":8,"type":"Forum","imports":["Design_Patterns_for_D","Formale_Grundlagen_de","Homepage_des_Buches","Teil_2_Probeklausur_1"]},{"name":"Homepage_des_Buches","size":8,"type":"Resource","imports":["Design_Patterns_for_D","Formale_Grundlagen_de","JFLAP","Teil_2_Klausur_WS09-1","Uebung_08","Klausur_Teil2","Foliensatz_07"]},{"name":"Foliensatz_05","size":7,"type":"Resource","imports":["Formale_Grundlagen_de","Foliensatz_04","Foliensatz_09","Foliensatz_06","Foliensatz_07"]},{"name":"Nachklausur","size":2,"type":"Assignment","imports":["Formale_Grundlagen_de","Klausur_Teil2"]},{"name":"DFA-1Zustand","size":7,"type":"Question","imports":["Uebung1"]},{"name":"Folien03_pdf","size":2,"type":"Resource","imports":["Foliensatz_04"]},{"name":"Foliensatz_04","size":4,"type":"Resource","imports":["Foliensatz_05"]},{"name":"Uebung2","size":14,"type":"Quiz","imports":["Uebung1","Formale_Grundlagen_de","Uebung03","NFA000","Uebung_09"]},{"name":"NFA000","size":5,"type":"Question","imports":["Uebung2"]},{"name":"Uebung03","size":8,"type":"Quiz","imports":["Formale_Grundlagen_de","ErweitUebrgang","Uebung_4","Foliensatz_07"]},{"name":"ErweitUebrgang","size":1,"type":"Question","imports":["Uebung03"]},{"name":"Uebung_4","size":7,"type":"Quiz","imports":["Formale_Grundlagen_de","NFA-e-DFA","_bung04A"]},{"name":"NFA-e-DFA","size":1,"type":"Question","imports":["Uebung_4"]},{"name":"_bung04A","size":6,"type":"Quiz","imports":["Formale_Grundlagen_de","RE","_bung05"]},{"name":"RE","size":2,"type":"Question","imports":["_bung04A"]},{"name":"DFAToRE","size":4,"type":"Question","imports":["_bung05"]},{"name":"DFAMin","size":4,"type":"Question","imports":["_bung06"]},{"name":"Design_Patterns_for_D","size":7,"type":"Resource","imports":["_","Formale_Grundlagen_de","JFLAP","Homepage_des_Buches"]},{"name":"JFLAP","size":2,"type":"Resource","imports":["JFLAP_Homepage"]},{"name":"JFLAP_Homepage","size":3,"type":"Resource","imports":["Formale_Grundlagen_de","Klausur_Teil1","_"]},{"name":"NFARaetsel","size":1,"type":"Resource","imports":["Klausur_Teil1"]},{"name":"Uebung_13","size":2,"type":"Quiz","imports":["Formale_Grundlagen_de","CNF02"]},{"name":"CNF02","size":1,"type":"Question","imports":["Uebung_13"]}];

    var test =  packages.root(data).children;
	//console.log(test);
    test.forEach(function(d) {
    if (d.imports!=undefined)
    d.amt=calculateAllLinks(d);
    });

var nodes = cluster.nodes(packages.root(data)),
    links = packages.imports(nodes),
    splines = bundle(links);

    //console.log(nodes);

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

var path = svg.selectAll("path.link")
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
    return "circle link srctar-" + d.source.key + " srctar-" + d.target.key;
    }
    else
    return "circle link source-" + d.source.key + " target-" + d.target.key;})
    .attr("d", function(d, i) { return line(splines[i]); });

  svg.selectAll("g.node")
    .data(nodes.filter(function(n) { return !n.children; }))
  .enter().append("svg:g")
    .attr("class", "node")
    .attr("id", function(d) { return "node-" + d.key; })
    .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
    .on("mouseover", function(d) {
    		console.log("HOVER ......"+ d.key)				
    		return mouseover(d);})
    .on("mouseout", function(d) {return mouseout(d);})
  .append("svg:text")
    .attr("dx", function(d) { return d.x < 180 ? 8 : -8; })
    .attr("dy", ".31em")
    .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
    .attr("transform", function(d) { return d.x < 180 ? null : "rotate(180)"; })
    .attr("font-size",function(d) {return 10+d.amt/10;})
    .text(function(d) { return d.key; })
    .on("mouseover", function(d) {
    		console.log("HOVER ......"+ d.key)				
    		return mouseover(d);})
    .on("mouseout", function(d) {return mouseout(d);});
    
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
  return (b.name.length-a.name.length);
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
   
   $('#viz').remove();
   
   div = d3.select("body").insert("div")
   .attr("id","viz");
   
   svg = div.append("svg:svg")
  .attr("width", w)
  .attr("height", w)
.append("svg:g")
  .attr("transform", "translate(" + rx + "," + ry + ")");

svg.append("svg:path")
  .attr("class", "arc")
  .attr("d", d3.svg.arc().outerRadius(ry - 120).innerRadius(0).startAngle(0).endAngle(2 * Math.PI))
  .on("mousedown", mousedown);
  
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
    .text(function(d) { return d.key; })
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