<?xml version="1.0" encoding="UTF-8" ?>
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="location" class="de.nilsreiter.web.beans.menu.Location" scope="session">
	<jsp:setProperty name="area" property="de.nilsreiter.web.beans.menu.Location.Area.Document" />
</jsp:useBean>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>

<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />

</head>
<body>

<div class="level1">
<%@ include file="../menu/area.jsp" %>