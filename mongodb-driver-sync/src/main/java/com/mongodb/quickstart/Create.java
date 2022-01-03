package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class Create {

    //private static final Random rand = new Random();

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {

            MongoDatabase myDB = mongoClient.getDatabase("MyDatabase");
            MongoCollection<Document> usersCollection = myDB.getCollection("Users");
            MongoCollection<Document> picturesCollection = myDB.getCollection("Pictures");

            
            //insertOneUser(usersCollection); // By default values for userId :1 and pictureId : 1
            insertManyUsers(usersCollection, picturesCollection);
            //insertOnePicture(picturesCollection); // By default value for pictureId : 1
            //insertManyPictures(picturesCollection);
        }
    }

    private static void insertOneUser(MongoCollection<Document> usersCollection) {
        usersCollection.insertOne(generateNewUser(1, "User","User","Member",generateNewPicture(1, "C:/NewPath")));
        System.out.println("One user inserted for userId 2.");
    }
    
    private static void insertOnePicture(MongoCollection<Document> picturesCollection) {
        picturesCollection.insertOne(generateNewPicture(1, "C:/..."));
        System.out.println("One picture inserted for pictureId 1.");
    }

    private static void insertManyUsers(MongoCollection<Document> usersCollection, MongoCollection<Document> picturesCollection) {
        List<Document> users = new ArrayList<>();
        for (double userId = 1d; userId <= 10d; userId++) {
        	Document pic = generateNewPicture(userId, "C:/User/file.jpg");
            users.add(generateNewUser(userId, "New fName", "New lName", "New role", pic ));
            picturesCollection.insertOne(pic);
        
        }
        usersCollection.insertMany(users, new InsertManyOptions().ordered(false));
        System.out.println("Ten users inserted.");
    }
    
    private static void insertManyPictures(MongoCollection<Document> picturesCollection) {
        List<Document> pics = new ArrayList<>();
        for (double pictureId = 1d; pictureId <= 10d; pictureId++) {
            pics.add(generateNewPicture(pictureId, "Pic-Path"));
        
        }
        picturesCollection.insertMany(pics, new InsertManyOptions().ordered(false));
        System.out.println("Ten pictures inserted.");
    }

    private static Document generateNewUser(double userId, String firstName, String lastName, String role, Document Picture) {
        return new Document("_id", new ObjectId()).append("userId", userId)
                                                  .append("FirstName", firstName)
                                                  .append("LastName", lastName)
                                                  .append("Role", role)
                                                  .append("Picture", Picture);
    }
    
    private static Document generateNewPicture(double pictureId, String pic_path) {
        return new Document("_id", new ObjectId()).append("pictureId", pictureId)
                                                  .append("PicturePath", pic_path);
    }
}