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
	<script src="jquery.noty.packaged.min.js">
	//<![CDATA[
	//]]>
	</script>
	<script src="rpc/config">
	// <![CDATA[
	// ]]>
	</script>
	<script src="script.js">
	// <![CDATA[
	// ]]>
	</script>
	<script src="script-projects.js">
	// <![CDATA[
	// ]]>
	</script>
	
	<c:if test="${not empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = ${param.projectId};
			var language = "${sessionScope.User.language}";
			var admin = ${sessionScope.User.admin};
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
	<c:if test="${empty param.projectId}">
		<script>
		//<![CDATA[
			var selected = -1;
			var language = "${sessionScope.User.language}";
			var admin = ${sessionScope.User.admin};
			$(document).ready(init_trans(init_projects));
		//]]>
		</script>
	</c:if>
</head>
<body>
	<div id="topbar">
		<span class="ui-widget left">
			<span><a href="index.jsp">${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}</a></span>
		</span>
		<span class="right">
			<button class="button_edit_user">${sessionScope.User.name }</button>
		</span>
	</div>
	<div id="content" class="splitcontent">
		<div class="splitleft">
			<div id="projectlistarea">
				<h2 data-i18n="list_of_projects">list_of_projects</h2>
				<table>
					<thead><tr><th data-i18n="project_id"></th><th data-i18n="project_name"></th></tr></thead>
					<tbody></tbody>
				</table>
				<button id="new_project_open_dialog"></button>
			</div>
			<c:if test="${ sessionScope.User.admin }">
			<div id="useradminarea">
				<h2 data-i18n="user_admin">user_admin</h2>
				<table>
					<thead><tr><th data-i18n="user_id">user_id</th><th data-i18n="user_name">user_name</th><th data-i18n="user_email">user_email</th><th data-i18n="user_actions">user_actions</th></tr></thead>
					<tbody>
					</tbody>
				</table>
				<button data-i18n="new_user.open_dialog" id="new_user_open_dialog"></button>
			</div>
			</c:if>
		</div>
		<div class="splitright">
		</div>
	</div>
	<div id="documentuploaddialog">
		<form class="upload" method="POST" action="rpc/NewDocument" enctype="multipart/form-data">
			<div>
				<input class="fileupload" type="file" name="files" multiple="multiple" accept="text/plain" />
				<p>(only plain text files)</p>
			</div>
			<div>
				<label for="segmenttype" data-i18n="new_document.type_description">new_document.type_description</label>
				<select name="segmenttype" id="segmenttype">
					<option value="sentence" data-i18n="new_document.sentence">new_document.sentence</option>
					<option value="token" data-i18n="new_document.token">new_document.token</option>
				</select>
			</div>
			<input type="hidden" name="projectId" value="1" />
		</form>
	</div>
	<div id="newuserdialog">
		<form method="POST" action="rpc/user/create" enctype="multipart/form-data">
			<div>
				<label for="new_user_name" data-i18n="new_user.name">new_user.name</label>
				<input type="text" id="new_user_name" />
			</div>
			<div>
				<label for="new_user_email" data-i18n="new_user.email">new_user.email</label>
				<input type="text" id="new_user_email" />
			</div>
			<div>
				<label for="new_user_language" data-i18n="new_user.language">new_user.language</label>
				<select id="new_user_language">
					<option value="en" selected="selected">English</option>
					<option value="de">German</option>
				</select>
			</div>
		</form>
	</div>
	<div id="newprojectdialog">
		<div>
			<label for="new_project_name" data-i18n="new_project.name">new_project.name</label>
			<input type="text" id="new_project_name" />
		</div>
	</div>
</body>
</html>
</jsp:root>