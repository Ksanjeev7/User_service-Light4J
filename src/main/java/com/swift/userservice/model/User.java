package com.swift.userservice.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;

public class User {
	private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    public User(int id, String name, String username, String email, Address address, String phone, String website, Company company) {
        this.id = id;
        this.name = name != null ? name : "";
        this.username = username != null ? username : "";
        this.email = email != null ? email : "";
        this.address = address != null ? address : new Address();
        this.phone = phone != null ? phone : "";
        this.website = website != null ? website : "";
        this.company = company != null ? company : new Company();
    }

    public Document toDocument() {
        return new Document("id", id)
                .append("name", name)
                .append("username", username)
                .append("email", email)
                .append("address", address.toDocument())
                .append("phone", phone)
                .append("website", website)
                .append("company", company.toDocument());
    }

    public static User fromDocument(Document doc) {
        return new User(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getString("username"),
                doc.getString("email"),
                doc.containsKey("address") ? Address.fromDocument((Document) doc.get("address")) : new Address(),
                doc.getString("phone"),
                doc.getString("website"),
                doc.containsKey("company") ? Company.fromDocument((Document) doc.get("company")) : new Company()
        );
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("id", id);
        json.put("name", name);
        json.put("username", username);
        json.put("email", email);
        json.put("phone", phone);
        json.put("website", website);
        json.put("address", address.toJson());
        json.put("company", company.toJson());
        return json;
    }

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail() {
		return this.email;
	}

	public Address getAddress() {
		return this.address;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getWebsite() {
		return this.website;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}