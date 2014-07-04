<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Document ${documents[0].id}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-similarity.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
</head>
<body>

<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<jsp:include page="menu.jsp">
	<jsp:param value="event-similarities" name="active"/>
</jsp:include>
<div class="content level3">
<div class="menu">
	<%@ include file="../controls.html" %>
</div>
<div id="loading">
	<img src="gfx/loading1.gif"/>
</div>


<div class="content level4">
<c:forEach var="i" begin="0" end="${arity-1}" >
	<div class="originaltext">
	
	<jsp:include page="../common/document-box-similarities.jsp">
		<jsp:param value="${i}" name="i"/>
	</jsp:include>
	</div>
</c:forEach>

</div>

</div>
</div>
<script>
jQuery(".content.level4").css("display", "none");

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
		jQuery(".content.level4").css("display", "block");
	}
});
	
</script>
<%@ include file="foot.jsp" %>