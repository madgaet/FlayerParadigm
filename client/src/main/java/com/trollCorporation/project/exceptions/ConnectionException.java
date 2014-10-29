package com.trollCorporation.project.exceptions;

import org.apache.log4j.Logger;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = 4159019031407046523L;
	private static Logger LOG = Logger.getLogger(ConnectionException.class.getName());
	
	public ConnectionException() {
		super();
		LOG.info("Connection error");
	}
	
	public ConnectionException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public ConnectionException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
