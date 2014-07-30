<%@ page import="de.nilsreiter.web.beans.menu.Location,de.nilsreiter.web.beans.menu.Location.Area"  %>
<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*,de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	if (session.getAttribute("location") == null) {
		session.setAttribute("location", new Location(Area.Alignment));
	}
	((Location)session.getAttribute("location")).setArea(Area.Alignment);
%>

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

<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>




<div id="alignmentdocumentpicker" class="dialog picker">
	<h1>Select Alignment</h1>
	<ul class="filelist">
	<c:forEach var="document" items="${docman.alignmentDocuments}">
		<li onclick="location.href='view-alignment?doc=${document.databaseId}'">${document.id} (${document.documentIds })</li>
	</c:forEach>
	</ul>
</div>

<!--<jsp:include page="/alignment/alignment-settings.jsp">
	<jsp:param value="rpc/align" name="action"/>
</jsp:include>-->

</div>
</div>
</body>
</html>