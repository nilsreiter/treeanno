<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<div class="menu">
<ul>
	<c:forEach var="menuItem" items="${location.currentMenu.menuItems}">
		<c:choose>
			<c:when test="${pageContext.request.servletPath == menuItem.jspFile}"><li class="active">${menuItem.name}
				<c:if test="${menuItem.needsDocument}"></c:if>
			</li></c:when>
			<c:when test="${menuItem.needsDocument && param.doc == null }"><li class="disabled">${menuItem.name}</li></c:when>
			<c:otherwise><li><a href="${menuItem.href}<c:if test="${menuItem.needsDocument}">?doc=${param.doc}</c:if>">${menuItem.name}</a></li></c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
</div>
<div style="clear:both;"></div>