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
	<li class="${param.class_event_scores}"><a href="view-event-scores">Event Scores</a></li>
</ul>
</div>
<div style="clear:both;"></div>