<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Document ${document.id}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="menu.jsp" />

<div class="content">
<h1>${document.id}</h1>

<div class="originaltext box">
<c:set var="lastpos" value="0" />
<c:out value="${map['t1420']}" />
<c:forEach var="sentence" items="${document.sentences}" ><span id="${sentence.id }" class="sentence"><c:forEach var="token" items="${sentence.tokens}"><c:if test="${token.begin > lastpos}"> </c:if><span id="${token.id}" class="token ${map[token]} " title="${map[token]}"><c:out value="${token.surface}"/></span><c:set var="lastpos" value="${token.end}" /></c:forEach>
</span>
</c:forEach>
</div>

<div class="box">
	<input type="checkbox" onchange="jQuery('.event').toggleClass('highlightEvent')" />Events
	<input type="checkbox" onchange="jQuery('.frame').toggleClass('highlightFrame')" />Frames
</div>
</div>
</body>
</html>
