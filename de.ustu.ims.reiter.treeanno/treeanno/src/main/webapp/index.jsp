<?xml version="1.0" encoding="UTF-8" ?>
<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false" import="java.util.*, javax.sql.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<sql:query var="rs" dataSource="jdbc/treeanno">
select id, username from treeanno_users
</sql:query>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}</title>
	<script src="jquery-2.1.3.min.js"></script>
	<script src="jquery-ui/jquery-ui.js"></script>
	<script src="i18next/i18next-1.8.0.min.js"></script>
	<script src="script.js"></script>
	<script>
	var language = "en-US";

	$(document).ready(init_trans(function() {
		init_all();
		$("#login").dialog({
			title: i18n.t("login_dialog_title"),
			buttons: 
			[
			    {
			      text: i18n.t("login_dialog_ok"),
			      icons: {
			        primary: "ui-icon-heart"
			      },
			      click: function() {
			    	  $("form").submit();
			      }
			    }
			]
		});
	}));


	</script>
	<link rel="stylesheet" href="formats.css" type="text/css" />
	<link rel="stylesheet" href="jquery-ui/jquery-ui.css" type="text/css" /> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.structure.css" type="text/css" /> 
	<link rel="stylesheet" href="jquery-ui/jquery-ui.theme.css" type="text/css" /> 

</head>
<body>
	<div>
		<div id="login">
			<form action="login" method="POST">
				
				<p><label for="form_username" class="trans">username</label> 
				<select size="1" id="form_username" name="username">
					<c:forEach var="row" items="${rs.rows}">
					<option value="${row.id}">${row.username}</option>
					</c:forEach>
				</select></p>
				
				<p><label for="form_password" class="trans">password</label> <input type="password" id="form_password" name="password" value="test" /></p>
			</form>
		</div>
	</div>
	<div id="topbar">
		<span class="left">
			<span class="ui-widget">
				<a href="index.jsp">${applicationScope['treeanno.name']}&nbsp;${applicationScope['treeanno.version']}</a>
			</span>
		</span>
	</div>
	<div id="content">
	<div id="news">
		<h1 data-i18n="changelog_h1">Changelog</h1>
		<%@include file="changelog.html"  %>
	</div>
	</div>
</body>
</html>
