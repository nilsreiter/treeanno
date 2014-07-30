<%@ page import="de.nilsreiter.web.beans.menu.Location,de.nilsreiter.web.beans.menu.Location.Area"  %>

<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*,de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	if (session.getAttribute("location") == null) {
		session.setAttribute("location", new Location(Area.DocumentSet));
	}
	((Location)session.getAttribute("location")).setArea(Area.DocumentSet);
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Set Viewer</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>
</head>
<body>
<div class="level1">

<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>

<div class="content level3">

<div id="documentsetpicker" class="dialog picker">
	<h1>Select Document Set</h1>
	<table class="filelist">
	<thead>
	<tr><th>Id</th><th>Name</th><th>Documents</th></tr></thead>
	<tbody></tbody></table>
</div>

<div id="documentsetcreator" class="dialog picker">
	<h1>Create Document Set</h1>
	<form action="create-documentset" method="POST">
	<p>Set name: <input type="text" name="setname"/></p>
	<table class="filelist">
	<thead><tr><th></th><th>Id</th><th>Corpus</th><th>Name</th></tr></thead>
	<tbody></tbody>
	</table>
	<p style="text-align:right;"><button style="width:50%;">Create &amp; Open</button></p>
	</form>
	
</div>

</div>
</div>
</div>
<script>
jQuery.getJSON('rpc/get-document-sets', function (data) { 
	for (var i in data) {
		var s = "<tr onclick=\"location.href='view-document-set?doc="+
				data[i]['databaseId']+"'\"><td>"+data[i]['databaseId']+"</td><td>"+
				data[i]['id']+"</td><td>"+
				data[i]['documentIds']+"</td></tr>";
		$("#documentsetpicker table.filelist tbody").append(s);
	}
});


jQuery.getJSON('rpc/get-document-info', function (data) { 
	for (var i in data) {
		var s = "<tr><td><input type=\"checkbox\" name=\"doc"+data[i]['id']+"\"/></td><td>"+data[i]['databaseId']+"</td><td>"+data[i]['corpus']+"</td><td>"+
				data[i]['id']+"</td></tr>";
		$("#documentsetcreator table.filelist tbody").append(s);
	}
});
</script>
</body>
</html>