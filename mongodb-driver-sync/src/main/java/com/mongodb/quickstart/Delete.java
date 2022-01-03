package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

public class Delete {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase myDB = mongoClient.getDatabase("MyDatabase");
            MongoCollection<Document> usersCollection = myDB.getCollection("Users");
            MongoCollection<Document> picturesCollection = myDB.getCollection("Pictures");

            // delete one document
            Bson filter = eq("userId", 2);
            DeleteResult result = usersCollection.deleteOne(filter);
            System.out.println(result);
            
            //Bson filter = eq("pictureId", 2);
            //DeleteResult result = picturesCollection.deleteOne(filter);
            //System.out.println(result);


            // findOneAndDelete operation
         /* filter = eq("userId", 2);
            Document doc = usersCollection.findOneAndDelete(filter);
            System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));
            
            filter = eq("pictureId", 2);
            Document doc = picturesCollection.findOneAndDelete(filter);
            System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));


            // delete many documents
            filter = gte("userId", 2);
            result = usersCollection.deleteMany(filter);
            System.out.println(result);
            
            filter = gte("pictureId", 2);
            result = picturesCollection.deleteMany(filter);
            System.out.println(result);
            
            */

            // delete the entire collection and its metadata (indexes, chunk metadata, etc).
           usersCollection.drop();
           picturesCollection.drop();
        }
    }
}