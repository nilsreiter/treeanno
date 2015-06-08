<?xml version="1.0" encoding="UTF-8" ?>
<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, javax.sql.*"/>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<script src="jquery-2.1.3.min.js"></script>
	<script src="jquery-ui/jquery-ui.js"></script>
	<script src="i18next/i18next-1.8.0.min.js"></script>
	<script src="nestedSortable-1.3.4/jquery.ui.nestedSortable.js"></script>
	<script src="script.js"></script>
	<script>
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		lng: "en-US" }, init);
	</script>
	<link rel="stylesheet" href="formats.css" type="text/css">
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"> 
</head>
<body>
	<div id="content">
	    <ul id="outline" class="text sortable">
    	</ul>
	</div>
	<div id="split">
			<textarea id="form_splittext" rows="5" cols="50" spellcheck="false"></textarea>
	</div>
	<div id="merge">
		<p></p>
		<select id="form_mergecandidates" size="2"></select>
	</div>
	<div id="topbar">
		<button class="button_save_document"></button>
		<button class="button_edit_user">${sessionScope.user.name }</button>
	</div>
</body>
</html>
