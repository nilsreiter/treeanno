<%@ page import="de.nilsreiter.web.beans.menu.Location,
	de.nilsreiter.web.beans.menu.Location.Area" %>

<%
	if (session.getAttribute("location") == null) {
		session.setAttribute("location", new Location(Area.Document));
	}
	((Location)session.getAttribute("location")).setArea(Area.Document);
%>
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

<div class="content level4">

<div class="content level5">

	<h1>Select Document</h1>
	<div>
	<table class="filelist">
	<thead><tr><th>Id</th><th>Corpus</th><th>Name</th><th>Content</th></tr></thead>
	<tbody></tbody>
	</table>
	</div>
</div>
</div>
</div>
</div>
<script>
$(".level5").accordion({ header: "h1", heightStyle:"content" });
jQuery.getJSON("rpc/get-document-info", function (data) { 
	for (var i in data) {
		var row = document.createElement("tr");
		
		$(row).append("<td>"+data[i]['databaseId']+"</td>");
		$(row).append("<td>"+data[i]['corpus']+"</td>");
		$(row).append("<td>"+data[i]['id']+"</td>");
		$(row).append("<td>"+data[i]['textBegin']+" ...</td>");
		$(row).bind("click", {did:data[i]['id']}, function(data) {location.href="doc?doc="+
			data.data['did']});
		$("table.filelist tbody").append(row);
	}
});
</script>
</body>
</html>
