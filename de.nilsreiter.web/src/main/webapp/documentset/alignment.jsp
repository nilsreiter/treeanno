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
	<input type="checkbox" onchange="toggleAlignments()" />Alignments
</div>




<div id="alignmentcontent" class="level4 content" data-alignment-ids="${ alignmentIds }">
<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="alignmenttext">
	<h1>${documents[i].id}</h1>
	<jsp:include page="../document-box.jsp">
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
</body>
</html>