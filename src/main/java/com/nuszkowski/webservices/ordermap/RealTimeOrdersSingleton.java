package com.nuszkowski.webservices.ordermap;

import java.util.ArrayList;
import java.util.Calendar;

import com.nuszkowski.geocoding.GeocodedPostalCode;

public enum RealTimeOrdersSingleton {
	instance;

	private ArrayList<GeocodedPostalCode> postal_code_array = new ArrayList<GeocodedPostalCode>();
	private ArrayList<OrderObject> order_object_array = new ArrayList<OrderObject>();
	private Calendar access_timestamp = Calendar.getInstance();
	private Boolean loop_status = false;

	private RealTimeOrdersSingleton() {
	}

	public Boolean getLoopStatus() {
		return loop_status;
	}

	public void setLoopStatus(Boolean val) {
		loop_status = val;
	}

	public Calendar getAccessTimestamp() {
		return access_timestamp;
	}

	public void setAccessTimestamp() {
		access_timestamp = Calendar.getInstance();
	}

	public ArrayList<OrderObject> getOrders() {
		return order_object_array;
	}

	public void regenerateArray(ArrayList<OrderObject> input_array) {
		order_object_array.clear();
		order_object_array = input_array;
	}

	public void touch() {
		setLoopStatus(true);
		setAccessTimestamp();
	}
}
