package com.trollCorporation.domain.exceptions;

import org.apache.log4j.Logger;

public class DbConnectionException extends Exception {
	
	private static final long serialVersionUID = 4159019031407046523L;
	private static Logger LOG = Logger.getLogger(DbConnectionException.class.getName());

	public DbConnectionException() {
		super();
		LOG.info("Db connection error");
	}
	
	public DbConnectionException(String message) {
		super(message);
		LOG.info(message);
	}
	
	public DbConnectionException(String message, Throwable t) {
		super(message, t);
		LOG.info(message, t);
	}
}
