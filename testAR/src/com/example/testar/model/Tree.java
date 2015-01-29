package com.example.testar.model;

import java.util.ArrayList;

public class Tree {
	protected Double latitude;
	protected Double longitude;
	protected long id;
	protected int proximity;
	protected String name;
	protected String kind;
    protected String specy;
    protected String type;
    protected String trunk;
    protected String crown;
    protected String height;
	
	protected ArrayList<String> messages = new ArrayList<String>();

//	public Tree(Double latitude, Double longitude, long id) {
//		super();
//		this.latitude = latitude;
//		this.longitude = longitude;
//		this.id = id;
//		
//	}
	
	public Tree(Double latitude, Double longitude, long id, int proximity,
			String name, String kind, String specy, String type, String trunk,
			String crown, String height) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.proximity = proximity;
		this.name = name;
		this.kind = kind;
		this.specy = specy;
		this.type = type;
		this.trunk = trunk;
		this.crown = crown;
		this.height = height;
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

	public ArrayList<String> getMessages() {
		return messages;
	}


	public int getProximity() {
		return proximity;
	}


	public String getName() {
		return name;
	}


	public String getKind() {
		return kind;
	}


	public String getSpecy() {
		return specy;
	}


	public String getType() {
		return type;
	}


	public String getTrunk() {
		return trunk;
	}


	public String getCrown() {
		return crown;
	}


	public String getHeight() {
		return height;
	}
	
	
	
	
	
	
}
