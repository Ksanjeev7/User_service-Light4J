package com.swift.userservice.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

public class Address {
	private String street;
	private String suite;
	private String city;
	private String zipcode;
	private Geo geo;

	public Address() {
        this.street = "";
        this.suite = "";
        this.city = "";
        this.zipcode = "";
        this.geo = new Geo();
    }

    public Address(String street, String suite, String city, String zipcode, Geo geo) {
        this.street = street != null ? street : "";
        this.suite = suite != null ? suite : "";
        this.city = city != null ? city : "";
        this.zipcode = zipcode != null ? zipcode : "";
        this.geo = geo != null ? geo : new Geo();
    }

    public Address(JSONObject jsonObject) {
        this.street = jsonObject.optString("street", ""); 
        this.suite = jsonObject.optString("suite", ""); 
        this.city = jsonObject.optString("city", ""); 
        this.zipcode = jsonObject.optString("zipcode", ""); 
        this.geo = jsonObject.has("geo") ? new Geo(jsonObject.getJSONObject("geo")) : new Geo();
    }


	public Document toDocument() {
        return new Document("street", street)
                .append("suite", suite)
                .append("city", city)
                .append("zipcode", zipcode)
                .append("geo", geo.toDocument());
    }

    public static Address fromDocument(Document doc) {
        if (doc == null) return new Address();
        return new Address(
                doc.getString("street"),
                doc.getString("suite"),
                doc.getString("city"),
                doc.getString("zipcode"),
                doc.containsKey("geo") ? Geo.fromDocument((Document) doc.get("geo")) : new Geo()
        );
    }

    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("street", street);
        jsonMap.put("suite", suite);
        jsonMap.put("city", city);
        jsonMap.put("zipcode", zipcode);
        jsonMap.put("geo", geo.toJson());
        return jsonMap;
    }
}