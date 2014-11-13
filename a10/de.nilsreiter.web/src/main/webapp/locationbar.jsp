<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="locationbar" class="bar">
Corpus: ${ location.corpus } <c:choose>
	<c:when test="${location.corpus == 'Document' }">&gt; Document ${location.documentOrAlignment }</c:when>
	<c:when test="${location.corpus == 'Alignment'}">&gt; Alignment ${location.documentOrAlignment }</c:when>
</c:choose>
</div>