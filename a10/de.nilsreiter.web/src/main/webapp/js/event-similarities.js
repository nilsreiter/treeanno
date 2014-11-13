var colors = new Object();
colors["WordNet"] = "255,100,100";
colors["FrameNet"] = "100,255,100";
colors["VerbNet"] = "100,100,255";
colors["ArgumentText"] = "255,100,255";
colors["GaussianDistanceSimilarity"] = "100,255,255";

called_load_similarities = 0;


function clear_similarities(type) {
	$("span.st"+type+" > span").unwrap();
	$("div#sm"+type).remove();
}

function show_similarities(type,eventId) {
	alert("show_similarities");
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


function load_similarities(documentId, number, doctype) {
	called_load_similarities++;
	// alert(called_load_similarities);
	if (called_load_similarities == number) {
		jQuery.getJSON('rpc/get-meta-information', function(data) {
			var functions = data['supportedFunctions'];
			var s = "";
			for (var i in functions) {
				s += '<label class="checkbox '+functions[i]['readable']+'"><input type="checkbox" value="'+functions[i]['readable']+'"><span></span></label>'+functions[i]['readable']+'<br/>';
			}
			$("#event-similarities span.event").append('<div class="typechooser">'+s+'</div>');
			// alert(s);
			jQuery.getJSON('rpc/get-event-similarities?doctype='+doctype+'&doc='+documentId, function (data) { 
				for(var tokId in data) {
					for (var type in data[tokId]) {
						//alert("#"+tokId+" div.typechooser label."+type+ " input");
						$("#event-similarities #"+tokId+" div.typechooser label."+type+ " input").bind("click", {'type':type,'data':data[tokId][type], 'tokId':tokId},  function (event) {
							var type = event.data.type;
							var tokId = event.data.tokId;
							$("#event-similarities span.st"+type+" > span").unwrap();
							$("#event-similarities div#sm"+type).remove();
							if ($("#event-similarities #"+tokId+" div.typechooser label."+type+ " input").prop("checked")) {
								for (var oTok in event.data.data) {
									$("#event-similarities #"+oTok).wrap("<span class=\"st"+type+"\" style=\"background-color:rgba("+colors[type]+","+event.data.data[oTok]+")\"></span>");
								}
								$("#event-similarities #"+tokId).prepend("<div id=\"sm"+type+"\" class=\"sourcemarker\" style=\"background-color:rgb("+colors[type]+");\"></div>");
								$("#event-similarities #sm"+type).bind("click", {type:type,tokId:tokId}, function(event) { 
									$("#event-similarities #"+event.data.tokId+" div.typechooser label."+event.data.type+ " input").click();
								});
							}
						});
					}
				}
			});
		});
	}
}