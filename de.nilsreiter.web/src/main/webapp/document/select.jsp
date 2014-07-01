<%@ page import="de.nilsreiter.web.Location,de.nilsreiter.web.Location.Area"  %>
<jsp:useBean id="location" class="de.nilsreiter.web.Location" scope="request">
	<jsp:setProperty property="area" name="location" value="Document"/>
	<jsp:setProperty property="corpus" name="location" value="Rituals"/>
</jsp:useBean>
<%@ include file="head.jsp" %>
<div id="documentpicker" class="dialog picker">
	<h1>Select Document</h1>
	<ul class="filelist">
	<c:forEach var="documentName" items="${docman.documents}">
		<li onclick="location.href='view-document?doc=${documentName}'">${documentName}</li>
	</c:forEach>
	</ul>
	
</div>
<%@ include file="foot.jsp" %>
