<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Alignment Viewer</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="level1">
<%@ include file="../menu/area_Alignment.jsp" %>

<div class="content level2">
<div class="menu">
<%@ include file="../common/document-menu.jsp" %>
</div>

<div class="content level3">
<ul>
	<li><a href="#view">View</a></li>
	<li><a href="#event-similarities">Event Similarities</a></li>
	<li><a href="#event-scores">Event Scores</a></li>
</ul>
<div id="view" class="content level4">
	<div class="menu">
		<input type="checkbox" onchange="toggleAlignments()" id="alignmentcheckbox"/><label for="alignmentcheckbox">Alignments</label>
		<%@ include file="../controls.html" %>
	</div>
	<div id="alignmentcontent" class="level5 content">
	<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="alignmenttext surface">
	<h1>${documents[i].id}</h1>
	<div class="${documents[i].id}">
	</div>
	<script>
		load_document("${documents[i].id}", ".${documents[i].id}", function() {});
	</script>

	</div>
	</c:forEach>
	<div style="clear:left;"></div>

	<div id="canvascontainer">
		<canvas id="canvas"></canvas>
	</div>
	</div>
</div>
<div id="event-similarities">Bla</div>
<div id="event-scores">Bla</div>
</div>
</div>
</div>
<script>
$(".level3.content").tabs();
init_controls("div.level4 > div.menu");
function toggleAlignments() {
    var context=document.getElementById("canvas").getContext('2d');

	if ($("#alignmentcheckbox").prop("checked")) {
		var canvasContainer = document.getElementById("alignmentcontent");

		var myCanvas = document.getElementById("canvas");
		myCanvas.style.width = canvasContainer.offsetWidth+"px";
		myCanvas.style.height = canvasContainer.offsetHeight+"px";
		// You must set this otherwise the canvas will be streethed to fit the container
		myCanvas.width=canvasContainer.offsetWidth;
		myCanvas.height=canvasContainer.offsetHeight;
		var canvasOffset = $(myCanvas).offset();

		jQuery.getJSON('rpc/get-alignments?doc=${param.doc}', function (data) { 
			var context=document.getElementById("canvas").getContext('2d');
			for (var alId in data) {
				if (data[alId].length > 1) {
					context.strokeStyle = "black";
					context.lineWidth = 1;
					offsets = new Array();
					for (var i = 0; i < data[alId].length; i++) {
						offsets.push($("."+data[alId][i]['d']+" ."+data[alId][i]['t']).offset());
					};
					// alert(JSON.stringify(offsets));
					context.beginPath();
					if (offsets.length>1) {
						for (var i = 0; i < offsets.length-1; i++) {
							context.moveTo(offsets[i].left, offsets[i].top-canvasOffset.top);
							context.lineTo(offsets[i+1].left, offsets[i+1].top-canvasOffset.top);
						}
					}
				context.stroke();
				}
			}
		});
    } else {

    	// Store the current transformation matrix
    	context.save();

    	// Use the identity matrix while clearing the canvas
    	context.setTransform(1, 0, 0, 1, 0, 0);
    	context.clearRect(0, 0, canvas.width, canvas.height);

    	// Restore the transform
    	context.restore();
    }
}

</script>
</body>
</html>