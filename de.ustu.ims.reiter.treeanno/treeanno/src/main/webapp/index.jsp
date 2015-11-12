<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:sql="http://java.sun.com/jsp/jstl/sql">
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false"/>
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="false" />
<sql:query var="rs" dataSource="jdbc/treeanno-mirror" sql="select id, username from treeanno_users">
</sql:query>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}</title>
	<script src="jquery-2.1.3.min.js">
	//<![CDATA[
	//]]>
	</script>
	<script src="jquery-ui/jquery-ui.js">
	// <![CDATA[
	// ]]>
	</script>
	<script src="i18next/i18next-1.8.0.min.js">
	//<![CDATA[
	//]]>
	</script>
	<script src="script.js">
	//<![CDATA[
	//]]>
	</script>
	<script>
	// <![CDATA[
	var language = "en-US";

	$(document).ready(init_trans(function() {
		init_all();
		$("button.button_login").button({
			disabled: true,
			label:i18n.t("login_open_dialog"),
			
		}).click(function(event, ui) {
			$("#login").dialog("open");
			$("button.button_login").button({disabled:true});
		});

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
			],
			close: function( event, ui ) {
				$("button.button_login").button({disabled:false});
			}
		});
	}));
	// ]]>
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
				<a href="index.jsp">${applicationScope['treeanno.name']}&#160;${applicationScope['treeanno.version']}</a>
			</span>
		</span>
		<span class="right">
			<button class="button_login" data-i18n="user_login">Login</button>
		</span>		
	</div>
	<div id="content">
	<div id="news">
		<h1 data-i18n="changelog_h1">Changelog</h1>
		<jsp:include page="changelog.html" />
	</div>
	</div>
</body>
</html>
</jsp:root>