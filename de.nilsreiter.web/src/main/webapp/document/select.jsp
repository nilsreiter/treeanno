<%@ page import="de.nilsreiter.web.beans.menu.Location,
	de.nilsreiter.web.beans.menu.Location.Area" %>

<%
	if (session.getAttribute("location") == null) {
		session.setAttribute("location", new Location(Area.Document));
	}
	((Location)session.getAttribute("location")).setArea(Area.Document);
%>
<%@ include file="head.jsp" %>

<div class="level2 content">
<%@ include file="../common/document-menu.jsp" %>

<div class="level3 content">

<div class="content level4">

<div class="content level5">

	<h1>Select Document</h1>
	<div>
	<table class="filelist">
	<thead><tr><th>Id</th><th>Corpus</th><th>Name</th><th>Content</th></tr></thead>
	<tbody></tbody>
	</table>
	</div>
</div>
</div>
</div>
</div>
<script>
$(".level5").accordion({ header: "h1", heightStyle:"content" });
jQuery.getJSON("rpc/get-document-info", function (data) { 
	for (var i in data) {
		var row = document.createElement("tr");
		
		$(row).append("<td>"+data[i]['databaseId']+"</td>");
		$(row).append("<td>"+data[i]['corpus']+"</td>");
		$(row).append("<td>"+data[i]['id']+"</td>");
		$(row).append("<td>"+data[i]['textBegin']+" ...</td>");
		$(row).bind("click", {did:data[i]['id']}, function(data) {location.href="view-document?doc="+
			data.data['did']});
		$("table.filelist tbody").append(row);
	}
});
</script>
</body>
</html>
