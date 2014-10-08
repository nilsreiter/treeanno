<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true" import="java.util.*, de.uniheidelberg.cl.a10.data2.*"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Similarities</title>
<LINK href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script src="js/event-search.js" type="text/javascript"></script>
<script src='js/jcanvas.min.js'></script>

<script src="js/jquery-ui/jquery-ui.min.js"></script>
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<script src="js/highcharts.js" type="text/javascript"></script>
<script src="js/modules/heatmap.js" type="text/javascript"></script>
</head>
<body>
<div class="level1">
<%@ include file="../menu/area.jsp" %>

<div class="content level2">
<%@ include file="../common/document-menu.jsp" %>

<div class="content level3">
<%@include file="../common/menu.jsp" %>

<div class="content level4">
<div class="menu">
 <input type="checkbox" id="showValuesButton" name="showValuesButton" value="1" checked="checked">
 <label for="showValuesButton">Show Values</label>
</div>
<div class="content level5">
<div id="container"></div>
<script>
 loadSimilarities();

$('#showValuesButton').button();
$('#showValuesButton').on("click",function () {
	loadSimilarities();
});


function loadSimilarities() {
jQuery.getJSON('rpc/get-document-similarities?doc=${param.doc}', function (data) { 

    $('#container').highcharts({

        chart: {
            type: 'heatmap',
            marginTop: 40,
            marginBottom: 40
        },


        title: {
            text: 'Similarity of Documents'
        },

        xAxis: {
            categories: data['list']
        },

        yAxis: {
            categories: data['list'],
            title: null
        },

        colorAxis: {
            min: 0,
            minColor: '#FFFFFF',
            maxColor: Highcharts.getOptions().colors[0]
        },

        legend: {
            align: 'right',
            layout: 'vertical',
            margin: 0,
            verticalAlign: 'top',
            y: 25,
            symbolHeight: 320
        },

        tooltip: {
            formatter: function () {
                //return '<b>' + this.series.xAxis.categories[this.point.x] + '</b> sold <br><b>' +
                //    this.point.value + '</b> items on <br><b>' + this.series.yAxis.categories[this.point.y] + '</b>';
            }
        },

        series: [{
            name: 'Sales per employee',
            borderWidth: 1,
            data: data['data'],
            dataLabels: {
                enabled: $("#showValuesButton").val() == 1,
                color: 'black',
                format: '{point.value:.2f}',
                style: {
                    textShadow: 'none',
                    HcTextStroke: null
                }
            }
        }]

    });
})
};
</script>

</div>
</div>
</div>
</div>
</div>
</body>
</html>