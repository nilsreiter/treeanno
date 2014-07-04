<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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


</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<jsp:include page="menu.jsp">
	<jsp:param value="event-scores" name="active"/>
</jsp:include>

<div class="content level3">
	<div id="chart_container"></div>
<script>
jQuery.getJSON('rpc/get-event-scores?doc=${doc}&scale', function (data) { 
    $('#chart_container').highcharts({
        chart: {
            type: 'spline'
        },
        title: {
            text: 'Event Scores'
        },
        xAxis: {
        	type:'linear'
        },
        yAxis: {
            title: {
                text: 'Transitions'
            }
        },series: data['series'],
        plotOptions: {
            spline: {
                dataLabels: {
                    enabled: false
                },
            }
        },

    });
});

</script>
</div>
</div>
</div>
</body>
</html>