(function(d3custom, $, undefined) {
	d3custom.prepareData = function(data,xAxis,yAxis) {
		var ret = [],
		shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'];
		ret.push({
			key: 'nicht bestanden',
			values: []
		})
		ret.push({
			key: 'bestanden',
			values: []
		})
		var instance;
		var countInstances = 0;
		for (j = 0; j < data.elements.length; j++) {
			instance = {
				x: data.elements[j].wordCount,
				y: data.elements[j].imageCount, 
				downVotes: data.elements[j].downVotes,
				upVotes: data.elements[j].upVotes, 
				linkCount: data.elements[j].linkCount,
				wordCount: data.elements[j].wordCount, 
				imageCount: data.elements[j].imageCount,
				progressPercentage: data.elements[j].progressPercentage,
				userId: data.elements[j].userId, 
				shape: shapes[j % 6]
			}
			if(xAxis=="Downvotes"){
				instance.x = data.elements[j].downVotes;
			} else if (xAxis=="Upvotes"){
				instance.x = data.elements[j].upVotes;
			} else if (xAxis=="Linkcount"){
				instance.x = data.elements[j].linkCount;
			} else if (xAxis=="Wordcount"){
				instance.x = data.elements[j].wordCount;
			} else if (xAxis=="Imagecount"){
				instance.x = data.elements[j].imageCount;
			} else if (xAxis=="Progress Percentage"){
				instance.x = data.elements[j].progressPercentage;
			};
			if(yAxis=="Downvotes"){
				instance.y = data.elements[j].downVotes;
			} else if (yAxis=="Upvotes"){
				instance.y = data.elements[j].upVotes;
			} else if (yAxis=="Linkcount"){
				instance.y = data.elements[j].linkCount;
			} else if (yAxis=="Wordcount"){
				instance.y = data.elements[j].wordCount;
			} else if (yAxis=="Imagecount"){
				instance.y = data.elements[j].imageCount;
			} else if (yAxis=="Progress Percentage"){
				instance.y = data.elements[j].progressPercentage;
			};
			if(instance.x > 0 || instance.y > 0){ 
				countInstances+=1;
				ret[data.elements[j].classId].values.push(instance);
			}			
		}
		$( "#numberOfInstances" ).html( "Number of displayed instances: " + countInstances );
		return ret;
	}

	d3custom.run = function() {
		var ajaxDataRenderer = function(url) {
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
				data:"cid="+$("#current_course option:selected").index()+
						"&targetCourseId="+$("#reference_course option:selected").index(),
				error: function(x, t, m) {
					if(t==="timeout") {
						alert("got timeout");
					} else {
						alert(x);
					}
				},
				dataType:"json",
				success: function(data) {
					ret=data;
				}
			});
			console.log("cid="+$("#current_course option:selected").index()
						+"&targetCourseId="+$("#reference_course option:selected").index());
			return ret;
		};

		//Format A
		var chart;
		// The url for our json data
		var jsonurl = 'http://localhost:8081/lemo/dms/questions/queryDatabase/';
		
		d3custom.rawData = ajaxDataRenderer(jsonurl);

		nv.addGraph(function() {
			var xAxis = $("#x_Axis option:selected").text();
			var yAxis = $("#y_Axis option:selected").text();
			chart = nv.models.scatterChart()
			.showDistX(false)
			.showDistY(false)
			.useVoronoi(true)
			.color(d3.scale.category10().range())
			.transitionDuration(300)
			.size(10).sizeRange([500,1000]);

			chart.xAxis.tickFormat(d3.format('d'));
			chart.xAxis.tickSubdivide(1);
			chart.xAxis.axisLabel(xAxis);
			chart.yAxis.tickFormat(d3.format('d')); //.02f
			chart.yAxis.axisLabelDistance(20);
			chart.yAxis.axisLabel(yAxis);
			chart.tooltipContent(function(key, xVal, yVal, e, chart) {
				return '<h4>User: ' + e.point.userId + '</h4>' + 
				'<p>downVotes:' + e.point.downVotes + '</p>' +
				'<p>upVotes:' + e.point.upVotes + '</p>' +
				'<p>linkCount:' + e.point.linkCount + '</p>' +
				'<p>wordCount:' + e.point.wordCount + '</p>' +
				'<p>Progress Percentage:' + e.point.progressPercentage + '</p>' +
				'<p>imageCount:' + e.point.imageCount + '</p>';
			});

			d3custom.data = d3custom.prepareData(d3custom.rawData,xAxis,yAxis);
			d3.select('#viz svg')
			.datum(d3custom.data)
			.call(chart);

			nv.utils.windowResize(chart.update);

			chart.dispatch.on('stateChange', function(e) { ('New State:', JSON.stringify(e)); });

			return chart;
		});

		d3custom.changeDesign = function(){
			var xAxis = $("#x_Axis option:selected").text();
			var yAxis = $("#y_Axis option:selected").text();
			for(var i=0; i < nv.graphs.length;i++){
				nv.graphs[i].xAxis.axisLabel(xAxis);
				nv.graphs[i].yAxis.axisLabel($("#y_Axis option:selected").text());
				d3custom.data=d3custom.prepareData(d3custom.rawData,xAxis,yAxis);
				d3.select('#viz svg')
				.datum(d3custom.data);
				//d3custom.selectData(xAxis,yAxis);
				nv.graphs[i].update();				
			}
		}
		$("#x_Axis").change(d3custom.changeDesign);
		$("#y_Axis").change(d3custom.changeDesign);
		//http://www.elijahmanor.com/dont-initialize-all-the-things-in-jquery-ready/
	};

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);