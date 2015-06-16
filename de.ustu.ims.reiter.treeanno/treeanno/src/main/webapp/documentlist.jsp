<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:sql="http://java.sun.com/jsp/jstl/sql">
	<jsp:directive.page contentType="application/json; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" trimDirectiveWhitespaces="true"/>
	<sql:query var="projects" dataSource="jdbc/treeanno">
	SELECT treeanno_projects.id, concat(treeanno_projects.id,'') AS pid, treeanno_projects.name FROM treeanno_projects JOIN treeanno_users_permissions ON treeanno_users_permissions.`projectId` = treeanno_projects.id  WHERE userId=? AND treeanno_users_permissions.level > 0 AND treeanno_projects.id=?
		<sql:param value="${sessionScope.user.databaseId}" />
		<sql:param value="${param.projectId}" />		
	</sql:query>
	<sql:query var="docs" dataSource="jdbc/treeanno" sql="SELECT treeanno_documents.id, modificationDate, NAME FROM treeanno_documents, treeanno_users_permissions WHERE treeanno_users_permissions.`projectId` = treeanno_documents.`project` AND userId=? AND LEVEL > 50 AND treeanno_documents.project=?">
		<sql:param value="${sessionScope.user.databaseId }" />
		<sql:param value="${param.projectId}" />
	</sql:query>
	<![CDATA[
	{"project":{
	]]>
	<c:forEach var="prow" items="${projects.rows}">
	<![CDATA[
		"name":"${fn:escapeXml(prow.name)}",
		"id":${prow.id}
	},"documents":
	]]>
	</c:forEach>
	<c:out value="[" />
	<c:set var="rowCounter" scope="page" value="0"/>
	<c:forEach var="prow" items="${docs.rows}">
		<c:if test="${rowCounter > 0 }">
			<c:out value="," />	
		</c:if>
		<c:if test="${rowCounter == 0}">
			<c:set var="rowCounter" scope="page" value="${rowCounter+1}"/>
		</c:if>
		<![CDATA[
			{"id":${prow.id},
			 "name":"${fn:escapeXml(prow.name)}",
			 "modificationDate":"${prow.modificationDate}"}
		]]>
	</c:forEach>
	<c:out value="]" />
	<c:out value="}" />
</jsp:root>