package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class Update {

    public static void main(String[] args) {
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase myDB = mongoClient.getDatabase("MyDatabase");
            MongoCollection<Document> usersCollection = myDB.getCollection("Users");
            MongoCollection<Document> picturesCollection = myDB.getCollection("Pictures");

            // update one document
           // Bson filter = eq("userId", 1);
            //Bson updateOperation = set("LastName", "Zotos");
            //updateOneDoc(filter, updateOperation, usersCollection, prettyPrint);

            // upsert
            for(int i =0;i<10;i++) {
            	Bson filter = and(eq("userId", i), eq("Name", "New fName"));
                Bson updateOperation = push("Users",eq("Email"));
                upsertOneUser(filter, updateOperation, usersCollection, prettyPrint);
            }
            

            // update many documents
            //filter = eq("userId", 1);
            //updateManyDocs(filter, usersCollection, updateOperation);

            // findOneAndUpdate
            //filter = eq("userId", 1);
            //findOneUserAndUpdate(filter, usersCollection, prettyPrint);

            
         // update one document
            //filter = eq("pictureId", 1);
            //Bson picupdateOperation = set("PicturePath", "C:/UpdatedPath");
            //updateOneDoc(filter, picupdateOperation, picturesCollection, prettyPrint);

            // upsert
            //filter = and(eq("userId", 2d), eq("FirstName", "Pavlos"));
            //updateOperation = push("Picture", eq("PicturePath","C:/User/file.jpg"));
            //upsertOneUser(filter, updateOperation, usersCollection, prettyPrint);

            // update many documents
            //filter = eq("pictureId", 2);
            //updateManyDocs(filter, picturesCollection, updateOperation);

            // findOneAndUpdate
            //filter = eq("pictureId", 3);
            //findOnePictureAndUpdate(filter, picturesCollection, prettyPrint);
            
        }
    }
    
    private static void updateOneDoc(Bson filter, Bson updateOperation, MongoCollection<Document> docsCollection, JsonWriterSettings prettyPrint)
    {
    	 UpdateResult updateResult = docsCollection.updateOne(filter, updateOperation);
         System.out.println("=> Updating the doc with {\"Id\":1}.");
         System.out.println(docsCollection.find(filter).first().toJson(prettyPrint));
         System.out.println(updateResult);
    }
    
    private static void upsertOneUser(Bson filter, Bson updateOperation, MongoCollection<Document> usersCollection, JsonWriterSettings prettyPrint)
    {
    	UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult updateResult = usersCollection.updateOne(filter, updateOperation, options);
        System.out.println("\n=> Upsert document with {\"Id\":2, \"PictureId\": 102} because it doesn't exist yet.");
        System.out.println(updateResult);
        System.out.println(usersCollection.find(filter).first().toJson(prettyPrint));
    }
    
    private static void updateManyDocs(Bson filter, MongoCollection<Document> docsCollection, Bson updateOperation)
    {
    	UpdateResult updateResult = docsCollection.updateMany(filter, updateOperation);
        System.out.println("\n=> Updating all the documents with {\"Id\":1}.");
        System.out.println(updateResult);
    }
    
    private static void findOnePictureAndUpdate(Bson filter, MongoCollection<Document> picturesCollection, JsonWriterSettings prettyPrint)
    {
    	Bson update1 = inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
        Bson update2 = rename("PicturePath", "Path"); // rename variable "class_id" in "new_class_id".
        Bson update3 = mul("scores.0.score", 2); // multiply the first score in the array by 2.
        Bson update4 = addToSet("comments", "This comment is uniq"); // creating an array with a comment.
        Bson update5 = addToSet("comments", "This comment is uniq"); // using addToSet so no effect.
        Bson updates = combine(update1, update2, update3, update4, update5);
        // returns the old version of the document before the update.
        Document oldVersion = picturesCollection.findOneAndUpdate(filter, updates);
        System.out.println("\n=> FindOneAndUpdate operation. Printing the old version by default:");
        System.out.println(oldVersion.toJson(prettyPrint));
     // but I can also request the new version
        filter = eq("pictureId", 3);
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Document newVersion = picturesCollection.findOneAndUpdate(filter, updates, optionAfter);
        System.out.println("\n=> FindOneAndUpdate operation. But we can also ask for the new version of the doc:");
        System.out.println(newVersion.toJson(prettyPrint));
    }
    
    private static void findOneUserAndUpdate(Bson filter, MongoCollection<Document> usersCollection, JsonWriterSettings prettyPrint)
    {
    	Bson update1 = inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
        Bson update2 = rename("FirstName", "Name"); // rename variable "class_id" in "new_class_id".
        Bson update3 = mul("scores.0.score", 2); // multiply the first score in the array by 2.
        Bson update4 = addToSet("comments", "This comment is uniq"); // creating an array with a comment.
        Bson update5 = addToSet("comments", "This comment is uniq"); // using addToSet so no effect.
        Bson updates = combine(update1, update2, update3, update4, update5);
        // returns the old version of the document before the update.
        Document oldVersion = usersCollection.findOneAndUpdate(filter, updates);
        System.out.println("\n=> FindOneAndUpdate operation. Printing the old version by default:");
        System.out.println(oldVersion.toJson(prettyPrint));
     // but I can also request the new version
        filter = eq("userId", 1);
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Document newVersion = usersCollection.findOneAndUpdate(filter, updates, optionAfter);
        System.out.println("\n=> FindOneAndUpdate operation. But we can also ask for the new version of the doc:");
        System.out.println(newVersion.toJson(prettyPrint));
    }
}