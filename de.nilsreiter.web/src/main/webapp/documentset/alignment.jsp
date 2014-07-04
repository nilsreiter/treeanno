<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
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
</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<jsp:include page="menu.jsp">
	<jsp:param value="view" name="active"/>
	<jsp:param value="${alignment.id}" name="payload" />
	<jsp:param value="${doc}" name="doc" />
</jsp:include>

<div class="content level3">
<div class="menu">
	<%@ include file="../controls.html" %>
	<input type="checkbox" onchange="toggleAlignments()" id="alignmentcheckbox"/>Alignments
</div>




<div id="alignmentcontent" class="level4 content">
<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="alignmenttext">
	<h1>${documents[i].id}</h1>
	<jsp:include page="../common/document-box.jsp">
		<jsp:param value="${i}" name="i"/>
	</jsp:include>
	</div>
</c:forEach>
<div style="clear:left;"></div>

<div id="canvascontainer">
<canvas id="canvas"></canvas>
</div>
</div>
</div>
</div>
</div>
<script>
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

		jQuery.getJSON('rpc/get-alignments?doc=${doc}', function (data) { 
			for (var alId in data) {
				context.strokeStyle = "black";
				context.lineWidth = 1;
				offsets = new Array();
				for (var i = 0; i < data[alId].length; i++) {
					offsets.push($("#"+data[alId][i]).offset());
				};
				context.beginPath();
				if (offsets.length>1) {
					for (var i = 0; i < offsets.length-1; i++) {
						context.moveTo(offsets[i].left, offsets[i].top-canvasOffset.top);
						context.lineTo(offsets[i+1].left, offsets[i+1].top-canvasOffset.top);
					}
				}
				context.stroke();
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