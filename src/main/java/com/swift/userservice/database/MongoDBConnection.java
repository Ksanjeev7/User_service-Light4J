package com.swift.userservice.database;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

	private static final String CONNECTION_STRING = "mongodb://localhost:27017";
	private static final String DATABASE_NAME = "user";
	private static final String COLLECTION_NAME = "swift";

	private static MongoDatabase database;
	private static MongoCollection<Document> userCollection;


	static {
		MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
		database = mongoClient.getDatabase(DATABASE_NAME);
		userCollection = database.getCollection(COLLECTION_NAME);

		// Check if the database is empty, then fetch and store users
		if (userCollection.countDocuments() == 0) {
			System.out.println("No users found in DB. Fetching from JSONPlaceholder...");
			fetchAndStoreUsers();
		}
	}

	public static MongoDatabase getDatabase() {
		return database;
	}

	public static MongoCollection<Document> getUserCollection() {
		return userCollection;
	}

	//Fetch users from JSONPlaceholder and store them in MongoDB
	private static void fetchAndStoreUsers() {
		try {
			// Check if users are already loaded to avoid duplicates
			if (userCollection.countDocuments() > 0) {
//				System.out.println("Users already exist in MongoDB..");
				return;
			}
			URL url = new URL("https://jsonplaceholder.typicode.com/users");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed to fetch users: HTTP error code " + conn.getResponseCode());
			}

			Scanner scanner = new Scanner(url.openStream());
			StringBuilder jsonStr = new StringBuilder();
			while (scanner.hasNext()) {
				jsonStr.append(scanner.nextLine());
			}
			scanner.close();

			// Parse JSON response
			JSONArray usersArray = new JSONArray(jsonStr.toString());
			for (int i = 0; i < usersArray.length(); i++) {
				JSONObject userJson = usersArray.getJSONObject(i);
				// Convert address & company into proper nested Documents
	            Document userDoc = new Document("id", userJson.getInt("id"))
	                .append("name", userJson.getString("name"))
	                .append("username", userJson.getString("username"))
	                .append("email", userJson.getString("email"))
	                .append("address", convertJsonToDocument(userJson.getJSONObject("address")))  
	                .append("phone", userJson.getString("phone"))
	                .append("website", userJson.getString("website"))
	                .append("company", convertJsonToDocument(userJson.getJSONObject("company"))); 

						userCollection.insertOne(userDoc);
			}
//			System.out.println("Users successfully fetched and stored in MongoDB.");
		} catch (IOException e) {
			System.err.println("Error fetching users: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Convert JSON to MongoDB Document
	private static Document convertJsonToDocument(JSONObject jsonObj) {
	    Document doc = new Document();
	    for (String key : jsonObj.keySet()) {
	        Object value = jsonObj.get(key);
	        
	        // Fix: Store JSON objects correctly as sub-documents, not as Strings
	        if (value instanceof JSONObject) {
	            doc.append(key, convertJsonToDocument((JSONObject) value));
	        } else if (value instanceof JSONArray) { // Handle arrays if needed
	            List<Document> list = new ArrayList<>();
	            JSONArray jsonArray = (JSONArray) value;
	            for (int i = 0; i < jsonArray.length(); i++) {
	                list.add(convertJsonToDocument(jsonArray.getJSONObject(i)));
	            }
	            doc.append(key, list);
	        } else {
	            doc.append(key, value); // Store normally
	        }
	    }
	    return doc;
}
}
