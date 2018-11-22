<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript">

      google.charts.load('current', {packages:['corechart']});
      google.charts.setOnLoadCallback(drawStuff);

        function drawStuff() {
            
        	var jsonData = $.ajax({
                //url: "./columnChart.json?random"+(new Date()).getTime(),
                url: "./ThreadInfoServlet",
                dataType: "text",
                async: false
                }).responseText;

            var activeThreadCnt = Number(jsonData);
	      	//alert(activeThreadCnt);
            
          //var data = new google.visualization.DataTable(jsonData);
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'Country');
          data.addColumn('number', 'GDP');
          
        //  data.addRows([
         //   ['US', 16768100],
         //   ['China', 9181204],
         //   ['Japan', 4898532],
         //   ['Germany', 3730261],
         //   ['France', 1]
         // ]);

          data.addRows([
           ['Server', activeThreadCnt]
            ]);
        
         var options = {
           title: ' Monitoring WorkerThread ',
           width: 500,
          height: 300,
          legend: 'none',
           bar: {groupWidth: '95%'},
           vAxis: { gridlines: { count: 4 } }
         };

         var chart = new google.visualization.ColumnChart(document.getElementById('number_format_chart'));
         chart.draw(data, options);

         document.getElementById('format-select').onchange = function() {
           options['vAxis']['format'] = this.value;
           chart.draw(data, options);
        	 };
      };
      
      setInterval(function () { drawStuff() }, 2000);
    </script>
  </head>
  <body>
    <select id="format-select">
      <option value="" selected>none</option>
      <option value="decimal" >decimal</option>
      <option value="scientific">scientific</option>
      <option value="percent" >percent</option>
      <option value="currency">currency</option>
      <option value="short">short</option>
      <option value="long">long</option>
    </select>
    <div id="number_format_chart">
  </body>
</html>
