<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Event Similarity Viewer</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-similarity.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>
</head>
<body>
<jsp:include page="menu.jsp">
	<jsp:param value="active" name="class_event_similarities"/>
</jsp:include>
<div class="content">
<div class="controls">
	<%@ include file="controls.html" %>
</div>

<div id="alignmentcontent" >
<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="alignmenttext box">
	<h2><a href="view-document.do?doc=${documents[i].id}">${documents[i].id}</a></h2>
	<jsp:include page="document-box-similarities.jsp">
		<jsp:param value="${i}" name="i"/>
	</jsp:include>
	</div>
</c:forEach>
<div style="clear:left;"></div>

</div>

</div>
</body>
</html>