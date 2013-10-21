package com.nuszkowski.webservices.ordermap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.nuszkowski.utils.ConfigurationSingleton;
import com.nuszkowski.geocoding.GeoNamesSigleton;

public class StartupListener implements ServletContextListener {
	static Logger log = Logger.getLogger("StartUpListener");

	private ScheduledExecutorService splunk_scheduler;

	public void contextInitialized(ServletContextEvent event) {
		ConfigurationSingleton.instance.readConfigFile();
		GeoNamesSigleton.instance.buildCache();
		try {
			int interval = Integer.parseInt(ConfigurationSingleton.instance
					.getConfigItem("splunk.query.interval"));
			splunk_scheduler = Executors.newSingleThreadScheduledExecutor();
			splunk_scheduler.scheduleAtFixedRate(new SplunkRequestor(), 0,
					interval, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error(e);
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		splunk_scheduler.shutdownNow();
	}

}