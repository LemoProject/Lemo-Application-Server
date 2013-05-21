$(document).ready(function() {
    $("#draggable").draggable();
    $("#draggable2").draggable();
  });
  
  function color(name) {
	 if(name && name != null) {
		  name=name.toLowerCase();
	  } else return "#000";
     if (name == "resource")
       return "#ff8f1e ";
     else if (name == "course")
       return "#1f77b4";
     else if (name == "forum")
       return "#2ca02c";
     else if (name == "question")
       return "#9467bd";
     else if (name == "quiz")
       return "#d62728";
     else if (name == "assignment")
       return "#8c564b";
     else if (name == "scorm")
       return "#7f7f7f";
     else if (name == "wiki")
       return "#17becf";
     return "#e377c2";
   }