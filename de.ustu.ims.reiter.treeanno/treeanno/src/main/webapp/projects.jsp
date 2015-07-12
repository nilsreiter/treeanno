<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions">
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true"/>
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="false" />
<c:if test="${ empty sessionScope.User }">
	<c:redirect url="index.jsp"/>
</c:if>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}: Projects</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="formats.css" type="text/css"> </link>
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"></link> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"></link> 
	
	<script src="jquery-2.1.3.min.js">
	//<![CDATA[
	//]]>
	</script>
	<script src="jquery-ui/jquery-ui.js" >
	// <![CDATA[
	// ]]>
	</script>
	<script src="i18next/i18next-1.8.0.min.js">
	// <![CDATA[
 	// ]]>
	</script>
	<script src="config.js.jsp">
	// <![CDATA[
	// ]]>
	</script>
	<script src="script.js">
	// <![CDATA[
	// ]]>
	</script>
	
	<c:if test="${not empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = ${param.projectId};
			var language = "${sessionScope.User.language}";
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
	<c:if test="${empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = -1;
			var language = "${sessionScope.User.language}";
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
</head>
<body>
	<div id="content" class="splitcontent">
		<div class="splitleft">
		<div id="projectlistarea">
			<h2 data-i18n="list_of_projects">list_of_projects</h2>
			<table>
				<thead><tr><th data-i18n="project_id"></th><th data-i18n="project_name"></th></tr></thead>
				<tbody>  </tbody>
			</table>
		</div>
		</div>
		<div class="splitright">
		<div id="documentlistarea">
		</div>
		</div>
	</div>
	<div id="topbar">
		<span class="ui-widget left">
			<span><a href="index.jsp">${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}</a></span>
		</span>
		<span class="right">
			<button class="button_edit_user">${sessionScope.User.name }</button>
		</span>
	</div>
</body>

</html>
</jsp:root>