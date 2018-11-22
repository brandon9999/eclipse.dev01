<%@ page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    
<link rel="stylesheet" href="css/bootstrap.css">

<title>Insert title here</title>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.js"></script>
<script>
    var xmlhttp = new XMLHttpRequest();
	
	function searchFunction()
	{
		//xmlhttp.open("Post", "./UserInfoServlet?userName=" + encodeURIComponet(document.getElementById("userName").value), true);
		xmlhttp.open("Post", "./ThreadInfoServlet?userName=" + document.getElementById("userName").value, true);
		//xmlhttp.open("GET", "./data.json", true);
		xmlhttp.onreadystatechange=searchProcess;
		xmlhttp.send(null);
		//alert( request.responseText);
	}
	
	function searchProcess()
	{
		var table = document.getElementById("ajaxTable");
		table.innerHTML = "";
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			var object = eval('(' + xmlhttp.responseText + ')');
			var result = object.result;
			for(var i=0; i < result.length; i++)
			{
				var row = table.insertRow(0);
				for(var j=0; j < result[i].length; j++)
				{
					var cell = row.insertCell(j);
					cell.innerHTML = result[i][j].value;
				}
			}
		}
	}
	
    setInterval(searchFunction, 30000);

</script>

    <script type="text/javascript">
    
    // Load the Visualization API and the piechart package.
    google.charts.load('current', {'packages':['corechart']});
      
    // Set a callback to run when the Google Visualization API is loaded.
    google.charts.setOnLoadCallback(drawChart);
      
    function drawChart() {
      var jsonData = $.ajax({
          url: "sampleData.json",
          dataType: "json",
          async: false
          }).responseText;
          
      // Create our data table out of JSON data loaded from server.
      var data = new google.visualization.DataTable(jsonData);

      // Instantiate and draw our chart, passing in some options.
      var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
      chart.draw(data, {width: 400, height: 240});
    }

   
    setInterval(function () { drawChart() }, 3000);

    
    </script>


</head>

<body>
	<br>
	<div class="container">
		<div class="form-group row pull-right">
			<div class="col-xs-8">
				<input class="form-control" id="userName" onkeyup="searchFunction()" type="text" size="20">
			</div>	
			<div class="col-xs-2">
				<button class="btn btn-primary" onclick="searchFunction();" type="button">검색</button>
			</div>
		</div>	
		<table class="table" style="text-align: center; border: 1px solid #dddddd">
			<thead>
				<tr>
					<th style="background-color: #fafafa; text-align: center;">이름</th>
					<th style="background-color: #fafafa; text-align: center;">나이</th>
					<th style="background-color: #fafafa; text-align: center;">성별</th>
					<th style="background-color: #fafafa; text-align: center;">이메일</th>
			</thead>
			<tbody id="ajaxTable">
			</tbody>
		</table>
	</div>
	

    <!--Div that will hold the pie chart-->
    <div id="chart_div"></div>
  </body>	
	
</body>
</html>