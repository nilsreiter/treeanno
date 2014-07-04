<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<div class="menu">
<ul>
	<li><a href="select-document">Open</a></li>
	<c:choose>
		<c:when test="${param.active == 'document'}">
			<li class="active">Document <strong>${param.doc}</strong></li>
		</c:when>
		<c:otherwise>
			<li><a href="view-document?doc=${param.doc}">Document <strong>${param.doc}</strong></a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.active == 'event-similarities' }">
			<li class="active">Event Similarities</li>
		</c:when>
		<c:otherwise>
			<li><a href="view-event-similarities-document?doc=${param.doc}">Event Similarities</a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.active == 'event-sequence' }">
			<li class="active">Event Sequence</li>
		</c:when>
		<c:otherwise>
			<li><a href="view-event-sequence?doc=${param.doc}">Event Sequence</a></li>
		</c:otherwise>
	</c:choose>
</ul>
</div>
<div style="clear:both;"></div>