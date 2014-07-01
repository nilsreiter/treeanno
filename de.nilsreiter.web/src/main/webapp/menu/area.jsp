<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="menu">
<ul>
	<c:choose>
		<c:when test="${location.area == 'Document'}">
			<li class="active">Document</li>
		</c:when>
		<c:otherwise>
			<li><a href="select-document">Document</a></li>		
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${location.area == 'Alignment'}">
			<li class="active">Document Set</li>
		</c:when>
		<c:otherwise>
			<li><a href="select-document-set">Document Set</a></li>		
		</c:otherwise>
	</c:choose>
	<li>Corpus</li>
</ul>
</div>
<div style="clear:both;"></div>