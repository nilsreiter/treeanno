<%@ page import="de.nilsreiter.web.Location,de.nilsreiter.web.Location.Area"  %>
<jsp:useBean id="location" class="de.nilsreiter.web.Location" scope="request">
	<jsp:setProperty property="area" name="location" value="Alignment"/>
	<jsp:setProperty property="corpus" name="location" value="Rituals"/>
</jsp:useBean>
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

<div id="alignmentdocumentpicker" class="dialog picker">
	<h1>Select Alignment</h1>
	<ul class="filelist">
	<c:forEach var="documentName" items="${docman.alignmentDocuments}">
		<li onclick="location.href='view-alignment?doc=${documentName}'">${documentName}</li>
	</c:forEach>
	</ul>
</div>

<div id="documentsetpicker" class="dialog picker">
	<h1>Create Document Set</h1>
	<form action="create-documentset" method="POST">
	<p>Set name: <input type="text" name="setname"/></p>
	<ul class="filelist">
	<c:forEach var="documentName" items="${docman.documents}">
		<li><input type="checkbox" name="doc${documentName}"/>${documentName}</li>
	</c:forEach>
	</ul>
	<p style="text-align:right;"><button style="width:50%;">Create &amp; Open</button></p>
	</form>
	
</div>

</div>
</body>
</html>