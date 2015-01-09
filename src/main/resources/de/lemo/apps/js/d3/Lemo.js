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