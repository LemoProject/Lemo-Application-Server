(function(d3vis, $, undefined) {
	d3vis.run = function() {
			  window.translate = function(x, y) {
			    return "translate(" + x + "," + y + ")";
			  };

			  window.getRandomRange = function(min, max) {
			    return Math.random() * (max - min) + min;
			  };

			  window.range = function(end) {
			    var array, i;
			    array = new Array();
			    i = 0;
			    while (i < end) {
			      array.push(i);
			      i++;
			    }
			    return array;
			  };

			  window.nested_min_max = function(data, nested_key, fn) {
			    return [
			      d3.min(data, function(x) {
			        return d3.min(x[nested_key], fn);
			      }), d3.max(data, function(x) {
			        return d3.max(x[nested_key], fn);
			      })
			    ];
			  };

			  window.unique = function(array) {
			    var i, l, o, r;
			    o = {};
			    i = void 0;
			    l = this.length;
			    r = [];
			    i = 0;
			    while (i < l) {
			      o[array[i]] = array[i];
			      i += 1;
			    }
			    for (i in o) {
			      r.push(o[i]);
			    }
			    return r;
			  };

	    // The url for the json data
	    var jsonurl = "http://localhost:8081/lemo/dms/questions/queryDatabase/";
	    d3vis.rawData = ajaxDataRenderer(jsonurl);
		d3vis.addGraph(d3vis.rawData.elements);
	};

	  var ajaxDataRenderer = function(url) {
		  var course1 = $("#current_course")[0].selectedIndex;
		  var course2 = $("#reference_course")[0].selectedIndex;
		  console.log("1:"+course1+" 2: "+course2);
		    var ret = [];
		    $.ajax({
		      // have to use synchronous here, else the function
		      // will return before the data is fetched
		      type:"POST",
		      async: false,
		      url: url,
			 beforeSend: function( xhr ) {
			xhr.overrideMimeType( "text/plain; charset=x-user-defined" );
			},
		      error: function(x, t, m) {
		        if(t==="timeout") {
		            alert("got timeout");
		        } else {
		            alert(x);
		        }
		},
			data:"cid="+course1+"&targetCourseId="+course2,
		      dataType:"json",
		      success: function(data) {

			ret = data;
		     }
		    });
		    return ret;
		  };
		  
	var getVariableName = function(name){
		if(name==="Answercount"){
			return "answerCount";
		}else if(name==="ClassId"){
			return "classId";
		}else if(name==="Commentcount"){
			return 'commentCount';
		}else if(name==="Downvotes"){
			return "downVotes";
		}else if(name==="Imagecount"){
			return "imageCount";
		}else if(name==="Linkcount"){
			return "linkCount";
		}else if(name==="Postcount"){
			return "postCount";
		}else if(name==="Post Rating Max"){
			return "postRatingMax";
		}else if(name==="Post Rating Min"){
			return "postRatingMin";
		}else if(name==="Post Rating Sum"){
			return "postRatingSum";
		}else if(name==="Progress Percentage"){
			return "progressPercentage";
		}else if(name==="Received Downvotes"){
			return "receivedDownVotes";
		}else if(name==="Reveived Upvotes"){
			return "receivedUpVotes";
		}else if(name==="Segment Progress"){
			return "segmentProgress";
		}else if(name==="Upvotes"){
			return "upVotes";
		}else if(name==="Wordcount"){
			return "wordCount";
		}		
	}

	d3vis.addGraph = function(dataset) {

		var xAxisText = $("#x_Axis option:selected").text();
		var yAxisText = $("#y_Axis option:selected").text();
		

	        var margin = {top: 10, right: 10, bottom: 60, left: 60},
	            width = 950 - margin.left - margin.right,
	            height = 550 - margin.top - margin.bottom;
	        var centered = undefined;

	        var svg = d3.select("#viz")
	            .append("svg")
	            .attr("width", width + margin.left + margin.right)
	            .attr("height", height + margin.top + margin.bottom)
	            .append("g")
	            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	        svg.append("defs").append("clipPath")
	            .attr("id", "clip")
	            .append("rect")
	            .attr("width", width)
	            .attr("height", height);				

	        var xScale = d3.scale.linear()
	            .domain([d3.min(dataset, function(d) {
	                return d[getVariableName(xAxisText)];
	            }), d3.max(dataset, function(d) {
	                return d[getVariableName(xAxisText)];
	            })])
	            .range([0, width]);
	        var yScale = d3.scale.linear()
	            .domain([d3.min(dataset, function(d) {
	                return d[getVariableName(yAxisText)];
	            }), d3.max(dataset, function(d) {
	                return d[getVariableName(yAxisText)];
	            })])
	            .range([height, 0]);

	        radiusScale = d3.scale.sqrt()
	            .domain([d3.min(dataset, function(d) {
	                return d['answerCount'];
	            }), d3.max(dataset, function(d) {
	                return d['answerCount'];
	            })])
	            .range([3, 15]);

	        var rect = svg.append("rect")
	            .attr("class", "background")
	            .attr("pointer-events", "all")
	            .attr("fill", "none")
	            .attr("width", width)
	            .attr("height", height)
	            .call(d3.behavior.zoom().x(xScale).y(yScale).on("zoom", redraw));

	        var circles = svg.selectAll("circle")
	            .data(dataset)
	            .enter()
	            .append("circle")
	            .attr("clip-path", "url(#clip)")
	            .attr("r", function(d) {
	                return radiusScale(d['answerCount']);
	            })
	            .attr("fill", function(d) {
	                return d['classId']===0?"skyblue":"orange";
	            });

	        circles.append("nodetitle")
			.text(function(d) {
				var tooltip = "UserId: " + d.userId +
								"</br>Class: " + d.classId +
								"</br>Commentcount: " + d.commentCount +
								"</br>Downvotes: " + d.downVotes +
								"</br>ImageCount: " + d.imageCount +
								"</br>Linkcount: " + d.linkCount +
								"</br>Postcount: " + d.postCount +
								"</br>Post Rating Max: " + d.postRatingMax +
								"</br>Post Rating Min: " + d.postRatingMin +
								"</br>Post Rating Sum: " + d.postRatingSum +
								"</br>Progress Percentage: " + d.progressPercentage +
								"</br>Received Up Votes: " + d.receivedUpVotes +
								"</br>Received Down Votes: " + d.receivedDownVotes +
								"</br>SegmentProgress: " + d.segmentProgress +
								"</br>Upvotes: " + d.upVotes +
								"</br>Wordcount: " + d.wordCount;
				return tooltip;
				});
	        
			$('nodetitle').parent().tipsy({ 
				gravity: 'sw', 
				html: true, 
				title: function() { return $(this).find('nodetitle').text(); }
			});

	        var xAxis = d3.svg.axis()
	            .scale(xScale)
	            .orient("bottom")
	            .ticks(5)
	            .tickSize(-height)
	            .tickFormat(d3.format("s"));

	        var yAxis = d3.svg.axis()
	            .scale(yScale)
	            .orient("left")
	            .ticks(10)
	            .tickFormat(d3.format("s"))
	            .tickSize(-width);

	        svg.append("g")
	            .attr("class", "x axis")
	            .attr("transform", "translate(0," + (height) + ")")
	            .call(xAxis);
	        svg.append("g")
	            .attr("class", "y axis")
	            .attr("transform", "translate(" + 0 + ",0)")
	            .call(yAxis);


	        svg.append("text")
	            .attr("class", "x label")
	            .attr("text-anchor", "middle")
	            .attr("x", width - width / 2)
	            .attr("y", height + margin.bottom / 2)
	            .text($("#x_Axis option:selected").text());


	        svg.append("text")
	            .attr("class", "y label")
	            .attr("text-anchor", "middle")
	            .attr("y", -margin.left + 5)
	            .attr("x", 0 - (height / 2))
	            .attr("dy", "1.5em")
	            .attr("transform", "rotate(-90)")
	            .text($("#y_Axis option:selected").text());

	        var objects = svg.append("svg")
	            .attr("class", "objects")
	            .attr("width", width)
	            .attr("height", height);

	        hAxisLine = objects.append("svg:line")
	            .attr("class", "axisLine hAxisLine");
	        vAxisLine = objects.append("svg:line")
	            .attr("class", "axisLine vAxisLine");


	        function redraw(duration) {
	            var duration = typeof duration !== 'undefined' ? duration : 0;
	            if (d3.event) {
	                svg.select(".x.axis").call(xAxis);
	                svg.select(".y.axis").call(yAxis);
	            }


	            hAxisLine.transition().duration(duration)
	                .attr("x1", 0)
	                .attr("y1", 0)
	                .attr("x2", width)
	                .attr("y2", 0)
	                .attr("transform", "translate(0," + (yScale(0)) + ")");
	            vAxisLine.transition().duration(duration)
	                .attr("x1", xScale(0))
	                .attr("y1", yScale(height))
	                .attr("x2", xScale(0))
	                .attr("y2", yScale(-height));


	            circles.transition().duration(duration)
	                .attr("cx", function(d) {
	                    return xScale(d[getVariableName(xAxisText)]);
	                })
	                .attr("cy", function(d) {
	                    return yScale(d[getVariableName(yAxisText)]);
	                })

	        }; 
	        redraw(0);
			d3vis.changeDesign = function(){
				d3.select("#viz svg").remove();
				d3vis.addGraph(d3vis.rawData.elements);
			}
			$("#x_Axis").change(d3vis.changeDesign);
			$("#y_Axis").change(d3vis.changeDesign);
	};

})(window.d3vis = window.d3vis || {}, jQuery);

$(document).ready(window.d3vis.run);