<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Document ${documents[0].id}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-sequence.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/sorttable.js" type="text/javascript"></script>
<script src="js/d3/d3.min.js" charset="utf-8"></script>


<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div class="level1">
<%@ include file="../menu/area.jsp" %>
<div class="level2 content">
<%@include file="../common/document-menu.jsp" %>

<div class="level3 content">
<%@include file="../common/menu.jsp" %>

<div class="content level4">

<div class="content level5">
	
<div class="${param.doc}"></div>
</div>
</div>
</div>
</div>
<div id="sidebar">
	<h1>Sidebar</h1>
	<div>
	<h2>Event Classes</h2>
	<div><table class="eventClasses sortable">
		<thead>
			<tr><th>Event Class</th><th class="sorttable_numeric"># Instances</th></tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
			<tr><td><input type="checkbox" onchange="jQuery('#sidebar ul li input.evClassToggler').click()" />Show All</td></tr>					
		</tfoot>
	</table></div>
	<h2>Entities</h2>
	<div><table class="entities sortable">
		<thead>
			<tr><th>Entity Id</th><th class="sorttable_numeric"># Mentions</th></tr>
		</thead>
		<tbody></tbody>
	</table></div></div>
</div>

</div>

<script>

//$(".level5").hide();
$(".level4").prepend('<div id="loading"><img src="gfx/loading1.gif"/></div>');


jQuery.getJSON("rpc/get-events?doc=${param.doc}", function (data) { 
	var id = data['id'];
	var eventClasses = new Object();
	
	for (ev in data['events']) {
		var event = data['events'][ev];
 		// alert(JSON.stringify(event));
		if (typeof event != "undefined") {
			$("div."+id).append('<div id="'+event['id']+'" class="event '+event['class']+'" style="overflow:auto"></div>');
			var pos = (parseInt(event['position'] * 1000))/10;
			$("#"+event['id']).append("<span>"+pos+"%</span>");
			append_eventsvg("div#"+event['id'], event, eventClasses);
		}

		
	}
	$("#sidebar table").css("font-size", "small");
	for (evClass in eventClasses) {
		$("#sidebar table.eventClasses tbody").append('<tr><td sorttable_customkey="'+evClass+'"><input class="evClassToggler" type="checkbox" checked="checked" onchange="jQuery(\'.event.'+evClass+'\').toggle()"/>'+evClass+" </td><td>"+eventClasses[evClass]+"</td></tr>");
	}
	// sorttable.makeSortable($("#sidebar table.eventClasses").get());
	jQuery.getJSON("rpc/get-entities?doc=${doc}", function (data) {
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
	//$(".level5").show();

})

</script>
<%@ include file="foot.jsp" %>
