<%@ include file="head.jsp" %>
<div class="level2 content">
<%@ include file="../common/document-menu.jsp" %>

<div class="level3 content">
<%@ include file="../common/menu.jsp" %>


<div class="content level4">
<div class="menu">
<%@ include file="../controls.html" %>
</div>

<div class="content level5">
		<div class="originaltext surface ${documents[0].id}"></div>
</div>

</div>
</div>
</div>
<script>
init_controls(".level4 > .menu");

jQuery.getJSON("rpc/get-document?doc=${param.doc}", function (data) { 
	var target = ".originaltext";
	var lastpos = 0;
	
	$(target).addClass(data['id']);
	var cont = "."+data['id'];
	for (sid in data['sentences']) {
		var sentence = data['sentences'][sid];
		$(cont).append('<span class="sentence '+sentence['id']+'"></span>');
		for (tid in sentence['tl']) {
			var tokid = sentence['tl'][tid];
			var token = data['tokens'][tokid];
			if (token['begin'] > lastpos) {
				$(cont+" ."+sentence['id']).append(" ");
			}
			$(cont+" ."+sentence['id']).append('<span class="token '+token['id']+'">'+token['surface']+"</span>");
			lastpos = token['end'];
			
			for (i in token['mentionIds']) {
				var mentionId = token['mentionIds'][i];
				$(cont+" ."+token['id']).addClass("mention "+mentionId);
			}
		}
	}
	
	for (i in data['frames']) {
		var frame = data['frames'][i];
		for (j in frame['tl']) {
			var tokId = frame['tl'][j];
			$(cont+" ."+tokId).addClass("frame "+frame['id']+" "+ frame['name']);			
		}
	}
	
	for (i in data['events']) {
		var event = data['events'][i];
		$(cont+" ."+event['anchorId']).addClass("event " + event['class']+" "+event['id']);
	};
});

</script>

<%@ include file="foot.jsp" %>