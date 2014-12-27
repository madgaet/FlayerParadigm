package com.trollCorporation.common.exceptions;

import org.apache.log4j.Logger;

public class TimeoutException extends Exception{
	
	private static final long serialVersionUID = 4159019031407046523L;
	private static Logger LOG = Logger.getLogger(TimeoutException.class.getName());
	
	public TimeoutException() {
		super();
		LOG.info("Time out error");
	}
	
	public TimeoutException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public TimeoutException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
