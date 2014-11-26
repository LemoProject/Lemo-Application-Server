(function(d3custom, $, undefined) {


	d3custom.run = function() {


		var objTypes = [];
		var data = d3custom.data;
		var locale = d3custom.locale;

		//check if we have values to work with
		if(!data) {
			$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
			return;
		}

		var chartWidth = 980 - 80;
		if ($("#viz").width() < 920)
			chartWidth = $("#viz").width();
		var chartHeight = 800 - 180;   
		var xscale = d3.scale.linear().range([0, chartWidth]);
		var yscale = d3.scale.linear().range([0, chartHeight]);
		var color = function(d) { 
			return hashColor(d); 
		};
		var headerHeight = 20;
		var headerColor = "#555555";
		var transitionDuration = 500;
		var root;
		var node;

		var treemap = d3.layout.treemap()
		.round(false)
		.size([chartWidth, chartHeight])
		.sticky(true)
		.value(function(d) {
			return d.requests;
		});

		var chart = d3.select("#viz")
		.append("svg:svg")
		.attr("width", chartWidth)
		.attr("height", chartHeight)
		.append("svg:g");
		
		start(data,treemap);
		
      	$('squaretitle').parent().tipsy({
      	    gravity: 'sw',
      	    html: true,
      	    title: function() { return $(this).find('squaretitle').text(); }
      	  });
      	
		function start(data,treemap) {
			node = root = data;
			var nodes = treemap.nodes(root);

			var children = nodes.filter(function(d) {
				return !d.children;
			});
			var parents = nodes.filter(function(d) {
				return d.children;
			});

			// create parent cells
			var parentCells = chart.selectAll("g.cell.parent")
			.data(parents, function(d) {
				return "p-" + d.name;
			});
			var parentEnterTransition = parentCells.enter()
			.append("g")
			.attr("class", "cell parent")
			.on("click", function(d) {
				zoom(d, treemap);
			})
			.append("svg")
			.attr("class", "clip")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", headerHeight);
			parentEnterTransition.append("rect")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", headerHeight)
			.style("fill", headerColor);
			parentEnterTransition.append('text')
			.attr("class", "label")
			.attr("transform", "translate(3, 13)")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", headerHeight)
			.text(function(d) {
				return d.name;
			});
			// update transition
			var parentUpdateTransition = parentCells.transition().duration(transitionDuration);
			parentUpdateTransition.select(".cell")
			.attr("transform", function(d) {
				return "translate(" + d.dx + "," + d.y + ")";
			});
			parentUpdateTransition.select("rect")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", headerHeight)
			.style("fill", headerColor);
			parentUpdateTransition.select(".label")
			.attr("transform", "translate(3, 13)")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", headerHeight)
			.text(function(d) {
				return d.name;
			});
			// remove transition
			parentCells.exit()
			.remove();

			// create children cells
			var childrenCells = chart.selectAll("g.cell.child")
			.data(children, function(d) {
				return "c-" + d.name;
			});
			// enter transition
			var childEnterTransition = childrenCells.enter()
			.append("g")
			.attr("class", "cell child")
			.on("click", function(d) {
				zoom(node === d.parent ? root : d.parent, treemap);
			})
			.append("svg")
			.attr("class", "clip");
			childEnterTransition.append("rect")
			.classed("background", true)
			.style("fill", function(d) {
				return color(d.name);
			});
			childEnterTransition.append('text')
			.attr("class", "label")
			.attr('x', function(d) {
				return d.dx / 2;
			})
			.attr('y', function(d) {
				return d.dy / 2;
			})
			.attr("dy", ".35em")
			.attr("text-anchor", "middle")
			.style("display", "none")
			.text(function(d) {
				return d.name;
			});
			// update transition
			var childUpdateTransition = childrenCells.transition().duration(transitionDuration);
			childUpdateTransition.select(".cell")
			.attr("transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			});
			childUpdateTransition.select("rect")
			.attr("width", function(d) {
				return Math.max(0.01, d.dx);
			})
			.attr("height", function(d) {
				return d.dy;
			})
			.style("fill", function(d) {
				return color(d.name);
			});
			childUpdateTransition.select(".label")
			.attr('x', function(d) {
				return d.dx / 2;
			})
			.attr('y', function(d) {
				return d.dy / 2;
			})
			.attr("dy", ".35em")
			.attr("text-anchor", "middle")
			.style("display", "none")
			.text(function(d) {
				return d.name;
			});
			
			childrenCells.append("squaretitle")
			.text(function(d) { return "<b>"+d.name+"</b>: <br /> "+locale.activities+": "+d.requests+"<br /> "+locale.user+": "+d.user+" </br>"+Math.round(d.value/data.value*100*10)/10+" %";});

			// exit transition
			childrenCells.exit()
			.remove();

			d3.select("select").on("change", function() {
				console.log("select zoom(node)");
				treemap.value(size)
				.nodes(root);
				zoom(node, treemap);
			});

			zoom(node, treemap);
		};


		function size(d) {
			return d.requests;
		}


		function count(d) {
			return 1;
		}


		//and another one
		function textHeight(d) {
			var ky = chartHeight / d.dy;
			yscale.domain([d.y, d.y + d.dy]);
			return (ky * d.dy) / headerHeight;
		}

		function getRGBComponents(color) {
			var r = color.substring(1, 3);
			var g = color.substring(3, 5);
			var b = color.substring(5, 7);
			return {
				R: parseInt(r, 16),
				G: parseInt(g, 16),
				B: parseInt(b, 16)
			};
		}


		function idealTextColor(bgColor) {
			var nThreshold = 105;
			var components = getRGBComponents(bgColor);
			var bgDelta = (components.R * 0.299) + (components.G * 0.587) + (components.B * 0.114);
			return ((255 - bgDelta) < nThreshold) ? "#000000" : "#ffffff";
		}


		function zoom(d,treemap) {
			treemap
			.padding([headerHeight / (chartHeight / d.dy), 0, 0, 0])
			.nodes(d);

			// moving the next two lines above treemap layout messes up padding of zoom result
			var kx = chartWidth / d.dx;
			var ky = chartHeight / d.dy;
			var level = d;

			xscale.domain([d.x, d.x + d.dx]);
			yscale.domain([d.y, d.y + d.dy]);

			if (node != level) {
				chart.selectAll(".cell.child .label")
				.style("display", "inline");
			} else {
				chart.selectAll(".cell.child .label")
				.style("display", "none");				
			}

			var zoomTransition = chart.selectAll("g.cell").transition().duration(transitionDuration)
			.attr("transform", function(d) {
				return "translate(" + xscale(d.x) + "," + yscale(d.y) + ")";
			})
			.each("start", function() {
				d3.select(this).select("label")
				.style("display", "none");
			})
			.each("end", function(d, i) {
				if (!i && (level !== self.root)) {
					chart.selectAll(".cell.child")
					.filter(function(d) {
						return d.parent === self.node; // only get the children for selected group
					})
					.select(".label")
					.style("display", "")
					.style("fill", function(d) {
						return idealTextColor(color(d.name));
					});
				}
			});

			zoomTransition.select(".clip")
			.attr("width", function(d) {
				return Math.max(0.01, (kx * d.dx));
			})
			.attr("height", function(d) {
				return d.children ? headerHeight : Math.max(0.01, (ky * d.dy));
			});

			zoomTransition.select(".label")
			.attr("width", function(d) {
				return Math.max(0.01, (kx * d.dx));
			})
			.attr("height", function(d) {
				return d.children ? headerHeight : Math.max(0.01, (ky * d.dy));
			})
			.text(function(d) {
				return d.name;
			});

			zoomTransition.select(".child .label")
			.attr("x", function(d) {
				return kx * d.dx / 2;
			})
			.attr("y", function(d) {
				return ky * d.dy / 2;
			});

			zoomTransition.select("rect")
			.attr("width", function(d) {
				return Math.max(0.01, (kx * d.dx));
			})
			.attr("height", function(d) {
				return d.children ? headerHeight : Math.max(0.01, (ky * d.dy));
			})
			.style("fill", function(d) {
				return d.children ? headerColor : color(d.name);
			});

			node = d;

			if (d3.event) {
				d3.event.stopPropagation();
			}
		}
	};

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
