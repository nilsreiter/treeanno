
var colors = new Object();
colors["WordNet"] = "255,0,0";
colors["FrameNet"] = "0,255,0";


function connect(id) {
	connect(id, "black");
}

function connect(id, color) {
		
	var canvasOffset = $("#canvas").offset();
	var ctx=document.getElementById("canvas").getContext("2d");
	// alert(id);
	ctx.strokeStyle = color;
	ctx.lineWidth = 1;
	offsets = new Array();
	$("."+id).each(function(index, value) {
		offsets.push($(value).offset());
	});
	ctx.beginPath();
	if (offsets.length>1) {
		for (var i = 0; i < offsets.length-1; i++) {
			ctx.moveTo(offsets[i].left, offsets[i].top-canvasOffset.top);
			ctx.lineTo(offsets[i+1].left, offsets[i+1].top-canvasOffset.top);
		}
	}
	ctx.stroke();
}

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
