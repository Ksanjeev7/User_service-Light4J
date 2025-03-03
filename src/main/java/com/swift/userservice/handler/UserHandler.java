	package com.swift.userservice.handler;

import java.util.List;
import java.util.Optional;

import org.jose4j.json.internal.json_simple.JSONObject;

import com.networknt.handler.LightHttpHandler;
import com.swift.userservice.model.Address;
import com.swift.userservice.model.Company;
import com.swift.userservice.model.Geo;
import com.swift.userservice.model.User;
import com.swift.userservice.repository.UserRepository;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class UserHandler implements LightHttpHandler{
	
  private final static UserRepository userRepo = new UserRepository();
	
  @Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
	  
	                   String requestPath = exchange.getRequestPath();
	                   
	                   System.out.println("RequestPath: "+ requestPath);
	                   
	                   String method = exchange.getRequestMethod().toString();
	                   
	                   System.out.println("method: "+ method);
	                   
	                   if (requestPath.equals("/users") && method.equals("GET")) {
	                       GetAllUsers(exchange);
	                   } else if (requestPath.startsWith("/users/") && method.equals("GET")) {
	                       GetUserById(exchange);
	                   } else if (requestPath.equals("/users") && method.equals("PUT")) {
	                       handleCreateUser(exchange);
	                   } else if (requestPath.startsWith("/users/") && method.equals("POST")) {
	                       UpdateUser(exchange);
	                   } else if (requestPath.startsWith("/users/") && method.equals("DELETE")) {
	                       DeleteUserById(exchange);
	                   }else if(requestPath.equals("/users") && method.equals("DELETE")) {    
	                	   DeleteAllUsers(exchange);
	                   } else {
	                       exchange.setStatusCode(404);
	                       exchange.getResponseSender().send("{\"message\": \"Path not found\"}");
	                   }
	}

	/**
	 * GET /users -> fetech all Users 
	 *  
	 */
	@SuppressWarnings("unchecked")
	private void GetAllUsers(HttpServerExchange exchange) {
		
                 List<User> users = userRepo.findAll();
                 
                 // Convert List<User> to JSONArray
                 org.jose4j.json.internal.json_simple.JSONArray jsonUsers = new org.jose4j.json.internal.json_simple.JSONArray();
                 for (User user : users) {
                     jsonUsers.add(new JSONObject(user.toJson()));
                 }
//                 
                 // Create Response JSON
                 JSONObject responseJson = new JSONObject();
                 responseJson.put("users", jsonUsers);
                 exchange.setStatusCode(200);
                 exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
//                 exchange.getResponseSender().send(JsonFormatConvertion.toJsonfromStringObject(users));
                 exchange.getResponseSender().send(jsonUsers.toJSONString());
             }
    
	/*
	 * GetById /users/{id} -> Fetch User By Id
	 */	
	private void GetUserById(HttpServerExchange exchange) {
		String idParam = exchange.getRelativePath().substring("/users/".length()); 
		
		int userId = Integer.parseInt(idParam);

		Optional<User> user = userRepo.findById(userId);
		
        if (user.isPresent()) {
            exchange.setStatusCode(200);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            
            JSONObject jsonResponse = new JSONObject(user.get().toJson());
            exchange.getResponseSender().send(jsonResponse.toString());

        } else {
            exchange.setStatusCode(404);
            exchange.getResponseSender().send("{\"message\": \"User not found\"}");
        }
	}
	
	/*
	 * PUT/users -> create new user
	 */
	private void handleCreateUser(HttpServerExchange exchange) {
		exchange.getRequestReceiver().receiveFullBytes((ex, data) -> {
	        try {
	            String requestBody = new String(data);
	            org.json.JSONObject json = new org.json.JSONObject(requestBody);
	            
//	            System.out.println("Received JSON: " + json.toString(2)); 
	            
	            // Extract Data from JSON Object
	            int id = json.optInt("id", -1);
	            String name = json.optString("name", ""); 
	            String username = json.optString("username", "");
	            String email = json.optString("email", "");
	            String phone = json.optString("phone", "");
	            String website = json.optString("website", "");
	            
	            //Parse Nested Objects (Address, Company)
	            Address address = json.has("address") ? parseAddress(json.getJSONObject("address")) : new Address();
	            Company company = json.has("company") ? parseCompany(json.getJSONObject("company")) : new Company();
	            
	            //Create New User Object
	            User newUser = new User(id, name, username, email, address, phone, website, company);
	            
	            //Save to MongoDB
	            userRepo.save(newUser);
	            
	            exchange.setStatusCode(201);
	            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
	            exchange.getResponseSender().send("{\"message\": \"User created successfully\"}");
	        } catch (Exception e) {
	            e.printStackTrace();
	            exchange.setStatusCode(400);
	            exchange.getResponseSender().send("{\"error\": \"Invalid request body\"}");
	        }
	    });
	}
 
	/*
	 * POST /User/{Id} - Update Exiting User by Id
	 */
	private void UpdateUser(HttpServerExchange exchange) {
		int userId = Integer.parseInt(exchange.getRelativePath().substring("/users/".length()));

	    exchange.getRequestReceiver().receiveFullBytes((ex, data) -> {
	        org.json.JSONObject json = new org.json.JSONObject(new String(data));

	        // Fetch the existing user from DB
	        Optional<User> existingUserOpt = userRepo.findById(userId);
	        if (existingUserOpt.isEmpty()) {
	            exchange.setStatusCode(404);
	            exchange.getResponseSender().send("{\"message\": \"User not found\"}");
	            return;
	        }

	        User existingUser = existingUserOpt.get();
	        
	        // Ensure `userId` in request body matches the one in URL
	        if (json.has("id") && json.getInt("id") != userId) {
	            exchange.setStatusCode(400); // 400 Bad Request
	            exchange.getResponseSender().send("{\"message\": \"User ID in request body does not match URL\"}");
	            return;
	        }

	        // Update only the provided fields
	        String name = json.has("name") ? json.getString("name") : existingUser.getName();
	        String username = json.has("username") ? json.getString("username") : existingUser.getUsername();
	        String email = json.has("email") ? json.getString("email") : existingUser.getEmail();
	        Address address = json.has("address") ? new Address(json.getJSONObject("address")) : existingUser.getAddress();
	        String phone = json.has("phone") ? json.getString("phone") : existingUser.getPhone();
	        String website = json.has("website") ? json.getString("website") : existingUser.getWebsite();
	        Company company = json.has("company") ? new Company(json.getJSONObject("company")) : existingUser.getCompany();

	        // Create updated user
	        User updatedUser = new User(userId, name, username, email, address, phone, website, company);

	        if (userRepo.updateUser(userId, updatedUser)) {
	            exchange.setStatusCode(200);
	            exchange.getResponseSender().send("{\"message\": \"User updated successfully\"}");
	        } else {
	            exchange.setStatusCode(500);
	            exchange.getResponseSender().send("{\"message\": \"Failed to update user\"}");
	        }
	    });
	}
	
	/*
	 * DELETE /users -> delete All Users
	 */
	private void DeleteAllUsers(HttpServerExchange exchange) {
		userRepo.deleteAllUsers(); 
        exchange.setStatusCode(StatusCodes.NO_CONTENT); // 204 No Content
        exchange.endExchange(); // End response
	}

	/*
	 * DELETE /user/{id} -> Delete user by Id
	 */   
	private void DeleteUserById(HttpServerExchange exchange) {
		String idParam = exchange.getRelativePath().substring("/users/".length());
		int userId = Integer.parseInt(idParam);  
		
        if (userRepo.deleteById(userId)) {
            exchange.setStatusCode(200);
            exchange.getResponseSender().send("{\"message\": \"User deleted successfully\"}");
        } else {
            exchange.setStatusCode(404);
            exchange.getResponseSender().send("{\"message\": \"User not found\"}");
        }
    }
	
	private Address parseAddress(org.json.JSONObject json) {
	    return new Address(
	        json.optString("street", ""),
	        json.optString("suite", ""),
	        json.optString("city", ""),
	        json.optString("zipcode", ""),
	        json.has("geo") ? parseGeo(json.getJSONObject("geo")) : new Geo()
	    );
	}
	
	private Geo parseGeo(org.json.JSONObject json) {
	    return new Geo(
	        json.optString("lat", "0.0"),
	        json.optString("lng", "0.0")
	    );
	}
	
	private Company parseCompany(org.json.JSONObject json) {
	    return new Company(
	        json.optString("name", ""),
	        json.optString("catchPhrase", ""),
	        json.optString("bs", "")
	    );
	}
}
