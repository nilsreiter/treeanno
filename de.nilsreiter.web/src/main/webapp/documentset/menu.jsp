<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<div class="menu">
<ul>
	<li><a href="select-document-set">Open</a></li>
	<c:choose>
		<c:when test="${param.active == 'view'}">
			<li class="active">Alignment ${param.doc}</li>
		</c:when>
		<c:otherwise>
			<li><a href="view-alignment?doc=${param.doc }">Alignment ${param.doc}</a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.active == 'event-similarities'}">
			<li class="active">Event Similarities</li>
		</c:when>
		<c:otherwise>
			<li><a href="view-event-similarities-documentset?doc=${param.doc}">Event Similarities</a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.active =='event-scores' }">
			<li class="active">Event Scores</li>
		</c:when>
		<c:otherwise>
			<li><a href="view-event-scores?doc=${param.doc}">Event Scores</a></li>		
		</c:otherwise>
	</c:choose>
</ul>
</div>
<div style="clear:both;"></div>