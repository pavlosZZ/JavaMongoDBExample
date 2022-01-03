package com.mongodb.quickstart;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class Read {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
        	MongoDatabase myDB = mongoClient.getDatabase("MyDatabase");
        	MongoCollection<Document> usersCollection = myDB.getCollection("Users");
        	MongoCollection<Document> picturesCollection = myDB.getCollection("Pictures");


            // find one document with new Document
            findUserById(usersCollection);
            findPictureById(picturesCollection);

            // find one document with Filters.eq()
            findAUserWithFilter(usersCollection);
            findAPictureWithFilter(picturesCollection);

            // find a list of documents and iterate throw it using an iterator.
            findIterator(picturesCollection);
            

            // find a list of documents and use a List object instead of an iterator
            findList(usersCollection);
            

            // find a list of documents and print using a consumer
            findCostumer(picturesCollection);
            

            // find a list of documents with sort, skip, limit and projection
            findListSkipSort(usersCollection);
            
        }
    }

	private static void findListSkipSort(MongoCollection<Document> usersCollection) {
		List<Document> docs = usersCollection.find(and(eq("userId", 2), gte("pictureId", 5)))
                .projection(fields(excludeId(), include("pictureId", "userId")))
                .sort(descending("pictureId"))
                .skip(2)
                .limit(2)
                .into(new ArrayList<>());
		System.out.println("User sorted, skipped, limited and projected: ");
		for (Document user : docs) {
			System.out.println(user.toJson());
			}
		}

	private static void findCostumer(MongoCollection<Document> picturesCollection) {
		System.out.println("Picture list using a Consumer:");
        Consumer<Document> printConsumer = document -> System.out.println(document.toJson());
        picturesCollection.find(gte("pictureId", 1)).forEach(printConsumer);
		
	}

	private static void findList(MongoCollection<Document> usersCollection) {
		List<Document> usersList = usersCollection.find(gte("userId", 1)).into(new ArrayList<>());
        System.out.println("Users list with an ArrayList:");
        for (Document user : usersList) {
            System.out.println(user.toJson());
        }
		
	}

	private static void findIterator(MongoCollection<Document> picturesCollection) {
		FindIterable<Document> iterable = picturesCollection.find(gte("pictureId", 1));
        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println("Picture list with a cursor: ");
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }
		
	}

	private static void findAUserWithFilter(MongoCollection<Document> usersCollection) {

        Document user2 = usersCollection.find(eq("userId", 2)).first();
        System.out.println("User 2: " + user2.toJson());
		
	}

	private static void findUserById(MongoCollection<Document> usersCollection) {
		Document user1 = usersCollection.find(new Document("userId", 1)).first();
        System.out.println("User 1: " + user1.toJson());
		
	}
	private static void findAPictureWithFilter(MongoCollection<Document> picturesCollection) {

        Document picture2 = picturesCollection.find(eq("pictureId", 2)).first();
        System.out.println("Picture 2: " + picture2.toJson());
		
	}

	private static void findPictureById(MongoCollection<Document> picturesCollection) {
		Document picture2 = picturesCollection.find(new Document("pictureId", 1)).first();
        System.out.println("Picture 1: " + picture2.toJson());
		
	}
}