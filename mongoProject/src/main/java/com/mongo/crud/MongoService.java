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
	
	//DB���� (Ŭ���� ����Ʈ�� ������ ��)
	private MongoClient mongoClient = new MongoClient("35.233.246.20", 27017); //����
	private MongoDatabase database = mongoClient.getDatabase("bigdata");  //��� ��
	//private MongoCollection<Document> collection = database.getCollection("TB_BIGDATA"); //���̺� ��
	private MongoCollection<Document> collection = database.getCollection("TB_RESULT3"); //���̺� ��
	
	
	
	
	//�ʸ��ེ ��� ��ȸ
	public List<Map<String, Object>> getData(Integer param) throws Exception {
		
		//��Ƽ���� ���� ������ȣ�� ��Ʈ������ ����
		String no = param.toString();
		
		BasicDBObject query = new BasicDBObject(); 
		
		if(no.equals("0")) {
			no = "000000"; //�� �˻�
		}else {
			no = no;
		}
		
		//like �߰��� �˻� %0000000%
		query.put("_id", Pattern.compile(no));
		
		//�˻��Ͽ� cursor�� ������� ����
		MongoCursor <Document> cursor = collection.find(query).iterator(); 
		/*	{
		 * 		{_id:������ȣ},
		 * 		{value:
		 * 			{MEAN_CLOUD:�},
		 * 			{DAILY_PRECIPITAION:������},
		 * 			{REGION:������}
		 * 		}
		 * 	}
		 */
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); //�������� ����ȴ�. �˻��ʱ�ȭ���ȴ�
		try {
			while(cursor.hasNext()){
				Map<String, Object> map = new HashMap<String,Object>(); //�ݺ��� �ȿ� ������ ���ο� �����Ͱ� �������� 
				
				//��°� ����
				Document document = cursor.next();
				
				//�ʿ��� �ʵ��� ���� ����
				Map<String, Object> values  = (Map<String, Object>) document.get("value");
				
				Double cloud = (Double) values.get("MEAN_CLOUD");
				String Precipitation  = values.get("DAILY_PRECIPITAION").toString();
				String region2 = (String) values.get("REGION2");
				String region3 = (String) values.get("REGION3");
				String id = (String) document.get("_id");
				
				if(!id.contains("NumberLong")) { //���ڿ��� �����ϰ� ���������� (���� �� ���� ���̵� ����)
					
					//���� ���� �� (equals(null)�� �ȴ�) �� ��ü
					if(region3.isEmpty()) {
						System.out.println("!!!!!!!"+document.get("_id")+"!!!!!!!"+region2);
						map.put("_id", id); //key�� 
						map.put("MEAN_CLOUD", cloud);
						map.put("DAILY_PRECIPITAION", Precipitation);
						map.put("REGION2", region2);
						map.put("REGION3", "");
						resultList.add(map);
						
					//���� ���� ��
					}else if(!region3.isEmpty()) {
						System.out.println("======="+document.get("_id")+"======"+region3);
						map.put("_id", id); //key�� 
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
		//����̹� ���� 3.7.1 ���� ���� Ŭ������
		BasicDBObject query = new BasicDBObject(); 
		
		//db.TB_BIGDATA.find({no:1})
		query.put("no", 1);
		
		//������� ����
		//MongoCursor <Document> cursor = collection.find(query).iterator();
		MongoCursor <Document> cursor = collection.find().iterator(); // ���Ǿ���
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
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
	
	//CRUD insert
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
	
	
	public void mapReduce() throws Exception {
		/**
		 * �������� ����ϴ� �ʸ��ེ ��ũ��Ʈ �ٿ��ֱ� (MEAN_TEMPERATURE�� NaN����)
		 */
/*		
		>use bigdata; //�÷����� �ִ� ���� ����

		//��: ����
		>var map = function (){
			var key = {};
			key.LOCATION_NO = this.LOCATION_NO; //�����ڵ� ��
			
			var value = {};
			value.MAXIMUM_TEMPERATURE = this.MAXIMUM_TEMPERATURE;
			value.MEAN_TEMPERATURE = this.MEAN_TEMPERATURE;
			value.MINIMUM_TEMPERATURE = this.MINIMUM_TEMPERATURE;

			//���ེ�� key,value�� ����
			//emit(key,value); 

			//NaN���� �����°� ���̷��� value�� ���İ� ���ེ �����ʵ��� ����(�̸�)�� ���ƾ��Ѵ�.
			emit(this.LOCATION_NO,{MAXIMUM_TEMPERATURE:this.MAXIMUM_TEMPERATURE, 
									MINIMUM_TEMPERATURE:this.MINIMUM_TEMPERATURE,
									MEAN_TEMPERATURE:this.MEAN_TEMPERATURE,
									REGION1:this.REGION1,REGION2:this.REGION2,REGION3:this.REGION3	
			});
		};

		//���ེ: �� ��� ������
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
			
			//���� ���� ���� for(var i in value){}
			for(var i=0; i<value.length; i++){
				maxTotal += value[i].MAXIMUM_TEMPERATURE;
				meanTatal += value[i].MEAN_TEMPERATURE;
				minTotal += value[i].MINIMUM_TEMPERATURE;

				maxCount += 1; //�ʿ��� ������ count�� ������ ������ ���� �ؾߴ�
				meanCount += 1; 
				minCount += 1; 
			}

			maxAvg = maxTotal/maxCount;
			meanAvg = meanTatal/meanCount;
			minAvg = minTotal/minCount;
			
			//0���� �ϴϱ� ������ε� ����
			var region = value[1].REGION1+" "+value[1].REGION2+" "+value[1].REGION3; 

			//��emit �� ������ �ʵ���� �����Ѵ�.
			return {"MAXIMUM_TEMPERATURE":maxAvg,"MEAN_TEMPERATURE":meanAvg,
					"MINIMUM_TEMPERATURE":minAvg,"REGION":region
			};
		};

		//�÷��ǿ��� �����ͼ� �ʸ��ེ var ȣ��
		>db.TB_WEATHER.mapReduce(
			map,reduce, //�ʰ� ���ེ�� ������
			{
				out:"TB_RESULT" 
			}
		);

		>db.TB_RESULT.find({}); //��������� Ȯ��
		
*/		
	}
	
}
