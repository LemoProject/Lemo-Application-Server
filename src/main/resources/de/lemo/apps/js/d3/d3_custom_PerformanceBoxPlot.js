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
	  
	  var margin = {top: 30, right: 50, bottom: 20, left: 50},
	    w = 120 - margin.left - margin.right,
	    h = 500 - margin.top - margin.bottom;

	  var min = quizMin,
	      max = quizMax;

	  if(min == 0 && max == 0) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
      var vis = d3.box()
	    .whiskers(iqr(1.5))
	    .width(w)
	    .height(h);

	  vis.domain([min, max]);
	  
	  var svg = d3.select("#viz").selectAll("svg")
    		.data(quizzes, function(d, i) { console.log("Name: "+d.name+" UW: "+d.upperWhisker); return d;} );
    	
	  var svgBox = svg.enter().append("svg")
    		.attr("class", "box")
    		.attr("width", w + margin.left + margin.right)
    		.attr("height", h + margin.bottom + margin.top)
    		
	  svgBox.append("text")
	  	.attr("class","days")
		.attr("x", 0 )
		.attr("y", 0)
		.attr("transform", "translate(" + (margin.left-8) + "," + 10 + ")")
		.text(function(d) {return d.name;});
	  
	  svgBox.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
		.call(vis);
	  
	  
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
