package com.trollCorporation.common.exceptions;

import org.apache.log4j.Logger;

public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 4159019031407046523L;
	private static Logger LOG = Logger.getLogger(ConnectionException.class.getName());
	
	public AuthenticationException() {
		super();
		LOG.info("Authentication error");
	}
	
	public AuthenticationException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public AuthenticationException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
