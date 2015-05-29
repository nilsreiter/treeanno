var Week = new Array(
		{name:"Sunday",short:"Sun"}, 
		{name:"Monday", short:"Mon"}, 
		{name:"Tuesday", short:"Tue"},
		{name:"Wednesday", short:"Wed"},
		{name:"Thursday", short:"Thu"},
		{name:"Friday", short:"Fri"},
		{name:"Saturday", short:"Sat"});



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
	$("#"+id).each(function(index, value) {
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



function populate_file_list(target, rpcCall) {
	jQuery.getJSON(rpcCall, function (data) { 
		for (var i in data) {
			var row = document.createElement("tr");
			
			$(row).append("<td>"+data[i]['databaseId']+"</td>");
			$(row).append("<td>"+data[i]['corpus']+"</td>");
			$(row).append("<td>"+data[i]['id']+"</td>");
			$(row).append("<td>"+data[i]['textBegin']+" ...</td>");
			$(row).bind("click", function() {location.href="view-document?doc="+
				data[i]['id']});
			$(target).append(row);
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

function append_loadedsvg(target, event) {
	jQuery.getJSON('rpc/svg?document='+event['documentId']+'&event='+event['id'], function (data) {
		alert("svg");
		alert(data['svg']);
		//$("#event-sequence div."+event['id']).append(data['svg']);
		var svgNode = (new DOMParser()).parseFromString(data['svg']);
		//console.log(svgNode);
		//alert(svgNode);
		$(target).get().appendChild(svgNode);
	});
}

function process_event(target, event, eventClasses) {
	// alert(JSON.stringify(event));
	if (typeof event != "undefined") {
		target.append('<div class="'+event['id']+' event '+event['class']+'" style="overflow:auto"></div>');
		var pos = (parseInt(event['position'] * 1000))/10;
		$("#event-sequence ."+event['id']).append("<span>"+pos+"%</span>");
		var url = 'rpc/svg?document='+event['documentId']+'&event='+event['id'];
		// alert(url);
		 append_eventsvg("#event-sequence div."+event['id'], event, eventClasses);
//		jQuery.getJSON(url, {}, function (data2) {
//			alert(data2);
//			if (data2['error'] == 0) {
				//$("#event-sequence div."+event['id']).append(data['svg']);
				//var svgNode = (new DOMParser()).parseFromString(data['svg']);
				//console.log(svgNode);
				//alert(svgNode);
				// $("#event-sequence div."+event['id']).get().appendChild(svgNode);						
//			} else {
				
//			}
//		});
	}
}

function load_event_sequence(docId) {
	jQuery.getJSON("rpc/get-events?doc="+docId, function (data) { 
		var id = data['id'];
		var eventClasses = new Object();
		var target = $("#event-sequence div."+id);
		for (ev in data['events']) {
			var event = data['events'][ev];
	 		process_event(target, event, eventClasses);
		}
		$("#sidebar table").css("font-size", "small");
		for (evClass in eventClasses) {
			$("#sidebar table.eventClasses tbody").append('<tr><td sorttable_customkey="'+evClass+'"><input class="evClassToggler" type="checkbox" checked="checked" onchange="jQuery(\'.event.'+evClass+'\').toggle()"/>'+evClass+" </td><td>"+eventClasses[evClass]+"</td></tr>");
		}
		// sorttable.makeSortable($("#sidebar table.eventClasses").get());
		jQuery.getJSON("rpc/get-entities?doc="+docId, function (data) {
			for (entI in data['entities']) {
				entity = data['entities'][entI];
				var n = $("rect."+entity['id']).length;
				if (n > 0) {
					$("#sidebar table.entities tbody").append("<tr class=\""+entity['id']+"\"><td><input type=\"checkbox\" class=\"entityToggler "+entity['id']+"\"/>"+entity['id']+"</td><td>"+n+"</td></tr>");
					$("input."+entity['id']).bind("change", {mentions:entity['mentionIds'],id:entity['id'],number:n},function(data) {
						d3.selectAll("rect."+data.data['id']).classed("mentionhighlight", $(data.target).prop('checked'));
						//jQuery("."+data.data['id']).toggleClass("mentionhighlight");
						//$("tr."+data.data['id']).toggleClass("mentionhighlight");
					});
				}
			}
		});
		$("#sidebar > div").accordion({ header: "h2", heightStyle: "fill" });
		$("#loading").remove();
	});
}

function append_eventsvg(target, event, eventClasses) {
	
	if (typeof(event) == 'undefined')
		return;	
	
	
	var svg = d3.select(target).append("svg");
	svg.attr("height", 200);
	svg.attr("class", event["id"]);
	if (typeof( eventClasses[event['class']]) =='undefined')
		eventClasses[event['class']] =1;
	else 
		eventClasses[event['class']]++;
	
	var tokenY = 180;
	// alert(svg);
	var sentenceIndex = new Object();
	var p_width=50;
	var w = p_width;;
	for (var tok = 0; tok < event['sentence']['tokens'].length; tok++) {
		var token = event['sentence']['tokens'][tok];
		var entityIds = token['entityIds'];
		var textelement = svg.append("text").text(token['surface'])
			.attr("x",w).attr("y", tokenY)
			.attr("class","surface"); 
		sentenceIndex[token['id']] = textelement;
		if (textelement.node() !=null && typeof entityIds !=  "undefined") {
			for (eId in entityIds) {
				svg.insert("rect", "text").attr("x", w).attr("y", tokenY-parseInt(textelement.node().getBBox().height)+4)
					.attr("width", textelement.node().getBBox().width)
					.attr("height",textelement.node().getBBox().height)
					.attr("class", "tokenbg "+entityIds[eId]);
			}
		}
		if (textelement.size()>0)
			w += textelement.node().getBBox().width+2;
		
	}
	svg.attr("width", w+p_width);
	$(target).css("width", w+p_width).css("margin-left","auto").css("margin-right","auto");
	// alert(JSON.stringify(sentenceIndex));
	// alert(event['token'][0]['id']);
	var targetElement = sentenceIndex[event['token'][0]['id']];
	
	var targetXpos = parseInt(targetElement.attr("x")) +
		parseInt(((targetElement.node().getBBox().width) / 2.0));
	
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
	//jQuery.post('rpc/svg',
	//	   { 'svg': (new XMLSerializer).serializeToString(svg.node()),
	//	   	 'event': event['id'],
	//	   	 'document': event['documentId']});
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
	$("#button-highlight-events").bind("click", function(event) {
		jQuery('.event').toggleClass('highlightEvent');
	});
	$("#button-highlight-frames").bind("click", function(event) {
		jQuery('.frame').toggleClass('highlightFrame');
	});
	$("#button-highlight-mentions").bind("click", function(event) {
		jQuery('.mention').toggleClass('highlightMention');
	});
	$("#button-split-sentences").bind("click", function(event) {
		jQuery('.sentence').toggleClass('div');		
	});
}

function load_document_html(documentId, otarget, callback) {
	$.get('rpc/get-document?format=HTML&doc='+documentId, function(data) {
		$(otarget).html(data);
		callback();
	});
}

function load_document(documentId, otarget, callback) {
	
	$(otarget).append('<div class="loading"><img src="gfx/loading1.gif" /></div>');
	jQuery.getJSON("rpc/get-document?doc="+documentId, function (data) { 
		// alert("received");
		var target = document.createElement("div"); // $("<div></div>");
		var lastpos = 0;
 		$(target).addClass(data['id']);
		var cont = "."+data['id'];
		for (sid in data['sentences']) {
			var sentence = data['sentences'][sid];
			$(target).append('<span class="sentence '+sentence['id']+'"></span>');
			for (tid in sentence['tl']) {
				var tokid = sentence['tl'][tid];
				var token = data['tokens'][tokid];
				var tokenHTML = '<span id="'+data['id']+'-'+token['id']+'" class="token '+token['id']+'" title="'+token['id']+'">'+token['surface']+"</span>";
				if (token['begin'] > lastpos) {
					$("span."+sentence['id'], target).append(" ").append(tokenHTML);
				} else {
					$("span."+sentence['id'], target).append(tokenHTML);
				}
				lastpos = token['end'];
				
				for (i in token['mentionIds']) {
					var mentionId = token['mentionIds'][i];
					$("span."+token['id'], target).addClass("mention "+mentionId);
				}
			}
		}
		// alert("tokens done.");
		
		for (i in data['frames']) {
			var frame = data['frames'][i];
			for (j in frame['tl']) {
				var tokId = frame['tl'][j];
				$("span."+tokId, target).addClass(frame['name'] + " frame " + frame['id']);			
				$("span."+tokId, target).attr( "title", function( i, val ) {
					  return val + " " + frame['name'];
				});
			}
		}
		
		for (i in data['events']) {
			var event = data['events'][i];
			$("span."+event['anchorId'], target).addClass(event['id'] + " event " + event['class']);
		};

		$(".loading", otarget).remove();
		$(target).hide();
		$(otarget).append(target);
		$(target).fadeIn();
		callback();
	});
}


function load_document_similarities(docId) {
	jQuery.getJSON('rpc/get-document-similarities?doc='+docId, function (data) { 

	    $('#container').highcharts({

	        chart: {
	            type: 'heatmap',
	            marginTop: 40,
	            marginBottom: 40
	        },


	        title: {
	            text: 'Similarity of Documents'
	        },

	        xAxis: {
	            categories: data['list']
	        },

	        yAxis: {
	            categories: data['list'],
	            title: null
	        },

	        colorAxis: {
	            min: 0,
	            minColor: '#FFFFFF',
	            maxColor: Highcharts.getOptions().colors[0]
	        },

	        legend: {
	            align: 'right',
	            layout: 'vertical',
	            margin: 0,
	            verticalAlign: 'top',
	            y: 25,
	            symbolHeight: 320
	        },

	        tooltip: {
	            formatter: function () {
	                return '<b>' + this.series.xAxis.categories[this.point.x] + '</b> sold <br><b>' +
	                    this.point.value + '</b> items on <br><b>' + this.series.yAxis.categories[this.point.y] + '</b>';
	            }
	        },

	        series: [{
	            name: 'Similarity of Document Pairs',
	            borderWidth: 1,
	            data: data['data'],
	            dataLabels: {
	                enabled: 'true',
	                color: 'black',
	                //format: '{point.value:.2f}',
	                style: {
	                    textShadow: 'none',
	                    HcTextStroke: null
	                },
	                useHTML: 'true',
	    	            formatter: function() {
	    	            	return '<a href="javascript:show_event_similarities(\''+data['list'][this.point.x]+'\',\''+data['list'][this.point.y]+'\')">'+this.point.value+"</a>";
	    	            }

	            },
	        }]

	    });
	})
	};
	
function show_event_similarities(doc1, doc2) {
	$(".level5 .menu input:checked").click();
	$("#toggle-document-"+doc1).click();
	$("#toggle-document-"+doc2).click();
	$(".level3.content").tabs("option", "active" , 1);
}