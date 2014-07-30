<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Document ${doc}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-similarity.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/event-similarities.js" type="text/javascript"></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />

</head>
<body>

<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="level2 content">
<%@include file="../common/document-menu.jsp" %>

<div class="level3 content">
<%@include file="../common/menu.jsp" %>




<div class="content level4">
<div class="menu">
<%@ include file="../controls.html" %>
</div>

<div class="content level5">
<div id="loading">
	<img src="gfx/loading1.gif"/>
</div>

<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="originaltext surface">
	
	<jsp:include page="../common/document-box-similarities.jsp">
		<jsp:param value="${i}" name="i"/>
	</jsp:include>
	</div>
</c:forEach>

</div>

</div>
</div>
</div>
</div>
<script>
init_controls("div.level4 > div.menu");
jQuery(".content.level5").css("display", "none");

jQuery.getJSON('rpc/get-event-similarities?doctype=document&doc=${doc}', function (data) { 
	for(var tokId in data) {
		for (var type in data[tokId]) {
			//alert("#"+tokId+" div.typechooser label."+type+ " input");
			//alert(type);
			$("#"+tokId+" div.typechooser label."+type+ " input").bind("click", {'type':type,'data':data[tokId][type], 'tokId':tokId},  function (event) {
				var type = event.data.type;
				var tokId = event.data.tokId;
				$("span.st"+type+" > span").unwrap();
				$("div#sm"+type).remove();
				if ($("#"+tokId+" div.typechooser label."+type+ " input").prop("checked")) {
					for (var oTok in event.data.data) {
						$("#"+oTok).wrap("<span class=\"st"+type+"\" style=\"background-color:rgba("+colors[type]+","+event.data.data[oTok]+")\"></span>");
					}
					$("#"+tokId).prepend("<div id=\"sm"+type+"\" class=\"sourcemarker\" style=\"background-color:rgb("+colors[type]+");\"></div>");
					$("#sm"+type).bind("click", {type:type,tokId:tokId}, function(event) { 
						$("#"+event.data.tokId+" div.typechooser label."+event.data.type+ " input").click();
					});
				}
			});
		}
		$("#loading").css("display", "none");
		jQuery(".content.level5").css("display", "block");
	}
});
	
</script>
</body>
</html>
