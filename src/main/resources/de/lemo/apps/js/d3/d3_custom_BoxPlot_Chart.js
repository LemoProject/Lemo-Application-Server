(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	  var data_test = [[850,740,500,400,600,720],[830,650,500,600,720,580]];
	  
	 var data = d3custom.data;
	 
	 
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
	  var width = 800,
	  	 height = 500,
	  		days = [],
	  	  hours = [],
	  	  counter = 0,
	  	  daysMin = 0,
	  	  daysMax = 0,
	  	  hoursMin = 0,
	  	  hoursMax = 0;
	  
	  var quizzes = [],
  	  counter = 0,
  	  quizMin = 0,
  	  quizMax = 0;
  
  //Seperating days per week and hours per day results and calculating Min and Max Values for domain setup
  $.each(data, function(i,v) {
	  
		console.log("QuizMin: "+quizMin+" QuizMax: "+quizMax+ " UW: "+v.upperWhisker+ " LW: "+v.lowerWhisker);
		  // "+" Prefix on strings make JS recognize them as numbers  
		if(v.lowerWhisker){
			  if(+v.lowerWhisker < +quizMin) quizMin = v.lowerWhisker;
		} else v.lowerWhisker = 0;
		if(v.upperWhisker){
			  if(+v.upperWhisker > +quizMax) quizMax = v.upperWhisker;
		} else v.upperWhisker = 0;
		if(!v.lowerQuartil) v.lowerQuartil=0;
		if(!v.upperQuartil) v.upperQuartil=0;
		if(!v.median) v.median=0;
		quizzes.push(v); 
	    counter++;
  });
  
  console.log("Counter:"+counter+" QuizMin: "+quizMin+" QuizMax: "+quizMax);
  
  var marginViz = {top: 5, right: 10, bottom: 0, left: 50},
  	marginPlot = {top: 5, right: 10, bottom: 0, left: 10},
    w = 30 - marginPlot.left - marginPlot.right,
    h = height - marginViz.top - marginViz.bottom;

  var min = quizMin,
      max = quizMax;

  if(min == 0 && max == 0) {
    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
    	return;
  }
  
   var users = data.map(function(d){return d.name;}); 
   
   users.splice(0,0,' ');
   
   //users.push([""]); //empty
  // users.push(['_']);
 
 
  var xScale = d3.scale.ordinal()
	  .rangePoints([0, width])
	  .domain(users);
	
	var yScale = d3.scale.linear()
	  .range([height, 0])
	  .domain([0,100]);
	
	var xAxis = d3.svg.axis()
	  .scale(xScale)
	  .orient("bottom")
	  .ticks(data.length,function(d, i) {
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
	      .attr("class","x axis")
	      .attr("transform", "translate("+marginViz.left+"," + height +")")
	      .call(xAxis)    
	    .selectAll("text")  
	    	.attr("class", function(d){return "boxId-"+d;})
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
                })
          
	  svgBox.append("g")
	      .attr("class", "y axis")
	      .attr("transform", "translate("+marginViz.left+", 0)")
	      .call(yAxis)
	    .append("text")
	      .attr("transform", "translate(-50, 0) rotate(-90)")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "end")
	      .text("Performance in %");
	  
	  
	  svgBox.selectAll("x axis").append("text")
	      .attr("class","legend") 	
	      //.attr("transform", "translate("+marginViz.left+"," + height +")")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "middle")
	      .text("Nutzer");
	  
	  
	   var gBox = svgBox.selectAll("g.box")
	    	.data(quizzes, function(d, i) { console.log("Name: "+d.name+" UW: "+d.upperWhisker); return d;} )
	     .enter().append("g")
    		.attr("class", function(d) {return "box gbox-"+d.name;})
    		.attr("transform", function (d,i) {return "translate(" + (xScale(d.name)+marginViz.left-14)+",0)"; })
    		.attr("width", w + marginPlot.left + marginPlot.right)
    		.attr("height", h + marginViz.bottom + marginViz.top)
    		.on("mouseover",function(d) {	
    				gBox.selectAll("text.boxText-"+d.name).style("fill","#0000") 
    				svgBox.selectAll("text.boxId-"+d.name).style("fill","red") 
    		})
    		.on("mouseout",function(d) {
    				gBox.selectAll("text.boxText-"+d.name).style("fill","none") 
    				svgBox.selectAll("text.boxId-"+d.name).style("fill","#0000") 
    		})
    		
//	  gBox.append("text")
//	  	.attr("class","quiz")
//		.attr("x", 0 )
//		.attr("y", 0)
//		.attr("transform", "translate(5 ," + 10 + ")")
//		.text(function(d, i) {return i;
//						//d.name.toUpperCase();
//						});
	  
	  gBox.append("g")
		.attr("transform", "translate(" + marginPlot.left + "," + marginPlot.top + ")")
		.call(box);
	  
	  
//	  d3.select("button").on("click", function() {
//		    svg.data(hours).call(vis.duration(1000)); // TODO automatic transitions
//		    svg.selectAll(".days")
//		    	.remove();
//		    svgBox.append("text")
//		    .attr("class","hours")
//		    .attr("x", 0 )
//			.attr("y", 0)
//			.attr("transform", "translate(" + (margin.left-8) + "," + 10 + ")")
//			.text(function(d) {return d.name+":00 h";});
//		  });

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
