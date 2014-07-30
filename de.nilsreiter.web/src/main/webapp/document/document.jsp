<%@ include file="head.jsp" %>
<div class="level2 content">
<%@ include file="../common/document-menu.jsp" %>

<div class="level3 content">
<%@ include file="../common/menu.jsp" %>


<div class="content level4">
<div class="menu">
<%@ include file="../controls.html" %>
</div>

<div class="content level5">
		<div class="originaltext surface ${documents[0].id}">
		<jsp:include page="../common/document-box.jsp">
			<jsp:param value="0" name="i"/>
		</jsp:include>
		</div>
</div>

</div>
</div>
</div>
<script>
init_controls(".level4 > .menu");
</script>

<%@ include file="foot.jsp" %>