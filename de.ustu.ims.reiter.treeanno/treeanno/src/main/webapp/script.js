var maxStringLength = 30;

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
		for (var i = 0; i < data["list"].length; i++) {
			var t = data["list"][i]["text"];
			if (t.length > maxStringLength)
				t = t.substring(0,maxStringLength-3)+"...";
		    $('#outline').append("<li class=\"tl0\"><div>"+t+"</div></li>");
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
			var keyCode = e.keyCode || e.which,
            	kbkey = { up: 38, down: 40, right: 39, left: 37, enter: 13 };
			var allItems = $("#outline li");
			switch (keyCode) {
			case kbkey.down:
				var index = $(".selected").index("#outline li");
				if (index < $(allItems).length-1) {
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
				// alert('TODO: Indent item (maybe)');
				indent();
				break;
			case kbkey.left:
				outdent();
				break;
			case kbkey.enter:
				$(this).prev().attr('checked', !$(this).prev().attr('checked'));
				break;
			}
		};
	});
	
}

function outdentcss() {
	var item = $("#outline li.selected");
	var l =  getLevel(item);
	if (l > 0) {
		$(item).removeClass("tl"+l);
		$(item).addClass("tl"+(l-1));
	}
}


function indentcss() {
	var item = $("#outline li.selected");
	var l =  getLevel(item);
	$(item).removeClass("tl"+l);
	$(item).addClass("tl"+(l+1));
}

function getLevel(item) {
	array = $(item).attr("class").split(/\s+/);
	for (var i = 0; i < array[i].length; i++) {
		if (array[i].substring(0, 2) == "tl") {
			return parseInt(array[i].substring(2, array[i].length));
		}
	}
	return 0;
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

}