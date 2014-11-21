package com.trollCorporation.common.exceptions;

import org.apache.log4j.Logger;

public class RegistrationException extends Exception {

	private static final long serialVersionUID = 4159019031407046523L;
	private static Logger LOG = Logger.getLogger(RegistrationException.class.getName());
	
	public RegistrationException() {
		super();
		LOG.info("Registration error");
	}
	
	public RegistrationException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public RegistrationException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
