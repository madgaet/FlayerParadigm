package com.trollCorporation.common.utils;

import java.io.IOException;
import java.util.Properties;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

public class PropertiesLoader extends Properties {
	
	private static final long serialVersionUID = -6177664196910209606L;
	private static Logger LOG = Logger.getLogger(PropertiesLoader.class.getName());
	private Properties properties;
	
	public PropertiesLoader(final String filePath) throws ConfigurationException {
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(filePath));
		} catch (NullPointerException e) {
			LOG.error("file not found. Wrong path", e);
			throw new ConfigurationException("Properties file " + filePath + " not found!");
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
	
}
