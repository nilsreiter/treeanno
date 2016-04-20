<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/javascript; charset=UTF-8" 
		pageEncoding="UTF-8" session="false"/>
	<jsp:useBean id="Perm" class="de.ustu.ims.reiter.treeanno.Perm" scope="page"/>
	<jsp:useBean id="ProjectType" class="de.ustu.ims.reiter.treeanno.ProjectType" scope="page" />
<![CDATA[
var includeSeparationWhenMerging = ${applicationScope['treeanno.includeSeparationWhenMerging']};
var showText = ${applicationScope['treeanno.ui.showTextOnButtons']};
var paragraphSplitBehaviour = "${applicationScope['treeanno.ui.paragraphSplitBehaviour']}";
var paragraphSplitCharacter = "${applicationScope['treeanno.ui.paragraphSplitCharacter']}";

var Perm = {
	NOACCESS:${Perm.NOACCESS},
	READACCESS:${Perm.READACCESS},
	WRITEACCESS:${Perm.WRITEACCESS},
	PADMINACCESS:${Perm.PADMINACCESS},
	ADMINACCESS:${Perm.ADMINACCESS}
};

var ProjectType = {
	DEFAULT:${ProjectType.DEFAULT},
	ARNDT:${ProjectType.ARNDT}
};
]]>
</jsp:root>