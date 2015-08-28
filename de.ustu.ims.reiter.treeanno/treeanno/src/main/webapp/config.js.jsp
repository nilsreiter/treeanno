<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/javascript; charset=UTF-8" 
		pageEncoding="UTF-8" session="false"/>
	<jsp:useBean id="Perm" class="de.ustu.ims.reiter.treeanno.Perm" scope="page"/>
<![CDATA[
var includeSeparationWhenMerging = ${applicationScope['treeanno.includeSeparationWhenMerging']};
var Perm = new Object();
Perm["NOACCESS"] = ${Perm.NOACCESS};
Perm["READACCESS"] = ${Perm.READACCESS};
Perm["WRITEACCESS"] = ${Perm.WRITEACCESS};
Perm["PADMINACCESS"] = ${Perm.PADMINACCESS};
Perm["ADMINACCESS"] = ${Perm.ADMINACCESS};
var showText = ${applicationScope['treeanno.showTextOnButtons']};

]]>
</jsp:root>