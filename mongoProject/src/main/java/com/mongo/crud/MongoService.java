package com.mongo.crud;



import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service("mongoService")

public class MongoService {
	
	//DB연결 (클래스 임포트에 주의할 것)
	private MongoClient mongoClient = new MongoClient("35.233.246.20", 27017); //서버
	private MongoDatabase database = mongoClient.getDatabase("bigdata");  //디비 명
	private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //테이블 명
	
	
	
	public void select() throws Exception {
		//드라이버 버전 3.7.1 부터 없는 클래스임
		BasicDBObject query = new BasicDBObject();


	}
	
	
	
	
	
	
	
	
	
}
