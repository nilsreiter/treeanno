<?xml version="1.0" encoding="UTF-8" ?>
<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, javax.sql.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${ empty sessionScope.user }">
	<c:redirect url="index.jsp"/>
</c:if>

<sql:query var="documentRowSet" dataSource="jdbc/treeanno">
SELECT treeanno_documents.id,project,treeanno_documents.name,treeanno_projects.name,concat(treeanno_projects.name,'') AS pname FROM treeanno_documents JOIN treeanno_projects ON treeanno_documents.`project` = treeanno_projects.id  WHERE treeanno_documents.id=?
	<sql:param value="${param.documentId}" />
</sql:query>

<jsp:useBean id="project" scope="request" class="de.ustu.ims.reiter.treeanno.beans.Project">
	<jsp:setProperty property="name" value="${documentRowSet.rows[0].pname}" name="project"/>
	<jsp:setProperty property="databaseId" value="${documentRowSet.rows[0].project}" name="project"/>
</jsp:useBean>

<jsp:useBean id="document" scope="request" class="de.ustu.ims.reiter.treeanno.beans.Document">
	<jsp:setProperty property="name" value="${documentRowSet.rows[0].name}" name="document"/>
	<jsp:setProperty property="databaseId" value="${documentRowSet.rows[0].id}" name="document"/>
	<jsp:setProperty property="project" value="${session.project}" name="document"/>
</jsp:useBean>


<sql:query var="rs" dataSource="jdbc/treeanno">
SELECT level FROM treeanno_users_permissions WHERE userId=? AND projectId=?
	<sql:param value="${sessionScope.user.databaseId}" />
	<sql:param value="${documentRowSet.rows[0].project}" />
</sql:query>


<c:if test="${empty rs || rs.rows[0].level < 10 }">
	<c:redirect url="index.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<script src="jquery-2.1.3.min.js"></script>
	<script src="jquery-ui/jquery-ui.js"></script>
	<script src="i18next/i18next-1.8.0.min.js"></script>
	<script src="nestedSortable-1.3.4/jquery.ui.nestedSortable.js"></script>
	<script src="script.js"></script>
	<script>
	var documentId = ${param.documentId};
	var projectId = ${requestScope.project.databaseId};
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		lng: "en-US" }, init);
	</script>
	<link rel="stylesheet" href="formats.css" type="text/css">
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"> 
</head>
<body>
	<c:if test="${empty param.documentId}">
		<p>Need to give a document parameter</p>
	</c:if>
	<div id="content">
	    <ul id="outline" class="text sortable">
    	</ul>
	</div>
	<div id="split">
			<textarea id="form_splittext" rows="5" cols="50" spellcheck="false"></textarea>
	</div>
	<div id="merge">
		<p></p>
		<select id="form_mergecandidates" size="2"></select>
	</div>
	<div id="topbar">
		<button class="nobutton">${requestScope.project.name}</button>
		<button class="button_change_document"></button>
		<button class="button_save_document"></button>
		<button class="button_edit_user">${sessionScope.user.name } (${rs.rows[0].level})</button>
	</div>
	<div id="error"></div>
</body>
</html>
