package com.swj.ics.mongoUtil;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.*;
import org.bson.types.ObjectId;

/**
 * Created by swj on 2017/11/1.
 * 展示mongo的增删改查。其中的查老费劲了。
 */
public class MongoCRUDDemo2 {
    static Mongo mongoClient = null;
    static DB mongoDb = null;
    static  {
        mongoClient = new Mongo("192.168.0.107:27017");
        mongoDb = mongoClient.getDB("test");
    }
    static DBObject createCollection(String tableName) {
        DBObject dbObject = new BasicDBObject();
        mongoDb.createCollection(tableName,dbObject);
        return dbObject;
    }
    
    static void insertData(DBObject dbObject,String tableName) {
        DBCollection collection = mongoDb.getCollection(tableName);
        collection.insert(dbObject);
        System.out.println("insert done !");
    }
    
    //批量插入数据
    
    static void insertDataBatch(List<DBObject> dbObjectList,String tableName) {
        DBCollection collection = mongoDb.getCollection(tableName);
        collection.insert(dbObjectList);
        System.out.println("insert batch done !");
    }
    
    static int deleteById(String id,String tableName) {
        DBCollection collection = mongoDb.getCollection(tableName);
         //Mongodb的Id 不是简单的字符是，而是 ObjectId("59f9e7b3f7076e1b7c9e619c")这种形式。
        //因此下面的写法是错误的
        //DBObject item = new BasicDBObject("_id",id);
        
        //正确的写法如下
        DBObject item = new BasicDBObject("_id",new ObjectId(id));
        WriteResult result = collection.remove(item);
        return result.getN();
    }
    
    
    static int deleteByDbs(DBObject dbs,String tableName) {
        DBCollection collection = mongoDb.getCollection(tableName);
        WriteResult result = collection.remove(dbs);
        if (result.wasAcknowledged()) {
            return result.getN();
        }
         return 0;
    }
    
    
    public static void main(String[] args) {
        //创建一个名字叫javaDb的数据表
        String tableName = "javadb";
       /* DBObject dbObject = createCollection(tableName);
        // 为javadb 添加一条数据
        dbObject.put("name","swj");
        dbObject.put("age",30);
        List<String> books = new ArrayList<String>(){{
            add("EXTJS");
            add("MONGODB");
        }};
        dbObject.put("books",books);*/
        
        // insertData(dbObject,tableName);
        
        // 批量插入数据
        DBObject jim = new BasicDBObject("name","jim");
       // DBObject swj = new BasicDBObject("name","swj");
        List<DBObject> dbObjectList = new ArrayList<DBObject>(){{
           add(jim);
          // add(swj);
        }};
         //insertDataBatch(dbObjectList,tableName);
        
        //根据ID删除数据
        String itemId = "59f9e7b3f7076e1b7c9e619c";
      //  deleteById(itemId,tableName);
        
        //根据条件删除
        //DBObject dbObject = new BasicDBObject("name","jim");
        DBObject dbObject = new BasicDBObject();
        dbObject.put("name","jim");
        /*int rows = deleteByDbs(dbObject,tableName);
        System.out.println("根据条件删除，受影响的行数为：" + rows);*/
                 
    }
    
    
}
