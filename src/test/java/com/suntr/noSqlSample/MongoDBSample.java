package com.suntr.noSqlSample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;

public class MongoDBSample {
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;

    @Before
    public void setUp(){
        try {
            mongoClient = new MongoClient("127.0.0.1", 27017);
            mongoDatabase = mongoClient.getDatabase("myMongo");
            System.out.println("mongoDb Connect Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetCollection(){
        mongoDatabase.createCollection("test");
        System.out.println("collection created successfully");
    }


}
