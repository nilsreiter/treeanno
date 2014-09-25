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
<script src='js/sorttable.js'></script>
<script src="js/moment-with-locales.js"></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="level1">

<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>

<div class="content level3">
<div class="content level4">
<div class="level5 content alignment-select">

	<h1>Select Alignment</h1>
	<div>
		<table class="filelist sortable">
		<thead>
			<tr><th>Id</th><th>Title</th><th>Created</th><th>Documents</th><th colspan="2">Actions</th></tr>
		</thead>
		<tbody></tbody>
		</table>
	</div>
<div>

<jsp:include page="/alignment/alignment-settings.jsp">
	<jsp:param value="rpc/align" name="action"/>
</jsp:include>
</div></div>
</div>
</div>
</div>
</div>
<script>
$(".content.level5").accordion({ header: "h1", heightStyle:"content" });
jQuery.getJSON("rpc/get-alignment-info", function (data) { 
	for (ali in data['alignments']) {
		var alignment = data['alignments'][ali];
		var tr = document.createElement("tr");
		// alert(JSON.stringify(alignment));
		var mom = moment(alignment['creationDate']);
		$(tr).append('<td>'+alignment['databaseId']+'</td>');
		$(tr).append('<td>'+alignment['id']+'</td>');
		$(tr).append('<td sorttable_customkey="'+mom.toDate().getTime()+'">'+mom.fromNow()+'</td>');
		$(tr).append('<td>'+alignment['documentIdList'].join(", ")+'</td>');
		$(tr).append('<td><a href="view-alignment?doc='+alignment['databaseId']+'">open</span></td>');
		$(tr).append('<td><a href="delete-alignment?doc='+alignment['databaseId']+'">delete</span></td>');
		
		$("table.filelist tbody").append(tr);
	}
});
</script>
</body>
</html>