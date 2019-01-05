package com.mongo.crud;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //테이블 명
	
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	
	public void select() throws Exception {
		//드라이버 버전 3.7.1 부터 없는 클래스임
		BasicDBObject query = new BasicDBObject(); 
		
		//db.TB_BIGDATA.find({no:1})
		query.put("no", 1);
		
		//쿼리결과 저장
		//MongoCursor <Document> cursor = collection.find(query).iterator();
		MongoCursor <Document> cursor = collection.find().iterator(); // 조건없음
		
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
	
	
	
	
	
	
}
