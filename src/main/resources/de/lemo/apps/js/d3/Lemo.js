//Javascript-Helperfunctions 
function hashColor(objectName,category) {
	String.prototype.hashCode = function(){
		var hash = 0;
		if (this.length == 0) return hash;
		for (i = this.length-1; i >= 0; i--) {
			char = this.charCodeAt(i);
			hash = char + (hash << 6) + (hash << 16) - hash;
		}	
		if (hash.toString(16).length < 10)
			hash = hash * 100;
		return Math.abs(hash);
	}
	var uniqueColor;
	if(typeof(category)==='undefined'){
		if(typeof(objectName)==='undefined'){
			uniqueColor = "white";
		} else
			uniqueColor = objectName.replace(/[^\A-Z0-9����+$]/gi,'_'); 		
	} 
	else 
		uniqueColor = category;
	
	var result = "#".concat(uniqueColor.toString().hashCode().toString(16).substring(2, 8));

	return result;
}

//Function calculates the hashvalue of the object name and maps this value 
//on a predefinded color table.
function hashColorToDomain(objectName,category) {
	String.prototype.hashCode = function(){		
	      var T = [
	      // 256 values 0-255 in any (random) order suffices
	       98,  6, 85,150, 36, 23,112,164,135,207,169,  5, 26, 64,165,219, //  1
	       61, 20, 68, 89,130, 63, 52,102, 24,229,132,245, 80,216,195,115, //  2
	       90,168,156,203,177,120,  2,190,188,  7,100,185,174,243,162, 10, //  3
	      237, 18,253,225,  8,208,172,244,255,126,101, 79,145,235,228,121, //  4
	      123,251, 67,250,161,  0,107, 97,241,111,181, 82,249, 33, 69, 55, //  5
	       59,153, 29,  9,213,167, 84, 93, 30, 46, 94, 75,151,114, 73,222, //  6
	      197, 96,210, 45, 16,227,248,202, 51,152,252,125, 81,206,215,186, //  7
	       39,158,178,187,131,136,  1, 49, 50, 17,141, 91, 47,129, 60, 99, //  8
	      154, 35, 86,171,105, 34, 38,200,147, 58, 77,118,173,246, 76,254, //  9
	      133,232,196,144,198,124, 53,  4,108, 74,223,234,134,230,157,139, // 10
	      189,205,199,128,176, 19,211,236,127,192,231, 70,233, 88,146, 44, // 11
	      183,201, 22, 83, 13,214,116,109,159, 32, 95,226,140,220, 57, 12, // 12
	      221, 31,209,182,143, 92,149,184,148, 62,113, 65, 37, 27,106,166, // 13
	        3, 14,204, 72, 21, 41, 56, 66, 28,193, 40,217, 25, 54,179,117, // 14
	      238, 87,240,155,180,170,242,212,191,163, 78,218,137,194,175,110, // 15
	       43,119,224, 71,122,142, 42,160,104, 48,247,103, 15, 11,138,239  // 16
	      ];
	      var hash = 0;
	      var index;
	      for (var i = 0; i < this.length; i++) {	    	  
	    	  index = hash^this.charCodeAt(i);
		      hash = T[index];
	      }
	      return hash;
	}
	var uniqueColor;
	if(typeof(category)==='undefined'){
		if(typeof(objectName)==='undefined'){
			uniqueColor = "white";
		} else
			uniqueColor = objectName.replace(/[^\A-Z0-9����+$]/gi,'_'); 		
	} 
	else 
		uniqueColor = category;
	
	var colors = ["red","blue"];
	var domainArray = domain(1,1000);
	var colorFunction = mapColorFunction(domainArray,colors);
	var result = colorFunction(uniqueColor.toString().hashCode());

	return result;
}
//Mapping of colors: Hashfunction gives domain values and colors are predefinded in range attribute.
function mapColorFunction(domainArray,colorArray){
	var color = d3.scale.ordinal()
		.domain(domainArray)
	    .range(colorArray);
	return color;
}

//Creates an domain array for the specified range
function domain(start, end) {
    var domainArray = [];
    for (var i = start; i <= end; i++) {
    	domainArray.push(i);
    }
    return domainArray;
}