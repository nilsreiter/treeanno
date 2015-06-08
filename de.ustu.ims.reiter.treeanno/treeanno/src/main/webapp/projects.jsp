<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:sql="http://java.sun.com/jsp/jstl/sql">
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true"/>
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="false" />

<sql:query var="rs" dataSource="jdbc/treeanno" sql="SELECT level FROM users_permissions WHERE userId=? AND projectId=?">
	<sql:param value="${sessionScope.user.databaseId}" />
	<sql:param value="1" />
</sql:query>

<sql:query var="projects" dataSource="jdbc/treeanno">
SELECT * FROM projects JOIN users_permissions ON users_permissions.`projectId` = projects.id  WHERE userId=? AND users_permissions.level &gt; 0
	<sql:param value="${sessionScope.user.databaseId}" />
</sql:query>




<c:if test="${empty rs || rs.rows[0].level &lt; 10 }">
	<c:redirect url="index.jsp"/>
</c:if>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"></meta>
	<script src="jquery-2.1.3.min.js"></script>
	<script src="jquery-ui/jquery-ui.js"></script>
	<script src="i18next/i18next-1.8.0.min.js"></script>
	<script src="script.js"></script>
	<script><![CDATA[
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		lng: "en-US" }, init);
	]]>
	</script>
	<link rel="stylesheet" href="formats.css" type="text/css"> </link>
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"></link> 
</head>
<body>
	<div id="content">
		<table>
		<tr><th>Id</th><th>Name</th></tr>
		<c:forEach var="row" items="${projects.rows}">
			<tr><td>${row.id}</td><td>${row.name}</td><td>Open</td></tr>	
			<sql:query var="docs" dataSource="jdbc/treeanno" sql="SELECT * FROM documents WHERE project=?">
				<sql:param value="${row.id}" />
			</sql:query>
			<div class="display:none;">
			<c:forEach var="docRow" items="${docs.rows}">
				<li>${docRow.id}</li>
			</c:forEach>
			</div>
		</c:forEach>
		</table>
	</div>
	<div id="topbar">
		<button class="button_edit_user">${sessionScope.user.name } (${rs.rows[0].level})</button>
	</div>
</body>
</html>
</jsp:root>