$.noty.defaults.layout = 'topRight';
$.noty.defaults.theme = 'relax';
$.noty.defaults.timeout = 2000;


function init_all() {
	$("#topbar .right").buttonset();
	$(".nobutton").button({
		disabled:true
	});
}



function init_trans(fnc) {
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		nsseparator:'::',
		lng: language.substring(0,2) }, function(t) {
			$("body").i18n();
			$(".trans").each(function(index, element) {
				var text = $(element).text().trim();
				$(element).empty();
				$(element).append(t(text));
			});
			if (fnc) fnc();
		});
}

// Source: http://stackoverflow.com/questions/280634/endswith-in-javascript
function ends_with(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function init_parallel() {
	init_all();
	$("#split").hide();
	$( "button.button_edit_user" ).button({
		icons: { primary: "ui-icon-person", secondary:null },
		disabled: true
	});
	
	$( "button.button_change_document" ).button({
		icons: { primary: "ui-icon-folder-collapsed", secondary:null },
		//label: i18n.t("open"),
	}).click(function() {
		window.location.href="projects.jsp?projectId="+projectId;
	});
	$( "button.button_save_document" ).button({
		icons: { primary: "ui-icon-disk", secondary:null },
		label: i18n.t("save"),
	}).click(
		function() {
			save_document();
		}
	);
	$("#button_search").button({
		icons:{primary: "ui-icon-search", secondary: null },
		label: i18n.t("search")
	}).click(search);
	$("#form_search").keyup(search);
	$("#form_search").focus(function() {enable_interaction=false});
	$("#form_search").blur(function() {enable_interaction=true});
	
	disableSaveButton();

	$(".outline").hide();
	$("#content > div > .outline").each(function(index, element) {
		var documentId = userDocumentIds[index];
		jQuery.getJSON("DocumentContentHandling?userDocumentId="+documentId, function(data) {
			$(element).parent().prepend("<h2>"+i18n.t("parallel.annotations_from_X",{"user":data["document"]["user"]["name"]})+"</h2>");
			if (ends_with($(".breadcrumb").text().trim(), ">")) {
				$(".breadcrumb").append(" <a href=\"projects.jsp?projectId="+data["document"]["document"]["project"]["id"]+"\">"+data["document"]["document"]["project"]["name"]+"</a> &gt; "+i18n.t("parallel.annotations_for_X",{"document":data["document"]["document"]["name"]}));
				document.title = treeanno["name"]+" "+treeanno["version"]+": "+i18n.t("parallel.annotations_title_for_X",{"document":data["document"]["document"]["name"]});
			} else {
				//$(".breadcrumb").append(", "+data["document"]["name"]);
				// document.title = document.title + ", " + data["document"]["name"];
			} 
			
			var list = data["list"];
			
			while (list.length > 0) {
				var item = list.shift();
				
				if ('parentId' in item) {
					var parentId = item['parentId'];
					var parentItem = $("li[data-treeanno-id='"+parentId+"']", element);
					if (parentItem.length == 0)
						list.push(item);
					else {
						if (parentItem.children("ul").length == 0)
							parentItem.append("<ul></ul>");
						$("li[data-treeanno-id='"+parentId+"'] > ul", element).append(get_html_item(item, 0));
					}
				} else {
					$(element).append(get_html_item(item, 0));
				}
			}
		
			// $(element).children('li:first-child').addClass("selected");
			/*document.onkeydown = function(e) {
				key_down(e);
			};
			document.onkeyup = function(e) {
				key_up(e);
			};*/
			/*$("li", element).click(function(e) {
				if (shifted) {
					// if they have the same parent
					if ($(this).parent().get(0) == $(".selected").last().parent().get(0)) {
						$(".selected").last().nextUntil(this).addClass("selected");
						$(this).addClass("selected");
					}
				} else {
					$("#outline li").removeClass("selected");
					$(this).addClass("selected");
				}
			});*/
			$("#status .loading").hide();
			$(element).show();
			/*$("li > div", element).smartTruncation({
			    'truncateCenter'    : true
			 });*/
		});
	});
	
}

