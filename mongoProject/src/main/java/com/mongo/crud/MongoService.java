package com.mongo.crud;



import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service("mongoService")

public class MongoService {
	
	//DB���� (Ŭ���� ����Ʈ�� ������ ��)
	private MongoClient mongoClient = new MongoClient("35.233.246.20", 27017); //����
	private MongoDatabase database = mongoClient.getDatabase("bigdata");  //��� ��
	private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //���̺� ��
	
	
	
	public void select() throws Exception {
		//����̹� ���� 3.7.1 ���� ���� Ŭ������
		BasicDBObject query = new BasicDBObject();


	}
	
	
	
	
	
	
	
	
	
}
