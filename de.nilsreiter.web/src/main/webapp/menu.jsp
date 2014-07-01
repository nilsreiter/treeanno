<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<div class="menu">
<ul>
	<c:choose>
		<c:when test="${param.active == 'document'}">
			<li class="active"><a href="view-document.do">Document <strong>${param.payload}</strong></a></li>
		</c:when>
		<c:otherwise>
			<li><a href="view-document.do">Document Viewer</a></li>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.active == 'alignment'}">
			<li class="active"><a href="view-alignment.do">Alignment <strong>${param.payload}</strong></a></li>
		</c:when>
		<c:otherwise>
			<li><a href="view-alignment.do">Alignment Viewer</a></li>
		</c:otherwise>
	</c:choose>
	<li class="${param.class_event_similarities}"><a href="view-event-similarities">Event Similarity Viewer</a></li>
	<li class="${param.class_event_scores}"><a href="view-event-scores">Event Scores</a></li>
</ul>
</div>
<div style="clear:both;"></div>