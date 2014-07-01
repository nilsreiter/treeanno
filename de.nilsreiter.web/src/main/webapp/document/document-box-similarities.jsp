<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    

<c:set var="lastpos" value="0" />
<c:forEach var="sentence" items="${documents[param.i].sentences}" ><span id="${sentence.globalId }" class="sentence"><c:forEach var="token" items="${sentence.tokens}"><c:if test="${token.begin > lastpos}"> </c:if><span id="${token.globalId}" class="token ${map[token]} " title="${map[token]}" data-similarities="${similarities[token]}"  ><c:out value="${token.surface}"/><c:if test='${map[token].contains(\"event\")}'><div class="typechooser" id="typechooserFor${token.globalId}">

<c:forEach var="type" items="${similarityTypes }">
	<label class="checkbox ${type.simpleName}"><input type="checkbox" value="${type.simpleName }" onclick="${action[token][type]}"><span></span></label>${type.simpleName }
</c:forEach>

</div></c:if></span><c:set var="lastpos" value="${token.end}" /></c:forEach>
</span>
</c:forEach>
