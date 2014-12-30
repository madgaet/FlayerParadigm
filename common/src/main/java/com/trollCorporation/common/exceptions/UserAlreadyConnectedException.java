package com.trollCorporation.common.exceptions;

import org.apache.log4j.Logger;

public class UserAlreadyConnectedException extends Exception {
	
	private static final long serialVersionUID = 4159019031407041233L;
	private static Logger LOG = Logger.getLogger(UserAlreadyConnectedException.class.getName());
	
	public UserAlreadyConnectedException() {
		super("User is already connected");
		LOG.info("user is already connected");
	}
	
	public UserAlreadyConnectedException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public UserAlreadyConnectedException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
