var interaction_mode = 0;
var shifted = false;

var idCounter = 0;

var kbkey = { up: 38, down: 40, right: 39, left: 37, 
		enter: 13, s: 83, m:77, c:67, d:68, shift: 16, one: 49 };
var keyString = {
		37:'&larr;',
		38:'&uarr;',
		39:'&rarr;',
		40:'&darr;',
		49:'1',
		67:'c',
		68:'d',
		77:'m',
		83:'s',
		1038:'&#8679;&uarr;',
		1039:'&#8679;&rarr;',
		1040:'&#8679;&darr;',
		1068:'&#8679;d',
		1083:'&#8679;s'
}
var operations = {
		37:[{
			// left
			'id':'outdent',
			fun:outdent,
			'desc':'action_outdent',
			history:true
		},{
			id:'move-splitpoint-left',
			fun:split_move_left,
			history:false
		}],
		38:[{
			// up
			id:'up',
			fun:move_selection_up,
			desc:'action_up',
			history:false,
			pre:{
				fun:function() {
					return !$(".selected").first().is($("#outline li").first());
				},
				fail: {
					type: "information",
					text: "action.up.prefail"
				}
			}
		}],
		39:[{
			// right
			'id':'indent',
			fun:indent,
			'desc':'action_indent',
			history:true
		},{
			id:'move-splitpoint-right',
			fun:split_move_right,
			history:false
		}],
		40:[{
			// down
			id:'down',
			fun:move_selection_down,
			desc:'action_down',
			history:false,
			pre: {
				fun: function() {
					return !$(".selected").last().is($("#outline li").last());
				},
				fail: {
					type: "information",
					text: "action.down.prefail"
				}
			}
		}],
		49:[{
			// one
			'id':'mark1',
			fun:function() {
				$(".selected").toggleClass("mark1");
				enableSaveButton();
			},
			desc:'action_mark1',
			history:true
		}],
		67:[{
			// c
			id:'categorize',
			fun:add_category,
			desc:'action_assign_category',
			history:true
		}],
		68:[{
			// d
			'id':'delete_category',
			fun:delete_category,
			desc:'action_delete_category',
			history:true
		}],
		77:[{
			// m
			'id':'merge',
			pre:{
				fun:function() {
					return $(".selected").length == 2;
				},
				fail:{
					text:"action.merge.prefail",
					type:"information"
				}
			},
			fun: mergeselected,
			desc:'action_merge',
			history:true
		}],
		83:[{
			// s
			'id':'split',
			fun:splitdialog,
			pre:{
				fun:function() {
					return $(".selected").length == 1;
				},
				fail:{
					text:"action.split.prefail",
					type:"information"
				}
			},
			'desc':'action_split',
			history:true
		}],
		1038:[{
			// shift + up
			id:'up',
			fun:move_selection_up,
			desc:'action.up.desc',
			history:false,
			pre: {
				fun:function() { return shifted; }
			}
		}],
		1039:[{
			// shift + right
			'id':'force_indent',
			fun:force_indent,
			desc:'action.force_indent',
			history:true
		}],
		1040:[{
			// shift + down
			id:'down',
			fun:extend_selection_down,
			desc:'action.down.desc',
			history:false,
			pre: {
				fun:function() { return shifted; }
			}
		}],
		1068:[{
			// shift + d
			'id':'delete_virtual_node',
			fun:delete_virtual_node,
			desc:'action.delete_vnode',
			history:true
		}],
		1083:[{
			// shift + s
			d:'save_document',
			fun:save_document,
			desc:'action.save_document',
			history:true
		}]
		
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

function init_operations(projectType) {
	switch(projectType){
	case ProjectType["ARNDT"]:
		operations[49][0]['disabled'] = 1;
		operations[67][0]['fun'] = function() {
			force_indent();
			move_selection_up();
			add_category();
		}
		operations[67][0]['desc'] = 'action.assign_category_t2';
		operations[68][0]['desc'] = "action.delete_category_t2";
		operations[1039][0]['desc'] = 'action.force_indent_t2';
		break;
	}
	operations[38][0]['pre']['fail']['text'] = i18n.t(operations[38][0]['pre']['fail']['text']);
	operations[40][0]['pre']['fail']['text'] = i18n.t(operations[40][0]['pre']['fail']['text']);
	operations[83][0]['pre']['fail']['text'] = i18n.t(operations[83][0]['pre']['fail']['text']);
	operations[77][0]['pre']['fail']['text'] = i18n.t(operations[77][0]['pre']['fail']['text']);

}

function init_help() {
	var helpElement = document.createElement("div");
	$(helpElement).attr("id", "help");
	$(helpElement).append("<div class=\"trans\">"+i18n.t('help_title')+"</div>");
	var helpTable = document.createElement("table");
	for (key in operations) {
		if (!operations[key]['disabled']) {
			$(helpTable).append("<tr><td><span class=\"command\">"+keyString[key]+"</span></td><td class=\"trans\">"+i18n.t(operations[key][0]['desc'])+"</td></tr>");
		}
	}
	$(helpElement).append(helpTable);
	
	$("body").append(helpElement);
	
	$(helpElement).draggable();
	$(helpElement).i18n();
	$("#topbar .right").prepend("<input type=\"checkbox\" id=\"show_helper\" /><label for=\"show_helper\"></label>");
	$("#show_helper").button({
		icons: { primary: "ui-icon-help", secondary:null },
		label: i18n.t("show_helper"),
		text:showText
	}).click(function() {
		$(helpElement).toggle();
	});
	$(helpElement).hide();
	$("#topbar .right").buttonset();
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
		$("#form_search").focus(function() {interaction_mode = -1});
		$("#form_search").blur(function() {interaction_mode = 0});
		
		$("#show_history").button({
			icons:{primary:null,secondary:null},
			label:i18n.t("show_history")
		}).click(function() {
			$("#rsidebar").toggle();
		});
		
		disableSaveButton();
		document.onkeydown = function(e) {
			key_down(e);
		};
		document.onkeyup = function(e) {
			key_up(e);
		};
		console.log("Querying for document content");
		jQuery.getJSON("DocumentContentHandling?documentId="+documentId, function(data) {
			console.log("Received document content");
			$(".breadcrumb").append("<a href=\"projects.jsp?projectId="+data["document"]["project"]["id"]+"\">"+data["document"]["project"]["name"]+"</a> &gt; "+data["document"]["name"])
			
			document.title = treeanno["name"]+" "+treeanno["version"]+": "+data["document"]["name"];
			
			var list = data["list"];
			
			init_operations(data['document']['project']['type']);
			
			while (list.length > 0) {
				var item = list.shift();
				if ('parentId' in item) {
					var parentId = item['parentId'];
					var parentItem = $("li[data-treeanno-id='"+parentId+"']");
					if (parentItem.length == 0) {
						// if items are ordered on the server, we don't need to push
						console.warn("Postponing item "+item['id']);
						list.push(item);
					} else {
						if (parentItem.children("ul").length == 0)
							parentItem.append("<ul></ul>");
						$("li[data-treeanno-id='"+parentId+"'] > ul").append(get_html_item(item, 0));
					}
				} else {
					$('#outline').append(get_html_item(item, 0));
				}
			}

			$('#outline > li:first-child').addClass("selected");
			
			$("#outline li > div").click(function(e) {
				var liElement = $(this).parent();
				if (shifted) {
					// if they have the same parent
					if ($(liElement).parent().get(0) == $(".selected").last().parent().get(0)) {
						$(".selected").last().nextUntil(liElement).addClass("selected");
						$(liElement).addClass("selected");
					}
				} else {
					$("#outline li").removeClass("selected");
					$(liElement).addClass("selected");
				}
			});
			init_help();
			$("#status .loading").hide();
			$("#outline").show();
		}).error(function(xhr) {
			noty({
				text:xhr,
				timeout:false,
				type:'error',
				modal:true
			});
			console.error(xhr);
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
			noty({
				text:textStatus+": "+errorThrown,
				timeout:false,
				type:'error',
				modal:true
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
	if (interaction_mode < 0) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which;
	switch(keyCode) {
	case kbkey.shift:
		shifted = false;
		break;
	}
}

function extend_selection_down() {
	var allItems = $("#outline li");

	// get index of last selected item
	var index = $(".selected").last().index("#outline li");
		
	// if nothing is selected
	if (index == -1) 
		// we select the first thing on the page
		$($(allItems).get(0)).toggleClass("selected");
	else 
		// if there is something after the last selected item, we
		// add that to the selection
		$(".selected").last().next().toggleClass("selected");
	
	// if the last selected thing is not in viewport, we scroll
	if (!isElementInViewport($(".selected").last()))
		$(window).scrollTop($(".selected").last().offset().top - 200);
}

function move_selection_down() {
	var allItems = $("#outline li");

	// get index of last selected item
	var index = $(".selected").last().index("#outline li");
	
	// we remove the selection from everything that is selected
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
	}
	// if the last selected thing is not in viewport, we scroll
	if (!isElementInViewport($(".selected").last()))
		$(window).scrollTop($(".selected").last().offset().top - 200);
}

function move_selection_up() {
	var allItems = $("#outline li");
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
}

function key_down(e) {
	if (interaction_mode < 0) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which;
	var allItems = $("#outline li");
	switch (keyCode) {
	case kbkey.shift:
		shifted = true;
		break;
	case kbkey.enter:
		$(this).prev().attr('checked', !$(this).prev().attr('checked'));
		break;
	default:
		kc = keyCode;
		if (shifted)
			kc = keyCode + 1000;
		if (kc in operations && !operations[kc][interaction_mode]['disabled']) {
			if (check_precondition(kc)) {
				if (operations[kc][interaction_mode]['history'])
					add_operation(kc, $(".selected"));
				operations[kc][interaction_mode].fun();
			} else {
				if ('fail' in operations[kc][interaction_mode]['pre'])
					noty(operations[kc][interaction_mode].pre.fail);
			}
		}
	}
	
}

function check_precondition(kc) {
	if ('pre' in operations[kc][interaction_mode])
		return operations[kc][interaction_mode]['pre'].fun();
	return true;
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
	interaction_mode = -1;
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
	interaction_mode = 0;
}
function enter_category() {
	var value = $("#cat_input").val();
	$("#cat_input").remove();
	$(".selected").prepend("<p class=\"annocat\">"+value+"</p>");
	// var oa = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):"");
	$(".selected").attr("data-treeanno-categories", value);
	enableSaveButton();
	interaction_mode = 0;

}

function get_item(id) {
	var obj = new Object();
	obj['id'] = id;
	var liElement= $("li[data-treeanno-id=\""+id+"\"]");
	obj['begin'] = parseInt($(liElement).attr("data-treeanno-begin"));
	obj['end'] = parseInt($(liElement).attr("data-treeanno-end"));
	obj['text'] = $(liElement).attr("title");
	return obj;
}

function mergeselected() {
	var item1 = get_item($(".selected").first().attr("data-treeanno-id"));
	var item0 = get_item($(".selected").last().attr("data-treeanno-id"));
	// add_operation(77, [$(".selected").last(), $(".selected").first()]);
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
	nitem['id'] = ++idCounter;
	//items[item1['id']] = undefined;
	//items[item0['id']] = undefined;
	
	var sublist0 = $("#outline li[data-treeanno-id='"+item0['id']+"'] > ul").detach();
	$("#outline li[data-treeanno-id='"+item0['id']+"']").remove();
	//items[++idCounter] = nitem;
	
	var nitem = get_html_item(nitem, idCounter);
	$(".selected").after(nitem);
	
	var newsel = $(".selected").next();
	var sublist1 = $(".selected > ul").detach();
	$(".selected").remove();
	$(newsel).addClass("selected");
	$(".selected").append(sublist0);
	$(".selected").append(sublist1);
	
	enableSaveButton();

}



function splitdialog() {
	interaction_mode = 1;
	var item = get_item($(".selected").first().attr("data-treeanno-id"));
	$("#form_splittext").append("¶"+item['text']);
	
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

function split_move_right() {
	var text = $("#form_splittext").text();
	var p = text.indexOf("¶");
	$("#form_splittext").text(
			text.substring(0,p)+
			text.charAt(p+1)+
			"¶"+
			text.substring(p+2, text.length));
}

function split_move_left() {
	var text = $("#form_splittext").text();
	var p = text.indexOf("¶");
	$("#form_splittext").text(
			text.substring(0,p-1)+
			"¶"+
			text.charAt(p-1)+
			text.substring(p+1, text.length));
}

function splitdialog_cleanup() {
	interaction_mode = 0;

	$("#split").dialog( "destroy" );
	$("#form_splittext").empty();
	add_operation(83, $(".selected"),[null]);

}

function splitdialog_enter() {
	var itemid = $(".selected").attr("data-treeanno-id");
	var item = get_item(itemid);
	var text = $("#form_splittext").text();
	var lines = text.split("¶");
	if (lines.length == 2) {
		add_operation(83, $(".selected"), {pos:lines[0].length});

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
		$(".selected").after(nitem1)
		$(".selected").next().append(sublist);
		// items[litems[0]['id']] = litems[0];
		
		var nitem0 = get_html_item(litems[0], idCounter);
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
		if (index == 0) {
			var vitem = new Object();
			vitem["begin"] = $(element).attr("data-treeanno-begin");
			vitem["end"] = vitem["begin"];
			vitem["id"] = ++idCounter;
			vitem["text"] = "";
			
			var htmlItem = get_html_item(vitem, 0);
			$(element).before(htmlItem);
			cleanup_list();
		}
		indentElement(element);
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

function add_operation(kc, tgts) {
	add_operation(kc, tgts, {});
}

function add_operation(kc, tgts, opts) {
	var s = [];
	$(tgts).each(function(index, element) {
		s.push($(element).attr("data-treeanno-id"));
	});
	var logObj = {op:operations[kc]['desc'], arg:s, opt:opts};
	console.log(logObj);
	$("#history").prepend("<li>"+JSON.stringify(logObj)+"</li>");
}

