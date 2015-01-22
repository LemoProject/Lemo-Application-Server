(function(d3custom, $, undefined) {


	d3custom.run = function() {
		  var ajaxDataRenderer = function(url) {
			    var ret = [],
			        shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'];
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
			      dataType:"json",
			      success: function(data) {

				ret.push({
					key: 'Durchgefallen',
					values: []
				})
				ret.push({
					key: 'Bestanden',
					values: []
				})
				var classValue;
				for (j = 0; j < data.elements.length; j++) {
					if(data.elements[j].imageCount>0){
					   classValue=0;
					   if(data.elements[j].progressPercentage>=80) classValue=1;
					      ret[classValue].values.push({
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
					      });
					}
				}
			     }
			    });
			    return ret;
			  };

			//Format A
			var chart;
			  // The url for our json data
			var jsonurl = 'http://localhost:8081/lemo/dms/questions/queryDatabase/';

			nv.addGraph(function() {
			  chart = nv.models.scatterChart()
			                .showDistX(false)
			                .showDistY(false)
			                .useVoronoi(true)
			                .color(d3.scale.category10().range())
			                .transitionDuration(300)
					.size(10).sizeRange([500,1000]);

			  chart.xAxis.tickFormat(d3.format('d'));
			  chart.xAxis.axisLabel("#Downvotes");
			  chart.xAxis.tickSubdivide(1);
			  chart.yAxis.tickFormat(d3.format('d')); //.02f
			  chart.yAxis.axisLabel("#Linkcount");
			  chart.yAxis.axisLabelDistance(40);
			  chart.tooltipContent(function(key, xVal, yVal, e, chart) {
			      return '<h4>User: ' + e.point.userId + '</h4>' + 
					'<p>downVotes:' + e.point.downVotes + '</p>' +
					'<p>upVotes:' + e.point.upVotes + '</p>' +
					'<p>linkCount:' + e.point.linkCount + '</p>' +
					'<p>wordCount:' + e.point.wordCount + '</p>' +
					'<p>Progress Percentage:' + e.point.progressPercentage + '</p>' +
					'<p>imageCount:' + e.point.imageCount + '</p>';
			  });

			  d3.select('#viz').
			      .datum(ajaxDataRenderer(jsonurl))
			      .call(chart);

			  nv.utils.windowResize(chart.update);

			  chart.dispatch.on('stateChange', function(e) { ('New State:', JSON.stringify(e)); });

			  return chart;
			});
	};

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);