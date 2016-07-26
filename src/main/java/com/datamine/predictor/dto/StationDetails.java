package com.datamine.predictor.dto;

public class StationDetails {
	
	private String stationName;
	private String iataCode;
	private String Latitude;
	private String Longitude;
	private String Altitude;
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getIataCode() {
		return iataCode;
	}
	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getAltitude() {
		return Altitude;
	}
	public void setAltitude(String altitude) {
		Altitude = altitude;
	}
	
	

}
