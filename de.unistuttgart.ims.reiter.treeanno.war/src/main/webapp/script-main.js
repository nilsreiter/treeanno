var INTERACTION_NONE = "none";
var INTERACTION_TREEANNO = "treeanno";
var INTERACTION_SPLIT = "split";
var INTERACTION_CATEGORY = "category";
var INTERACTION_CATEGORY_T2 = "category_t2";
var INTERACTION_MERGE_SEGMENTATION = "segmentation";
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
	},
	category_t2:{
		preventDefault:false
	},
	segmentation:{
		preventDefault:true
	}
};

/**
 * set to true when shift is pressed (and held)
 */
var shifted = false;
var idCounter = 0;
var loaded = 0;

/**
 * This array stores the edit history (locally)
 */
var history = [];


var kbkey = { up: 38, down: 40, right: 39, left: 37,
		enter: 13, s: 83, m:77, c:67, d:68, shift: 16, one: 49 };
var keyString = {
		8: '&#9003;',
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
};

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
					merge(get_item(action['opt']['newItems'][0]),get_item(action['opt']['newItems'][1]), action['arg'][0]);
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
			fun:function() { split_move_left(1); },
			history:false
		},
		split_move_left_big:{
			id:'split_move_left_big',
			fun:function() { split_move_left(25); },
			history:false
		},
		split_move_right:{
			// move the split point to the right
			id:'split_move_right',
			fun:function() { split_move_right(1); },
			history:false
		},
		split_move_right_big:{
			id:'split_move_right_big',
			fun:function() { split_move_right(25); },
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
		force_indent_and_categorize:{
			// c (in arndt projects)
			id:'force_indent_and_categorize',
			fun:function() {
				force_indent();
				move_selection_up();
				add_category();
			},
			desc:'action.assign_category_t2',
			history:true,
			post:{
				mode:INTERACTION_CATEGORY_T2
			},
			revert: {
				fun: function(action) {
					ops.force_indent.revert.fun(action);
				}
			}
		},
		delete_category:{
			// d
			id:'delete_category',
			fun:function() {
				return set_category($(".selected"), null);
			},
			pre: {
				fun: function() {
					return $(".selected > p.annocat").length > 0;
				}
			},
			desc:'action_delete_category',
			history:true,
			revert: {
				fun: function(action) {
					set_category(id2element(action['arg'][0]), action['opt']['oldcategory']);
				}
			}
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
					return ($("ul ul .selected").length > 0);
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
				fun: function() { return ($(".selected").first().prev("li").length > 0); },
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
					return !$(".selected").first().is($(".active .outline li:visible").first());
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
					return !$(".selected").last().is($(".active .outline li:visible").last());
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
			history:true,
			revert: {
				fun: function(action) {
					for (var i = 0; i < action['arg'].length; i++) {
						id2element(action['arg'][i]).toggleClass("mark1");
					}
				}
			}
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
					var lines = [text.substring(0,action['opt']['split']), text.substring(action['opt']['split'], text.length)];
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
			history:true,
			revert: {
				fun: function(action) {
					for (var i = 0; i < action['opt'].length; i++) {
						var arr = [];
						for (var j = 0; j < action['opt'][i]['children'].length; j++) {
							arr.push(id2element(action['opt'][i]['children'][j]));
						}
						force_indent_elements($(arr), action['opt'][i]['vnode']);
					}
				}
			}
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
		// backspace
		 8: { treeanno: ops.undo },
		// enter
		13: { split: ops.split_enter,
			  category: ops.category_enter,
			  category_t2: ops.category_enter },
		// escape
		27: { category: ops.category_cancel,
			  split: ops.split_cancel,
			  category_t2: ops.category_cancel},
		// left
		37: { treeanno: ops.outdent,
			  split: ops.split_move_left },
		// up
		38: { treeanno: ops.up,
			  segmentation: ops.up },
		// right
		39: { treeanno: ops.indent,
			  split: ops.split_move_right },
		// down
		40: { treeanno: ops.down,
			  segmentation: ops.down },
		// 1
		49: { treeanno: ops.mark },
		// c
		67: { treeanno: ops.categorize },
		// d
		68: { treeanno: ops.delete_category },
		// m
		77: { treeanno: ops.merge,
		      segmentation: ops.merge },
		// s
		83: { treeanno: ops.split,
			  segmentation: ops.split }, 
		1037: { split:ops.split_move_left_big },
		1038: { treeanno: ops.select_up,
			    segmentation: ops.select_up },
		1039: { treeanno: ops.force_indent,
			    split:ops.split_move_right_big },
		1040: { treeanno: ops.select_down,
				segmentation: ops.select_down},
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
		operations[67][INTERACTION_TREEANNO] = ops.force_indent_and_categorize;
		var oldRevertFunction = ops.category_enter.revert.fun;
		ops.category_enter.revert.fun = function(action) {
			oldRevertFunction(action);
			undo();
		};
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
		text:configuration["treeanno.ui.showTextOnButtons"]
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
			text:configuration["treeanno.ui.showTextOnButtons"]
		});

		$( "button.button_save_document" ).button({
			icons: { primary: "ui-icon-disk", secondary:null },
			label: i18n.t("save"),
			text:configuration["treeanno.ui.showTextOnButtons"]
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
			text:configuration["treeanno.ui.showTextOnButtons"],
			disabled:true
		}).click(undo);

		disableSaveButton();
		document.onkeydown = function(e) {
			key_down(e);
		};
		document.onkeyup = function(e) {
			key_up(e);
		};

		var url = "rpc/c/0/"+documentId+(master?"":"/"+targetUserId);
		console.log("Querying for document content: " + url);
		jQuery.getJSON(url, function(data) {
			console.log("Received document content");
			// fixing master setting
			master=("master" in data?true:false);
			var breadcrumbHTML = "<a href=\"projects.jsp?projectId="+
				data["document"]["project"]["id"]+
				"\">"+data["document"]["project"]["name"]+
				"</a> &gt; "+(master?i18n.t("bc.master"):"")+
				data["document"]["name"];
			if (targetUserId != userId) {
				breadcrumbHTML += " &gt; "+data["user"]["name"];
			}
			$(".breadcrumb").append(breadcrumbHTML);

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
					$('.outline').append(get_html_item(item, 0));
				}
			}

			$('.outline > li:first-child').addClass("selected");

			$(".outline li > div").click(function(e) {
				var liElement = $(this).parent();
				if (shifted) {
					// if they have the same parent
					if ($(liElement).parent().get(0) == $(".selected").last().parent().get(0)) {
						$(".selected").last().nextUntil(liElement).addClass("selected");
						$(liElement).addClass("selected");
					}
				} else {
					$(".outline li").removeClass("selected");
					$(liElement).addClass("selected");
				}
			});
			init_help();
			$("#status .loading").hide();
			$(".outline").show();
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

function init_segmentation_merge() {
	init_all();
	$("#split").hide();
	$( "button.button_edit_user" ).button({
		icons: { primary: "ui-icon-person", secondary:null },
		disabled: true
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
	interaction_mode = INTERACTION_TREEANNO;
	document.onkeydown = function(e) {
		key_down(e);
	};
	document.onkeyup = function(e) {
		key_up(e);
	};

	var allData = {
			'uDoc':{},
			'doc':{}
		};
	for (var i = 0; i < userIds.length; i++) {
		var userId = userIds[i];
		jQuery.getJSON("rpc/c/0/"+documentIds[0]+"/"+userId, function(data) {
			allData['uDoc'][data['user']['id']] = data;
			loaded++;
			if (loaded == 3) 
				init_segmentation_merge2(allData);
		});
	}
	jQuery.getJSON("rpc/c/0/"+documentIds[0], function(data) {
		allData['doc'][documentIds[0]] = data;
		loaded++;
		if (loaded == 3) 
			init_segmentation_merge2(allData);
	});
	
}

function init_segmentation_merge2(data) {
	var data0 = data['uDoc'][userIds[0]];
	var data1 = data['uDoc'][userIds[1]];
	var doc = data['doc'][documentIds[0]];
	console.log(doc);
	for (var i = 0; i < data0['list'].length; i++) {
		var b0 = data0['list'][i]['begin'];
		var e0 = data0['list'][i]['end'];
		for (var j = 0; j < data1['list'].length; j++) {
			var b1 = data1['list'][j]['begin'];
			var e1 = data1['list'][j]['end'];
			if (b0 == b1 && e0 == e1) {
				data0['list'].splice(i--, 1);
				data1['list'].splice(j--, 1);
			}
		}
	}
	var areas = [];
	var item0 = data0.list.shift();
	var item1 = data1.list.shift();
	var thisArea = [];
	while(typeof(item0) !== "undefined" || typeof(item1) !== "undefined") {
		if (item0.begin === item1.begin) {
			thisArea = [];
			if (item0.end < item1.end) {
				item0.src = 1;
				thisArea.push(item0);
				item0 = data0.list.shift();
			} else if (item0.end > item1.end) {
				item1.src = 2;
				thisArea.push(item1);
				item1 = data1.list.shift();
			} else if (item0.end === item1.end) {
				item0.src = 1;
				item1.src = 2;
				thisArea.push(item0);
				thisArea.push(item1);
				areas.push(thisArea);
				thisArea = [];
				item0 = data0.list.shift();
				item1 = data1.list.shift();
			}
		} else if (item0.end === item1.end) {
			item0.src = 1;
			item1.src = 2;
			thisArea.push(item0);
			thisArea.push(item1);
			areas.push(thisArea);
			thisArea = [];
			item0 = data0.list.shift();
			item1 = data1.list.shift();
		} else if (item0.end < item1.end) {
			item0.src = 1;
			thisArea.push(item0);
			item0 = data0.list.shift();	
		} else if (item0.end > item1.end) {
			item1.src = 2;
			thisArea.push(item1);
			item1 = data1.list.shift();
		}
	}
	var doclist = doc.list;
	for (var area of areas) {
		var row = document.createElement("tr");
		var min = Number.MAX_SAFE_INTEGER;
		var max = 0;
		$(row).append("<td><ul class=\"outline\"></ul></td>");
		$(row).append("<td><ul class=\"outline\"></ul></td>");
		$(row).append("<td class=\"active\"><ul class=\"outline\"></ul></td>");
		
		for (var item of area) {
			min = Math.min(min, item.begin);
			max = Math.max(max, item.end);
			$(row).children("td:nth-child("+item.src+")").children("ul").append(get_html_item(item));
		};
		for (var i = 0; i < doclist.length; i++) {
			docItem = doclist.shift();
			if (docItem.begin >= min && docItem.end <= max) {
				$(row).children("td:nth-child(3)").children("ul").append(get_html_item(docItem));
			} else {
				doclist.push(docItem);
			}
		}
		
		$("#content tbody").append(row);
	}
	var cellar  = document.createElement("ul");
	$(cellar).addClass("outline").hide();
	for (var item of doclist) {
		$(cellar).append(get_html_item(item));
	}
	$("#content").append(cellar);
	
	$(".userDocument.id-"+data0.user.id).show();
	$(".userDocument.id-"+data1.user.id).show();
	$(".document.id-"+doc.document.id).show();
	$("#status").hide();
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
	if (parallel_mode == "segmentation") {
		interaction_mode = INTERACTION_MERGE_SEGMENTATION;
		document.onkeydown = function(e) {
			key_down(e);
		};
		document.onkeyup = function(e) {
			key_up(e);
		};

	}

	$(".outline").hide();
	// first we load the two document by the annotators
	for (var i = 0; i < userDocumentIds.length; i++) {
		var uDocId = userDocumentIds[i];
		load_parallel($(".userDocument.id-"+uDocId), "rpc/c/0/"+documentId, userId, false);
	}
	var goalElement = $(".id-"+documentIds[0]).first();
	// ... then we load the merge document, which is a (new?) master document
	load_parallel(goalElement, "DocumentContentHandling?documentId=", documentIds[0], true);

}

function load_parallel(element, urlhead, dId, goal) {
	jQuery.getJSON(urlhead+dId, function(data) {
		var titleString = "parallel.annotations_from_X";
		if (parallel_mode == "segmentation") {
			titleString = "parallel.segmentations_from_X";
		}
		if (!goal) {
			$(element).parent().prepend("<h2>"+i18n.t(titleString,{"user":data["document"]["user"]["name"]})+"</h2>");
			if (ends_with($(".breadcrumb").text().trim(), ">")) {
				$(".breadcrumb").append(" <a href=\"projects.jsp?projectId="+data["document"]["project"]["id"]+"\">"+data["document"]["project"]["name"]+"</a> &gt; "+i18n.t("parallel.annotations_for_X",{"document":data["document"]["name"]}));
				document.title = treeanno["name"]+" "+treeanno["version"]+": "+i18n.t("parallel.annotations_title_for_X",{"document":data["document"]["name"]});
			} else {
				//$(".breadcrumb").append(", "+data["document"]["name"]);
				// document.title = document.title + ", " + data["document"]["name"];
			}
		} else {
			$(element).parent().prepend("<h2>"+i18n.t("parallel.merged")+"</h2>");

		}
		
			var list = data["list"];

			while (list.length > 0) {
				var item = list.shift();

			if (parallel_mode != 'segmentation' && 'parentId' in item) {
					var parentId = item['parentId'];
				var parentItem = $(element).children("li[data-treeanno-id='"+parentId+"']");
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
		loaded++;
		if (loaded == 3) {
			$(".userDocument > li").each(function(index, element) {
				var begin = parseInt($(element).attr("data-treeanno-begin"));
				var end = parseInt($(element).attr("data-treeanno-end"));
				
				var objects = $(".userDocument li[data-treeanno-begin='"+begin+"'][data-treeanno-end='"+end+"']");
				if (objects.length == 2) {
					$(objects).replaceWith("<hr/>");
					$(".document li[data-treeanno-begin='"+begin+"'][data-treeanno-end='"+end+"']").replaceWith("<hr/>");
				}
				
		});
			$(".text > hr + hr").remove();
			
			$(".text > hr").each(function(index, element) {
				$(element).nextUntil("hr").wrapAll("<div></div>");
	});

			
			$(".document li:visible()").first().addClass("selected");
}
	});

}

function search() {
	$("li.searchFound").removeClass("searchFound");
	var val = $("#form_search").val();
	$("li[title*=\""+val+"\"]").addClass("searchFound");
}

function save_document() {
	var sitems = new Array(); //items;

	$(".outline li").each(function(index, element) {
		var item = new Object();
		item['id'] = $(element).attr("data-treeanno-id");
		item['begin'] = $(element).attr("data-treeanno-begin");
		item['end'] = $(element).attr("data-treeanno-end");
		item['Mark1'] = $(element).hasClass("mark1");
		// alert(id);
		var parents = $(element).parentsUntil(".outline", "li");
		if (parents.length > 0) {
			var parent = parents.first();
			var parentId = parseInt(parent.attr("data-treeanno-id"));
			item["parentId"] = parentId;
		}
		item['category'] = $(element).children("p").text();
		sitems.push(item);
	});
	var url = "rpc/c/0/"+documentId+(master?"":"/"+userId);
	console.log(url);
	$.ajax({
		type: "POST",
		url: url,
		// processData: false,
		data: JSON.stringify({
			items:sitems
		}),
		dataType:"json",
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
	if ($( "button.button_save_document" ).button("option", "disabled") == true && targetUserId == userId) {
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
	var allItems = $(".outline li");

	// get index of last selected item
	var index = $(".selected").last().index(".outline li");

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
	var allItems = $(".active .outline li:visible");

	// get index of last selected item
	var index = allItems.index($("li.selected").last());

	// we remove the selection from everything that is selected
		
	$(".selected").toggleClass("selected");
	// if nothing is selected
	if (index == -1) {
		// we select the first thing on the page
		$($(allItems).get(0)).toggleClass("selected");
	// if there is something after the last selected item, we select that
	} else if (index < $(allItems).length-1) {
		// if shift is not pressed, we select the next item of all items
			$($(allItems).get(index+1)).toggleClass("selected");
	}
	// if the last selected thing is not in viewport, we scroll
	if (!isElementInViewport($(".selected").last()))
		$(window).scrollTop($(".selected").last().offset().top - 200);
}

function extend_selection_up() {
	var allItems = $(".active ul.outline li");
	
	// get index of first selected item
	var index = allItems.index($("li.selected").first());

	// if select the new item
	if (index > 0) {
			$(".selected").first().prev().toggleClass("selected");
	}
	// if not in viewport, scroll
	if (!isElementInViewport($(".selected").first()))
		$(window).scrollTop($(".selected").first().offset().top - 200);
}

function move_selection_up() {
	var allItems = $(".active ul.outline li:visible");

	// get index of first selected item
	var index = allItems.index($("li.selected").first());
	
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
				console.log(operations[kc][interaction_mode]);
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
	var val = $(".selected").attr("data-treeanno-categories");
	$(".selected").removeAttr("data-treeanno-categories");

	if (value) {
		$(element).prepend("<p class=\"annocat\">"+value+"</p>");
		$(element).attr("data-treeanno-categories", value);
	}
	return { oldcategory:val };
}

function enter_category() {
	var oldVal = ($(".selected").attr("data-treeanno-categories")?$(".selected").attr("data-treeanno-categories"):null);

	var value = $("#cat_input").val();
	$("#cat_input").remove();

	var r = set_category($(".selected"), value);
	r['newcategory'] = value;
	return r;

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
	var str = (configuration["treeanno.includeSeparationWhenMerging"]?new Array(distance+1).join(" "):"");

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
	$("#form_splittext").append(configuration["treeanno.ui.paragraphSplitCharacter"]+item['text']);

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
		 		click: function() { act(13) },
		 		tabindex:1
		    }
		]
	});
}

function split_move_right_text(text, dist) {
	var p = text.indexOf(configuration["treeanno.ui.paragraphSplitCharacter"]);

	var newText = text.substring(0,p)+
		text.substring(p+1,p+1+dist)+
		configuration["treeanno.ui.paragraphSplitCharacter"]+
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
	var p = text.indexOf(configuration["treeanno.ui.paragraphSplitCharacter"]);
	return text.substring(0,p - dist)+
		configuration["treeanno.ui.paragraphSplitCharacter"]+
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

	if (configuration["treeanno.ui.paragraphSplitBehaviour"] == "BEFORE-SPACE") {
		if (text.includes(" " + configuration["treeanno.ui.paragraphSplitCharacter"])) {
			return false;
		}
	} else if (configuration["treeanno.ui.paragraphSplitBehaviour"] == "AFTER-SPACE") {
		if (text.includes(configuration["treeanno.ui.paragraphSplitCharacter"]+" "))
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
	console.log(logObj);
	return logObj;
}

function splitdialog_enter() {
	var itemid = $(".selected").attr("data-treeanno-id");
	var item = get_item(itemid);
	var text = $("#form_splittext").text();
	var lines = text.split(configuration["treeanno.ui.paragraphSplitCharacter"]);
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
	if (!$(element).parent("ul.outline").length) {
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

function force_indent_elements(elements, newId) {
	$(elements).each(function(index, element) {
		if (index == 0) {
			var vitem = new Object();
			vitem["begin"] = $(element).attr("data-treeanno-begin");
			vitem["end"] = vitem["begin"];
			vitem["id"] = (newId?newId:++idCounter);
			vitem["text"] = "";

			var htmlItem = get_html_item(vitem, 0);
			$(element).before(htmlItem);
			cleanup_list();
		}
		indentElement(element);
		cleanup_list();
	});
}

function force_indent() {
	force_indent_elements($(".selected"), null);
}

function delete_virtual_node() {
	var arr = [];
	$(".selected").each(function(index, element) {
		arr.push(deleteVirtualNodeElement(element, true));
	});
	return arr;
}

function deleteVirtualNodeElement(element, moveSelection) {
	// check if it's really a virtual node
	var children=[];

	if ($(element).attr("data-treeanno-begin") == $(element).attr("data-treeanno-end")) {
		console.log("TreeAnno: Found a virtual node to delete")
		var vNodeId = $(element).attr("data-treeanno-id");

		$(element).children("ul").children("li").each(function(i2, e2) {
			console.log("TreeAnno: Outdenting children of virtual node");
			outdentElement(e2);
			children.push($(e2).attr("data-treeanno-id"));
		});

		if (moveSelection)
			$(element).prev().addClass("selected");
		$(element).remove();
		return {vnode:vNodeId, children:children};

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
	$(".outline ul:not(:has(*))").remove();
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
	ops[action['op']]['revert'].fun(action);
	$("#history > li:first()").remove();
	$( "button.button_undo" ).button({disabled:(history.length==0)});
}

function id2element(id) {
	return $(".active li[data-treeanno-id=\""+id+"\"]");
}
