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
		<div class="originaltext surface ${documents[0].id}"></div>
</div>

</div>
</div>
</div>
<script>
init_controls(".level4 > .menu");

jQuery(window).load(function () {
	load_document("${param.doc}", ".originaltext");
});


</script>

<%@ include file="foot.jsp" %>