<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="menu">
<div id="running"></div>

<ul>
	<li class="active">Document</li>
	<li><a href="select-document-set">Document Set</a></li>		
	<li><a href="select-alignment">Alignment</a></li>		
	<li>Corpus</li>
</ul>
</div>
<div style="clear:both;"></div>
<script>
if (false) {
window.setInterval(function(){
	jQuery.getJSON("rpc/get-meta-information", function (data) { 
		$("#running").empty();
		if (data['background']['active'] > 0) {
			$("#running").prepend(data['background']['active']+" (" + data['background']['waiting'] + ")");
			var dtable = document.createElement("table");
			// $(dtable).hide();
			$("#running").append(dtable);
			for (var proc in data['background']['running']) {
				
				var process = data['background']['running'][proc];
				if (!process["done"]) {
					var row = document.createElement("tr");
					var algo = process['settings']['algorithm'][0];
					var s = algo.split(".");
					$(row).append("<td>"+s[s.length-1]+"</td>");
					$(row).append("<td>"+process['settings']['fileselector']+"</td>");
					$(row).append("<td>"+process['settings']['formula']+"</td>");
					$(dtable).append(row);
				}
			}
			
			$("#running").bind("click", function(event) {
				$("#running > table").show();
			});	
		}
	});
}, 1000);
}
</script>