var colors = new Object();
colors["WordNet"] = "255,0,0";
colors["FrameNet"] = "0,255,0";


function clear_similarities(type) {
	$("span.st"+type+" > span").unwrap();
	$("div#sm"+type).remove();
}

function show_similarities(type,eventId) {
	
	clear_similarities(type);
	if ($("#"+eventId+" div.typechooser label."+type+ " input").prop("checked")) {
		var sims = $("#"+eventId).data("similarities").trim().split(' ');
		for (var i = 0; i< sims.length; i++) {
			var pair = sims[i].split(':');
			if (pair[2] == type) {
				$("#"+pair[0]).wrap("<span class=\"st"+type+"\" style=\"background-color:rgba("+colors[type]+","+pair[1]+")\"></span>");
			}
		}
		$("#"+eventId).prepend("<div id=\"sm"+type+"\" class=\"sourcemarker\" style=\"background-color:rgb("+colors[type]+");\"></div>");
		$("#sm"+type).bind("click", function() { 
			$("#"+eventId+" div.typechooser label."+type+ " input").click();
		});
	}
	
}