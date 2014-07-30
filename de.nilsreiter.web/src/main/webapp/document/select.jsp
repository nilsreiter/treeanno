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

<div id="documentpicker" class="dialog picker">
	<h1>Open Document</h1>
	<table class="filelist">
	<thead><tr><th>Id</th><th>Corpus</th><th>Name</th><th>Content</th></tr></thead>
	<tbody></tbody>
	</table>
	
</div>
</div>
</div>
</div>
<script>
populate_file_list("#documentpicker");
</script>
</body>
</html>
