var maxStringLength = 160;


var enable_interaction = true;

var shifted = false;

var items = new Array();

var idCounter = 0;

var includeSeparationWhenMerging = true;

function get_html_item(item, i) {
	var htmlItem = document.createElement("li");
	$(htmlItem).attr("title", item['text']);
	$(htmlItem).addClass("tl0");
	$(htmlItem).attr("data-treeanno-id", item['id']);
	idCounter = Math.max(idCounter, item['id']);
	//$(htmlItem).attr("data-treeanno-uid", item['id']);
	if ('category' in item)
		$(htmlItem).append("<p class=\"annocat\">"+item['category']+"</p>");
	$(htmlItem).append("<div>"+dtext(item['text'])+"</div>");
	return htmlItem;
}


function dtext(s) {
	if (s.length > maxStringLength)
		s = s.substring(0,maxStringLength-3)+"...";
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
	jQuery.getJSON("projectlist.jsp", function(data) {
		for (var i = 0; i < data.length; i++) {
			var tr = document.createElement("tr");
			var id=data[i]['id'];
			$(tr).append("<td>"+data[i]['id']+"</td>");
			$(tr).append("<td>"+data[i]['name']+"</td>");
			$(tr).append("<td><button class=\"button_open\">open</button></td>");
			$(tr).find("button.button_open").button({
				label: i18n.t("open")
			}).click({'projectId':data[i]['id']}, function(event) {	
				show_documentlist(event.data['projectId']); 
			});
			$("#projectlistarea table tbody").append(tr);
			if (typeof(selected)!=undefined) {
				if (selected == id)
					show_documentlist(id);
			}

		};
		
	});
	
	$( "button.button_edit_user" ).button({
		icons: { primary: "ui-icon-person", secondary:null }
	});
}

function show_documentlist(id) {
	$("#documentlistarea").empty();
	$("#topbar .left .pname").remove();

	jQuery.getJSON("documentlist.jsp?projectId="+id, function(data) {
		var header = false;
		var table = document.createElement("table");
		for (var i = 0; i < data['documents'].length; i++) {
			if (!header) {
				var trh = document.createElement("tr");
				$(trh).append("<th>"+i18n.t("document_id")+"</th>");
				$(trh).append("<th>"+i18n.t("document_name")+"</th>");
				$(trh).append("<th>"+i18n.t("document_mod_date")+"</th>");
				$(table).append(trh);
				header = true;
			}
			var tr = document.createElement("tr");
			$(tr).append("<td>"+data['documents'][i]['id']+"</td>");
			$(tr).append("<td>"+data['documents'][i]['name']+"</td>");
			$(tr).append("<td>"+data['documents'][i]['modificationDate']+"</td>");
			$(tr).append("<td><button class=\"button_open\"></button></td>");
			$(tr).append("<td><button class=\"button_clone\">clone</button></td>");
			$(tr).append("<td><button class=\"button_delete\">delete</button></td>");
			$(tr).find("button.button_open").button({label:i18n.t("open")}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				window.location.href="main.jsp?documentId="+event.data.documentId;
			});
			$(tr).find("button.button_clone").button({label:i18n.t("clone")}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				jQuery.getJSON("DocumentHandling?action=clone&documentId="+event.data.documentId, function() {
					show_documentlist(id);
				});
			});
			$(tr).find("button.button_delete").button({label:i18n.t("delete")}).click({
				'documentId':data['documents'][i]['id']
			}, function(event) {
				jQuery.getJSON("DocumentHandling?action=delete&documentId="+event.data.documentId, function() {
					show_documentlist(id);
				});
			});
			$(table).append(tr);
		}
		$("#documentlistarea").append("<h2>"+i18n.t("documents_in_X", {"projectname":data['project']['name']})+"</h2>");
		$("#documentlistarea").append(table);
		$("#topbar .left").append("<span class=\"pname\">&nbsp;&gt; "+data['project']['name']+"</span>");
	});
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

function init_main() {
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
		
		jQuery.getJSON("ControllerServlet?documentId="+documentId, function(data) {
			
			// idCounter = data["list"].length;
			for (var i = 0; i < data["list"].length; i++) {
				
				var item = data["list"][i];
				items[item["id"]] = item;
				
				var t = item["text"];
				
				if (t.length > maxStringLength)
					t = t.substring(0,maxStringLength-3) + "...";
				if ('parentId' in item) {
					parentId = item['parentId'];
					parentItem = $("li[data-treeanno-id='"+parentId+"']");
					if (parentItem.children("ul").length == 0)
						parentItem.append("<ul></ul>");
					$("li[data-treeanno-id='"+parentId+"'] > ul").append(get_html_item(item, i));
				} else {
					$('#outline').append(get_html_item(item, i));
				}
			}
			$('#outline > li:first-child').addClass("selected");
			
			document.onkeydown = function(e) {
				key_down(e);
			};
			document.onkeyup = function(e) {
				key_up(e);
			}
		
	});
}

function search() {
	$("li.searchFound").removeClass("searchFound");
	var val = $("#form_search").val();
	$("li[title*=\""+val+"\"]").addClass("searchFound");
}

function save_document() {
	sitems = items;
	
	$("#outline li").each(function(index, element) {
		var id = $(element).attr("data-treeanno-id");
		// alert(id);
		parents = $(element).parentsUntil("#outline", "li");
		if (parents.length > 0) {
			parent = parents.first();
			parentId = parseInt(parent.attr("data-treeanno-id"));
			sitems[id]["parentId"] = parentId;
		}
		sitems[id]['category'] = $(element).children("p").text();
	});
	$.ajax({
		type: "POST",
		url: "ControllerServlet",
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
	var keyCode = e.keyCode || e.which,
		kbkey = { shift:16 };
	switch(keyCode) {
	case kbkey.shift:
		shifted = false;
		break;
	}
}


function key_down(e) {
	if (!enable_interaction) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which,
    	kbkey = { up: 38, down: 40, right: 39, left: 37, 
			enter: 13, s: 83, m:77, c:67, d:68, shift: 16 };
	var allItems = $("#outline li");
	switch (keyCode) {
	case kbkey.shift:
		shifted = true;
		break;
	case kbkey.d:
		delete_category();
		break;
	case kbkey.c:
		add_category();
		break;
	case kbkey.m:
		mergedialog();
		break;
	case kbkey.s:
		splitdialog();
		break;
	case kbkey.down:
		var index = $(".selected").last().index("#outline li");
		if (!shifted)
			$(".selected").toggleClass("selected");
		// alert(index);
		if (index == -1) {
			$($(allItems).get(0)).toggleClass("selected");					
		} else if (index < $(allItems).length-1) {
			$($(allItems).get(index+1)).toggleClass("selected");
		}
		if (!isElementInViewport($(".selected").last()))
			$(window).scrollTop($(".selected").last().offset().top - 200);
		break;
	case kbkey.up:
		var index = $(".selected").first().index("#outline li");
		if (!shifted)
			$(".selected").toggleClass("selected");
		if (index > 0) {
			$($(allItems).get(index-1)).toggleClass("selected");
		}
		if (!isElementInViewport($(".selected").first()))
			$(window).scrollTop($(".selected").first().offset().top - 200);
		break;
	case kbkey.right:
		indent();
		break;
	case kbkey.left:
		outdent();
		break;
	case kbkey.enter:
		$(this).prev().attr('checked', !$(this).prev().attr('checked'));
		break;
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
	var val = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):"");
	$(".selected").prepend("<input type=\"text\" id=\"cat_input\" value=\""+val+"\"/>");
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
	value = $("#cat_input").val();
	$("#cat_input").remove();
	$(".selected").prepend("<p class=\"annocat\">"+value+"</p>");
	// var oa = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):"");
	$(".selected").attr("data-treeanno-categories", value);
	enableSaveButton();
	enable_interaction = true;
}

function mergedialog() {
	enable_interaction = false;
	var item = items[$(".selected").data("treeanno-id")];
	
	var prevCand = $(".selected").prev("li").attr("data-treeanno-id");
	var nextCand = $(".selected").next("li").attr("data-treeanno-id");
	$("#merge p").append("Merge <span class=\"tt\">&quot;"+dtext(item['text'])+"&quot;</span> with ...");
	if (typeof prevCand !== 'undefined') 
		$("#form_mergecandidates").append("<option value=\""+prevCand+"\">"+dtext(items[prevCand]['text'])+"</option>");
	if (typeof nextCand !== 'undefined' && $(".selected ul").length == 0) 
		$("#form_mergecandidates").append("<option value=\""+nextCand+"\">"+dtext(items[nextCand]['text'])+"</option>");
	$('#form_mergecandidates').on('keydown', function(e) {
	    if (e.which == 13) {
	        e.preventDefault();
	        mergedialog_enter();
	    }
	});
	$("#merge").dialog({
		title: i18n("Merge Segments"),
		modal:true,
		minWidth:400,
		close: mergedialog_cleanup,
		buttons: [{
		        	  text: i18n("ok"),
		        	  click: mergedialog_enter
		        }, {
		        	text: i18n("cancel"),
		        	click: mergedialog_cleanup
		        }
		]
	});
	document.getElementById("form_mergecandidates").focus();
}


function mergedialog_enter() {
	nid = $("#form_mergecandidates").val();
	item1 = items[nid];
	item0 = items[$(".selected").attr("data-treeanno-id")];
	
	correctOrder = (item1['begin'] > item0['begin']);
	
	nitem = new Object();
	var distance = (correctOrder?item1['begin']-item0['end']:item0['begin']-item1['end']);
	var str = (includeSeparationWhenMerging?new Array(distance+1).join(" "):"");

	nitem['text'] = (correctOrder?item0['text']+str+item1['text']:item1['text']+str+item0['text']);
	nitem['begin'] = (correctOrder?item0['begin']:item1['begin']);
	nitem['end'] = (correctOrder?item1['end']:item0['end']);
	nitem['id'] = idCounter++;
	items[nid] = undefined;
	items[$(".selected").attr("data-treeanno-id")] = undefined;
	
	sublist0 = $("#outline li[data-treeanno-id='"+nid+"'] > ul").detach();
	$("#outline li[data-treeanno-id='"+nid+"']").remove();
	items[++idCounter] = nitem;
	
	$(".selected").after(get_html_item(nitem, idCounter));
	newsel = $(".selected").next();
	sublist1 = $(".selected > ul").detach();
	$(".selected").remove();
	$(newsel).addClass("selected");
	$(".selected").append(sublist0);
	$(".selected").append(sublist1);
	enableSaveButton();
	cleanup_list();
	mergedialog_cleanup();
}
function mergedialog_cleanup() {
	enable_interaction = true;
	$("#merge p").empty();
	$("#form_mergecandidates").unbind('keydown');
	$("#form_mergecandidates").empty();
	$("#merge").dialog( "destroy" );
	
}

function splitdialog() {
	enable_interaction = false;
	var item = items[$(".selected").attr("data-treeanno-id")];
	$("#form_splittext").val(item['text'].trim());
	$("#split").dialog({
		title: i18n("Split Segment"),
		modal: true,
		minWidth: 400,
		close: splitdialog_cleanup,
		buttons: 
		[
		 	{
		 		text: i18n("ok"),
		 		
		 		click: splitdialog_enter
		    },
		    {
		    	text: i18n("cancel"),
		    	click: splitdialog_cleanup
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
	itemid = $(".selected").data("treeanno-id");
	item = items[itemid];
	var text = $("#form_splittext").val();
	var lines = text.split("\n\n");
	if (lines.length == 2) {
		
		litems = new Array();
		litems[0] = new Object();
		litems[0]['begin'] = item['begin'];
		litems[0]['text'] = lines[0];
		litems[0]['end'] = item['begin']+lines[0].length;
		litems[0]['id'] = ++idCounter;
		litems[1] = new Object();
		litems[1]['end'] = item['end'];
		litems[1]['text'] = lines[1];
		litems[1]['begin'] = litems[0]['end'];
		litems[1]['id'] = ++idCounter;
		items[itemid] = undefined;
		
		sublist = $(".selected > ul").detach();
		items[litems[1]['id']] = litems[1];
		
		$(".selected").after(get_html_item(litems[1], idCounter))
		$(".selected").next().append(sublist);
		items[litems[0]['id']] = litems[0];
		$(".selected").after(get_html_item(litems[0], idCounter));
		nsel = $(".selected").next();
		$(".selected").remove();
		$(nsel).addClass("selected");
	}
	enableSaveButton();
	cleanup_list();
	splitdialog_cleanup();
}


function outdent() {
	$(".selected").each(function(index, element) {
		var id = $(element).attr("data-treeanno-id");
		
		// if it's not the very first item
		if (!$(element).parent("ul#outline").length) {
			var newParent = $(element).parentsUntil("li").parent();
			var parentId = parseInt($(newParent).attr("data-treeanno-id"));
			siblings = $(element).nextAll("li").detach();
			if ($(element).children("ul").length == 0)
				$(element).append("<ul></ul>");
			$(element).children("ul").append(siblings);
			s = $(element).detach();
			$(newParent).after(s);
			delete items[id]['parentId'];
		}
	});
	cleanup_list();
	enableSaveButton();
}

function indent() {
	$(".selected").each(function(index, element) {
		if ($(element).prev("li").length > 0) {
			prev = $(element).prev("li");
			if ($(prev).children("ul").length == 0)
				prev.append("<ul></ul>");
			s = $(element).detach();
			$(prev).children("ul").append(s);
		}
		cleanup_list();		
	});
	enableSaveButton();
}


function cleanup_list() {
	$("#outline ul:not(:has(*))").remove();
}