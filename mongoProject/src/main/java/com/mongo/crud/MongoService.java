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
	
	//DB���� (Ŭ���� ����Ʈ�� ������ ��)
	private MongoClient mongoClient = new MongoClient("35.233.246.20", 27017); //����
	private MongoDatabase database = mongoClient.getDatabase("bigdata");  //��� ��
	private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //���̺� ��
	
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	
	public void select() throws Exception {
		//����̹� ���� 3.7.1 ���� ���� Ŭ������
		BasicDBObject query = new BasicDBObject(); 
		
		//db.TB_BIGDATA.find({no:1})
		query.put("no", 1);
		
		//������� ����
		//MongoCursor <Document> cursor = collection.find(query).iterator();
		MongoCursor <Document> cursor = collection.find().iterator(); // ���Ǿ���
		
		try {
			while(cursor.hasNext()){
				
				Map<String, Object> map = new HashMap<String,Object>();
			
				//�ʿ��� �ʵ��� ���� ���
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
/*		//1�� �μ�Ʈ
 * 		Document document = new Document();
		document.put("no", 4);
		document.put("gg", "ggg");
		collection.insertOne(document);*/
		
		//�Է°� ����
		List<Document> documents = new ArrayList<Document>();
		for(int i=0;i<3;i++) {
			Document document = new Document();
			document.put("no", i+4);
			document.put("gg", "ggg");
			
			documents.add(document);
		}
		//�μ�Ʈ
		collection.insertMany(documents);
	}
	
	
	
	
	
	
}
