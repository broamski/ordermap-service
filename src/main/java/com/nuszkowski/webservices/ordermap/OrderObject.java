package com.nuszkowski.webservices.ordermap;

public class OrderObject {
	public String postalCode, latitude, longitude, timestamp;

	public OrderObject() {
	}

	public OrderObject(String post_val, String lat_val, String lng_val,
			String tstamp_val) {
		this.postalCode = post_val;
		this.latitude = lat_val;
		this.longitude = lng_val;
		this.timestamp = tstamp_val;
	}

}
