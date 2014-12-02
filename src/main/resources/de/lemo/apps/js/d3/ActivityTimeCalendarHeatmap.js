(function(d3custom, $) {

  d3custom.run = function() {
	  	var cal1 = new CalHeatMap();
	  	var cal2 = new CalHeatMap();
	  	var transformedData = transformData(d3custom.data);
	  	var minVal = 100000000;
	  	var maxVal = 0;
	  	var startDate = calcStartDate(d3custom.data);

	  	
	  	function calcStartDate(data){
	  		var tmin = new Date().getTime();
	  		for(var timestamp in data){
	  			if(timestamp < tmin){
	  				tmin = timestamp; 
	  			}
	  			if(data[timestamp]>maxVal)
	  				maxVal = data[timestamp];
	  			if(data[timestamp]<minVal)
	  				minVal = data[timestamp];
	  		}
	  		return new Date(tmin*1000);
	  	}
	  	
	  	function transformData(data){
	  		return data;
	  	}
	  	
	  	for (var i = 0; i < 12; i++){
	  		var cal = new CalHeatMap();
	  		cal.init({
		    	itemSelector: "#cal-heatmap"+(i+1).toString(),
		        data: transformedData,
		        start: new Date(startDate.getFullYear(), startDate.getMonth()+i, 1),
		        range: 1, 
		        domain: "month", 
		        subDomain: "day",
		        domainMargin: 4,
		        cellSize: 25,
				subDomainTextFormat: "%d",
		        browsing: true,
		        legendColors: ["#FFFFFF", "#FF0000"],
		        displayLegend: false,   
		        considerMissingDataAsZero: true,
			})
	  	}
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
