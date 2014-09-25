<h1>Create Alignment</h1>
<div>
<form id="alignmentform">
	<div>
		<label for="alignmenttitle">File Name</label>
		<input type="text" id="alignmenttitle" name="alignmenttitle"/><br/>
		<label for="inputdocumentset">Input Document Set</label>
		<select id="inputdocumentset" name="fileselector"></select><br/>
		<label for="selectalgorithm">Alignment Algorithm</label>
		<select name="algorithm" id="selectalgorithm">
			<option value="de.nilsreiter.alignment.algorithm.NeedlemanWunsch">Needleman-Wunsch</option>
			<option value="de.nilsreiter.alignment.algorithm.MRSystem">Graph-based predicate alignment</option>
			<option value="de.nilsreiter.alignment.algorithm.BayesianModelMerging">Bayesian Model Merging</option>
		</select>
	</div>
	<div>
		<label for="threshold">Threshold</label>
		<input name="threshold" size="3" id="threshold" value="0.5" type="text"/>
		<div id="formula" style="border:thin solid #CCC;padding:5px;">
			<label>Link weight formula</label><br/>
			<select name="combination" id="combination">
				<option value="AVG">Arithmetic Mean</option>
				<option value="GEO">Geometric Mean</option>
			</select>
			
		</div>
	</div>
	<button id="submitbutton">Align</button>
</form>
</div>
<div id="message"></div>
<script>
$("#selectalgorithm").selectmenu({width:"100%"});
$("#threshold").spinner({min:0,max:1,step:0.01,numberFormat:"n"});
$("#combination").selectmenu({width:"100%"});

jQuery.getJSON("rpc/get-meta-information", function (data) {
	
	for (var fI in data['supportedFunctions']) {
		var func = data['supportedFunctions'][fI];

		var span = document.createElement("div");
		
		
		$(span).append('<input value="0" size="3" id="weight_'+func['canonical']+'" name="weight_'+func['canonical']+'"/>');
		$(span).append('<label for="weight_'+func['canonical']+'">'+func['readable']+'</label>');
		$("#formula").append(span);

		
	}
	
	$("#formula input").spinner({min:0, max:1,step:0.01,numberFormat: "n" });
	
	

});

jQuery.getJSON('rpc/get-document-sets', function (data) { 
	for (var i in data) {
		var s = "<option value=\""+data[i]['databaseId']+"\">"+
				data[i]['id']+" ("+
				data[i]['documentIds']+")</option>";
		$("#inputdocumentset").append(s);
	}
	$("#inputdocumentset").selectmenu({width:"100%"});
});

$("#submitbutton").button().bind("click", function(event) {
	event.preventDefault();
	$.post( "rpc/align", $( "#alignmentform " ).serialize(), function(data){
		$("#message").empty();
		$("#message").append("<p>Running algorithm <code>"+data['algorithm']+"</code></p>");
		$("#message").dialog({autoOpen:true, width:"400px"});
		// alert(JSON.stringify(data));
	});

});

</script>