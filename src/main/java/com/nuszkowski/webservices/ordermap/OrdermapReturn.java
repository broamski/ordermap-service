package com.nuszkowski.webservices.ordermap;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.nuszkowski.geocoding.GeocodedPostalCode;

@XmlRootElement
public class OrdermapReturn {
	public int count;
	public long requestTimeMS;
	public String error;
	public ArrayList<OrderObject> zipCodeList;

	public OrdermapReturn() {
	}

	public OrdermapReturn(int count, long time, ArrayList<OrderObject> array_val) {
		this.count = count;
		this.requestTimeMS = time;
		this.zipCodeList = array_val;
	}

	public OrdermapReturn(Exception e) {
		this.error = e.toString();
	}
}