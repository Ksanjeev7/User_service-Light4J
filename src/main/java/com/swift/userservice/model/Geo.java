package com.swift.userservice.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

public class Geo {

	private String lat;
	private String lng;

	public Geo() {}
	 public Geo(JSONObject jsonObject) {
	        this.lat = "0.0";
	        this.lng = "0.0";
	    }

	    public Geo(String lat, String lng) {
	        this.lat = lat != null ? lat : "0.0";
	        this.lng = lng != null ? lng : "0.0";
	    }

		public Document toDocument() {
	        return new Document("lat", lat).append("lng", lng);
	    }

	    public static Geo fromDocument(Document doc) {
	        if (doc == null) return new Geo();
	        return new Geo(doc.getString("lat"), doc.getString("lng"));
	    }

	    public Map<String, Object> toJson() {
	        Map<String, Object> jsonMap = new LinkedHashMap<>();
	        jsonMap.put("lat", lat);
	        jsonMap.put("lng", lng);
	        return jsonMap;
	    }
}
