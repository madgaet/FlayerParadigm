package com.trollCorporation.common.utils;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

public class ConfigurationsUtils {
	
	public static Logger LOG = Logger.getLogger(ConfigurationsUtils.class);
	public static PropertiesLoader configs;
	
	static {
		try {
			configs = new PropertiesLoader("config.properties");
		} catch (ConfigurationException e) {
			LOG.error("Unable to load properties");
		}
	}
	
	public static String getProperty(final String key, final String defaultValue) {
		return configs.getProperty(key, defaultValue);
	}
}
