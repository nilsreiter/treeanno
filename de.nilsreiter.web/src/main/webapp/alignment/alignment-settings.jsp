<div class="dialog">
<h1>Create Alignment</h1>
<div>
<form id="alignmentform">
	<div>
		<h2>File Name</h2>
		<input type="text" id="alignmenttitle" name="alignmenttitle"/>
	</div>
	<div id="inputdocumentset">
		<h2>Input Document Set</h2>
		<select name="fileselector"></select>
	</div>
	<div>
		<h2>Alignment Algorithm</h2>
		<select name="algorithm" id="selectalgorithm">
			<option value="NW">Needleman-Wunsch</option>
			<option value="GPA">Graph-based predicate alignment</option>
			<option value="BMM">Bayesian Model Merging</option>
		</select>
	</div>
	<div id="formula">
		<h2>Link weight</h2>
	</div>
	<button id="submitbutton">Align</button>
</form>
</div>
</div>
<script>
$("#selectalgorithm").selectmenu({width:"100%"});

jQuery.getJSON("rpc/get-meta-information", function (data) {
	
	for (var fI in data['supportedFunctions']) {
		var func = data['supportedFunctions'][fI];

		var span = document.createElement("div");
		
		
		$(span).append('<input value="0" id="weight_'+func['canonical']+'" name="weight_'+func['canonical']+'"/>');
		$(span).append('<label for="weight_'+func['canonical']+'">'+func['readable']+'</label>');
		$("div#formula").append(span);

		
	}
	
	$("#formula input").spinner({min:0, max:1,step:0.01,numberFormat: "n" });
	
	

});

jQuery.getJSON('rpc/get-document-sets', function (data) { 
	for (var i in data) {
		var s = "<option value=\""+data[i]['databaseId']+"\">"+
				data[i]['id']+" ("+
				data[i]['documentIds']+")</option>";
		$("#inputdocumentset select").append(s);
	}
	$("#inputdocumentset select").selectmenu({width:"100%"});
});

$("#submitbutton").button().bind("click", function(event) {
	event.preventDefault();
	$.post( "rpc/align", $( "#alignmentform " ).serialize(), function(data){
		// alert(JSON.stringify(data));
	});

});

</script>