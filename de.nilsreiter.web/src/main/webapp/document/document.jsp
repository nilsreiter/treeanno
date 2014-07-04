<%@ include file="head.jsp" %>

<div class="content level2">
<jsp:include page="menu.jsp">
	<jsp:param value="document" name="active"/>
	<jsp:param value="${documents[0].id}" name="payload"/>
	<jsp:param value="${documents[0].id}" name="doc"/>
</jsp:include>

<div class="content level3">
<div class="menu">
	<%@ include file="../controls.html" %>
</div>

<div class="content level4">
		<div class="originaltext">
		<jsp:include page="../common/document-box.jsp">
			<jsp:param value="0" name="i"/>
		</jsp:include>
		</div>
</div>

</div>
</div>
<%@ include file="foot.jsp" %>