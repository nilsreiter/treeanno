<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Set</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>


<div class="content level3">
<%@include file="../common/menu.jsp" %>


<div class="content level4">
<div class="menu">
	<%@ include file="../controls.html" %>
</div>





<div id="alignmentcontent" class="level5 content">
<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="surface alignmenttext ${documents[i].id}">
	<h1>${documents[i].id}</h1>
	<jsp:include page="../common/document-box.jsp">
		<jsp:param value="${i}" name="i"/>
	</jsp:include>
	</div>
</c:forEach>
<div style="clear:left;"></div>

</div>
</div>
</div>
</div>
</div>
<script>
init_controls("div.level4 > div.menu");

</script>
</body>
</html>