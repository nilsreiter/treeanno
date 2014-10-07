<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Event Search</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/event-search.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>

<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<script src="js/highcharts.js" type="text/javascript"></script>
</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>

<div class="content level3">
<%@ include file="../common/menu.jsp" %>


<div class="content level3">
<div class="menu">
<span class="results-view">
<input id="button-event-roles" type="checkbox" onchange="jQuery('.event .role').toggle()" /><label for="button-event-roles">Roles</label>
<input id="button-surface" type="checkbox" onchange="jQuery('.event .surface').toggle()" /><label for="button-surface">Surface</label>
<input id="button-chart" type="checkbox" onchange="jQuery('#chart').toggle()" checked="checked" /><label for="button-chart">Chart</label>
<button id="button-increase-context">+Context</button>
<button id="button-decrease-context">-Context</button>
</span>
<span class="search-mask">
<input id="button-search" type="checkbox"  /><label for="button-search">New Search</label>
<button id="addprev">Add previous event</button>
<button id="addnext">Add next event</button>
<button id="button-add-role-specifier">+Role</button>
<button id="submitbutton">Search</button>
</span>
</div>
<div class="content level4">
<div id="loading">
	<img src="gfx/loading1.gif"/>
</div>

<form accept-charset="UTF-8">
<div id="eventpattern">
<div class="eventspecifier" id="eventspec0" title="Event 0">
<input type="hidden" name="doc" value="${param.doc}" />
<input type="hidden" name="position" value="0" />
<table>
<tbody>
<tr>
	<td><label>Class</label></td><td><input type="text" name="eventclass" value=""/></td>
</tr><tr>
	<td><label>Surface</label></td><td><input type="text" name="eventsurface" value=""/></td>
</tr><tr>
	<td><label>Lemma</label></td><td><input type="text" name="eventlemma" value=""/></td>
</tr>
</tbody>
</table>
</div>
</div>
</form>
<div id="results">
<div id="chart" style="width:100%;height:300px;"></div>
<table>

</table>
</div>
</div>
</div>
</div>
</div>
</div>
<script>
var currentWindow = 1;
var eventClassesArray = new Array();
var numberOfRoles = 0;

  $(function() {
	 $.ajaxSetup({ scriptCharset: "utf-8" , contentType: "application/json; charset=utf-8"});

	 $("#loading").hide();

	$( "#windowselect" ).spinner({
		min:1,
		max:5
	});

    $( "#eventspec0" ).dialog({
        autoOpen: false,
        resizable: false,
        draggable:true,
	  	beforeClose: function( event, ui ) {
	    	$( ".level3 .search-mask" ).buttonset().buttonset("disable");
	    	$( "#button-search").button("enable");
	  	},
	  	appendTo:"#eventpattern"
    
    });
	$( ".level3 > .menu > #button-search" ).button("enable");
    $( "#addprev").button().bind("click", function(event) {
		add_event_column_left(-1);
    });
    $( "#addnext").button().bind("click", function(event) {
    	add_event_column_right(1);
    });
    
  	$( "#addprev").button("enable");
  	$( "#addnext").button("enable");
	$( "#button-add-role-specifier").button().bind("click", function(event) {
		event.preventDefault();
		$(".eventspecifier > table > tbody").append(get_role_specifier(numberOfRoles++));
	});

  	$( "#button-increase-context" ).bind("click", function (event) {
		jQuery(".cont"+currentWindow).show();
		currentWindow++;
		if (currentWindow >= 10) {
			$( "#button-increase-context" ).button("disable");
		}
		if (currentWindow > 1)
			$( "#button-decrease-context" ).button("enable");

	}); 
	$( "#button-decrease-context" ).bind("click", function (event) {
		currentWindow--;
		jQuery(".cont"+currentWindow).hide();
		if (currentWindow <= 1) {
			$( "#button-decrease-context" ).button("disable");
		}
		if (currentWindow <= 10)
			$( "#button-increase-context" ).button("enable");
	});
  	
    $( "#button-search").bind("click", function (data) {
    	$( '#results table').empty();
    	$( '#results #chart').empty();
    	$( ".level3 .results-view" ).buttonset().buttonset("disable");
    	$( ".level3 .search-mask" ).buttonset().buttonset("enable");

    	
    	$( '#eventspec0' ).dialog('open');
    	$( "#button-search").button("disable");
    	
    	$("input").bind("keyup", function(e) {
    		if(e.keyCode == 13) {
    			$("#submitbutton").click();
    		}
    	});
    	
    	
    	jQuery.getJSON("rpc/get-event-classes?doctype=documentset&doc=${param.doc}", function (data) { 
    		eventClassesArray = data['eventclasses'];
    	    $( "#eventspec0 input[name='eventclass']" ).autocomplete({ 
    	    	source: eventClassesArray,
    	    	appendTo: '#eventspec0'
    	    });
    	});
    });




    
	$( "#submitbutton" ).button().bind("click", function( event ) {
		event.preventDefault();
		$("#loading").show();

		$( ".search-mask").buttonset("disable");
		$("input").unbind("keyup");

		var seri = $(".eventspecifier input").serialize();
		$( ".eventspecifier" ).dialog("close");
		// alert(seri);
		var url = "rpc/search-events?"+seri;
		jQuery.getJSON(url, function (data) { 

			var windowsize = data['windowsize'];
			var querylength = data['querylength'];
			eventClasses = new Object();
			var headrow = document.createElement("tr");
			$(headrow).append("<th>Document</th><th>Position</th>");
			for (var i = 0; i < data['windowsize']*2+querylength; i++) {
				cssClass = "";
				content = i-windowsize
				if (i < windowsize) {
					cssClass = "context cont" + -1*(i-windowsize);
					content = "";
				}
				if (i >= windowsize+querylength) {
					cssClass = "context cont" + (i-(windowsize+querylength)+1);
					content = "";
				}
				$(headrow).append("<th class=\""+cssClass+"\">"+content+"</th>");
			}
			$("#results > table").append(headrow);
			
			for (hitI in data['hits']) {
				var hit = data['hits'][hitI]['list'];
				var row = document.createElement("tr");
				$(row).append("<td>"+data['hits'][hitI]['document']+"</td><td>"+Math.floor(data['hits'][hitI]['position']*100)+"%</td>");
				for (evI in hit) {
					var event = hit[evI];
					
					cssClass = "";
					if (evI < windowsize)
						cssClass = "context cont" + -1*(evI-windowsize);
					else if (evI >= windowsize+querylength)
						cssClass = "context cont" + (evI-(windowsize+querylength)+1);
					else 
						cssClass= "hit";
					
					if (jQuery.isEmptyObject(event)) {		
						$(row).append('<td class="'+cssClass+'"></td>');
					} else {
						var cell = document.createElement("td"); 			
						$(cell).addClass(cssClass);
						$(cell).append(eventtable(event, eventClasses));					
						$(row).append(cell);
					} 				}
				$("#results > table").append(row);
				
			}
			$( ".context" ).hide();

			$( ".results-view" ).buttonset("enable");
			
			
			$( "#button-decrease-context" ).button("disable");

			
			
			
			$('#chart').highcharts({
	            chart: {
	                type: 'column'
	            },
	            title: {text:'Event Positions'},
	            series: data['chartdata'],
	            yAxis: {
	                title: {
	                    text: 'Number of events'
	                },
	                labels: {
	                    formatter: function() {
	                        return this.value;
	                    }
	                }
	            },
	            xAxis: {
	            	labels: {
	            		formatter: function() {
	            			var low = this.value*10-5;
	            			var high = this.value*10+5;
	                    	return (low<0?0:low) + '-'+(high>100?100:high)+'%';
	                	}
	            	}
	            },
	            plotOptions: {
	                column: {
	                	stacking: 'normal',
	                    marker: {enabled: false}
	                }
	            },
			});
			$("#loading").hide();

		});
	});
	$( "#submitbutton").button("enable");

  	
	$( "#button-search" ).click();

    
 
  });
  </script>
</body>
</html>