package com.nuszkowski.geocoding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.nuszkowski.utils.ConfigurationSingleton;
import com.nuszkowski.utils.Timer;
import com.nuszkowski.webservices.ordermap.SplunkRequestor;

public enum GeoNamesSigleton {
	instance;
	private static Hashtable<String, GeocodedPostalCode> zip_to_lat_lng_cache = new Hashtable();
	static Logger log = Logger.getLogger(GeoNamesSigleton.class);

	public void buildCache() {
		int results = 0;
		Timer t = new Timer();

		ConfigurationSingleton.instance.readConfigFile();
		String file = ConfigurationSingleton.instance
				.getConfigItem("geonames.data.location");

		try {
			t.start();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokenized_line = line.split("\t");
				GeocodedPostalCode gpc = new GeocodedPostalCode(
						tokenized_line[1], tokenized_line[9],
						tokenized_line[10]);
				zip_to_lat_lng_cache.put(tokenized_line[1], gpc);
				results++;
			}
			t.stop();
			log.debug("GeoNames source file " + file + " Processed " + results
					+ " items in " + t.getElapsed() + " miliseconds");
		} catch (Exception e) {
			log.error("Something went wrong when loading GeoNames Data: " + e);
		}
	}

	public GeocodedPostalCode getItem(String val) {
		GeocodedPostalCode geo_temp = new GeocodedPostalCode();
		geo_temp = zip_to_lat_lng_cache.get(val);
		return zip_to_lat_lng_cache.get(val);
	}
}
