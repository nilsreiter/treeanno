<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/scripts.js" type="text/javascript"></script>

<title></title>
</head>
<body>
<jsp:include page="${param.area}/menu.jsp" />

<div class="error dialog">
<h1>${title}</h1>
<p>${message}</p>
</div>
</body>
</html>