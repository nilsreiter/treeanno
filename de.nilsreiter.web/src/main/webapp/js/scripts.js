var alignments = 0;

var colors = new Object();
colors["WordNet"] = "255,0,0";
colors["FrameNet"] = "0,255,0";

function toggleAlignments() {
    var context=document.getElementById("canvas").getContext('2d');

	if (alignments == 0) {
		var canvasContainer = document.getElementById("alignmentcontent");

		var myCanvas = document.getElementById("canvas");
		myCanvas.style.width = canvasContainer.offsetWidth+"px";
		myCanvas.style.height = canvasContainer.offsetHeight+"px";
		// You must set this otherwise the canvas will be streethed to fit the container
		myCanvas.width=canvasContainer.offsetWidth;
		myCanvas.height=canvasContainer.offsetHeight;
		var alIds = $(canvasContainer).data("alignmentIds").trim().split(' ');
		for (var i = 0; i <= alIds.length; i++) {
			connect(alIds[i]);
		}
    	alignments = 1;
    } else {

    	// Store the current transformation matrix
    	context.save();

    	// Use the identity matrix while clearing the canvas
    	context.setTransform(1, 0, 0, 1, 0, 0);
    	context.clearRect(0, 0, canvas.width, canvas.height);

    	// Restore the transform
    	context.restore();
    	alignments = 0;
    }
}

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
