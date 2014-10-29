package com.trollCorporation.common.utils;

import java.io.IOException;
import java.util.Properties;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

public class PropertiesLoader extends Properties {
	
	private static final long serialVersionUID = -6177664196910209606L;
	private static Logger LOG = Logger.getLogger(PropertiesLoader.class.getName());
	
	public PropertiesLoader(final String filePath) throws ConfigurationException {
		super();
		try {
			super.load(this.getClass().getClassLoader().getResourceAsStream(filePath));
		} catch (NullPointerException e) {
			LOG.error("file not found. Wrong path", e);
			throw new ConfigurationException("Properties file " + filePath + " not found!");
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
	
}
