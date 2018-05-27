package com.swj.ics.mongoUtil;

import java.util.List;
import com.mongodb.*;
import com.mongodb.util.JSON;

/**
 * Created by swj on 2017/11/1.
 */
public class MongoDemo1 {
    
    public static void main(String[] args) {
        //建立一个mongo数据库连接对象
        Mongo mongo = new Mongo("192.168.0.107:27017");
        List<String> dbNames =  mongo.getDatabaseNames();
        System.out.println(dbNames);

        DB db = mongo.getDB("test");
        
       /* for (String name : db.getCollectionNames()) {
            System.out.println("collectionName:" + name);
        }*/
        
        DBCollection persons = db.getCollection("persons");
        DBCursor cursor = persons.find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            System.out.println(object.get("name"));
        }
        System.out.println(cursor.count());
        System.out.println(cursor.getCursorId());
        System.out.println(JSON.serialize(cursor));
    }
    
    //打印结果如下
    /*
    * [admin, local, test]
collectionName:books
collectionName:depts
collectionName:fs.chunks
collectionName:fs.files
collectionName:goods
collectionName:myColl
collectionName:persons
collectionName:students
collectionName:user
    * */
}
