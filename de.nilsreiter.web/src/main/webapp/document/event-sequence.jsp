<%@ include file="head.jsp" %>

<div class="content level2">
<jsp:include page="menu.jsp">
	<jsp:param value="event-sequence" name="active"/>
	<jsp:param value="${documents[0].id}" name="doc"/>
</jsp:include>

<div class="content level3">

<div class="content level4">
	<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" version="1.1">
		<rect x="0" y="0" width="1137" height="400" strokeWidth="0" fill="#AAA" class=" highcharts-background"></rect>	
	</svg>
<c:forEach var="event" items="${documents[0].events}">
	<div class="event">
		<table>
		<tr><td><div class="eventclass">${event.eventClass}</div></td><td>
		<div class="surface">${event.tokens[0].surface}</div></td></tr>
		<c:forEach var="role" items="${event.arguments}">
			<tr><td>${role.key}</td><td>
				<c:forEach var="token" items="${role.value}">${token.surface} 
					<c:forEach var="mention" items="${token.mentions}">${mention.id }</c:forEach>
				</c:forEach></td></tr>
		</c:forEach>
		</table>
	</div>
</c:forEach>
</div>
</div>
</div>

<%@ include file="foot.jsp" %>
