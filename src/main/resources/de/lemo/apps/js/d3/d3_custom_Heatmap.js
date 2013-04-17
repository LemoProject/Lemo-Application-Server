(function(d3custom, $) {

  d3custom.run = function() {
	  
	  function timeConverter(UNIX_timestamp){
		  var a = new Date(UNIX_timestamp);
		  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
		      var year = a.getFullYear();
		      var month = months[a.getMonth()];
		      var date = a.getDate();
		      var hour = a.getHours();
		      var min = a.getMinutes();
		      var sec = a.getSeconds();
		      var time = date+'. '+month+' '+year;
		      return time;
		  }
	  
	  var data = d3custom.data;
	  	
	  var cells,
	    rowNames=[],
	    colNames=[],
	    rowNum,
	    colNum,
	    color = d3.interpolateRgb("#fff", "#37c"),
	    width=$('#heatmap').width(),
	    height=595;
 
		$(window).resize(function() {
			console.log("resized");
			console.log($('#heatmap').width());
			width = $('#heatmap').width();
			d3.select("svg")
       			.remove();
			createHeatchart();
		});
 	
	    var calculateCells = function() {
	        var index=0;
	        rowNum = data.length;
	        colNum = data[0].values.length;
	        for (var i=0; i < data.length; i++) {
	            rowNames[i] = data[i].key;
	            if (data[i].values.length > colNum) {
	                colNum = data[i].values.length;
	                index = i;
	            }
	        }
	        for (var i=0; i<data[index].values.length; i++) {
	            colNames[i]=data[index].values[i][0];
	        }
	        cells = new Array(rowNum);
	        for (var i = 0; i < rowNum; i++) {
	           cells[i] = new Array(colNum);
	        }
	        for (var i = 0; i<data.length; i++) {
	            for (var j = 0; j < data[i].values.length; j++) {                
	                cells[i][colNames.indexOf(data[i].values[j][0])]={};
	                cells[i][colNames.indexOf(data[i].values[j][0])].clicks=data[i].values[j][1];
	                cells[i][colNames.indexOf(data[i].values[j][0])].row=i;
	                cells[i][colNames.indexOf(data[i].values[j][0])].col=colNames.indexOf(data[i].values[j][0]);
	            }
	        }
	    }    

	    var createHeatchart = function() {

	    var min = 0;
	    var max = 0;
	    var l;

	    for (var rowNum = 0; rowNum < cells.length; rowNum++) {
	        for (var col = 0; col < colNum; col++) {
	            l = cells[rowNum][col].clicks;

	            if (l > max) {
	                max = l;
	            }
	            if (l < min) {
	                min = l;
	            }
	        }
	    }

	    var heatchart = d3.select("div#heatmap").append("svg:svg").attr("width", width).attr("height", height);

	    var scaleY = d3.scale.linear().domain([0,colNum+1]).range([0,height]);
	    var scaleX = d3.scale.linear().domain([0,rowNum+1]).range([0,width]);

	    var colG = heatchart.append("svg:g")
	    .attr("class","columnNames");

	    var colNameG = colG.selectAll("g.columnNames")
	    .data(rowNames)
	    .enter().append("svg:g")
	    .attr("class","colName")
	    .attr("width", scaleX(1))
	    .append("svg:text")
	    .attr("x", function(d, i) {
	        return scaleX(i+1)+0.5*scaleX(1);
	    }) .
	    attr("y", function(d, i) {
	        return scaleY(0)+0.5*scaleY(1);
	    })
	    .attr("font-size",function(d) {return ""+12+"px"})
	    .attr("text-anchor", "middle")    
	    .attr("font-family", "sans-serif")
	    .text(function(d) {
	  		return d;
	    });

	    var rowG = heatchart.append("svg:g")
	    .attr("class","rowNames");

	    var rowNameG = rowG.selectAll("g.rowNames")
	    .data(colNames)
	    .enter().append("svg:g")
	    .attr("class","colName")
	    .attr("width", scaleX(1))
	    .append("svg:text")
	    .attr("x", function(d, i) {
	        return scaleX(0)+0.5*scaleX(1);
	    }) .
	    attr("y", function(d, i) {
	        return scaleY(i+1)+0.5*scaleY(1);
	    })
	    .attr("font-size",function(d) {return ""+12+"px"})
	    .attr("text-anchor", "middle")    
	    .attr("font-family", "sans-serif")
	    .text(function(d) {
	    	return timeConverter(d);	    	
	    });

	    var g = heatchart.selectAll("g.row")
	    .data(cells)
	    .enter().append("svg:g")
	    .attr("class", "row");
	    
	    var g2 = g.selectAll("rect").data(function(d) {
	        return d;
	    })
	    .enter().append("svg:g")
	    .attr("class", "cell");
	    g2.append("svg:rect").attr("x", function(d, i) {
	        return scaleX(d.row+1);
	    })
	    .attr("y", function(d, i) {
	        return scaleY(d.col+1);
	    })
	    .attr("width", scaleX(1))
	    .attr("height", scaleY(1))
	    .attr("fill", function(d, i) {
	        return color(d.clicks/max);
	    })
	    .attr("stroke", "#fff")
	    g2.append("svg:text")
	    .attr("x", function(d, i) {
	        return scaleX(d.row+1)+0.5*scaleX(1);
	    }) .
	    attr("y", function(d, i) {
	        return scaleY(d.col+1)+0.5*scaleY(1);
	    })
	    .attr("font-size",function(d) {return ""+12+"px"})
	    .attr("text-anchor", "middle")    
	    .attr("font-family", "sans-serif")
	    .text(function(d) {return d.clicks});    
	};



	var init = function() {
	    calculateCells();
	    createHeatchart();
	};

	init();

  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
