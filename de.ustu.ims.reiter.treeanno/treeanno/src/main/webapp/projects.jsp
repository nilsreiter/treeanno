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



<sql:query var="projects" dataSource="jdbc/treeanno">
SELECT treeanno_projects.id, concat(treeanno_projects.id,'') AS pid, treeanno_projects.name FROM treeanno_projects JOIN treeanno_users_permissions ON treeanno_users_permissions.`projectId` = treeanno_projects.id  WHERE userId=? AND treeanno_users_permissions.level &gt; 0
	<sql:param value="${sessionScope.user.databaseId}" />
</sql:query>




<c:if test="${empty projects}">
	<c:redirect url="index.jsp"/>
</c:if>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="formats.css" type="text/css"> </link>
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"><![CDATA[]]></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"></link> 
	
	<script src="jquery-2.1.3.min.js">
	//<![CDATA[
	//]]>
	</script>
	<script src="jquery-ui/jquery-ui.js" >
	// <![CDATA[
	// ]]>
	</script>
	<script src="i18next/i18next-1.8.0.min.js">
	// <![CDATA[
 	// ]]>
	</script>
	<script src="script.js">
	// <![CDATA[
	// ]]>
	</script>
	
	<c:if test="${not empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = ${param.projectId};
			var language = "${sessionScope.user.language}";
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
	<c:if test="${empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = 0;
			var language = "${sessionScope.user.language}";
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
</head>
<body>
	<div id="content" class="splitcontent">
		<div id="projectlistarea">
		<h2 class="trans">list_of_projects</h2>
		<table>
		<tr><th class="trans">project_id</th><th class="trans">project_name</th></tr>
		<c:forEach var="prow" items="${projects.rows}">			
			<tr>
				<td>${prow.pid}</td>
				<td>${prow.name}</td>
				<td><button id="project-show-button-${prow.pid}" onclick="show_documentlist(${prow.pid} , '${prow.name}' )" class="trans">open</button></td>
			</tr>	
		</c:forEach>
		</table>
		</div>
		<div id="documentlistarea">
			<c:forEach var="p2row" items="${projects.rows}">
				<sql:query var="docs" dataSource="jdbc/treeanno" sql="SELECT id,name,modificationDate FROM treeanno_documents WHERE project=?">
					<sql:param value="${p2row.pid}" />
				</sql:query>
				<div class="documentlist project-${p2row.pid}">
					<h2>Documents in &quot;${p2row.name}&quot;</h2>
					<table>
					<tr><th class="trans">document_id</th><th class="trans">document_name</th><th class="trans">document_mod_date</th></tr>
					<c:forEach var="docRow" items="${docs.rows}">
						<tr><td>${docRow.id}</td><td>${docRow.name }</td><td>${docRow.modificationDate}</td><td><button class="trans" onclick="window.location.href='main.jsp?documentId=${docRow.id}'">open</button></td></tr>
					</c:forEach>
					</table>
				</div>
			</c:forEach>
		</div>
	</div>
	<div id="topbar">
		<span class="ui-widget left">
			<span><a href="index.jsp">${applicationScope['treeanno.name']} ${applicationScope['treeanno.version']}</a></span>
		</span>
		<span class="right">
			<button class="button_edit_user">${sessionScope.user.name }</button>
		</span>
	</div>
</body>

</html>
</jsp:root>