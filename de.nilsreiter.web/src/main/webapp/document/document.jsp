<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Document ${documents[0].id}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-sequence.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-similarity.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/event-similarities.js" type="text/javascript"></script>
<script src="js/sorttable.js" type="text/javascript"></script>
<script src="js/d3/d3.min.js" charset="utf-8"></script>


<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div class="level1"> 
<%@ include file="../menu/area.jsp" %>
<div class="level2 content">
<%@ include file="../common/document-menu.jsp" %>

<div class="level3 content">
	<ul>
		<li><a href="#view">View</a></li>
		<li><a href="#event-sequence">Event Sequence</a></li>
		<li><a href="#event-similarities">Event Similarities</a></li>
	</ul>
	<div id="view"  class="content level4">
		<div class="menu">
			<%@ include file="../controls.html" %>
		</div>
		<div class="content level5">
			<div class="originaltext surface ${param.doc}" ></div>
		</div>
	</div>
	<div id="event-sequence"  class="content level4">
		<div class="content level5 ${param.doc}">
		</div>
		<div id="sidebar">
			<h1>Sidebar</h1>
			<div>
				<h2>Event Classes</h2>
				<div>
					<table class="eventClasses sortable">
					<thead>
						<tr><th>Event Class</th><th class="sorttable_numeric"># Instances</th></tr>
					</thead>
					<tbody></tbody>
					<tfoot>
						<tr><td><input type="checkbox" onchange="jQuery('#sidebar ul li input.evClassToggler').click()" />Show All</td></tr>					
					</tfoot>
					</table>
				</div>
				<h2>Entities</h2>
				<div>
					<table class="entities sortable">
					<thead>
						<tr><th>Entity Id</th><th class="sorttable_numeric"># Mentions</th></tr>
					</thead>
					<tbody></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
  <div id="event-similarities" class="content level4">
	<div class="menu">
		<%@ include file="../controls.html" %>
	</div>
	<div class="content level5">
		<div class="originaltext surface ${param.doc}" ></div>
	</div>
  </div>
</div>

</div>
<script>
var loaded = new Object();
loaded["#event-sequence"] = 0;
loaded["#event-similarities"] = 0;
$( ".level3.content" ).tabs({ 
	active: 0, 
	activate: function( event, ui ) {
		switch (ui['newPanel']['selector']) {
		case "#event-sequence":
			if (loaded["#event-sequence"] == 1) break;
			jQuery.getJSON("rpc/get-events?doc=${param.doc}", function (data) { 
				var id = data['id'];
				var eventClasses = new Object();
				var target = $("#event-sequence div."+id);
				for (ev in data['events']) {
					var event = data['events'][ev];
			 		// alert(JSON.stringify(event));
					if (typeof event != "undefined") {
						target.append('<div class="'+event['id']+' event '+event['class']+'" style="overflow:auto"></div>');
						var pos = (parseInt(event['position'] * 1000))/10;
						$("#event-sequence #"+event['id']).append("<span>"+pos+"%</span>");
						append_eventsvg("#event-sequence div."+event['id'], event, eventClasses);
					}

					
				}
				$("#sidebar table").css("font-size", "small");
				for (evClass in eventClasses) {
					$("#sidebar table.eventClasses tbody").append('<tr><td sorttable_customkey="'+evClass+'"><input class="evClassToggler" type="checkbox" checked="checked" onchange="jQuery(\'.event.'+evClass+'\').toggle()"/>'+evClass+" </td><td>"+eventClasses[evClass]+"</td></tr>");
				}
				// sorttable.makeSortable($("#sidebar table.eventClasses").get());
				jQuery.getJSON("rpc/get-entities?doc=${param.doc}", function (data) {
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
			loaded["#event-sequence"] = 1;
			break;
		case "#event-similarities":
			if (loaded["#event-similarities"] == 1)	break;
			load_document_html("${param.doc}", "#event-similarities .originaltext", function() {
				jQuery.getJSON('rpc/get-meta-information', function(data) {
					var functions = data['supportedFunctions'];
					var s = "";
					for (var i in functions) {
						s += '<label class="checkbox '+functions[i]['readable']+'"><input type="checkbox" value="'+functions[i]['readable']+'"><span></span></label>'+functions[i]['readable']+'<br/>';
					}
					$("#event-similarities span.event").append('<div class="typechooser">'+s+'</div>');
					
					jQuery.getJSON('rpc/get-event-similarities?doctype=document&doc=${param.doc}', function (data) { 
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
			});
			loaded["#event-similarities"] = 1;
			break;
		}	
	} 
});

init_controls(".level4 > .menu");
jQuery(window).load(function () {
	load_document_html("${param.doc}", "#view .originaltext", function() {});
	
})
	
	
	

</script>
</div>
</body>
</html>