<%@page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>

<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
//첫실행
$(function(){
	//이중차트
	dualChart();
	
	$("#button").click(function(){
		var input = $("#selectBox option:selected").val();
	
		if(input==""){
			alert("구를 선택해주세요.");
			return;
		}
		getData(input);
	});
	
});

	//데이터 조회
	function getData(param){
		$.ajax({
			url:"/getData",
			type:"POST",
			dataType:'json',
			contentType : "application/json; charset=UTF-8",
			data: JSON.stringify(param),
			success: function(data){ 
				console.log(data);
			},
			error: function (xhr, ajaxOptions, thrownError) { 
				alert(xhr.status+' Error');
			}  
		});	
	}


	function dualChart(){
		//https://www.highcharts.com/demo/combo-dual-axes

		Highcharts.chart('dualChart', {
		  chart: {
		    //zoomType: 'xy' 마우스 드래그시 줌
		  },
		  title: {
		    text: ''
		  },
		  xAxis: [{
		    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
		    crosshair: true //호버시 불투명한 구분박스 쳐줌
		  }],
		  yAxis: [{ // Primary yAxis
		    title: {//선그래프 축제목
		      text: '선그래프',
		      style: {color: '#000000'}
		    },
		    labels: { //선그래프 y축
		      format: '{value}',
		      style: {color:'#000000'}
		    }
		  }, /* { // Secondary yAxis
		    title: { //막대그래프 축제목
		      text: '막대그래프', 
		      style: {color: '#008299'}
		    },
		    labels: { //막대그래프 y축
		      format: '{value}',
		      style: {color: '#008299'}
		    },
		    opposite: true //y축을 오른쪽에 둠
		  } */  ],
		  tooltip: {
		    shared: true //마우스 호버시 두개의 그래프의 정보가 같이보임
		  },
		  legend: {
		    layout: 'vertical',
		    align: 'right', //범례위치
		    verticalAlign: 'top', //범례상하 위치
		    x: -10,
		    y: 100,        //범례 상세위치 
		    floating: true,
		    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
		  },
		  series: [{
		    name: '막대그래프',
		    type: 'column',
		    yAxis: 0, //둘중 하나가 1일때 두 그래프 겹치고 2번째 그래프의 y축보임
		    data: [252,215,182,145,95,69,70] 
		  }, {
		    name: '선그래프',
		    type: 'spline',
		    yAxis: 0,
		    data: [356, 560, 440, 292, 164, 115, 99],
		    color: '#980000'
		  }]
		});
	}
</script>

<html>
<head>
	<title>Home</title>
</head>

<body>
<h1>
	서울시 평균 구름량 & 강수량 (2016.01 ~ 2018.12)
</h1>

<div style="padding-left: 100px; padding-bottom: 20px;">
	<select id="selectBox" style="height: 25px; width: 150px;">
		<option value="000000">서울시 선택</option>
		<option value="11110">종로구</option>
		<option value="11140">중구</option>
		<option value="11170">용산구</option>
		<option value="11120">성동구??</option>
		<option value="11230">동대문구</option>
		<option value="11260">중랑구</option>
		<option value="11290">성북구</option>
		<option value="11320">도봉구</option>
		<option value="11350">노원구</option>
		<option value="11380">은평구</option>
		<option value="11410">서대문구</option>
		<option value="11440">마포구</option>
		<option value="11470">양천구</option>
		<option value="11500">강서구</option>
		<option value="11530">구로구</option>
		<option value="11560">영등포구</option>
		<option value="11590">동작구</option>
		<option value="11620">관악구</option>
		<option value="11650">서초구</option>
		<option value="11680">강남구</option>
		<option value="11710">송파구</option>
		<option value="11740">강동구</option>
	</select>
	<input type="button" id="button" value="선택"/>
</div>



<div id="dualChart" style="border:1px gray solid; width: 1000px; height: 400px; margin: 0 auto"></div>















</body>
</html>
