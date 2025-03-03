package com.swift.userservice.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.swift.userservice.database.MongoDBConnection;
import com.swift.userservice.model.Address;
import com.swift.userservice.model.Company;
import com.swift.userservice.model.Geo;
import com.swift.userservice.model.User;

public class UserRepository {
	
	 private final MongoCollection<Document> userCollection;

	    public UserRepository() {
	        MongoDatabase database = MongoDBConnection.getDatabase();
	        
	        this.userCollection = database.getCollection("swift");
//	        System.out.println("My Collection: "+ userCollection.toString());
	    }

	    public Optional<User> findById(int userId) {
	    	 Document doc = userCollection.find(Filters.eq("id", userId)).first();

	    	    if (doc != null) {
//	    	        System.out.println("Fetched from DB: " + doc.toJson());  // DEBUG OUTPUT
	    	        return Optional.of(convertDocumentToUser(doc));
	    	    } else {
//	    	        System.out.println("User not found in DB for ID: " + userId);
	    	        return Optional.empty();
	    	    }
	    }
	    
	    public List<User> findAll() {
	        List<User> users = new ArrayList<>();
	        for (Document doc : userCollection.find()) {
	            users.add(User.fromDocument(doc));
	        }
	        return users;
	    }

	    public void save(User user) {
	        userCollection.insertOne(user.toDocument());
	    }

	    public boolean updateUser(int userId, User updatedUser) {
	        Document updatedDoc = updatedUser.toDocument();
	        return userCollection.replaceOne(Filters.eq("id", userId), updatedDoc).getMatchedCount() > 0;
	    }

	    public boolean deleteById(int userId) {
	        return userCollection.deleteOne(Filters.eq("id", userId)).getDeletedCount() > 0;
	    }

	    public void deleteAllUsers() {
	        userCollection.deleteMany(new Document()); // Deletes all documents in collection
	    }
	    
	    private User convertDocumentToUser(Document doc) {
	    	    return new User(
	    	        doc.getInteger("id"),
	    	        doc.getString("name"),
	    	        doc.getString("username"),
	    	        doc.getString("email"),
	    	        convertDocumentToAddress(doc.get("address", Document.class)),
		            doc.getString("phone"),
		    	    doc.getString("website"),
		    	    convertDocumentToCompany(doc.get("company", Document.class)) 	    	    );
	    }
	 // Convert Address object from Document
	 private Address convertDocumentToAddress(Document doc) {
	     return new Address(
	         doc.getString("street"),
	         doc.getString("suite"),
	         doc.getString("city"),
	         doc.getString("zipcode"),
	         convertDocumentToGeo((Document) doc.get("geo",Document.class))  //  Convert geo properly
	     );
	 }

	 // Convert Geo object from Document
	 private Geo convertDocumentToGeo(Document doc) {
	     return new Geo(doc.getString("lat"), doc.getString("lng"));
	 }

	 // Convert Company object from Document
	 private Company convertDocumentToCompany(Document doc) {
	     return new Company(
	         doc.getString("name"),
	         doc.getString("catchPhrase"),
	         doc.getString("bs")
	     );
	 }
}
