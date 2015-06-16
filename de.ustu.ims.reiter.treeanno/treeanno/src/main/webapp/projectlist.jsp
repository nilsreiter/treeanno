<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:sql="http://java.sun.com/jsp/jstl/sql">
	<jsp:directive.page contentType="application/json; charset=UTF-8" 
		pageEncoding="UTF-8" session="true"/>
<sql:query var="projects" dataSource="jdbc/treeanno">
SELECT treeanno_projects.id, treeanno_projects.name FROM treeanno_projects JOIN treeanno_users_permissions ON treeanno_users_permissions.`projectId` = treeanno_projects.id  WHERE userId=? AND treeanno_users_permissions.level &gt; 0
	<sql:param value="${sessionScope.user.databaseId}" />
</sql:query>
<c:out value="[" />
<c:set var="rowCounter" scope="page" value="0"/>
<c:forEach var="prow" items="${projects.rows}">
	<c:if test="${rowCounter > 0 }">
		<c:out value="," />	
	</c:if>
	<c:if test="${rowCounter == 0}">
		<c:set var="rowCounter" scope="page" value="${rowCounter+1}"/>
	</c:if>
	<![CDATA[
	{"id":${prow.id},
	 "name":"${fn:escapeXml(prow.name)}"
	}
	]]>
</c:forEach>
<c:out value="]" />
</jsp:root>