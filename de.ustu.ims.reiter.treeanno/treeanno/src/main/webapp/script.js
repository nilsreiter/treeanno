var maxStringLength = 30;

var i18n;

var enable_interaction = true;

var items;

var idCounter;

var includeSeparationWhenMerging = true;


function init(t) {
	i18n = t;
	$(function() {
		$("#split").hide();
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

function get_html_item(item, i) {
	var htmlItem = document.createElement("li");
	$(htmlItem).addClass("tl0");
	$(htmlItem).attr("data-treeanno-id", i);
	$(htmlItem).append("<div>"+dtext(item['text'])+"</div>");
	return htmlItem;
}

function dtext(s) {
	if (s.length > maxStringLength)
		s = s.substring(0,maxStringLength-3)+"...";
	return s;
}

function login() {
	var docid = "1";
	$( this ).dialog( "close" );
	var username = $("#form_username").val();
	jQuery.getJSON("ControllerServlet?document="+docid, function(data) {
		items = data["list"];
		idCounter = data["list"].length;
		for (var i = 0; i < data["list"].length; i++) {
			var item = data["list"][i];
			var t = item["text"];
			
			if (t.length > maxStringLength)
				t = t.substring(0,maxStringLength-3)+"...";
			$('#outline').append(get_html_item(item, i));
		}
		$('#outline li:first-child').addClass("selected");
		$('#outline').nestedSortable({
			handle: 'div',
			items: 'li',
			toleranceElement: '> div',
			listType: 'ul',
		    placeholder:'placeholder',
		    forcePlaceholderSize:true
		});
		document.onkeydown = function(e) {
			user_input(e);
		}
	});
	
}

function user_input(e) {
	if (!enable_interaction) return;
	e.preventDefault();
	var keyCode = e.keyCode || e.which,
    	kbkey = { up: 38, down: 40, right: 39, left: 37, 
			enter: 13, s: 83, m:77, c:67, d:68 };
	var allItems = $("#outline li");
	switch (keyCode) {
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
		var index = $(".selected").index("#outline li");
		if (index == -1) {
			$($(allItems).get(0)).toggleClass("selected");					
		} else if (index < $(allItems).length-1) {
			$($(allItems).get(index)).toggleClass("selected");
			$($(allItems).get(index+1)).toggleClass("selected");
		}
		break;
	case kbkey.up:
		var index = $(".selected").index("#outline li");
		if (index > 0) {
			$($(allItems).get(index)).toggleClass("selected");
			$($(allItems).get(index-1)).toggleClass("selected");
		}
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
		litems[1] = new Object();
		litems[1]['end'] = item['end'];
		litems[1]['text'] = lines[1];
		litems[1]['begin'] = litems[0]['end'];
		items[itemid] = undefined;
		
		sublist = $(".selected > ul").detach();
		items[++idCounter] = litems[1];
		$(".selected").after(get_html_item(litems[1], idCounter))
		$(".selected").next().append(sublist);
		items[++idCounter] = litems[0];
		$(".selected").after(get_html_item(litems[0], idCounter));
		nsel = $(".selected").next();
		$(".selected").remove();
		$(nsel).addClass("selected");
	}
	cleanup_list();
	splitdialog_cleanup();
}


function outdent() {
	var currentItem = $("#outline li.selected");
	if ($("ul#outline > li.selected").length == 0) {
		var newParent = $("#outline li.selected").parentsUntil("li").parent();
		
		siblings = $("#outline li.selected ~ li").detach();
		
		if ($("#outline li.selected > ul").length == 0)
			$("#outline li.selected").append("<ul></ul>");
		$("#outline li.selected > ul").append(siblings);
		s = $(currentItem).detach();
		$(newParent).after(s);
	}
	cleanup_list();
}

function indent() {
	var currentItem = $("#outline li.selected");
	if ($(".selected").prev("li").length > 0) {
		prev = $(".selected").prev("li");
		if ($(prev).children("ul").length == 0)
			prev.append("<ul></ul>");
		s = $(".selected").detach();
		$(prev).children("ul").append(s);
	}
	cleanup_list();
}


function cleanup_list() {
	$("#outline ul:not(:has(*))").remove();
}