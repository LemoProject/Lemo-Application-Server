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
		console.log(this + " / " + hash);
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