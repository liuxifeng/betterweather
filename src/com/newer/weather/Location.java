package com.newer.weather;

public class Location {

	private String lat;
	private String lon;
	
	public Location() {
	}

	public Location(String lat, String lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lon=" + lon + "]";
	}
	
	
	
}
