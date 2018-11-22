<script type="text/javascript" src="http://www.google.com/jsapi"></script>


<script type="text/javascript">
   
    google.load('visualization', '1', {packages: ['corechart'],'language':'ko'});
   
    function drawVisualization(dataFromAjax) {
         var data = google.visualization.arrayToDataTable(dataFromAjax);

         new google.visualization.ColumnChart(document.getElementById('map')).
         draw(data, {fontName: "맑은 고딕, Malgon Gothic",
                fontSize: 11,
                forceIFrame: false,
                vAxis: {maxValue: 100}}
           );
    }
   
    function drawInit()
    {
         var data = null;
         var table_data = null;


         $.ajax({
             url:'data.jsp',
             data: {},
             success: function(res) {
                 table_data = eval("(" + res + ")");
                 drawVisualization(table_data);
             }
        });
    }
   
    google.setOnLoadCallback(drawInit);
 
    setInterval(function() { drawInit(); }, 3000);
</script>

<div id="map"></div>