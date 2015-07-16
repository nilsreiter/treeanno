<?xml version="1.0" encoding="UTF-8" ?>
<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, javax.sql.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${ empty sessionScope.User }">
	<c:redirect url="index.jsp"/>
</c:if>



<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<script src="jquery-2.1.3.min.js"></script>
	<script src="jquery.smarttruncation.js"></script>
	<script src="jquery-ui/jquery-ui.js"></script>
	<script src="i18next/i18next-1.8.0.min.js"></script>
	<script src="nestedSortable-1.3.4/jquery.ui.nestedSortable.js"></script>
	<script src="config.js.jsp"></script>
	<script src="script.js"></script>
	<script>

	var language = "${sessionScope.User.language}";
	var documentIds = [ "${paramValues.documentId[0]}", "${paramValues.documentId[1]}" ];
	var treeanno = new Object();
	treeanno["version"] = "${applicationScope['treeanno.version']}";
	treeanno["name"] = "${applicationScope['treeanno.name']}";
	$(document).ready(init_trans(init_parallel));
	
	</script>
	<link rel="stylesheet" href="help.css" type="text/css">
	<link rel="stylesheet" href="formats.css" type="text/css">
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css"> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css"> 
</head>
<body>
	<c:if test="${empty param.documentId}">
		<p>Need to give a document parameter</p>
	</c:if>
	<div id="status"><span class="loading"><img src="gfx/loading1.gif" /></span></div>
	<div id="content">
		<ul  class="outline text sortable">
    	</ul>
    	<ul  class="outline text sortable">
    	</ul>
	</div>
	<div id="split">
		<p data-i18n="howto_split" class="trans">howto_split</p>
		<textarea id="form_splittext" rows="5" cols="50" spellcheck="false" tabindex="0"></textarea>
	</div>
	<div id="topbar">
		<span class="left">
			<span class="ui-widget breadcrumb">
				<a href="index.jsp">${applicationScope['treeanno.name']}&nbsp;${applicationScope['treeanno.version']}</a> &gt;
			</span>
		</span>
		<span class="middle ui-widget" >
			<span class="search_container">
				<span class="ui-icon ui-icon-search"></span>
				<input type="search" id="form_search" class="" />
			</span>
		</span>
		<span class="right">
			<!-- <button class="button_change_document">open</button> -->
			<button class="button_save_document">save</button>
			<button class="button_edit_user">${sessionScope.User.name}</button>
		</span>
	</div>
	<div id="error"></div>
	<jsp:include page="help.html" />
</body>
</html>
