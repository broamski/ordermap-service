package com.nuszkowski.geocoding;

public class GeocodeAbstractor {

	public GeocodedPostalCode geocode(String input_zip) {
		GeocodedPostalCode gpc = GeoNamesSigleton.instance.getItem(input_zip);
		return gpc;
	}

}
