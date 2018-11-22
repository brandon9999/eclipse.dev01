<%@ page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
   
    google.load('visualization', '1', {packages: ['corechart'],'language':'ko'});
    
    function drawVisualization(dataFromAjax) {
       var data_t = google.visualization.arrayToDataTable(dataFromAjax);
       var option_t = {
           title:'온도 그래프',
           backgroundColor:{stroke:'#99FF33'}, 
           colors:['red','#004411'],
           vAxis: {title:'단위 ℃',maxValue:35,minValue:15},
           hAxis: {title:'18~25℃ 적정'}
        }; 
        var chart_t = new google.visualization.LineChart(document.getElementById('chart_div_t'));  


﻿        chart_t.draw(data_t, option_t);  

   }
 function drawInit()
 {
       var data = null;
       var table_data = null; 

       $.ajax({
             //url:'data_read.jsp', // db읽어올 jsp파일
             url:'gchart02.jsp',
             data: {},
             success: function(res) {
                 table_data = eval("(" + res + ")");
                 drawVisualization(table_data);
             }
        });
 }
    
 google.setOnLoadCallback(drawInit);
 
 setInterval(function() { drawInit(); }, 3000);
</script> ﻿﻿

<div id="chart_div_t" style="width: 320px; height: 210px;"></div> 