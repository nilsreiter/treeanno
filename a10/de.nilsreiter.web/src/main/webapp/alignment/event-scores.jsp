<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>		

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document ${documents[0].id}</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<LINK href="css/event-scores.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/highcharts.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />


</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<div class="menu">
<%@ include file="../common/document-menu.jsp" %>
</div>

<div class="content level3">
<jsp:include page="../common/menu.jsp">
	<jsp:param value="event-scores" name="active"/>
</jsp:include>

<div class="content level4">
<div class="menu">

<div>k = <input id="randomwalk_k" value="3" size="2"></input></div>
<div>n = <input id="randomwalk_n" value="20" size="2"></input></div>
<div id="randomwalk_redraw">Redraw</div>
</div>

<div class="content level5">
	<div id="loading"><img src="gfx/loading1.gif"/></div>
	<div id="chart_container"></div>
<script>

$( ".level4 .menu div").css("display", "inline-block");
$( ".level4 .menu div").css("margin-right", "10px");

$( "#randomwalk_k" ).spinner({min:1, max:10,step:1, value:3});
$( "#randomwalk_n" ).spinner({min:10, max:100,step:10, value:20});

$( "#randomwalk_redraw").button().click(function( event ) {
    event.preventDefault();
    var n = $("#randomwalk_n").spinner("value");
    var k = $("#randomwalk_k").spinner("value");
    redraw(n, k);
});

redraw(10, 5);

function redraw(n, k) {
	$("#loading").show();
	$("#chart_container").hide();
	
	
	jQuery.getJSON('rpc/get-event-scores?doc=${param.doc}&scale=scale&k='+k+'&n='+n, function (data) { 
		$("#loading").hide();
		$("#chart_container").show();
    	$('#chart_container').highcharts({
    	    chart: {
        	    type: 'spline',  
        	    zoomType: 'x'
        	},
        	title: {
        	    text: 'Event Scores'
        	},
        	xAxis: { type:'linear' },
       	 	yAxis: {
            	title: { text: 'Transitions' }
        	}, 
        	series: data['series'],
        	tooltip: {
                shared: true,
                crosshairs: true
            },
        	plotOptions: {
        		spline: {
                	dataLabels: { enabled: false },
            	}
        	},
    	});
	});
}
</script>
</div>
</div>
</div>
</div>
</div>
</body>
</html>