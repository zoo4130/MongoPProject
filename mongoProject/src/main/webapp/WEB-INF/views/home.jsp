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
	
	//초기데이터 조회
	getData("000000");
	
	$("#button").click(function(){
		var input = $("#selectBox option:selected").val();
	
		if(input==""){
			alert("구를 선택해주세요.");
			return;
		}
		getData(input);  //차트 삭제 안해도 다시그려지네
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
				//차트 데이터 생성
				makeArray(data);
			},
			error: function (xhr, ajaxOptions, thrownError) { 
				alert(xhr.status+' Error');
			}  
		});	
	}
	
	//차트 데이터 생성
	function makeArray(data){
		//리스트맵 받아서 각각 배열로 저장
		var regionArray=[];
		var cloudArray=[];
		var precipiArray=[];
		
		for(var i=0;i<data.length;i++){
			
			//동이 없는 경우
			if(data[i].REGION3==""){
				regionArray[i] = data[i].REGION2;
				cloudArray[i] = Number(Number(data[i].MEAN_CLOUD).toFixed(3)); //넘버로 변환하고 반올림 하고 다시 넘버
				precipiArray[i] = Number(Number(data[i].DAILY_PRECIPITAION).toFixed(3));
				
			//동이 있는 경우	
			}else if(data[i].REGION3!=""){
				regionArray[i] = data[i].REGION3;
				cloudArray[i] = Number(Number(data[i].MEAN_CLOUD).toFixed(3)); 
				precipiArray[i] = Number(Number(data[i].DAILY_PRECIPITAION).toFixed(3));
			}
		}
		
		//차트 실행
		dualChart(regionArray, cloudArray, precipiArray);
	}

	function dualChart(regionArray, cloudArray, precipiArray){
		//https://www.highcharts.com/demo/combo-dual-axes

		Highcharts.chart('dualChart', {
		  chart: {
		    //zoomType: 'xy' 마우스 드래그시 줌
		  },
		  title: {
		    text: ''
		  },
		  xAxis: [{
		    //categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
		    categories: regionArray,
		    crosshair: true //호버시 불투명한 구분박스 쳐줌
		  }],
		  yAxis: [{ // Primary yAxis
		    title: {//첫번째 그래프 축제목
		      text: '일일 강수량',
		      style: {color: '#008299'}
		    },
		    labels: { //y축
		      format: '{value} mm',
		      style: {color:'#008299'}
		    }
		  },{ // Secondary yAxis
		    title: { //두번째 그래프 축제목
		      text: '일평균 운량', 
		      style: {color: '#000000'}
		    },
		    labels: { //막대그래프 y축
		      format: '{value} %',
		      style: {color: '#000000'}
		    },
		    opposite: true //y축을 오른쪽에 둠
		  }   ],
		  tooltip: {
		    shared: true //마우스 호버시 두개의 그래프의 정보가 같이보임
		  },
		  legend: {
		    layout: 'vertical',
		    align: 'right', //범례위치
		    verticalAlign: 'top', //범례상하 위치
		    x: -60,    //범례 위치에 기반한 상세 조정
		    y: 5,      
		    floating: true,
		    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
		  },
		  series: [{
		    name: '일일 강수량', //막대그래프
		    type: 'column',
		    yAxis: 0, //y축의 위치 오른쪽0 왼쪽1. 두 그래프 겹치고 2번째 그래프의 y축보임(상단 yAxis설정 해야댐)
		    //data: [252,215,182,145,95,69,70] 
		    data: precipiArray,
		    tooltip: {
	            valueSuffix: ' mm'
	        }
		  }, {
		    name: '일평균 운량', //선그래프
		    type: 'spline',
		    yAxis: 1,
		    //data: [356, 560, 440, 292, 164, 115, 99],
		    data: cloudArray,
		    color: '#000000',
		    tooltip: {
	            valueSuffix: ' %'
	        }
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
	서울시 평균 운량 & 강수량 (2016.11.26 ~ 2019.1.3)
</h1>

<div style="padding-left: 100px; padding-bottom: 20px;">
	<select id="selectBox" style="height: 25px; width: 150px;">
		<option value="000000">서울시 선택</option>
		<option value="11110">종로구</option>
		<option value="11140">중구</option>
		<option value="11170">용산구</option>
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
	<input type="button" id="button" value="선택" style="height: 25px;"/>
</div>



<div id="dualChart" style="border:1px gray solid; width: 1200px; height: 400px; margin-left: 100px;]]"></div>















</body>
</html>
