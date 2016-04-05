var INTERACTION_NONE = "none";
var INTERACTION_TREEANNO = "treeanno";
var INTERACTION_SPLIT = "split";
var INTERACTION_CATEGORY = "category";
var interaction_mode = INTERACTION_TREEANNO;

var mode = {
	treeanno:{ 
		preventDefault:true
	},
	split:{
		preventDefault:true
	},
	category:{
		preventDefault:false
	}
}

/**
 * set to true when shift is pressed (and held)
 */
var shifted = false;
var idCounter = 0;

/**
 * This array stores the edit history (locally)
 */
var history = [];


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

/**
 * the main object containing possible operations in TreeAnno.
 * Each operation is a hash of the following form:
 * 
 * {
 *    // an identifier for the operation
 * 	  id:'an-id',
 * 
 *    // a function that implements the operation
 *    fun:function,
 *    
 *    // whether this op makes an entry in the edit history
 *    // This also determines whether the save button will be activated
 *    history: (true|false),
 *    
 *    // a locale key to be put into the help menu.
 *    // if null, no help entry will be produced
 *    desc: "bla bla",
 *    
 *    // precondition definitions
 *    pre: {
 *        
 *        // a function that checks the precondition. Needs to return a boolean 
 *        // value
 *    	  fun: function,
 *    
 *        // a hash that is passed to the notification framework in case the
 *        // precondition fails
 *        // There is an issue with localisation here, that's why we need to 
 *        // re-process some keys in init_operations(...).
 *        fail: {
 *           // ... 
 *        }
 *    },
 *    
 *    // post-op definitions
 *    post: {
 *    	  // if the op changes the interaction mode
 *    	  mode:INTERACTION_TREEANNO
 *    }
 * }
 */
var ops={
		split_enter:{
			// enter pressed in the split dialog
			id:'split_enter',
			fun:splitdialog_enter,
			history:true,
			pre: {
				fun:splitdialog_validate,
				fail: {
					type:"error",
					text:"Split character at invalid position",
					timeout:null
				}
			},
			post: {
				mode:INTERACTION_TREEANNO
			},
			revert: {
				fun: function(action) {
					merge(get_item(action['opt']['newItems'][0]),get_item(action['opt']['newItems'][1]), action['arg'][0])
				}
			}
		},
		split_cancel:{
			id:'split_cancel',
			fun:splitdialog_cleanup,
			history:false,
			post: {
				mode:INTERACTION_TREEANNO
			}
		},
		split_move_left:{
			// move the split point to the left
			id:'split_move_left',
			fun:function() { split_move_left(1) },
			history:false
		},
		split_move_left_big:{
			id:'split_move_left_big',
			fun:function() { split_move_left(25) },
			history:false
		},
		split_move_right:{
			// move the split point to the right
			id:'split_move_right',
			fun:function() { split_move_right(1) },
			history:false
		},
		split_move_right_big:{
			id:'split_move_right_big',
			fun:function() { split_move_right(25) },
			history:false
		},
		split:{
			// s
			id:'split',
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
			desc:'action_split',
			history:false,
			post:{
				mode:INTERACTION_SPLIT
			}
		},
		category_enter:{
			// enter pressed when editing category string
			id:'category_enter',
			fun:enter_category,
			history:true,
			desc:'assign-category',
			post:{
				mode:INTERACTION_TREEANNO
			},
			revert:{
				fun: function(action) {
					if (action['opt']['oldcategory']) {
						set_category(id2element(action['arg'][0]), action['opt']['oldcategory']);
					} else {
						set_category(id2element(action['arg'][0]), null);
					}
				}
			}
		},
		category_cancel:{
			id:'category_cancel',
			fun:cancel_category,
			history:false,
			post:{
				mode:INTERACTION_TREEANNO
			}
		},
		categorize:{
			// c
			id:'categorize',
			fun:add_category,
			desc:'action_assign_category',
			history:false,
			post:{
				mode:INTERACTION_CATEGORY
			}
		},
		delete_category:{
			// d
			id:'delete_category',
			fun:delete_category,
			desc:'action_delete_category',
			history:true
		},
		outdent:{
			// left
			id:'outdent',
			fun:outdent,
			desc:'action_outdent',
			history:true,
			revert:{
				fun: function(action) {
					for (var i = 0; i < action['arg'].length; i++) {
						indentById(action['arg'][i]);
					}
				}			},
			pre:{
				fun:function() { 
					return ($("ul ul .selected").length > 0)
				},
				fail: {
					type:"information",
					text:"action.left.prefail"
				}
			}
		},
		indent:{
			// right
			id:'indent',
			fun:indent,
			desc:'action_indent',
			history:true,
			revert: {
				fun: function(action) {
					for (var i = 0; i < action['arg'].length; i++) {
						outdentById(action['arg'][i]);
					}
				}
			},
			pre: {
				fun: function() { return ($(".selected").first().prev("li").length > 0) },
				fail: {
					type: "information",
					text: "action.right.prefail"
				}
			}
		},
		force_indent:{
			// shift + right
			id:'force_indent',
			fun:force_indent,
			desc:'action.force_indent',
			history:true,
			revert:{
				fun: function(action) {
					deleteVirtualNodeElement(id2element(action['arg'][0]).parent().parent(), false);
				}
			}
		},
		up:{
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
		},
		down:{
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
		},
		mark:{
			// one
			id:'mark',
			fun:function() {
				$(".selected").toggleClass("mark1");
				enableSaveButton();
			},
			desc:'action_mark1',
			history:true
		},
		merge: {
			// m
			id:'merge',
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
			history:true,
			revert:{
				fun: function(action) {
					var item = get_item(action['opt']['newId']);
					var text = item['text'];
					var lines = [text.substring(0,action['opt']['split']), text.substring(action['opt']['split'], text.length)]
					split(item, lines, false, action['arg']);
				}
			}
		},
		select_down:{
			// shift + down
			id:'select_down',
			fun:extend_selection_down,
			desc:'action.down.desc',
			history:false,
			pre: {
				fun:function() { return shifted; }
			}
		},
		select_up:{
			// shift + up
			id:'select_up',
			fun:extend_selection_up,
			desc:'action.up.desc',
			history:false,
			pre: {
				fun:function() { return shifted; }
			}
		},
		delete_virtual_node:{
			// shift + d
			id:'delete_virtual_node',
			fun:delete_virtual_node,
			desc:'action.delete_vnode',
			history:true
		},
		save_document:{
			// shift + s
			id:'save_document',
			fun:save_document,
			desc:'action.save_document',
			history:false
		},
		undo:{
			id:'undo',
			fun:undo,
			desc:'action.undo',
			history:false
		}
	};
var operations = {
		 8: { treeanno: ops.undo },
		13: { split: ops.split_enter,
			  category: ops.category_enter },
		27: { category: ops.category_cancel,
			  split: ops.split_cancel },
		37: { treeanno: ops.outdent, 
			  split: ops.split_move_left },
		38: { treeanno: ops.up },
		39: { treeanno: ops.indent,
			  split: ops.split_move_right },
		40: { treeanno: ops.down },
		49: { treeanno: ops.mark },
		67: { treeanno: ops.categorize },
		68: { treeanno: ops.delete_category },
		77: { treeanno: ops.merge },
		83: { treeanno: ops.split }, 
		1037: { split:ops.split_move_left_big },
		1038: { treeanno: ops.select_up },
		1039: { treeanno: ops.force_indent,
			    split:ops.split_move_right_big },
		1040: { treeanno: ops.select_down },
		1068: { treeanno:ops.delete_virtual_node },
		1083: { treeanno:ops.save_document }
};


function get_html_item(item, i) {
	var htmlItem = document.createElement("li");
	$(htmlItem).attr("title", item['text']);
	$(htmlItem).attr("data-treeanno-id", item['id']);
	$(htmlItem).attr("data-treeanno-begin", item['begin']);
	$(htmlItem).attr("data-treeanno-end", item['end']);
	$(htmlItem).attr("data-treeanno-categories", item['category']);
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
		operations[49][INTERACTION_TREEANNO]['disabled'] = 1;
		operations[67][INTERACTION_TREEANNO]['fun'] = function() {
			act(1039);
			move_selection_up();
			add_category();
			interaction_mode = INTERACTION_CATEGORY;
		}
		operations[67][INTERACTION_TREEANNO]['desc'] = 'action.assign_category_t2';
		operations[68][INTERACTION_TREEANNO]['desc'] = "action.delete_category_t2";
		operations[1039][INTERACTION_TREEANNO]['desc'] = 'action.force_indent_t2';
		break;
	}
	operations[37][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[37][INTERACTION_TREEANNO]['pre']['fail']['text']);
	operations[38][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[38][INTERACTION_TREEANNO]['pre']['fail']['text']);
	operations[39][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[39][INTERACTION_TREEANNO]['pre']['fail']['text']);
	operations[40][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[40][INTERACTION_TREEANNO]['pre']['fail']['text']);
	operations[83][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[83][INTERACTION_TREEANNO]['pre']['fail']['text']);
	operations[77][INTERACTION_TREEANNO]['pre']['fail']['text'] = i18n.t(operations[77][INTERACTION_TREEANNO]['pre']['fail']['text']);

}

function init_help() {
	var helpElement = document.createElement("div");
	$(helpElement).attr("id", "help");
	$(helpElement).append("<div class=\"trans\">"+i18n.t('help_title')+"</div>");
	var helpTable = document.createElement("table");
	for (key in operations) {
		if (!operations[key]['disabled']) {
			if (INTERACTION_TREEANNO in operations[key]) {
				$(helpTable).append("<tr><td><span class=\"command\">"+keyString[key]+"</span></td><td class=\"trans\">"+i18n.t(operations[key][INTERACTION_TREEANNO]['desc'])+"</td></tr>");	
			}
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
		$("#form_search").focus(function() {interaction_mode = INTERACTION_NONE});
		$("#form_search").blur(function() {interaction_mode = INTERACTION_TREEANNO});
		
		$("#show_history").button({
			icons:{primary:null,secondary:null},
			label:i18n.t("show_history")
		}).click(function() {
			$("#rsidebar").toggle();
			if ($("#rsidebar").is(':visible'))
				$("#content").css("width", "calc(100% - 400px)");
			else 
				$("#content").width("100%");
		});
		
		$( "button.button_undo" ).button({
			icons: { primary: "ui-icon-arrowreturnthick-1-w", secondary:null },
			label: i18n.t("undo"),
			text:showText,
			disabled:true
		}).click(undo);
		
		disableSaveButton();
		document.onkeydown = function(e) {
			key_down(e);
		};
		document.onkeyup = function(e) {
			key_up(e);
		};
		console.log("Querying for document content");
		jQuery.getJSON("DocumentContentHandling?"+(master?"master=master&":"")+"documentId="+documentId, function(data) {
			console.log("Received document content");
			// fixing master setting
			master=("master" in data?true:false);
			$(".breadcrumb").append("<a href=\"projects.jsp?projectId="+data["document"]["project"]["id"]+"\">"+data["document"]["project"]["name"]+"</a> &gt; "+(master?i18n.t("bc.master"):"")+data["document"]["name"])
			
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
	$("#form_search").focus(function() { interaction_mode = INTERACTION_NONE; });
	$("#form_search").blur(function() { interaction_mode = INTERACTION_TREEANNO; });
	
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
		
			$("#status .loading").hide();
			$(element).show();
			/*$("li > div", element).smartTruncation({
			    'truncateCenter'    : true
			 });*/
		});
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
			master:(master?true:false),
			items:sitems
		}),
		contentType:'application/json; charset=UTF-8',
		success: function(data) {
			if (data['error'] == 0) {
				noty({
					type:'information',
					text:'Save successful'
				});
				$( "button.button_save_document" ).button( "option", "disabled", true );
				$( "button.button_save_document" ).button( "option", "icons", { primary: "ui-icon-check", secondary:null });				
			} else {
				noty({
					type:'error',
					timeout:false,
					text:data['classname']+": "+data['message']
				});
			}
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
	if (interaction_mode === INTERACTION_NONE) return;
	if (mode[interaction_mode]['preventDefault'])
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

function extend_selection_up() {
	var allItems = $("#outline li");
	// get index of first selected item
	var index = $(".selected").first().index("#outline li");
	
	// if select the new item
	if (index > 0) {
			$(".selected").first().prev().toggleClass("selected");
	}
	// if not in viewport, scroll
	if (!isElementInViewport($(".selected").first()))
		$(window).scrollTop($(".selected").first().offset().top - 200);
}

function move_selection_up() {
	var allItems = $("#outline li");
	// get index of first selected item
	var index = $(".selected").first().index("#outline li");
	// if shift is not pressed, remove the selection
	if (index > 0)
		$(".selected").toggleClass("selected");
	
	// if select the new item
	if (index > 0) {
		$($(allItems).get(index-1)).toggleClass("selected");
	}
	// if not in viewport, scroll
	if (!isElementInViewport($(".selected").first()))
		$(window).scrollTop($(".selected").first().offset().top - 200);
}

function act(keyCode) {
	switch (keyCode) {
	case kbkey.shift:
		shifted = true;
		break;
		// What does this do?
		/*	case kbkey.enter:
		$(this).prev().attr('checked', !$(this).prev().attr('checked'));
		break;*/
	default:
		kc = keyCode;
		if (shifted)
			kc = keyCode + 1000;
		if (kc in operations && interaction_mode in operations[kc] && !operations[kc][interaction_mode]['disabled']) {
			if (check_precondition(kc)) {
				var selection = $(".selected");
				var val = operations[kc][interaction_mode].fun();
				if (operations[kc][interaction_mode]['history']) {
					add_operation(kc, selection, val);
					enableSaveButton();
				}
				if ("post" in operations[kc][interaction_mode] && 
						"mode" in operations[kc][interaction_mode]["post"]) {
					interaction_mode = operations[kc][interaction_mode]["post"]["mode"];
				}
			} else {
				if ('fail' in operations[kc][interaction_mode]['pre'])
					noty(operations[kc][interaction_mode].pre.fail);
			}
		}
	}
}

function key_down(e) {
	if (interaction_mode === INTERACTION_NONE) return;
	if (mode[interaction_mode]['preventDefault'])
		e.preventDefault();
	var keyCode = e.keyCode || e.which;
	var allItems = $("#outline li");
	
	act(keyCode);
}

/**
 * This method checks whether the preconditions for a certain keycode are met, 
 * if they are set at all. If no preconditions have been set, the method 
 * returns true.
 */
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
	$(".selected > p.annocat").remove();
	var val = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):$(".selected").attr("title"));
	$(".selected").first().prepend("<input type=\"text\" size=\"100\" id=\"cat_input\" value=\""+val+"\"/>");
	document.getElementById("cat_input").focus();
}

function cancel_category() {
	$("#cat_input").remove();
}

function set_category(element, value) {
	$(element).children("p.annocat").remove();
	if (value) {
		$(element).prepend("<p class=\"annocat\">"+value+"</p>");
		$(element).attr("data-treeanno-categories", value);
	}
}

function enter_category() {
	var oldVal = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):null);

	var value = $("#cat_input").val();
	$("#cat_input").remove();
	
	set_category($(".selected"), value);
	return {
		newcategory:value,
		oldcategory:oldVal
	};

}

/**
 * Functions returns a javascript object representing an item. Field values are 
 * retrieved from the DOM tree.
 * @param id The id of the item
 * @returns {___anonymous20257_20259}
 */
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
	var l = $(".selected").last().next();
	var item1 = get_item($(".selected").first().attr("data-treeanno-id"));
	var item0 = get_item($(".selected").last().attr("data-treeanno-id"));
	// add_operation(77, [$(".selected").last(), $(".selected").first()]);
	var r = merge(item1, item0, null);
	l.addClass("selected");
	return r;
}

function merge(item1, item0, newId) {
	var correctOrder = (item1['begin'] > item0['begin']);
	var element0 = id2element(item0['id']);
	var element1 = id2element(item1['id']);
	
	var nitem = new Object();
	var distance = (correctOrder?item1['begin']-item0['end']:item0['begin']-item1['end']);
	var str = (includeSeparationWhenMerging?new Array(distance+1).join(" "):"");

	nitem['text'] = (correctOrder?item0['text']+str+item1['text']:item1['text']+str+item0['text']);
	nitem['begin'] = (correctOrder?item0['begin']:item1['begin']);
	nitem['end'] = (correctOrder?item1['end']:item0['end']);
	nitem['id'] = (newId?newId:++idCounter);
	
	var sublist0 = $(element0).children("ul").detach();
	element0.remove();
	
	var nhitem = get_html_item(nitem, idCounter);
	var nj = $(element1).after(nhitem);
	
	var sublist1 = element1.children("ul").detach();
	element1.remove();
	nj.append(sublist0);
	nj.append(sublist1);
	
	return {
		split:(correctOrder?item0['end']-item0['begin']:item1['end']-item1['begin']),
		'newId':nitem['id']
	};
	
}



function splitdialog() {
	var item = get_item($(".selected").first().attr("data-treeanno-id"));
	$("#form_splittext").append(paragraphSplitCharacter+item['text']);
	
	$("#split").dialog({
		title: i18n.t("split_dialog.title"),
		modal: true,
		minWidth: 400,
		close: splitdialog_cleanup,
		buttons: 
		[
		    {
		    	text: i18n.t("split_dialog.cancel"),
		    	click: splitdialog_cleanup,
		    	tabindex:2
		    },{
		 		text: i18n.t("split_dialog.ok"),
		 		click: splitdialog_enter,
		 		tabindex:1
		    }
		]
	});
}

function split_move_right_text(text, dist) {
	var p = text.indexOf(paragraphSplitCharacter);
	
	var newText = text.substring(0,p)+
		text.substring(p+1,p+1+dist)+
		paragraphSplitCharacter+
		text.substring(p+1+dist, text.length);
	return newText;
}

function split_move_right(dist) {
	var text = $("#form_splittext").text();
	var newText = split_move_right_text(text, dist);
/*	if (paragraphSplitBehaviour == "AFTER-SPACE") {
		while(newText.includes(paragraphSplitCharacter+" ")) {
			newText = split_move_right_text(newText, 1);
		}
	} else if (paragraphSplitBehaviour == "BEFORE-SPACE") {
		while(newText.includes(" " + paragraphSplitCharacter)) {
			newText = split_move_left_text(newText, 1);
		}
	}*/
 	$("#form_splittext").text(newText);
}

function split_move_left_text(text, dist) {
	var p = text.indexOf(paragraphSplitCharacter);
	return text.substring(0,p - dist)+
		paragraphSplitCharacter+
		text.substring(p-dist,p)+
		text.substring(p+1, text.length)
}

function split_move_left(dist) {
	var text = $("#form_splittext").text();
	var newText = split_move_left_text(text, dist);
	$("#form_splittext").text(newText);
}

function splitdialog_cleanup() {
	$("#split").dialog( "destroy" );
	$("#form_splittext").empty();
}

function splitdialog_validate() {
	var text = $("#form_splittext").text();

	if (paragraphSplitBehaviour == "BEFORE-SPACE") {
		if (text.includes(" " + paragraphSplitCharacter)) {
			return false;
		}
	} else if (paragraphSplitBehaviour == "AFTER-SPACE") {
		if (text.includes(paragraphSplitCharacter+" "))
			return false;
	}
	return true;
}

function split(item, lines, moveSelection, ids) {
	var element = id2element(item['id']);
	var litems = new Array();
	litems[0] = new Object();
	litems[0]['begin'] = item['begin'];
	litems[0]['text'] = lines[0];
	litems[0]['end'] = parseInt(item['begin'])+parseInt(lines[0].length);
	litems[0]['id'] = (ids?ids[0]:++idCounter);
	litems[1] = new Object();
	litems[1]['end'] = item['end'];
	litems[1]['text'] = lines[1];
	litems[1]['begin'] = litems[0]['end'];
	litems[1]['id'] = (ids?ids[1]:++idCounter);
	// items[itemid] = undefined;
	
	var sublist = $(element).children("ul").detach();
	// items[litems[1]['id']] = litems[1];
	
	var nitem1 = get_html_item(litems[1], idCounter);
	element.after(nitem1);
	element.next().append(sublist);
	// items[litems[0]['id']] = litems[0];
	
	var nitem0 = get_html_item(litems[0], idCounter);
	element.after(nitem0);
	var nsel = element.next();
	element.remove();
	if (moveSelection)
		$(nsel).addClass("selected");
	logObj = {
			newItems:[litems[0]['id'], litems[1]['id']]
	};
	return logObj;
}

function splitdialog_enter() {
	var itemid = $(".selected").attr("data-treeanno-id");
	var item = get_item(itemid);
	var text = $("#form_splittext").text();
	var lines = text.split(paragraphSplitCharacter);
	var logObj;
	if (lines.length == 2) {
		noty({
			type:"information",
			text:i18n.t("action.split.done", {
				left:lines[0].substring(lines[0].length-10,lines[0].length), 
				right:lines[1].substring(0, 10)
			})
		});

		logObj = split(item, lines, true, null);

	}
	cleanup_list();
	splitdialog_cleanup();
	return logObj;
}

function outdentById(id) {
	outdentElement($("li[data-treeanno-id=\""+id+"\"]"))
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
}

function delete_virtual_node() {
	$(".selected").each(function(index, element) {
		deleteVirtualNodeElement(element, true);
	});
}

function deleteVirtualNodeElement(element, moveSelection) {
	// check if it's really a virtual node
	if ($(element).attr("data-treeanno-begin") == $(element).attr("data-treeanno-end")) {
		console.log("TreeAnno: Found a virtual node to delete")
		
		$(element).children("ul").children("li").each(function(i2, e2) {
			console.log("TreeAnno: Outdenting children of virtual node");
			outdentElement(e2);
		});
		
		if (moveSelection)
			$(element).prev().addClass("selected");
		$(element).remove();
	}
}

function indentById(id) {
	indentElement($("li[data-treeanno-id=\""+id+"\"]"))
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
	var logObj = {op:operations[kc][interaction_mode]['id'], arg:s, opt:opts};
	history.push(logObj);
	$( "button.button_undo" ).button({disabled:false});
	console.log(logObj);
	$("#history").prepend("<li>"+JSON.stringify(logObj)+"</li>");
}

function undo() {
	var action = history.pop();
	$("#history > li:first()").remove();

	ops[action['op']]['revert'].fun(action);
	$( "button.button_undo" ).button({disabled:(history.length==0)});
	
}

function id2element(id) {
	return $("li[data-treeanno-id=\""+id+"\"]");
}

