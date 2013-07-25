(function(d3custom, $, undefined) {


  d3custom.run = function() {
	  
	  
	  var objTypes = [];
	 
	  
	 var data = d3custom.data;
	 
	 
	//check if we have values to work with
	  if(!data) {
	    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
	    	return;
	  }
	  
	  var width = $('#viz').width(),
	  	 height = 600,
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
  	  quizMax = 0,
  	  page = 1,
  	  maxPerPage = 30,
  	  pages = 1;
  
  //Seperating days per week and hours per day results and calculating Min and Max Values for domain setup
  $.each(data, function(i,v) {
	  
		//console.log("QuizMin: "+quizMin+" QuizMax: "+quizMax+ " UW: "+v.upperWhisker+ " LW: "+v.lowerWhisker);
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
  
  var perPage = quizzes.length;
  
  while (perPage > maxPerPage) {
   
  	pages++;
  	perPage=quizzes.length/pages;
  	perPage = Math.ceil(perPage);
   
  }
  
  quizzes_page = quizzes.slice((page-1)*perPage,page*perPage);
    
  $("#pages").html('' + page + "/" + pages);

    if (pages > 1)
      $("#pagination").show();
    if (page == 1)
      $("#prev").hide();
    if (page == pages)
      $("#next").hide();
  
  //console.log("Counter:"+counter+" QuizMin: "+quizMin+" QuizMax: "+quizMax);
  
  var marginViz = {top: 5, right: 10, bottom: 100, left: 50},
  	marginPlot = {top: 5, right: 7, bottom: 0, left: 10},
    w = 30 - marginPlot.left - marginPlot.right,
    h = height - marginViz.top - marginViz.bottom;

  var min = quizMin,
      max = quizMax;

  if(min == 0 && max == 0) {
    	$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
    	return;
  }
  
   var users = quizzes_page.map(function(d){return d.name;}); 
   
   users.splice(0,0,' ');
    
  var xScale = d3.scale.ordinal()
	  .rangePoints([0, width - marginViz.left -100 ])
	  .domain(users);
	
	var yScale = d3.scale.linear()
	  .range([h, 0])
	  .domain([0,100]);
	
	var xAxis = d3.svg.axis()
	  .scale(xScale)
	  .orient("bottom")
	  .ticks(perPage,function(d, i) {
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
		  .attr("transform", "translate("+marginViz.left+"," + (h+5) +")")
	      .call(make_x_axis()
            .tickSize(-height, 0, 0)
            .tickFormat(""))
	      .call(xAxis) 
	    .selectAll("text")  
	    	.attr("class", function(d){return "boxId-"+d.replace(new RegExp("[\W :]","g"),"_");})
            .style("text-anchor", "start")
            .attr("dx", "0.6em")
            .attr("dy", "0.3em")
            .attr("transform", function(d) {
                return "rotate(45)" 
                })
          
	  svgBox.append("g")
	      .attr("class", "y axis grid")
	      .attr("transform", "translate("+marginViz.left+", "+marginViz.top+")")
	      .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat(""))
          .call(yAxis)
	    .append("text")
	      .attr("transform", "translate(-50, 0) rotate(-90)")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "end")
	      .text("Performance in %");
	  
	  
	  svgBox.selectAll("x axis").append("text")
	      .attr("class","legend") 	
	      .attr("transform", "translate("+marginViz.left+"," + h +")")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "middle")
	      .text("Nutzer");
	  
	  
	   var gBox = svgBox.selectAll("g.box")
	    	.data(quizzes_page, function(d, i) {  return d;} )
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
		.attr("transform", "translate(" + marginPlot.left + "," +marginViz.top + ")")
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
      
      next = function(bool) {
      if (bool) {
        page++;
      } else {
        page--;
      }
      if (page == 1)
        $("#prev").hide();
      else
        $("#prev").show();
      if (page == pages)
        $("#next").hide();
      else
        $("#next").show();
      $("#pages").html('' + page + "/" + pages);
      
	  quizzes_page = quizzes.slice((page-1)*perPage,page*perPage);
    		
      users = quizzes_page.map(function(d){return d.name;}); 
   
   	  users.splice(0,0,' ');
   	  
   	  svgBox.select(".x.axis.grid").remove();
      
      var xScale = d3.scale.ordinal()
	  .rangePoints([0, width - marginViz.left -10 ])
	  .domain(users);
	  
	  var xAxis = d3.svg.axis()
	  .scale(xScale)
	  .orient("bottom")
	  .ticks(perPage,function(d, i) {
        return d.name;
      });
      
      svgBox.append("g")
	      .attr("class","x axis grid")
	      .attr("transform", "translate("+marginViz.left+"," + (h+5) +")")
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
                });
                  
      svgBox.selectAll("g.box").remove();
      
      var gBox = svgBox.selectAll("g.box")
	    	.data(quizzes_page, function(d, i) {  return d;} )
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
		.attr("transform", "translate(" + marginPlot.left + "," +marginViz.top + ")")
		.call(box);
      
      }
      
      
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
