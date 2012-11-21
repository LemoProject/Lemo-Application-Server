(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	  var data_test = [[850,740,500,400,600,720],[830,650,500,600,720,580]];
	  
	 var data = d3custom.data;
	 
	 
	 //check if we have values to work with
	  if(!data || !data.elements) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
	  var days = [],
	  	  hours = [],
	  	  counter = 0,
	  	  daysMin = 0,
	  	  daysMax = 0,
	  	  hoursMin = 0,
	  	  hoursMax = 0;
	  
	  //Seperating days per week and hours per day results and calculating Min and Max Values for domain setup
	  $.each(data.elements, function(i,v) {
		  if(counter<7){
			  console.log("DaysMin: "+daysMin+" DaysMax: "+daysMax+ " UW: "+v.upperWhisker+ " LW: "+v.lowerWhisker);
			  // "+" Prefix on strings make JS recognize them as numbers  
			  if(v.lowerWhisker && +v.lowerWhisker < +daysMin) daysMin = v.lowerWhisker;
			  if(v.upperWhisker && +v.upperWhisker > +daysMax) daysMax = v.upperWhisker;
			  days.push(v);
		  } else {
			  // "+" Prefix on strings make JS recognize them as numbers  
			  if(v.lowerWhisker && +v.lowerWhisker < +hoursMin) hoursMin = v.lowerWhisker;
			  if(v.upperWhisker && +v.upperWhisker > +hoursMax) hoursMax = v.upperWhisker;
			  hours.push(v);
		  }
		  counter++;
	  });
	  
	  console.log("DaysMin: "+daysMin+" DaysMax: "+daysMax);
	  
	  var margin = {top: 10, right: 50, bottom: 20, left: 50},
	    w = 120 - margin.left - margin.right,
	    h = 500 - margin.top - margin.bottom;

	  var min = daysMin,
	      max = daysMax;

	  
      var vis = d3.box()
	    .whiskers(iqr(1.5))
	    .width(w)
	    .height(h);

	  vis.domain([min, max]);
	  
	  var svg = d3.select("#viz").selectAll("svg")
    		.data(days, function(d, i) { console.log("Name: "+d.name+" UW: "+d.upperWhisker); return d;} )
    	.enter().append("svg")
    		.attr("class", "box")
    		.attr("width", w + margin.left + margin.right)
    		.attr("height", h + margin.bottom + margin.top)
    	.append("g")
    		.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
    		.call(vis);

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
