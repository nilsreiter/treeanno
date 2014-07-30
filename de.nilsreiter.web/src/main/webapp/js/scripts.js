



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



function populate_file_list(target) {
	jQuery.getJSON('rpc/get-document-info', function (data) { 
		for (var i in data) {
			var s = "<tr onclick=\"location.href='view-document?doc="+
					data[i]['id']+"'\"><td>"+data[i]['databaseId']+"</td><td>"+data[i]['corpus']+"</td><td>"+
					data[i]['id']+"</td><td>"+data[i]['textBegin']+" ...</td></tr>";
			$(target+" table.filelist tbody").append(s);
		}
	});
}

function localsearch(id) {
	jQuery('.localsearchresult').toggleClass('localsearchresult');
	if (id.length > 0) {
		jQuery('.'+id).toggleClass('localsearchresult');
	}
}

function dragmove(d) {
    d3.select(this)
      .attr("y", d3.event.y)
      .attr("x", d3.event.x)
}

function append_eventsvg(target, event, eventClasses) {
	var svg = d3.select(target).append("svg").attr("width", "100%").attr("height", 200);
	if (typeof( eventClasses[event['class']]) =='undefined')
		eventClasses[event['class']] =1;
	else 
		eventClasses[event['class']]++;
	
	var tokenY = 180;
	
	var sentenceIndex = new Object();
	var w = 50;
	for (var tok in event['sentence']['tokens']) {
		var token = event['sentence']['tokens'][tok];
		var entityIds = token['entityIds'];
		textelement = svg.append("text").text(token['surface'])
			.attr("x",w).attr("y", tokenY)
			.attr("class","surface"); 
		sentenceIndex[token['id']] = textelement;
		
		if (textelement != null && textelement.node() !=null && typeof entityIds !=  "undefined") {
			for (eId in entityIds) {
				svg.insert("rect", "text").attr("x", w).attr("y", tokenY-parseInt(textelement.node().getBBox().height)+4)
					.attr("width", textelement.getBBox().width)
					.attr("height",textelement.node().getBBox().height)
					.attr("class", "tokenbg "+entityIds[eId]);
			}
		}
		
		w += textelement.node().getBBox().width+2;
	}
	var targetElement = sentenceIndex[event['token'][0]['id']];
	var targetXpos = parseInt(targetElement.attr("x"))+parseInt(((targetElement.node().getBBox().width) / 2.0));
	svg.append("text").text(event['class']).attr("x",targetXpos).attr("y","20").style("text-anchor", "middle").attr("class", "eventclass");
	
	svg.append("line").attr("x1", targetXpos).attr("y1", 30).attr("x2", targetXpos).attr("y2", 160)
		.style("stroke", "grey").style("stroke-width", "thin");
	var drag = d3.behavior.drag().on("drag", dragmove);
	for (var role in event['roles']) {
		var lineFunction = d3.svg.diagonal();
		var left = 1000000;
		var right = 0;
		for (var tokI in event['roles'][role]['tokens']) {
			lp = sentenceIndex[event['roles'][role]['tokens'][tokI]['id']].attr("x");
			rp = parseInt(lp)+sentenceIndex[event['roles'][role]['tokens'][tokI]['id']].node().getBBox().width;
			if (lp < left) {
				left = lp;
			}
			if (rp > right) {
				right = rp;
			}
		}
		var center = parseInt(left)+parseInt((right-left)/2);
		svg.append("line").attr("x1", left).attr("x2", right).attr("y1",160).attr("y2",160).style("stroke","black");
		
		var xfactor = (center-targetXpos)/2;
		var path = {
				'source':{'x':targetXpos,'y':30},
				'target':{'x':center,'y':160}
			};
		svg.append("path").attr("d", lineFunction(path)).style("stroke","black").style("fill","none");

		
		svg.append("text").text(event['roles'][role]['name']).attr("x",left).attr("y",150).call(drag);
	}
}

function eventtable(event, eventClasses) {
	// eventrow
	eventRow = document.createElement("tr");

	// event class
	$(eventRow).append('<td class="eventclass">'+event['class']+'</td>');	
	if (typeof( eventClasses[event['class']]) =='undefined')
		eventClasses[event['class']] =1;
	else 
		eventClasses[event['class']]++;
	// event
	eventCell = document.createElement("td");
	$(eventCell).addClass("surface");
	$(eventCell).hide();
	for (var tok in event['token']) {
		$(eventCell).append(event['token'][tok]['surface']);
	} 
	$(eventRow).append(eventCell);
	
	t = document.createElement("table");		
	$(t).addClass("event "+event['id']+" " +event['class']).append(eventRow);

	for (var role in event['roles']) {
		roleRow = document.createElement("tr");
		
		$(roleRow).addClass("role").append("<td>"+event['roles'][role]['name']+"</td>");
		$(roleRow).hide();
		var s = "";
		for (var tokI in event['roles'][role]['tokens']) {
			var token = event['roles'][role]['tokens'][tokI];
			s += "<span class=\"token ";
			s += token['id'];
			for (var mI in token['mentionIds']) {
				s += " ";
				s += token['mentionIds'][mI]+" " + token['entityIds'][mI];
				s += " ";
			}
			s += "\">";
			s += token['surface'];
			s += "</span> ";	
		}
		surfacecell = document.createElement("td");
		$(surfacecell).addClass("surface").hide();
		$(surfacecell).append(s);
		$(roleRow).append(surfacecell);
		
		$(t).append(roleRow);
	}
	return t;
}

function init_controls(controlcontainer) {
	$(controlcontainer).buttonset();
	$("#button-highlight-events").button().bind("click", function(event) {
		jQuery('.event').toggleClass('highlightEvent');
	});
	$("#button-highlight-frames").button().bind("click", function(event) {
		jQuery('.frame').toggleClass('highlightFrame');
	});
	$("#button-highlight-mentions").button().bind("click", function(event) {
		jQuery('.mention').toggleClass('highlightMention');
	});
	$("#button-split-sentences").button().bind("click", function(event) {
		jQuery('.sentence').toggleClass('div');		
	});
}

