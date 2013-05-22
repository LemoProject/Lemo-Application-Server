(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	 
	  
	 var data = d3custom.data;
	 
	 
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
	  var width = 800,
	  	 height = 600,
	  	  days = [],
	  	  hours = [],
	  	  counter = 0,
	  	  daysMin = 0,
	  	  daysMax = 0,
	  	  hoursMin = 0,
	  	  hoursMax = 0;
	  
	  var dataPerHour, dataPerDay, dataPerQuarterDay, 
	  	  boxPlotData = [],
	  	  boxPlotDataPerQuarterDay = [],
  	  counter = 0,
  	  innerCounter = 0;
	  overAllCounter = 0;
  	  valueMin = 0,
  	  valueMax = 0,
	  dataAmount = 0;
	  
 console.log("DATA LENGTH: "+data.length);	
  	  
  //Cutting out data cumulated per day monday-sunday 
  dataPerDay = data.splice(0,7)
  
  //Cutting out data cumulated per hour 0-24 
  dataPerHour = data.splice(0,24)  
  
  //Cutting out data cumulated per quarter day 0-6h 7-12h 13-18h 19-24h 
  dataPerQuarterDay = data.splice(0,28)  
	 
  dataAmount = dataPerDay.length + dataPerQuarterDay.length; 
  
  function normalizeBoxPlotData(v){

	// "+" Prefix on strings make JS recognize them as numbers  
	if(v.lowerWhisker){
		  if(+v.lowerWhisker < +valueMin) valueMin = v.lowerWhisker;
	} else v.lowerWhisker = 0;
	if(v.upperWhisker){
		  if(+v.upperWhisker > +valueMax) valueMax = v.upperWhisker;
	} else v.upperWhisker = 0;
	if(!v.lowerQuartil) v.lowerQuartil=0;
	if(!v.upperQuartil) v.upperQuartil=0;
	if(!v.median) v.median=0;
	return v;
  }
  
  var vQuarter, normV, normQuarterV;
  
  //Seperating days per week and hours per day results and calculating Min and Max Values for domain setup
  $.each(dataPerDay, function(i,v) {
	  
		console.log("OUTERLOOP: Counter: "+counter+" QuizMin: "+valueMin+" QuizMax: "+valueMax+ " UW: "+v.upperWhisker+ " LW: "+v.lowerWhisker);
		  // "+" Prefix on strings make JS recognize them as numbers  
		normV = normalizeBoxPlotData(v);
		normV.id = overAllCounter;
		boxPlotData.push(normV); 
		for (var i = counter*4; i < ((counter+1)*4); i++){
			overAllCounter++;
			console.log("INNERLOOP: i: "+i+" QuarterDay length: "+dataPerQuarterDay.length);
			vQuarter = dataPerQuarterDay[i];
			console.log("INNERLOOP: UW: "+vQuarter.upperWhisker+ " LW: "+vQuarter.lowerWhisker);
			vQuarter.name = normV.name.slice(0,3)+" "+(innerCounter*6)+"-"+(((innerCounter+1)*6))+"h";
			normQuarterV = normalizeBoxPlotData(vQuarter);
			normQuarterV.id = overAllCounter;
			
			//
			boxPlotDataPerQuarterDay.push(normQuarterV); 
			innerCounter++;
			
		}
		innerCounter = 0;
	    counter++;
	    overAllCounter++;
  });
  
  console.log("Counter:"+counter+" QuizMin: "+valueMin+" QuizMax: "+valueMax);
  
  var marginViz = {top: 5, right: 0, bottom: 100, left: 50},
  	marginPlot = {top: 5, right: 10, bottom: 0, left: 10},
    w = 30 - marginPlot.left - marginPlot.right,
    h = height - marginViz.top - marginViz.bottom;

  var min = valueMin,
      max = valueMax;

  if(min == 0 && max == 0) {
    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
    	return;
  }
  
  //Generating the tick names for the x-axis
  var timeSlots = boxPlotData.map(function(d){return d.name;}); 	  

   
   //Adding empty slot to shift all entries one step to the left
   timeSlots.splice(0,0,' ');
   
 
 
  var xScale = d3.scale.ordinal()
	  .rangePoints([0, width - marginViz.left -10 ])
	  .domain(timeSlots);
	
	var yScale = d3.scale.linear()
	  .range([h, 0])
	  .domain([0,max]);
	
	var xAxis = d3.svg.axis()
	  .scale(xScale)
	  .orient("bottom")
      .ticks(dataAmount,function(d, i) {
        return d.name;
      });
	
	var yAxis = d3.svg.axis()
	  .scale(yScale)
	  .orient("left");
	  
      var box = d3.box()
	    .whiskers(iqr(1.5))
	    .width(w)
	    .height(h);

	  box.domain([min, max]);
	  
	  var div = d3.select("#viz");
    		
    	
	  var svgBox = div.append("svg:svg")
	  	.attr("width", width)
	  	.attr("height", height);
	  
	  
	  svgBox.append("g")
	      .attr("class","x axis grid")
	      .attr("transform", "translate("+marginViz.left+"," + h +")")
	      .call(make_x_axis()
            .tickSize(-height, 0, 0)
            .tickFormat(""))
	      .call(xAxis) 
	    .selectAll("text")  
	    	.attr("class", function(d){return "boxId-"+d.replace(new RegExp("[\W :]","g"),"_");})
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
                })
          
	  svgBox.append("g")
	      .attr("class", "y axis grid")
	      .attr("transform", "translate("+marginViz.left+", "+ 0 +")")
	      .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat(""))
          .call(yAxis)
	    .append("text")
	      .attr("transform", "translate(-50, 0) rotate(-90)")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "end")
	      .text("Zugriffe");
	  
	  
	  svgBox.selectAll("x axis").append("text")
	      .attr("class","legend") 	
	      .attr("transform", "translate("+marginViz.left+"," + h +")")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "middle")
	      .text("Zeitraum");
	  
	  
	   var gBox = svgBox.selectAll("g.box")
	    	.data(boxPlotData, function(d, i) { console.log("Name: "+d.name+" UW: "+d.upperWhisker); return d;} )
	     .enter().append("g")
    		.attr("class", function(d) {return "box gbox-"+d.name.replace(new RegExp("[\W :]","g"),"_");})
    		.attr("transform", function (d,i) {return "translate(" + (xScale(d.name)+marginViz.left-14)+",0)"; })
    		.attr("width", w + marginPlot.left + marginPlot.right)
    		.attr("height", h + marginViz.bottom + marginViz.top)
    		.on("mouseover",function(d) {	
    				gBox.selectAll("text.boxText-"+d.name.replace(new RegExp("[\W :]","g"),"_")).style("fill","#0000") 
    				svgBox.selectAll("text.boxId-"+d.name.replace(new RegExp("[\W :]","g"),"_")).style("fill","red") 
    		})
    		.on("mouseout",function(d) {
    				gBox.selectAll("text.boxText-"+d.name.replace(new RegExp("[\W :]","g"),"_")).style("fill","none") 
    				svgBox.selectAll("text.boxId-"+d.name.replace(new RegExp("[\W :]","g"),"_")).style("fill","#0000") 
    		})
    		
  
	  gBox.append("g")
		.attr("transform", "translate(" + marginPlot.left + "," + 0 + ")")
		.call(box);
	 
	   
	   //Adding helper grid lines for x-axis
	   function make_x_axis() {        
		    return d3.svg.axis()
		         .scale(xScale)
		         .orient("bottom")
		         .ticks(5)
		}
	   //Adding helper grid lines for y-axis
		function make_y_axis() {        
		    return d3.svg.axis()
		        .scale(yScale)
		        .orient("left")
		        .ticks(5)
		}   

	// Returns a function to compute the interquartile range.
      function iqr(k) {
        return function(d, i) {
          var q1 = d.quartiles[0],
              q3 = d.quartiles[2],
              iqr = (q3 - q1) * k,
              i = -1,
              j = d.length;
          while (d[++i] < q1 - iqr);
          while (d[--j] > q3 + iqr);
          return [i, j];
        };
      }
      
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
