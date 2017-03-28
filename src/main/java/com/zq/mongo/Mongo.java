package com.zq.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ResourceBundle;

/**
 * Created by zhangqiang on 2017/3/22.
 */
public class Mongo {

    private static ResourceBundle bundle = ResourceBundle.getBundle("mongo");
    private static MongoClient mongoClient;


    public static MongoCollection<Document> getCollection(String collectionName) {
        mongoClient = new MongoClient(bundle.getString("ip"), Integer.parseInt(bundle.getString("port")));
        MongoDatabase database = mongoClient.getDatabase(bundle.getString("db"));
        return database.getCollection(collectionName);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
