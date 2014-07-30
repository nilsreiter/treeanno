function add_event_column_left(number) {
	var s = '<div class="eventspecifier" id="eventspec'+number+'" title="Event '+number+'"><table><tbody>'+
	'<tr><td><label>Class</label></td><td><input type="text" name="eventclass" value=""/></td></tr>'+
	'<tr><td><label>Surface</label></td><td><input type="text" name="eventsurface" value=""/></td></tr>'+
	'<tr><td><label>Lemma</label></td><td><input type="text" name="eventlemma" value=""/></td></tr>'+
	'<input type="hidden" name="position" value="'+number+'" />'+
	'</tbody></table></div>';
	$( "#eventpattern" ).prepend(s);
	add_role_row(number);
	$( "#eventspec"+number).dialog({
		autoOpen: true,
		close:function(event) {
	    	$("#eventspec"+number).remove();
	    	$( "#addprev").unbind("click");
	        $( "#addprev" ).button().bind("click", function(event) {
	        	add_event_column_left(number);
	        })
	   },
	   position:{
			my:"right center",
			at:"left center",
			of:"#eventspec"+(number+1)
		},
		appendTo:"#eventpattern"
	});
	$("#eventspec"+number+" input").bind("keyup", function(e) {
		if(e.keyCode == 13) {
			$("#submitbutton").click();
		}
	});
	$( "#eventspec"+number+" input[name='eventclass']" ).autocomplete({ 
    	source: eventClassesArray,
    	appendTo: '#eventspec'+number
    });

	$( "#addprev" ).unbind("click");
    $( "#addprev" ).bind("click", function(event) {
    	add_event_column_left(number-1);
    });

}

function add_role_row(number) {
	for (i = 0; i < numberOfRoles; i++) {
		$("#eventspec"+number+" > table > tbody").append(get_role_specifier(i));
	}
}

function add_event_column_right(number) {
	var s = '<div class="eventspecifier" id="eventspec'+number+'" title="Event '+number+'"><table><tbody>'+
	'<tr><td><label>Class</label></td><td><input type="text" name="eventclass" value=""/></td></tr>'+
	'<tr><td><label>Surface</label></td><td><input type="text" name="eventsurface" value=""/></td></tr>'+
	'<tr><td><label>Lemma</label></td><td><input type="text" name="eventlemma" value=""/></td></tr>'+
	'<input type="hidden" name="position" value="'+number+'" />'+
	'</tbody></table></div>';
	$( "#eventpattern" ).append(s);
	add_role_row(number);

	$( "#eventspec"+number).dialog({      
		autoOpen: true,
		close:function(event) {
	    	$("#eventspec"+number).remove();
	    	$( "#addnext").unbind("click");
	        $( "#addnext" ).button().bind("click", function(event) {
	        	add_event_column_right(number);
	        })
	   },
	   appendTo:"#eventpattern",
	   position:{
			my:"left center",
			at:"right center",
			of:"#eventspec"+(number-1)
		}
	});
	$("#eventspec"+number+" input").bind("keyup", function(e) {
		if(e.keyCode == 13) {
			$("#submitbutton").click();
		}
	});
	$( "#eventspec"+number+" input[name='eventclass']" ).autocomplete({ 
    	source: eventClassesArray,
    	appendTo: '#eventspec'+number
    });
	$( "#addnext" ).unbind("click");
    $( "#addnext" ).bind("click", function(event) {
    	add_event_column_right(number+1);
    });


}

function add_role(event) {

}

function get_role_specifier(number) {
	var roleSpecifier = "<tr>"+
	"<td><label>Role "+number+"</label></td><td>"+
	"<table><tr><td>Name</td><td><input type=\"text\" name=\"eventRoleName"+number+"\" /></td></tr>"+
	"<tr><td>Filler</td><td><input type=\"text\" name=\"eventRoleFiller"+number+"\" /></td></tr>"+
	"</table></td></tr>";
	return roleSpecifier;
}
