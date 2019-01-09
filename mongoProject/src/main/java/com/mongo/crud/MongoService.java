package com.mongo.crud;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Service("mongoService")

public class MongoService {
	
	//DB연결 (클래스 임포트에 주의할 것)
	private MongoClient mongoClient = new MongoClient("35.233.246.20", 27017); //서버
	private MongoDatabase database = mongoClient.getDatabase("bigdata");  //디비 명
	//private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //테이블 명
	private MongoCollection<Document> collection = database.getCollection("TB_RESULT3"); //테이블 명
	
	
	
	
	//맵리듀스 결과 조회
	public List<Map<String, Object>> getData(Integer param) throws Exception {
		
		//인티저로 받은 지역번호를 스트링으로 변경
		String no = param.toString();
		
		BasicDBObject query = new BasicDBObject(); 
		
		if(no.equals("0")) {
			no = "000000"; //구 검색
		}else {
			no = no;
		}
		
		//like 중간값 검색 %0000000%
		query.put("_id", Pattern.compile(no));
		
		//검색하여 cursor에 쿼리결과 저장
		MongoCursor <Document> cursor = collection.find(query).iterator(); 
		/*	{
		 * 		{_id:지역번호},
		 * 		{value:
		 * 			{MEAN_CLOUD:운량},
		 * 			{DAILY_PRECIPITAION:강수량},
		 * 			{REGION:지역명}
		 * 		}
		 * 	}
		 */
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			while(cursor.hasNext()){
				Map<String, Object> map = new HashMap<String,Object>();
				
				//출력값 저장
				Document document = cursor.next();
				
				//필요한 필드의 값만 추출
				Map<String, Object> values  = (Map<String, Object>) document.get("value");
				
				Double cloud = (Double) values.get("MEAN_CLOUD");
				String Precipitation  = values.get("DAILY_PRECIPITAION").toString();
				String region2 = (String) values.get("REGION2");
				String region3 = (String) values.get("REGION3");
				String id = (String) document.get("_id");
				
				if(!id.contains("NumberLong")) { //문자열을 포함하고 있지않을때 (서울 외 지역 아이디 여과)
					
					//동이 없을 때 (equals(null)은 안댐) 구 전체
					if(region3.isEmpty()) {
						System.out.println("!!!!!!!"+document.get("_id")+"!!!!!!!"+region2);
						map.put("_id", id); //key값 
						map.put("MEAN_CLOUD", cloud);
						map.put("DAILY_PRECIPITAION", Precipitation);
						map.put("REGION2", region2);
						map.put("REGION3", "");
						resultList.add(map);
						
					//동이 있을 때
					}else if(!region3.isEmpty()) {
						System.out.println("======="+document.get("_id")+"======"+region3);
						map.put("_id", id); //key값 
						map.put("MEAN_CLOUD", cloud);
						map.put("DAILY_PRECIPITAION", Precipitation);
						map.put("REGION2", region2);
						map.put("REGION3", region3);
						resultList.add(map);
					}
				}
			}
		}finally {
			cursor.close();
		}
		System.out.println(resultList);
		return resultList;
	}
	
	
	
	
	
	
	
	
	
	//CRUD select
	public void select() throws Exception {
		//드라이버 버전 3.7.1 부터 없는 클래스임
		BasicDBObject query = new BasicDBObject(); 
		
		//db.TB_BIGDATA.find({no:1})
		query.put("no", 1);
		
		//쿼리결과 저장
		//MongoCursor <Document> cursor = collection.find(query).iterator();
		MongoCursor <Document> cursor = collection.find().iterator(); // 조건없음
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			while(cursor.hasNext()){
				
				Map<String, Object> map = new HashMap<String,Object>();
			
				//필요한 필드의 값만 출력
				Document document = cursor.next();
				map.put("no", document.get("no"));
				
				resultList.add(map);
			}
		}finally {
			cursor.close();
		}
		
		System.out.println(resultList);
	}
	
	//CRUD insert
	public void insert() throws Exception {
/*		//1건 인설트
 * 		Document document = new Document();
		document.put("no", 4);
		document.put("gg", "ggg");
		collection.insertOne(document);*/
		
		//입력값 쿼리
		List<Document> documents = new ArrayList<Document>();
		for(int i=0;i<3;i++) {
			Document document = new Document();
			document.put("no", i+4);
			document.put("gg", "ggg");
			
			documents.add(document);
		}
		//인설트
		collection.insertMany(documents);
		
	}
	
	
	public void mapReduce() throws Exception {
		/**
		 * 몽고쉘에서 사용하는 맵리듀스 스크립트 붙여넣기 (MEAN_TEMPERATURE는 NaN나옴)
		 */
/*		
		>use bigdata; //컬렉션이 있는 디비로 접속

		//맵: 추출
		>var map = function (){
			var key = {};
			key.LOCATION_NO = this.LOCATION_NO; //지역코드 별
			
			var value = {};
			value.MAXIMUM_TEMPERATURE = this.MAXIMUM_TEMPERATURE;
			value.MEAN_TEMPERATURE = this.MEAN_TEMPERATURE;
			value.MINIMUM_TEMPERATURE = this.MINIMUM_TEMPERATURE;

			//리듀스로 key,value를 전달
			//emit(key,value); 

			//NaN값이 나오는걸 줄이려면 value의 형식과 리듀스 리턴필드의 형식(이름)이 같아야한다.
			emit(this.LOCATION_NO,{MAXIMUM_TEMPERATURE:this.MAXIMUM_TEMPERATURE, 
									MINIMUM_TEMPERATURE:this.MINIMUM_TEMPERATURE,
									MEAN_TEMPERATURE:this.MEAN_TEMPERATURE,
									REGION1:this.REGION1,REGION2:this.REGION2,REGION3:this.REGION3	
			});
		};

		//리듀스: 맵 결과 재정의
		>var reduce = function (key, value){
			var maxCount = 0;
			var maxTotal = 0;
			var maxAvg = 0;

			var meanCount = 0;
			var meanTatal = 0;
			var meanAvg = 0;

			var minCount = 0;
			var minTotal = 0;
			var minAvg = 0;
			
			//향상된 포문 가능 for(var i in value){}
			for(var i=0; i<value.length; i++){
				maxTotal += value[i].MAXIMUM_TEMPERATURE;
				meanTatal += value[i].MEAN_TEMPERATURE;
				minTotal += value[i].MINIMUM_TEMPERATURE;

				maxCount += 1; //맵에서 벨류로 count를 넣으면 위에와 같이 해야댐
				meanCount += 1; 
				minCount += 1; 
			}

			maxAvg = maxTotal/maxCount;
			meanAvg = meanTatal/meanCount;
			minAvg = minTotal/minCount;
			
			//0으로 하니까 언디파인드 떴음
			var region = value[1].REGION1+" "+value[1].REGION2+" "+value[1].REGION3; 

			//맵emit 의 벨류와 필드명을 같게한다.
			return {"MAXIMUM_TEMPERATURE":maxAvg,"MEAN_TEMPERATURE":meanAvg,
					"MINIMUM_TEMPERATURE":minAvg,"REGION":region
			};
		};

		//컬렉션에서 가져와서 맵리듀스 var 호출
		>db.TB_WEATHER.mapReduce(
			map,reduce, //맵과 리듀스의 변수명
			{
				out:"TB_RESULT" 
			}
		);

		>db.TB_RESULT.find({}); //결과데이터 확인
		
*/		
	}
	
}
