<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Set</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-similarity.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/event-similarities.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<script src="js/highcharts.js" type="text/javascript"></script>
<script src="js/modules/heatmap.js" type="text/javascript"></script>

</head>
<body>
<div class="level1">
<%@ include file="../menu/area_DocumentSet.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>


<div class="level3 content">
	<ul>
		<li><a href="#view">View</a></li>
		<li><a href="#event-similarities">Event Similarities</a></li>
		<li><a href="#event-search">Event Search</a></li>
		<li><a href="#document-similarities">Document Similarities</a>
	</ul>
	<div id="view" class="content level4">
		<div class="menu">
			<%@ include file="../controls.html" %>
		</div>
		<div class="level5 content">
		<c:forEach var="i" begin="0" end="${arity-1}" >
			<div class="alignmenttext surface">
				<h1>${documents[i].id}</h1>
				<div class="${documents[i].id}"></div>
				<script>	
		load_document_html("${documents[i].id}", ".${documents[i].id}", function() {$("#loading").hide();});
				</script>
			</div>
		</c:forEach>
		<div style="clear:left;"></div>
		</div>
	</div>
	<div id="event-similarities"  class="content level4">
		<div class="menu">
			<div>
				<c:forEach var="i" begin="0" end="${arity-1}" >
					<input type="checkbox" id="toggle-document-${documents[i].id}" checked="checked" value="${documents[i].id}" /><label for="toggle-document-${documents[i].id}">${documents[i].id}</label>
				</c:forEach>
			</div>
		</div>
		<script>
			$(".level4 > .menu > div").buttonset();
			$(".level4 .menu div input").bind("change", function(event) {
				val = $(event.target).val();
				jQuery('#event-similarities .alignmenttext .'+val).parent().toggle();		
			});
			</script>
		<div class="content level5" >
			
			
		<c:forEach var="i" begin="0" end="${arity-1}" >
			<div class="alignmenttext surface">
				<h1>${documents[i].id}</h1>
				<div class="${documents[i].id}"></div>
				<script>
					load_document_html("${documents[i].id}", ".${documents[i].id}", function() {
						
						load_similarities("${param.doc}", 2, "documentset");
					});
				</script>
			</div>
		</c:forEach>
		<div style="clear:left;"></div>
		</div>
	</div>
	<div id="event-search" class="content level4">
		<%@ include file="event-search.html" %>
	</div>
	<div id="document-similarities" class="content level4">
		<div class="menu">
			<input type="checkbox" id="showValuesButton" name="showValuesButton" value="1" checked="checked">
			<label for="showValuesButton">Show Values</label>
		</div>
		<div class="content level5">
			<div id="container"></div>
			<script>
				load_document_similarities("${param.doc}");
				$('#showValuesButton').button().on("click",function () {
					load_document_similarities("${param.doc}");
				});
			</script>

		</div>
	</div>
</div>
</div>
</div>
<script>
init_controls("div.level4 > div.menu");

$( ".level3.content" ).tabs();

jQuery.getJSON("rpc/set-area?arg=DocumentSet", function (data) { });
	

</script>
</body>
</html>