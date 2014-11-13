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
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<sql:query var="rs" dataSource="jdbc/a10">
select id, databaseId, corpus, text from documents
</sql:query>
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
	<table class="filelist sortable">
	<thead><tr><th>Id</th><th>Corpus</th><th>Name</th><th>Content</th><th>Action</th></tr></thead>
	<tbody>
		<c:forEach var="row" items="${rs.rows}">
		<tr>
    		<td>${row.databaseId}</td>
    		<td>${row.corpus}</td>
    		<td>${row.id}</td>
    		<td>${fn:substring(row.text,0,50)}</td>
    		<td><a href="document?doc=${row.id}">Open</a></td>
		</tr>
		</c:forEach>
	</tbody>
	</table>
	</div>
</div>
</div>
</div>
</div>
</div>
<script>
$(".level5").accordion({ header: "h1", heightStyle:"content" });
</script>
</body>
</html>
