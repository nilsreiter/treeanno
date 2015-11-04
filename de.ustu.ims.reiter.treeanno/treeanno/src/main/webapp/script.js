var enable_interaction = true;

var shifted = false;

var idCounter = 0;

var documents_selected_for_diff = new Array();
var max_documents_for_diff = 2;


var kbkey = { up: 38, down: 40, right: 39, left: 37, 
		enter: 13, s: 83, m:77, c:67, d:68, shift: 16, one: 49 };
var operations = {
		39:{
			// right
			'id':'indent',
			fun:indent
		},
		37:{
			// left
			'id':'outdent',
			fun:outdent
		},
		49:{
			// one
			'id':'mark1',
			fun:function() {
				$(".selected").toggleClass("mark1");
				enableSaveButton();
			}
		},
		67:{
			// c
			'id':'categorize',
			fun:add_category
		},
		68:{
			// d
			'id':'delete_category',
			fun:delete_category
		},
		77:{
			// m
			'id':'merge',
			fun:function() {
				if ($(".selected").length == 2) mergeselected();
			}
		},
		83:{
			// s
			'id':'split',
			fun:splitdialog
		},
		1039:{
			// shift + right
			'id':'force_indent',
			fun:force_indent
		},
		1068:{
			// shift + d
			'id':'delete_virtual_node',
			fun:delete_virtual_node
		}
		
};

function get_html_item(item, i) {
	var htmlItem = document.createElement("li");
	$(htmlItem).attr("title", item['text']);
	$(htmlItem).attr("data-treeanno-id", item['id']);
	$(htmlItem).attr("data-treeanno-begin", item['begin']);
	$(htmlItem).attr("data-treeanno-end", item['end']);
	$(htmlItem).addClass("unselectable");
	if (item['Mark1']) $(htmlItem).addClass("mark1");
	idCounter = Math.max(idCounter, item['id']);
	if ('category' in item)
		$(htmlItem).append("<p class=\"annocat\">"+item['category']+"</p>");
	$(htmlItem).append("<div>"+dtext(item['text'])+"</div>");
	return htmlItem;
}


function dtext(s) {
	return s;
}

function init_all() {
	$("#topbar .right").buttonset();
	$(".nobutton").button({
		disabled:true
	});
}

function init_projects() {
	init_all();
	$("#documentuploaddialog").dialog({
		hide:false,
		title:i18n.t("new_document.title"),
		buttons:[{
			text:i18n.t("new_document.submit"),
			click:function() {
				$("#documentuploaddialog form").submit();
			}
		}]
	});
	$("#documentuploaddialog").dialog("close");
	
	$(".splitleft").append("<img src=\"gfx/loading1.gif\" />");
	$(".splitleft #projectlistarea").hide();
	jQuery.getJSON("rpc/projectlist", function(data) {
		for (var i = 0; i < data.length; i++) {
			var tr = document.createElement("tr");
			var id=data[i]['databaseId'];
			$(tr).append("<td>"+data[i]['id']+"</td>");
			$(tr).append("<td>"+data[i]['name']+"</td>");
			$(tr).append("<td><button class=\"button_open project "+data[i]['id']+"\"></button></td>");
			$(tr).find("button.button_open").button({
				label: i18n.t("project_action_open"),
				icons:{primary:"ui-icon-folder-collapsed",secondary:null},
				text:showText
			}).click({'projectId':data[i]['id']}, function(event) {	
				show_documentlist(event.data['projectId']); 
			});
			$("#projectlistarea table tbody").append(tr);
			if (selected != -1) {
				if (selected == id)
					show_documentlist(id);
			}

		};
		$(".splitleft img").remove();
		$(".splitleft #projectlistarea").show();		
	});
	
	$( "button.button_edit_user" ).button({
		icons: { primary: "ui-icon-person", secondary:null },
		text:showText
	});
}

function show_annodoclist(id) {
	$("#annodoclistarea").remove();
	$("#topbar .left .adocname").remove();

	$("#content .splitright").append("<div id=\"annodoclistarea\"></div>");
	$("#annodoclistarea").hide();

	jQuery.getJSON("rpc/userdocumentlist?documentId="+id, function(data) {
		var header = false;
		var table = document.createElement("table");

		if ('documents' in data) {
			for (var i = 0; i < data['documents'].length; i++) {
				if (!header) {
					var trh = document.createElement("tr");
					$(trh).append("<th>"+i18n.t("annodoclistarea.id")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.username")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.mod_date")+"</th>");
					$(trh).append("<th>"+i18n.t("annodoclistarea.actions")+"</th>");
					$(table).append(trh);
					header = true;
				}
				var tr = document.createElement("tr");
				$(tr).append("<td>"+data['documents'][i]['id']+"</td>");
				$(tr).append("<td>"+data['documents'][i]['user']['name']+"</td>");
				$(tr).append("<td>"+data['documents'][i]['modificationDate']+"</td>");
			
				var actionCell = document.createElement("td");
				// if (al >= Perm["PADMINACCESS"]) 
					$(actionCell).append("<input class=\"button_diff\" id=\"diffselect-"+data['documents'][i]['id']+"\" type=\"checkbox\" name=\"diff\" value=\""+data['documents'][i]['id']+"\"/><label for=\"diffselect-"+data['documents'][i]['id']+"\"></label>");

				$(actionCell).buttonset();
				$(tr).append(actionCell);
				$(table).append(tr);
				
				// diff select button
				$(actionCell).find("input.button_diff").button({
					label:i18n.t("parallel.select"),
					icons:{primary:"ui-icon-transferthick-e-w",secondary:null},
					text:showText
				}); 
			}
			$("#annodoclistarea").append("<h2>"+i18n.t("annodoclistarea.title_for_X", {"document":data['src']['name']})+"</h2>");
			$("#annodoclistarea").append(table);
			$("#topbar .left").append("<span class=\"adocname\">&nbsp;&gt; "+i18n.t("annodoclistarea.breadcrumb_for_X", {"document":data['src']['name']})+"</span>");
			$("#annodoclistarea").append("<button id=\"button_open_diff\"></button>");
			
			$("button#button_open_diff").button({
				label:i18n.t("parallel.open_view"),
				icons:{primary:"ui-icon-zoomin",secondary:null},
				text:showText
			}).click(function() {
				if($("input.button_diff:checked").length == 2) {
					var doc = new Array();
					$("input.button_diff:checked").each(function(index, element) {
						doc[index] = $(element).val();
					});
	 				window.location.href="parallel.jsp?userDocumentId="+doc[0]+"&userDocumentId="+doc[1];
				}
			});
		} else {
			$("#annodoclistarea").append("<p>"+i18n.t("annodoclistarea.no-documents")+"</p>");
		}
		$("#annodoclistarea").show();

	});

}

function show_documentlist(id) {
	$("#content .splitright").empty();
	$("#content .splitright").append("<img src=\"gfx/loading1.gif\" />");
	$("#content .splitright").append("<div id=\"documentlistarea\"></div>");

	$("button.button_open.project").button({
		disabled:false,
		icons:{primary:"ui-icon-folder-collapsed",secondary:null}
	});
	$("button.button_open.project."+id).button({
		disabled:true,
		icons:{primary:"ui-icon-folder-open",secondary:null}
	});
	$("#topbar .left .adocname").remove();
	$("#topbar .left .pname").remove();
	$("#documentlistarea").hide();		

	
	jQuery.getJSON("rpc/documentlist?projectId="+id, function(data) {
		var header = false;
		var table = document.createElement("table");
		var al = data['accesslevel'];
		if ("documents" in data) {
		for (var i = 0; i < data['documents'].length; i++) {
			if (!header) {
				var trh = document.createElement("tr");
				$(trh).append("<th>"+i18n.t("document_id")+"</th>");
				$(trh).append("<th>"+i18n.t("document_name")+"</th>");
				//$(trh).append("<th>"+i18n.t("document_mod_date")+"</th>");
				$(trh).append("<th>"+i18n.t("document_actions")+"</th>");
				$(table).append(trh);
				header = true;
			}
			var tr = document.createElement("tr");
			$(tr).append("<td>"+data['documents'][i]['id']+"</td>");
			$(tr).append("<td>"+data['documents'][i]['name']+"</td>");
			//$(tr).append("<td>"+data['documents'][i]['modificationDate']+"</td>");
			
			var actionCell = document.createElement("td");
			if (al >= Perm["READACCESS"])
				$(actionCell).append("<button class=\"button_open\"></button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_rename\">rename</button>")
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_delete\">delete</button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_view_annodoc\">view annotation</button>");
			if (al >= Perm["PADMINACCESS"])
				$(actionCell).append("<button class=\"button_export\">export</button>");
			
			
			$(actionCell).find("button.button_open").button({
				label:i18n.t("document_action_open"),
				icons:{primary:"ui-icon-document",secondary:null},
				text:showText
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				window.location.href="main.jsp?documentId="+event.data.documentId;
			});
			$(actionCell).find("button.button_rename").button({
				label:i18n.t("document_action_rename"),
				icons:{primary:"ui-icon-pencil",secondary:null},
				text:showText
			}).click({
				'document':data['documents'][i]
			}, function(event) {
				var diagDiv = document.createElement("div");
				$(diagDiv).append(i18n.t("rename_dialog.desc")+"<input type=\"text\" value=\""+event.data.document['name']+"\" />");
				$("body").append(diagDiv);
				
				$(diagDiv).dialog({
					title:i18n.t("rename_dialog.title"),
					dialogClass: "no-close",
					buttons: [{ text: i18n.t("rename_dialog.ok"),
					            click: function() {
					            	// alert($(diagDiv).children("input").val());
									jQuery.getJSON("DocumentHandling?action=rename&name="+$(diagDiv).children("input").val()+"&documentId="+event.data.document['id'], function() {
										show_documentlist(id);
									});

					              $( this ).dialog( "close" );
					            }
					          },{
					        	  text:i18n.t("rename_dialog.cancel"),
					        	  click: function() {
					        		  $(this).dialog("destroy");
					        		  document.getElementsByTagName("BODY")[0].removeChild(diagDiv);
					        	  }
					          }],
					closeOnEscape: true,
					modal:true	
				}).show();
				
			});
			
			$(actionCell).find("button.button_delete").button({
				label:i18n.t("document_action_delete"),
				icons:{primary:"ui-icon-trash", secondary:null},
				text:showText
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				jQuery.getJSON("DocumentHandling?action=delete&documentId="+event.data.documentId, function() {
					show_documentlist(id);
				});
			});
			
			$(actionCell).find("button.button_view_annodoc").button({
				label:i18n.t("document_action_view_annodoc"),
				icons:{primary:"ui-icon-cart", secondary:null},
				text:showText
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				show_annodoclist(event.data.documentId);
			});
			
			
			
			$(actionCell).find("button.button_export").button({
				label:i18n.t("document_action_export_xmi"),
				icons:{primary:"ui-icon-arrowstop-1-s", secondary:null},
				text:showText
			}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				window.location.href="DocumentHandling?action=export&documentId="+event.data.documentId;
			});
			
			
			
			$(actionCell).buttonset();
			$(tr).append(actionCell);
			$(table).append(tr);
		}
		}
		$("#documentlistarea").append("<h2>"+i18n.t("documents_in_X", {"projectname":data['project']['name']})+"</h2>");
		$("#documentlistarea").append(table);
		$("#topbar .left").append("<span class=\"pname\">&nbsp;&gt; "+data['project']['name']+"</span>");
		
		
		$("#content .splitright img").remove();
		
		

		if (al >= Perm["PADMINACCESS"]) {
			$("#documentuploaddialog input[name='projectId']").attr("value", data['project']['id']);
			$("#documentlistarea").append("<button data-i18n=\"new_document.open_dialog\" id=\"new_document_open_dialog\"></button>");	
			$("button#new_document_open_dialog").button({
				label:i18n.t("new_document.open_dialog"),
				icons: { primary: "ui-icon-arrowthickstop-1-n", secondary: null },
				text:showText
			}).click(function() {
				$("#documentuploaddialog").dialog("open");
			});
		}
		$("#documentlistarea").show();	

	});
}

function select_document_for_diff(did) {
	if ($("#diffselect-"+did).val() == did) {
		if (documents_selected_for_diff.push(did) > max_documents_for_diff) {
			var d = documents_selected_for_diff.shift();
			$("#diffselect-"+d).removeAttr('checked');
			$("#diffselect-"+d).button( "refresh" );
		}
	} else {
		documents_selected_for_diff.splice(documents_selected_for_diff.indexOf(did),1);
		$("#diffselect-"+did).button( "refresh" );
	}
}

function init_trans(fnc) {
	i18n.init({ 
		resGetPath:'locales/__ns__-__lng__.json',
		lng: language }, function(t) {
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
			$("li > div", element).smartTruncation({
			    'truncateCenter'    : true
			 });
		});
	});
	
}

function init_main() {
		init_all();
		$("#split").hide();
		$( "button.button_edit_user" ).button({
			icons: { primary: "ui-icon-person", secondary:null },
			disabled: true,
			text:showText
		});
		
		$( "button.button_change_document" ).button({
			icons: { primary: "ui-icon-folder-collapsed", secondary:null },
			text:showText
			//label: i18n.t("open"),
		}).click(function() {
			window.location.href="projects.jsp?projectId="+projectId;
		});
		$( "button.button_save_document" ).button({
			icons: { primary: "ui-icon-disk", secondary:null },
			label: i18n.t("save"),
			text:showText
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
		
		jQuery.getJSON("DocumentContentHandling?documentId="+documentId, function(data) {
			
			$(".breadcrumb").append("<a href=\"projects.jsp?projectId="+data["document"]["project"]["databaseId"]+"\">"+data["document"]["project"]["name"]+"</a> &gt; "+data["document"]["name"])
			
			document.title = treeanno["name"]+" "+treeanno["version"]+": "+data["document"]["name"];
			
			var list = data["list"];
			
			switch(data['document']['project']['type']){
			case ProjectType["ARNDT"]:
				operations[49]['disabled'] = 1;
				break;
			}
			
			while (list.length > 0) {
				var item = list.shift();
				
				if ('parentId' in item) {
					var parentId = item['parentId'];
					var parentItem = $("li[data-treeanno-id='"+parentId+"']");
					if (parentItem.length == 0)
						list.push(item);
					else {
						if (parentItem.children("ul").length == 0)
							parentItem.append("<ul></ul>");
						$("li[data-treeanno-id='"+parentId+"'] > ul").append(get_html_item(item, 0));
					}
				} else {
					$('#outline').append(get_html_item(item, 0));
				}
			}
			$("#outline li > div").smartTruncation({
			    'truncateCenter'    : true
			  });
			$('#outline > li:first-child').addClass("selected");
			document.onkeydown = function(e) {
				key_down(e);
			};
			document.onkeyup = function(e) {
				key_up(e);
			};
			$("#outline li").click(function(e) {
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
			});
			$("#status .loading").hide();
			$("#outline").show();
			
			
	});
}

function search() {
	$("li.searchFound").removeClass("searchFound");
	var val = $("#form_search").val();
	$("li[title*=\""+val+"\"]").addClass("searchFound");
}

function save_document() {
	var sitems = new Array(); //items;
	
	$("#outline li").each(function(index, element) {
		var item = new Object();
		item['id'] = $(element).attr("data-treeanno-id");
		item['begin'] = $(element).attr("data-treeanno-begin");
		item['end'] = $(element).attr("data-treeanno-end");
		item['Mark1'] = $(element).hasClass("mark1");
		// alert(id);
		var parents = $(element).parentsUntil("#outline", "li");
		if (parents.length > 0) {
			var parent = parents.first();
			var parentId = parseInt(parent.attr("data-treeanno-id"));
			item["parentId"] = parentId;
		}
		item['category'] = $(element).children("p").text();
		sitems.push(item);
	});
	$.ajax({
		type: "POST",
		url: "DocumentContentHandling",
		// processData: false,
		data: JSON.stringify({
			document:documentId,
			items:sitems
		}),
		contentType:'application/json; charset=UTF-8',
		success: function() {
			$( "button.button_save_document" ).button( "option", "disabled", true );
			$( "button.button_save_document" ).button( "option", "icons", { primary: "ui-icon-check", secondary:null });
		}, 
		error: function(jqXHR, textStatus, errorThrown) {
			$("#error").append(textStatus);
			$("#error").dialog({
				  dialogClass: "alert",
				  modal:true,
				  title:"Error"
			});
			console.log(textStatus, errorThrown);			
		}
	});
}

function enableSaveButton() {
	if ($( "button.button_save_document" ).button("option", "disabled") == true) {
		$( "button.button_save_document" ).button("option", "disabled", false);
		$( "button.button_save_document" ).button( "option", "icons", { primary: "ui-icon-disk", secondary:null });
	}
}

function disableSaveButton() {
	if ($( "button.button_save_document" ).button("option", "disabled") == false) {
		$( "button.button_save_document" ).button("option", "disabled", true);
		$( "button.button_save_document" ).button( "option", "icons", { primary: "ui-icon-check", secondary:null });
	}
}

function key_up(e) {
	if (!enable_interaction) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which;
	switch(keyCode) {
	case kbkey.shift:
		shifted = false;
		break;
	}
}


function key_down(e) {
	if (!enable_interaction) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which;
	var allItems = $("#outline li");
	switch (keyCode) {
	case kbkey.shift:
		shifted = true;
		break;
	case kbkey.down:
		// get index of last selected item
		var index = $(".selected").last().index("#outline li");
		
		// if shift is not pressed, we remove the selection from everything that is selected
		if (!shifted && index < $("#outline li").length-1)
			$(".selected").toggleClass("selected");
		
		// if nothing is selected
		if (index == -1) {
			// we select the first thing on the page
			$($(allItems).get(0)).toggleClass("selected");		
		// if there is something after the last selected item, we select that
		} else if (index < $(allItems).length-1) {
			// if shift is not pressed, we select the next item of all items
			if (!shifted)
				$($(allItems).get(index+1)).toggleClass("selected");
			// if it is pressed, we select the next sibling
			if (shifted)
				$(".selected").last().next().toggleClass("selected");
		}
		// if the last selected thing is not in viewport, we scroll
		if (!isElementInViewport($(".selected").last()))
			$(window).scrollTop($(".selected").last().offset().top - 200);
		break;
	case kbkey.up:
		// get index of first selected item
		var index = $(".selected").first().index("#outline li");
		// if shift is not pressed, remove the selection
		if (!shifted && index > 0)
			$(".selected").toggleClass("selected");
		
		// if select the new item
		if (index > 0) {
			if (!shifted)
				$($(allItems).get(index-1)).toggleClass("selected");
			if (shifted)
				$(".selected").first().prev().toggleClass("selected");
		}
		// if not in viewport, scroll
		if (!isElementInViewport($(".selected").first()))
			$(window).scrollTop($(".selected").first().offset().top - 200);
		break;
	case kbkey.enter:
		$(this).prev().attr('checked', !$(this).prev().attr('checked'));
		break;
	default:
		kc = keyCode;
		if (shifted)
			kc = keyCode + 1000;
		if (kc in operations && !operations[kc]['disabled'])
			operations[kc].fun();
	}
	
}

function isElementInViewport (el) {

    //special bonus for those using jQuery
    if (typeof jQuery === "function" && el instanceof jQuery) {
        el = el[0];
    }

    var rect = el.getBoundingClientRect();

    return (
        rect.top > 0 &&
        rect.left > 0 &&
        rect.bottom < (window.innerHeight || document.documentElement.clientHeight) && /*or $(window).height() */
        rect.right < (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */
    );
}

function delete_category() {
	$(".selected > p.annocat").remove();
	$(".selected").removeAttr("data-treeanno-categories");
}
function add_category() {
	enable_interaction = false;
	$(".selected > p.annocat").remove();
	var val = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):$(".selected").attr("title"));
	$(".selected").prepend("<input type=\"text\" size=\"100\" id=\"cat_input\" value=\""+val+"\"/>");
	$("#cat_input").keyup(function(event){
	    if(event.keyCode == 13){
	       enter_category();
	    }
	    if(event.keyCode == 27){
	    	event.preventDefault();
	    	cancel_category();
	    }
	});
	document.getElementById("cat_input").focus();
}


function cancel_category() {
	$("#cat_input").remove();
	enable_interaction = true;

}
function enter_category() {
	var value = $("#cat_input").val();
	$("#cat_input").remove();
	$(".selected").prepend("<p class=\"annocat\">"+value+"</p>");
	// var oa = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):"");
	$(".selected").attr("data-treeanno-categories", value);
	enableSaveButton();
	enable_interaction = true;
}

function get_item(id) {
	var obj = new Object();
	obj['id'] = id;
	var liElement= $("li[data-treeanno-id=\""+id+"\"]");
	obj['begin'] = $(liElement).attr("data-treeanno-begin");
	obj['end'] = $(liElement).attr("data-treeanno-end");
	obj['text'] = $(liElement).attr("title");
	return obj;
}

function mergeselected() {
	var item1 = get_item($(".selected").first().attr("data-treeanno-id"));
	var item0 = get_item($(".selected").last().attr("data-treeanno-id"));
	merge(item1, item0);

}

function merge(item1, item0) {
	var correctOrder = (item1['begin'] > item0['begin']);
	
	var nitem = new Object();
	var distance = (correctOrder?item1['begin']-item0['end']:item0['begin']-item1['end']);
	var str = (includeSeparationWhenMerging?new Array(distance+1).join(" "):"");

	nitem['text'] = (correctOrder?item0['text']+str+item1['text']:item1['text']+str+item0['text']);
	nitem['begin'] = (correctOrder?item0['begin']:item1['begin']);
	nitem['end'] = (correctOrder?item1['end']:item0['end']);
	nitem['id'] = idCounter++;
	//items[item1['id']] = undefined;
	//items[item0['id']] = undefined;
	
	var sublist0 = $("#outline li[data-treeanno-id='"+item0['id']+"'] > ul").detach();
	$("#outline li[data-treeanno-id='"+item0['id']+"']").remove();
	//items[++idCounter] = nitem;
	
	var nitem = get_html_item(nitem, idCounter);
	$(".selected").after(nitem);
	$(nitem).children("div").smartTruncation({
	    'truncateCenter'    : true
	  });
	var newsel = $(".selected").next();
	var sublist1 = $(".selected > ul").detach();
	$(".selected").remove();
	$(newsel).addClass("selected");
	$(".selected").append(sublist0);
	$(".selected").append(sublist1);
}



function splitdialog() {
	enable_interaction = false;
	var item = get_item($(".selected").first().attr("data-treeanno-id"));
	$("#form_splittext").val(item['text'].trim());
	$("#split").dialog({
		title: i18n.t("Split Segment"),
		modal: true,
		minWidth: 400,
		close: splitdialog_cleanup,
		buttons: 
		[
		 	{
		 		text: i18n.t("ok"),
		 		click: splitdialog_enter,
		 		tabindex:1
		    },
		    {
		    	text: i18n.t("cancel"),
		    	click: splitdialog_cleanup,
		    	tabindex:2
		    }
		]
	});
}

function splitdialog_cleanup() {
	$("#form_splittext").val("");
	enable_interaction = true;
	$("#split").dialog( "destroy" );
	
}

function splitdialog_enter() {
	var itemid = $(".selected").attr("data-treeanno-id");
	var item = get_item(itemid);
	var text = $("#form_splittext").val();
	var lines = text.split("\n\n");
	if (lines.length == 2) {
		
		var litems = new Array();
		litems[0] = new Object();
		litems[0]['begin'] = item['begin'];
		litems[0]['text'] = lines[0];
		litems[0]['end'] = parseInt(item['begin'])+parseInt(lines[0].length);
		litems[0]['id'] = ++idCounter;
		litems[1] = new Object();
		litems[1]['end'] = item['end'];
		litems[1]['text'] = lines[1];
		litems[1]['begin'] = litems[0]['end'];
		litems[1]['id'] = ++idCounter;
		// items[itemid] = undefined;
		
		var sublist = $(".selected > ul").detach();
		// items[litems[1]['id']] = litems[1];
		
		var nitem1 = get_html_item(litems[1], idCounter);
		$(nitem1).children("div").smartTruncation({
		    'truncateCenter'    : true
		  });
		$(".selected").after(nitem1)
		$(".selected").next().append(sublist);
		// items[litems[0]['id']] = litems[0];
		
		var nitem0 = get_html_item(litems[0], idCounter);
		$(nitem0).children("div").smartTruncation({
		    'truncateCenter'    : true
		  });
		$(".selected").after(nitem0);
		var nsel = $(".selected").next();
		$(".selected").remove();
		$(nsel).addClass("selected");
		enableSaveButton();
	}
	cleanup_list();
	splitdialog_cleanup();
}

function outdentElement(element) {
	var id = $(element).attr("data-treeanno-id");
	
	// if it's not the very first item
	if (!$(element).parent("ul#outline").length) {
		var newParent = $(element).parentsUntil("li").parent();
		var parentId = parseInt($(newParent).attr("data-treeanno-id"));
		var siblings = $(element).nextAll("li").detach();
		if ($(element).children("ul").length == 0)
			$(element).append("<ul></ul>");
		$(element).children("ul").append(siblings);
		var s = $(element).detach();
		$(newParent).after(s);
	}
}

function outdent() {
	$(".selected").each(function(index, element) {
		outdentElement(element);
	});
	cleanup_list();
	enableSaveButton();
}

function force_indent() {
	$(".selected").each(function(index, element) {
			var vitem = new Object();
			vitem["begin"] = $(element).attr("data-treeanno-begin");
			vitem["end"] = vitem["begin"];
			vitem["id"] = ++idCounter;
			vitem["text"] = "";
			
			var htmlItem = get_html_item(vitem, 0);
			$(element).before(htmlItem);
			
			var prev = $(element).prev("li");
			if ($(prev).children("ul").length == 0)
				prev.append("<ul></ul>");
			var s = $(element).detach();
			$(prev).children("ul").append(s);
		cleanup_list();		
	});
	enableSaveButton();
}

function delete_virtual_node() {
	$(".selected").each(function(index, element) {
		
		// check if it's really a virtual node
		if ($(element).attr("data-treeanno-begin") == $(element).attr("data-treeanno-end")) {
			console.log("TreeAnno: Found a virtual node to delete")
			
			$(element).children("ul").children("li").each(function(i2, e2) {
				console.log("TreeAnno: Outdenting children of virtual node");
				outdentElement(e2);
			});
			
			$(element).prev().addClass("selected");
			$(element).remove();
		}
	});
}

function indentElement(element) {
	if ($(element).prev("li").length > 0) {
		var prev = $(element).prev("li");
		if ($(prev).children("ul").length == 0)
			prev.append("<ul></ul>");
		var s = $(element).detach();
		$(prev).children("ul").append(s);
	}
}

function indent() {
	$(".selected").each(function(index, element) {
		indentElement(element);
		cleanup_list();		
	});
	enableSaveButton();
}


function cleanup_list() {
	$("#outline ul:not(:has(*))").remove();
}
