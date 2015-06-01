function init(t) {
	$(function() {
		$("#artifact").hide();
		$("#login").dialog({
			title: t("username"),
			buttons: 
			[
			    {
			      text: t("ok"),
			      icons: {
			        primary: "ui-icon-heart"
			      },
			      click: login
			    }
			]
		});
		
	});
}

function login() {
	var docid = "doc01";
	$( this ).dialog( "close" );
	var username = $("#form_username").val();
	jQuery.getJSON("ControllerServlet?document="+docid, function(data) {
		
	});
	
}