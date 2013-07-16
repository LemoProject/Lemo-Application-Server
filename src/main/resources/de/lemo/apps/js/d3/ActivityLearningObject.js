(function(d3custom, $, _) {

  d3custom.run = function() {

    var data = d3custom.data;
    var locale = data.pop();
    var chart;
    
    if (!data) {
      // TODO there's some nvd3/d3 function for this case
      $("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
      return;
    }

    var dataRequests = data[0].values;
    var dataUser = data[1].values;
    var nameSortToggle = false;
    var perPage = 30,
    page = 1,
    pages = 0;
    
    var sortData = function(nameToggle){

	    dataRequests.sort(function(a, b) {
	    	if(nameToggle){ // sorting by name
	    		console.log("Sorting by name");
	    		if ( a.x < b.x )
	    		  return -1;
	    		if ( a.x > b.x )
	    		  return 1;
	    		return 0;
	    	} else {
	    		console.log("Sorting by value")
	    		return (b.y - a.y); // sorting by value
	    	}
	    });
	    dataUser.sort(function(a, b) {
	    	if(nameToggle){ // sorting by name
	    		console.log("Sorting by name");
	    		if ( a.x < b.x )
	    		  return -1;
	    		if ( a.x > b.x )
	    		  return 1;
	    		return 0;
	    	} else {
	    		console.log("Sorting by value")
	    		return (b.y - a.y); // sorting by value
	    	}
	    });
    }
    
    
    var sortToggle = function() {
    	console.log("SortToggle function fired ... nameSortToggle Before:"+nameSortToggle);
    	nameSortToggle = !nameSortToggle;
    	console.log("nameSortToggle After:"+nameSortToggle);
   		sortData(nameSortToggle);
   		console.log("Chart: "+chart )
   		chart.update();
    }
    
    $( "#sortToggle" ).bind( "click", function(event, ui) {
  	  console.log("SortToggel pressed"); 
  	  sortToggle(); 
    });
    
    //sorting data
    sortData(nameSortToggle);
    
    pages = Math.ceil(data[0].values.length/perPage);
    realData = jQuery.extend(true, [], data);
    realData[0].values = realData[0].values.slice((page-1)*perPage,page*perPage);
	realData[1].values = realData[1].values.slice((page-1)*perPage,page*perPage);
    
    $("#pages").html('' + page + "/" + pages);

    if (pages > 1)
      $("#pagination").show();
    if (page == 1)
      $("#prev").hide();
    if (page == pages)
      $("#next").hide();
    
    nv.addGraph(function() {
      chart = nv.models.multiBarChart().showControls(false).reduceXTicks(false);

  
      chart.yAxis.tickFormat(d3.format('d'));
	  console.log(data);
      d3.select('#viz svg').datum(realData).transition().duration(500).call(chart);
      d3.selectAll('.nv-x text').attr('transform', 'translate(0,5)rotate(45)').style('text-anchor', 'start');

      nv.utils.windowResize(chart.update);

     /* var wrap = d3.selectAll('g.nv-wrap.nv-multiBarWithLegend');
      var g = wrap.select('g');
      var controlSelection = d3.selectAll('g.nv-controlsWrap').select("g.nv-legend").select("g")	
      		.append('g')
      		.attr('transform', 'translate(150,' + (5) +')')
      		.attr("class", 'nv-series');
      
      controlSelection.append('svg:text')
	   .attr('class', function() {
		   		console.log("Sort Toggle added");
	   			return	'sortControls'})
	   .on('click', function(){
		   		console.log("Sort Toggle clicked");
		   		nameSortToggle = !nameSortToggle;
		   		sortData(nameSortToggle);
		   		//
		   		
		   		})
	   .text(function(d) { return "Sorting: "; });*/
      	
      dataExport.barChartButton('.export-button', d3.select('#viz svg').data(), chart, locale);
      
      
      
      return chart;
    });
    
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
      
      realData = jQuery.extend(true, [], data);
      realData[0].values = realData[0].values.slice((page-1)*perPage,page*perPage);
	  realData[1].values = realData[1].values.slice((page-1)*perPage,page*perPage);
      
      d3.select('#viz svg').datum(realData).transition().duration(500).call(chart);
      d3.selectAll('.nv-x text').attr('transform', 'translate(0,5)rotate(45)').style('text-anchor', 'start');
      
      }


  };


})(window.d3custom = window.d3custom || {}, jQuery, T5._);

$(document).ready(window.d3custom.run);
