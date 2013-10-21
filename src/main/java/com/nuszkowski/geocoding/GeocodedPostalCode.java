package com.nuszkowski.geocoding;

public class GeocodedPostalCode {
	public String postalCode, latitude, longitude, timestamp;

	public GeocodedPostalCode() {
	}

	public GeocodedPostalCode(String post_val, String lat_val, String lng_val) {
		this.postalCode = post_val;
		this.latitude = lat_val;
		this.longitude = lng_val;
	}

	public String getpostalCode() {
		return this.postalCode;
	}

	public String getlatitude() {
		return this.latitude;
	}

	public String getlongitude() {
		return this.longitude;
	}
}
