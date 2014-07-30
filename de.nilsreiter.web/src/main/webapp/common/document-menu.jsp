<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<div class="menu">
<ul>
	<c:choose>
		<c:when test="${param.doc == null }">
			<li class="active">Open</li>
		</c:when>
		<c:otherwise>
			<li><a href="${location.openTarget}">Open</a></li>
		</c:otherwise>
	</c:choose>
	<c:forEach var="object" items="${location.openObjects}">
		<c:choose>
			<c:when test="${object.key == param.doc}">
				<li class="active">${object.value['title']}</li>
			</c:when>
			<c:otherwise>
				<li><a href="${location.viewTarget}?doc=${object.key}">${object.value['title']}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
</div>
<div style="clear:both;"></div>