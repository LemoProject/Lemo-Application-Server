(function(d3custom, $, undefined) {


	d3custom.run = function() {


		var objTypes = [];
		var data = [];

		data = d3custom.data;
		var locale = d3custom.locale;
		var color = function(d) { 
			return hashColor(d); 
		};

		//check if we have values to work with
		if(!data) {
			$("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
			return;
		}

		var w = 980 - 80;
		if ($("#viz").width() < 920)
			w=$("#viz").width();
		var h = 800 - 180,
		x = d3.scale.linear().range([0, w]),
		y = d3.scale.linear().range([0, h]),
		textLengthMulti = 4.5,
		namesList = [],
		parentNamesList=[],
		root,
		node;     
	};

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
