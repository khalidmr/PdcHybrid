package com.example.testar;

public class Tree {
	protected Double latitude;
	protected Double longitude;
	protected long id;
	protected String description;

	public Tree(Double latitude, Double longitude, long id, String desc) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.description = desc;
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
	
	
	
}
