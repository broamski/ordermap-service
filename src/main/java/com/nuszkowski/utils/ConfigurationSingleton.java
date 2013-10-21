package com.nuszkowski.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ConfigurationSingleton {
	instance;

	private Properties prop = new Properties();

	private ConfigurationSingleton() {
	}

	public void readConfigFile() {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("config.properties");
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getConfigItem(String val) {
		try {
			if (prop.getProperty(val) == null) {
				throw new Exception("Configuration item " + val
						+ " is missing from config.properties");
			}
			return prop.getProperty(val);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
}
