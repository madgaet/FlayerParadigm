package com.trollCorporation.common.exceptions;

import org.apache.log4j.Logger;

public class AlreadyExistsUserException extends RegistrationException {
	
	private static final long serialVersionUID = 4159019031407046543L;
	private static Logger LOG = Logger.getLogger(RegistrationException.class.getName());
	
	public AlreadyExistsUserException() {
		super("User already exists");
		LOG.info("Registration error");
	}
	
	public AlreadyExistsUserException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public AlreadyExistsUserException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
