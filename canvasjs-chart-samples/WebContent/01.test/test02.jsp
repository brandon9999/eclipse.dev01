<!DOCTYPE html>
<html>
<head>
	<title>Google Chart Example</title>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="https://www.google.com/jsapi"></script>
	<script type="text/javascript">
	var example_url = 'https://api.github.com/repos/hadley/ggplot2/issues';
	function drawChart() {
        var options = {
              title: 'A Scatter Plot of User ID vs Issues Number of ggplot2 respository on GitHub',
              hAxis: {title: 'User ID',  titleTextStyle: {color: '#333'}},
              vAxis: {title: 'Issues Number'},
              animation: {duration: 20}
            };
        var chart = new google.visualization.ScatterChart(document.getElementById('google_chart_example'));
        function refreshData() {
          var jsonData = $.ajax({
            url: example_url,
            dataType: 'json',
          }).done(function (results) {     
        	   var data = new google.visualization.DataTable();
        	   data.addColumn('number', 'user_id');
        	   data.addColumn('number', 'issues_number');
        	   results.forEach(function(issue)  {  
          	   	data.addRow([ issue.user['id'], issue.number]);
          		  });
        	   chart.draw(data, options);
      		  });
          }
        refreshData();
        setInterval(refreshData, 6000);
      }
	google.load('visualization', '1', {packages: ['corechart']});  
    	google.setOnLoadCallback(drawChart);
	</script>

</head>
<body>
<div id='google_chart_example' style='width: 900px;height: 500px'> </div>
</body>
</html>