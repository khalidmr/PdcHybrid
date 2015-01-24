package com.example.testar.model;

import java.util.ArrayList;

public class Tree {
	protected Double latitude;
	protected Double longitude;
	protected long id;
	protected String description;
	protected ArrayList<String> messages = new ArrayList<String>();

	public Tree(Double latitude, Double longitude, long id, String desc) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.description = desc;
		this.messages.add("Non ergo erunt homines deliciis diffluentes audiendi, si quando de amicitia, quam nec usu nec ratione habent cognitam, disputabunt. Nam quis est, pro deorum fidem atque hominum! ");
	}
	
	public Tree(Double latitude, Double longitude, long id){
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	
	
	
	
	
}
