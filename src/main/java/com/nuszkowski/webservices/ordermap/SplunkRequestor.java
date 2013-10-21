package com.nuszkowski.webservices.ordermap;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.splunk.Args;
import com.splunk.ResultsReaderXml;
import com.splunk.Service;
import com.splunk.ServiceArgs;

import com.nuszkowski.utils.ConfigurationSingleton;
import com.nuszkowski.geocoding.GeocodeAbstractor;
import com.nuszkowski.geocoding.GeocodedPostalCode;

public class SplunkRequestor implements Runnable {
	static Logger log = Logger.getLogger(SplunkRequestor.class);

	private String splunk_username = ConfigurationSingleton.instance
			.getConfigItem("splunk.api.username");
	private String splunk_password = ConfigurationSingleton.instance
			.getConfigItem("splunk.api.password");
	private String splunk_hostname = ConfigurationSingleton.instance
			.getConfigItem("splunk.api.hostname");
	private int splunk_serviceport = Integer
			.parseInt(ConfigurationSingleton.instance
					.getConfigItem("splunk.api.server.port"));
	private String splunk_start = ConfigurationSingleton.instance
			.getConfigItem("splunk.start");
	private String splunk_end = ConfigurationSingleton.instance
			.getConfigItem("splunk.end");
	private String splunk_search_query = ConfigurationSingleton.instance
			.getConfigItem("splunk.search.query");
	private String splunk_output_mode = ConfigurationSingleton.instance
			.getConfigItem("splunk.outputmode");
	private String postalcode_key = ConfigurationSingleton.instance
			.getConfigItem("postalcode.key");
	private String postalcode_exclusion = ConfigurationSingleton.instance
			.getConfigItem("postalcode.exclusion");

	public void checkTimestamp() {
		Calendar current_timestamp = Calendar.getInstance();
		Calendar last_access = RealTimeOrdersSingleton.instance
				.getAccessTimestamp();
		log.debug("Last access timestamp: " + last_access.getTime());

		long time_difference = current_timestamp.getTimeInMillis()
				- last_access.getTimeInMillis();
		if (time_difference >= Integer.parseInt(ConfigurationSingleton.instance
				.getConfigItem("splunk.query.inactivity.timeout")) * 1000) {
			if (RealTimeOrdersSingleton.instance.getLoopStatus()) {
				RealTimeOrdersSingleton.instance.setLoopStatus(false);
				log.debug("Time since last access: "
						+ time_difference
						+ "ms - Splunk Search Query Loop has been paused until next user request.");
			}
		}
	}

	public void run() {
		checkTimestamp();
		if (RealTimeOrdersSingleton.instance.getLoopStatus()) {
			ServiceArgs loginArgs = new ServiceArgs();
			loginArgs.setUsername(splunk_username);
			loginArgs.setPassword(splunk_password);
			loginArgs.setHost(splunk_hostname);
			loginArgs.setPort(splunk_serviceport);

			try {
				Service service = Service.connect(loginArgs);

				String outputMode = splunk_output_mode;

				Args queryArgs = new Args();
				queryArgs.put("earliest_time", splunk_start);
				queryArgs.put("latest_time", splunk_end);
				queryArgs.put("output_mode", outputMode);
				queryArgs.put("count", 0);

				log.debug("Executing Search Query: " + splunk_search_query);
				InputStream stream = service.oneshotSearch(splunk_search_query,
						queryArgs);

				ArrayList<OrderObject> temp_order_object_array = new ArrayList<OrderObject>();
				// 2013-09-22T15:37:15.000-04:00
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSS");
				try {
					ResultsReaderXml resultsReader = new ResultsReaderXml(
							stream);
					HashMap<String, String> map;
					while ((map = resultsReader.getNextEvent()) != null) {
						GeocodeAbstractor geo = new GeocodeAbstractor();
						GeocodedPostalCode gpc = new GeocodedPostalCode();
						Boolean pass = false;

						String current_timestamp = map.get("_time");
						current_timestamp = current_timestamp.substring(0,
								current_timestamp.length() - 6);
						Date whatever = dateFormat.parse(current_timestamp);
						Calendar startthis = Calendar.getInstance();
						startthis.setTime(whatever);
						current_timestamp = Long.toString(startthis
								.getTimeInMillis());

						String full_zip = map.get(postalcode_key);

						if (full_zip != null && full_zip.length() >= 5
								&& !full_zip.contains(postalcode_exclusion)) {
							String short_zip = full_zip.substring(0, 5);
							pass = true;
							gpc = geo.geocode(short_zip);

						}
						if (pass)
							temp_order_object_array.add(new OrderObject(gpc
									.getpostalCode(), gpc.getlatitude(), gpc
									.getlongitude(), current_timestamp));
					}
					resultsReader.close();
					RealTimeOrdersSingleton.instance
							.regenerateArray(temp_order_object_array);
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			} catch (Exception e) {
				log.error(e);
			}
		}

	}

}