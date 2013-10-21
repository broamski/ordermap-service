package com.nuszkowski.webservices.ordermap;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nuszkowski.utils.Timer;
import com.nuszkowski.geocoding.GeocodedPostalCode;

@Path("/orders")
public class OrdermapResource {
	@GET
	@Produces("application/json")
	public OrdermapReturn getComments() {
		try {
			Timer t = new Timer();
			t.start();
			RealTimeOrdersSingleton.instance.touch();
			ArrayList<OrderObject> cca = RealTimeOrdersSingleton.instance
					.getOrders();
			t.stop();
			return new OrdermapReturn(cca.size(), t.getElapsed(), cca);
		} catch (Exception e) {
			return new OrdermapReturn(e);
		}
	}
}
