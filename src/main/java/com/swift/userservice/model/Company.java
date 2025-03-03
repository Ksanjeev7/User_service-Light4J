package com.swift.userservice.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

public class Company {

	private String name;
    private String catchPhrase;
    private String bs;

    //Default Constructor with Empty Values (TO Prevent null)
    public Company() {
        this.name = "";
        this.catchPhrase = "";
        this.bs = "";
    }

    public Company(String name, String catchPhrase, String bs) {
        this.name = name != null ? name : "";
        this.catchPhrase = catchPhrase != null ? catchPhrase : "";
        this.bs = bs != null ? bs : "";
    }

    public Company(JSONObject jsonObject) {}

	public Document toDocument() {
        return new Document("name", name)
                .append("catchPhrase", catchPhrase)
                .append("bs", bs);
    }

    public static Company fromDocument(Document doc) {
        if (doc == null) return new Company();
        return new Company(
                doc.getString("name"),
                doc.getString("catchPhrase"),
                doc.getString("bs")
        );
    }

    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("name", name);
        jsonMap.put("catchPhrase", catchPhrase);
        jsonMap.put("bs", bs);
        return jsonMap;
    }
}
