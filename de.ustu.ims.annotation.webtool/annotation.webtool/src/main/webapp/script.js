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
	jQuery.getJSON("annotatorjs/document/"+docid, function(data) {
		$("#artifact").append(data['text']);
		var ann = $('#artifact').annotator();
		ann.annotator('addPlugin', 'Unsupported');
		ann.annotator('addPlugin', 'Store', {
			prefix: 'annotatorjs/document/'+docid
		});
		ann.annotator('addPlugin', 'Permissions', {
		  user: username,
		  permissions: {
		    'read':   [],
		    'update': [username],
		    'delete': [username],
		    'admin':  [username]
		  }
		});
		ann.annotator('addPlugin', 'Tags');
		$("#artifact").show();
	});
	
}