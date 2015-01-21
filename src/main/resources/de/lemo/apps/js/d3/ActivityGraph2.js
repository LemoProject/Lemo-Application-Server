
(function(d3custom, $, undefined) {


	d3custom.run = function() {

		var locale = d3custom.locale;
		var w= $('#viz').width(), 
		h = 700,
		color = function(d) { return hashColor(d.name)},
		selectedNodes,
		vis,
		root;


		$(window).resize(function() {
			width = $('#viz').width();
			d3.select("svg")
			.remove();
			drawChart();
		});

		var startDistance = 200;

		var drawChart = function() {  

			var outer = d3.select("#viz").append("svg:svg")
			.attr("width", w)
			.attr("height", h)
			.attr("pointer-events", "all");

			//adding zoom / drag layer   
			var svg = outer.append('svg:g')
			.call(d3.behavior.zoom()
					.scaleExtent([0.1,2])
					.on("zoom", rescale))
					.on("dblclick.zoom", null)
					.append('svg:g')
					.on("mousedown", mousedown)
					.on("touchstart", mousedown);

			svg.append('svg:rect')
			.attr("class", "parent")
			.attr('width', w)
			.attr('height', h);
			/* Build the directional arrows for the links/edges */
			svg.append("svg:defs")
			.selectAll("marker")
			.data(["end"])
			.enter().append("svg:marker")
			.attr("id", String)
			.attr("viewBox", "0 -5 10 10")
			.attr("refX", 9)
			.attr("refY", 0)
			.attr("markerWidth", 6)
			.attr("markerHeight", 6)
			.attr("orient", "auto")
			.append("svg:path")
			.attr("d", "M0,-5L10,0L0,5");

			var _nodes = d3custom.nodes,
			_links = d3custom.links;

			//check if we have values to work with
			if(!_nodes || !_links) {
				$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
				return;
			}

			init();
			function makeDiag() {
				/* Draw the node labels first */
				var texts = svg.selectAll("text")
				.data(_nodes)
				.enter()
				.append("text")
				.attr("fill", "black")
				.attr("font-family", "sans-serif")
				.attr("font-size", "10px")
				.text(function(d) { return d.name; });
				/* Establish the dynamic force behavor of the nodes */
				var force = d3.layout.force()
				.nodes(_nodes)
				.links(_links)
				.size([w,h])
				.linkDistance([250])

				/* Add link distance depending on target node value.
				 * function(d) { 
			                    	var scale = d3.scale.linear()
                    				.domain([300, 900])
                    				.range([250, 50]);		
			                    	console.log(d.target.value);
			                    	return scale(d.target.value) })*/
				.charge([-1500])
				.gravity(0.3);
				/* Draw the edges/links between the nodes */
				var edges = svg.selectAll("line")
				.data(_links)
				.enter()
				.append("line")
				.style("stroke", "#ccc")
				.style("stroke-width", 1)
				.attr("marker-end", "url(#end)");
				/* Draw the nodes themselves */               
				var nodes = svg.selectAll("circle")
				.data(_nodes)
				.enter()
				.append("circle")
				.attr("r", 20)
				.attr("opacity", 0.5)
				.style("fill", function(d,i) { return color(d); })
				.call(force.drag);
				nodes.append("nodetitle")
				.text(function(d) { return "<b> "+locale.learningObject +":</b> "+ d.name+"<br /><br /><b> "+locale.resourcetypeLabel+" :</b> "+ d.type+"<br /><br /> <b> "+locale.activities+" </b>: "+d.value;});
				function normalize(d){
					var deltaX = d.target.x - d.source.x,
					deltaY = d.target.y - d.source.y,
					dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),
					normX = deltaX / dist,
					normY = deltaY / dist;
					return {normX:normX,normY:normY};
				}				
				/* Run the Force effect */
				force.on("tick", function() {
					edges.attr("x1", function(d) { return d.source.x + (20 * normalize(d).normX);})
						 .attr("y1", function(d) { return d.source.y + (20 * normalize(d).normY);})
						 .attr("x2", function(d) { return d.target.x - (20 * normalize(d).normX);})
						 .attr("y2", function(d) { return d.target.y - (20 * normalize(d).normY);});
					nodes.attr("cx", function(d) { 	
						if(d.connected==1){
							return d.x;
						} else return 30; })
						.attr("cy", function(d) { return d.y; })
						texts.attr("transform", function(d) {
							if(d.connected==1){
								return "translate(" + d.x + "," + d.y + ")";
							} else return "translate(" + 30 + "," + d.y + ")";                        
						});
				}); // End tick func
				force.start();
				for (var i = 100; i > 0; --i) force.tick();
				force.stop();
			}; // End makeDiag worker func

			function init(){
				var support = d3custom.support/100;
				var size = Math.floor(_links.length*(1-support));
				var maxValue = 0;
				_links.forEach(function(link){
					link.source = parseInt(link.source);
					link.target = parseInt(link.target);
					link.value = parseInt(link.value);
					if (link.value > maxValue) maxValue = link.value;
				})
				_links = _links.sort(function(a,b){return b.value-a.value}).slice(0,size);
				var normalizingFaktor = 100/maxValue;
				_links.forEach(function(link){
					link.value = Math.round(link.value*normalizingFaktor);
					_nodes[link.target].connected=1;
					_nodes[link.source].connected=1;
				})			
				makeDiag();
				// Bind slide event to update minSup Value in front end
				$("#sliderZone-slider").bind("slide", function(event, ui) {
					var oldLabel = $("#sliderZone-label").text().split(":");
					$("#sliderZone-label").html( oldLabel[0]+":" + ui.value / 100);
				});
			}   

			function rescale() {
				trans=d3.event.translate;
				scale=d3.event.scale;
				current_scale= scale;
				scroll= svg.attr("transform",
						"translate(" + trans + ")"
						+ " scale(" + scale + ")");
				console.log("SCALE: "+ scale + " TRANS: "+ trans);

			}

			function mousedown() {

				// allow panning if nothing is selected
				svg.call(d3.behavior.zoom().on("zoom"), rescale);
				return;

			}


			$('nodetitle').parent().tipsy({ 
				gravity: 'sw', 
				html: true, 
				title: function() { return $(this).find('nodetitle').text(); }
			});

		} //drawChart
		drawChart();
	};

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
